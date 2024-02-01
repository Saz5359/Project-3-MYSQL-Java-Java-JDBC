package poised;

import java.util.Scanner;
import java.sql.*;

public class Main {

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/PoisePMS?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
					"otheruser", "swordfish");

			Statement statement = connection.createStatement();

			Boolean exit = false;

			while (!(exit == true)) {
				System.out.println("Select a number: " + '\n' + "1. Enter new Data" + '\n'
						+ "2. Update existing Project" + '\n' + "3. Delete Data" + '\n' + "4. Finalise Project" + '\n'
						+ "5. Find Project" + '\n' + "0. Exit");
				int chosenFunction = scan.nextInt();
				scan.nextLine();

				boolean correctInput = false;

				while (correctInput == false) {
					if (!(chosenFunction == 0 || chosenFunction == 1 || chosenFunction == 2 || chosenFunction == 3
							|| chosenFunction == 4 || chosenFunction == 5)) {
						System.out.println("Please enter a number from 0 to 5");
						chosenFunction = scan.nextInt();

					} else {
						correctInput = true;
					}
				}

				// The fist choice is to add new Data
				if (chosenFunction == 1) {
					System.out.println(
							"Select a letter:" + '\n' + "a. Start a new Project" + '\n' + "b. Enter a new Architect"
									+ '\n' + "c. Enter a new  Contractor" + '\n' + "d. Enter a new Customer");
					String chosenLetter = scan.next().trim();

					boolean correctLetterInput = false;
					while (correctLetterInput == false) {
						if (!(chosenLetter.equalsIgnoreCase("a") || chosenLetter.equalsIgnoreCase("b")
								|| chosenLetter.equalsIgnoreCase("c") || chosenLetter.equalsIgnoreCase("d"))) {

							System.out.println("Please enter a Letter from a to d");
							chosenLetter = scan.next().trim();

						} else {
							correctLetterInput = true;
						}
					}

					// The user is asked to choose what new data they would like to change and the
					// Relevant methods are called for each option
					if (chosenLetter.equalsIgnoreCase("a")) {
						String projectData = enterProjectData(scan, statement);
						addToProjectsTable(statement, projectData);
					} else if (chosenLetter.equalsIgnoreCase("b")) {
						String architectData = enterArchitectData(scan);
						addToArchitectsTable(statement, architectData);
					} else if (chosenLetter.equalsIgnoreCase("c")) {
						String contractorData = enterContractorData(scan);
						addToContractorsTable(statement, contractorData);
					} else if (chosenLetter.equalsIgnoreCase("d")) {
						String CustomerData = enterCustomerData(scan);
						addToCustomersTable(statement, CustomerData);
					}
				}
				// The second choice is to update a project
				else if (chosenFunction == 2) {

					boolean updateMoreAttributes = true;

					// A while loop is added in case the user wants to add more information
					while (updateMoreAttributes == true) {
						// The projects table is displayed
						displayProjectsTable(statement);

						System.out.println("Enter the Project number of the project you want to update");
						int projectNumber = scan.nextInt();
						scan.nextLine();

						String updatedAttribute = attributeToUpdate(scan);

						updateProjectsTable(statement, updatedAttribute, projectNumber);

						System.out.println("Would you like to  update something else y/n");
						String moreUpdates = scan.next().trim();

						boolean correctmoreUpdatesInput = false;
						while (correctmoreUpdatesInput == false) {
							if (!(moreUpdates.equalsIgnoreCase("y") || moreUpdates.equalsIgnoreCase("n"))) {

								System.out.println("Select y or n");
								moreUpdates = scan.next().trim();

							} else {
								correctmoreUpdatesInput = true;
							}
						}

						// This is used to break out of the loop
						if (moreUpdates.equalsIgnoreCase("y")) {
							updateMoreAttributes = true;
						} else if (moreUpdates.equalsIgnoreCase("n")) {
							updateMoreAttributes = false;
						}
					}
				}
				// The third choice is to delete a loop
				else if (chosenFunction == 3) {
					displayProjectsTable(statement);

					System.out.println("Enter the the project number of the project you want to delete");
					int projectNumber = scan.nextInt();

					deleteProject(statement, projectNumber);
				}
				// The third choice is to finalise a project
				else if (chosenFunction == 4) {
					displayProjectsTable(statement);

					// In this option the user has to enter the project number of the project they
					// want to finalise
					// Then the user is then asked to enter the project date and to avoid errors the
					// information about the date is added in specific steps first is the year
					// followed by the month then the date
					System.out.println("Enter the project number of the project you want to finalise");
					int projectNumber = scan.nextInt();
					scan.nextLine();

					System.out.println("Enter the year the project was completed (0000) e.g 2023");
					String completionYear = scan.next();

					System.out.println("Enter the month the project was completed (00) e.g 06");
					String completionMonth = scan.next();

					System.out.println("Enter the day the project was completed (00) 01");
					String completionDay = scan.next();

					// when all data has been added the completed date is created in the correct
					// MSQL DATE format
					String completionDate = "'" + completionYear + "-" + completionMonth + "-" + completionDay + "'";

					finaliseProject(statement, completionDate, projectNumber);

				}
				// The fifth choice is to find projects
				else if (chosenFunction == 5) {
					System.out.println("Select a letter:" + '\n' + "a. Find all ongoing projects" + '\n'
							+ "b. Find all projects past their deadline" + '\n'
							+ "c. Find project by thier project number");
					String chosenMethod = scan.next();

					boolean correctMethodInput = false;
					while (correctMethodInput == false) {
						if (!(chosenMethod.equalsIgnoreCase("a") || chosenMethod.equalsIgnoreCase("b")
								|| chosenMethod.equalsIgnoreCase("c"))) {

							System.out.println("Please enter a Letter from a to c");
							chosenMethod = scan.next().trim();

						} else {
							correctMethodInput = true;
						}
					}

					if (chosenMethod.equalsIgnoreCase("a")) {
						locateOngingProjects(statement);
					} else if (chosenMethod.equalsIgnoreCase("b")) {
						locateLateProjects(statement);
					} else if (chosenMethod.equalsIgnoreCase("c")) {
						System.out.println("Enter the project number of the project you want to find");
						int projectNumber = scan.nextInt();

						locateWithProjectNumber(statement, projectNumber);
					}
				}
				// The last option is to exit the loop and terminate the program
				else if (chosenFunction == 0) {
					exit = true;
					statement.close();
					connection.close();
					scan.close();
					System.out.println("Terminated");
				}

			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Method printing all values in all rows of the projects table.
	 * 
	 * @param statement a statement on an existing connection
	 * @throws SQLException
	 */
	// This method displays the projects table when called
	// All records and fields are displayed to the user
	public static void displayProjectsTable(Statement statement) throws SQLException {
		ResultSet results = statement.executeQuery("SELECT * FROM projects");
		while (results.next()) {
			System.out.println(results.getInt("Proj_number") + ", " + results.getString("Proj_name") + ", "
					+ results.getString("Building_type") + ", " + results.getString("Proj_address") + ", "
					+ results.getInt("ERF_num") + ", " + results.getString("Proj_total") + ", "
					+ results.getString("Total_paid") + ", " + results.getString("Proj_deadline") + ", "
					+ results.getString("Completion_date") + ", " + results.getInt("Architect_ID") + ", "
					+ results.getInt("Contractor_ID") + ", " + results.getInt("Customer_ID") + ", "
					+ results.getString("Proj_status"));
		}
	}

	/**
	 * Method printing all values in all rows of the Architects table.
	 * 
	 * @param statement a statement on an existing connection
	 * @throws SQLException
	 */
	// This method displays the Architect Table to the user
	// All rows and columns in the table are displayed
	public static void displayArchitectTable(Statement statement) throws SQLException {
		ResultSet results = statement.executeQuery("SELECT * FROM Architects");
		while (results.next()) {
			System.out.println(results.getInt("Architect_ID") + ", " + results.getString("Architect_name") + ", "
					+ results.getString("Architect_surname") + ", " + results.getInt("Architect_number") + ", "
					+ results.getString("Architect_email") + ", " + results.getString("Architect_address"));
		}
	}

	/**
	 * Method printing all values in all rows of the Contractors table.
	 * 
	 * @param statement a statement on an existing connection
	 * @throws SQLException
	 */
	// This method displays the Contractor Table to the user
	// All rows and columns in the table are displayed
	public static void displayContractorTable(Statement statement) throws SQLException {
		ResultSet results = statement.executeQuery("SELECT * FROM Contractors");
		while (results.next()) {
			System.out.println(results.getInt("Contractor_ID") + ", " + results.getString("Contractor_name") + ", "
					+ results.getString("Contractor_surname") + ", " + results.getInt("Contractor_number") + ", "
					+ results.getString("Contractor_email") + ", " + results.getString("Contractor_address"));
		}
	}

	/**
	 * Method printing all values in all rows of the Customers table.
	 * 
	 * @param statement a statement on an existing connection
	 * @throws SQLException
	 */
	// This method displays the Customer Table to the user
	// All rows and columns in the table are displayed
	public static void displayCustomerTable(Statement statement) throws SQLException {
		ResultSet results = statement.executeQuery("SELECT * FROM Customers");
		while (results.next()) {
			System.out.println(results.getInt("Customer_ID") + ", " + results.getString("Customer_name") + ", "
					+ results.getString("Customer_surname") + ", " + results.getInt("Customer_number") + ", "
					+ results.getString("Customer_email") + ", " + results.getString("Customer_address"));
		}
	}

	/**
	 * This method records information about a new project being constructed from
	 * user input and returns the new project information
	 * 
	 * @param scanner   a text scanner that takes in user input
	 * @param statement a statement on an existing connection
	 * @return the new project information
	 * @throws SQLException
	 */
	// This method prompts the the user to enter new project data data based of
	// their chosen letter
	// The method takes in a scanner and a statement and throws an exception when an
	// error occurs
	public static String enterProjectData(Scanner scanner, Statement statement) throws SQLException {
		String newProjectData = "";

		// the user enters all the project information
		scanner.nextLine();
		System.out.println("Enter the name of the new Project");
		String projectName = scanner.nextLine();

		System.out.println("Enter the type of building being designed");
		String buildingType = scanner.nextLine();

		System.out.println("Enter the address of the project");
		String projectAddress = scanner.nextLine();

		System.out.println("Enter the ERF number");
		String erfNumber = scanner.nextLine();

		System.out.println("Enter the total fee being charged for the project");
		String projectTotal = scanner.nextLine();

		System.out.println("Enter the total amount paid to date");
		String totalPaid = scanner.nextLine();

		System.out.println("You will now enter the deadline of the project");

		System.out.println("Enter the Year of the deadline YYYY e.g 2023");
		String deadlineYear = scanner.nextLine();

		System.out.println("Enter the Month of the deadline MM e.g 02");
		String deadlineMonth = scanner.nextLine();

		System.out.println("Enter the Day of the deadline DD e.g 06");
		String deadlineDay = scanner.nextLine();

		String projectDeadline = deadlineYear + "-" + deadlineMonth + "-" + deadlineDay;

		// For the customer, Architect and Contractor information the tables are first
		// displayed and then the user has to enter the ID of the person they want in
		// the project
		displayArchitectTable(statement);

		System.out.println("Enter ID of the Architect you want on this project");
		int architectID = scanner.nextInt();

		displayContractorTable(statement);

		System.out.println("Enter ID of the Contractor you want on this project");
		int contractorID = scanner.nextInt();

		displayCustomerTable(statement);

		System.out.println("Enter ID of the Customer of this project");
		int customerID = scanner.nextInt();

		// If the project name is empty then the project name will be the building type
		// combined with the customers name
		if (projectName.isEmpty()) {
			projectName = buildingType + getCustomerSurname(statement, customerID);
		}

		// When all the information has been added the data is returned in the MYSQL
		// syntax for VALUES to insert
		newProjectData = "'" + projectName + "'," + " '" + buildingType + "'," + " '" + projectAddress + "'," + " '"
				+ erfNumber + "'," + " '" + projectTotal + "'," + " '" + totalPaid + "'," + " '" + projectDeadline
				+ "'," + " " + architectID + "," + " " + contractorID + "," + " " + customerID;

		return newProjectData;
	}

	/**
	 * This method prints the surname of a specific customer from the customers
	 * table.
	 * 
	 * @param statement  a statement on an existing connection
	 * @param customerID id of the customer
	 * @return the surname of the customer
	 * @throws SQLException
	 */
	public static String getCustomerSurname(Statement statement, int customerID) throws SQLException {
		String customerSurname = "";
		ResultSet results = statement.executeQuery("SELECT * FROM Customers WHERE Customer_ID =" + customerID);
		while (results.next()) {
			customerSurname = results.getString("Customer_surname");
		}

		return customerSurname;
	}

	/**
	 * This method adds the new project information into the projects table in the
	 * database.
	 * 
	 * @param statement a statement on an existing connection
	 * @param data      this is the new project information that will be added to
	 *                  the projects table
	 * @throws SQLException
	 */
	// This method adds all the new project data created by the user to the projects
	// table in the database
	public static void addToProjectsTable(Statement statement, String data) throws SQLException {
		int rowsAffected = statement.executeUpdate(
				"INSERT INTO projects (Proj_name, Building_type, Proj_address, ERF_num, Proj_total, Total_paid, Proj_deadline, Architect_ID, Contractor_ID, Customer_ID) VALUES ("
						+ data + ")");
		System.out.println("Successfully added " + rowsAffected + " rows");
		displayProjectsTable(statement);
	}

	/**
	 * This method records information about a new Architect being constructed from
	 * user input and returns the new Architect information
	 * 
	 * @param scan a text scanner that takes in user input
	 * @return the new Architect information
	 */
	// This method prompts the user to enter new Architect information
	public static String enterArchitectData(Scanner scan) {
		String newArchitectData = "";

		scan.nextLine();
		System.out.println("Enter the name of the architect");
		String architectName = scan.nextLine();

		System.out.println("Enter the surname of the architect");
		String architectSurname = scan.nextLine();

		System.out.println("Enter the telephone number of the architect");
		String architectNumber = scan.nextLine();

		System.out.println("Enter the email address of the architect");
		String architectEmail = scan.nextLine();

		System.out.println("Enter the physical address of the architect");
		String architectAddress = scan.nextLine();

		// The Architect information is returned in the MYSQL VALUES syntax
		newArchitectData = "'" + architectName + "'," + " '" + architectSurname + "'," + " '" + architectNumber + "',"
				+ " '" + architectEmail + "'," + " '" + architectAddress + "'";

		return newArchitectData;
	}

	/**
	 * This method adds the new Architect information into the Architects Table in
	 * the database
	 * 
	 * @param statement     a statement on an existing connection
	 * @param architectData this is the new Architect information that will be added
	 *                      to the Architects table
	 * @throws SQLException
	 */
	// This method adds the Architect information into the Architects table in the
	// database
	public static void addToArchitectsTable(Statement statement, String architectData) throws SQLException {
		int rowsAffected = statement.executeUpdate(
				"INSERT INTO architects (Architect_name, Architect_surname, Architect_number, Architect_email, Architect_address) VALUES ("
						+ architectData + ")");
		System.out.println("Successfully added " + rowsAffected + " rows");
		displayArchitectTable(statement);
	}

	/**
	 * This method records information about a new Contractor being constructed from
	 * user input and returns the new Contractor information
	 * 
	 * @param scan a text scanner that takes in user input
	 * @return the new Contractor information
	 */
	// This method prompts the user to enter new Contractor information
	public static String enterContractorData(Scanner scan) {
		String newContractorData = "";

		scan.nextLine();
		System.out.println("Enter the name of the contractor");
		String contractorName = scan.nextLine();

		System.out.println("Enter the surname of the contractor");
		String contractorSurname = scan.nextLine();

		System.out.println("Enter the telephone number of the contractor");
		String contractorNumber = scan.nextLine();

		System.out.println("Enter the email address of the contractor");
		String contractorEmail = scan.nextLine();

		System.out.println("Enter the physical address of the contractor");
		String contractorAddress = scan.nextLine();

		// The Contractor information is returned in the MYSQL VALUES syntax
		newContractorData = "'" + contractorName + "'," + " '" + contractorSurname + "'," + " '" + contractorNumber
				+ "'," + " '" + contractorEmail + "'," + " '" + contractorAddress + "'";

		return newContractorData;
	}

	/**
	 * This method adds the new Contractor information into the Contractors Table in
	 * the database
	 * 
	 * @param statement      a statement on an existing connection
	 * @param ContractorData this is the new Contractor information that will be
	 *                       added to the Contractors table
	 * @throws SQLException
	 */
	// This method adds the Contractor information into the Contractors table in the
	// database
	public static void addToContractorsTable(Statement statement, String ContractorData) throws SQLException {
		int rowsAffected = statement.executeUpdate(
				"INSERT INTO Contractors (Contractor_name, Contractor_surname, Contractor_number, Contractor_email, Contractor_address) VALUES ("
						+ ContractorData + ")");
		System.out.println("Successfully added " + rowsAffected + " rows");
		displayContractorTable(statement);
	}

	/**
	 * This method records information about a new Customer being constructed from
	 * user input and returns the new Customer information
	 * 
	 * @param scan a text scanner that takes in user input
	 * @return the new Customer information
	 */
	// This method prompts the user to enter new Customer information
	public static String enterCustomerData(Scanner scan) {
		String newCustomerData = "";

		scan.nextLine();
		System.out.println("Enter the name of the Customer");
		String customerName = scan.nextLine();

		System.out.println("Enter the surname of the Customer");
		String customerSurname = scan.nextLine();

		System.out.println("Enter the telephone number of the Customer");
		String customerNumber = scan.nextLine();

		System.out.println("Enter the email address of the Customer");
		String customerEmail = scan.nextLine();

		System.out.println("Enter the physical address of the Customer");
		String customerAddress = scan.nextLine();

		// The Customer information is returned in the MYSQL VALUES syntax
		newCustomerData = "'" + customerName + "'," + " '" + customerSurname + "'," + " '" + customerNumber + "',"
				+ " '" + customerEmail + "'," + " '" + customerAddress + "'";

		return newCustomerData;
	}

	/**
	 * This method adds the new Customer information into the Customers Table in the
	 * database
	 * 
	 * @param statement    a statement on an existing connection
	 * @param CustomerData this is the new Customer information that will be added
	 *                     to the Customers table
	 * @throws SQLException
	 */
	// This method adds the Customer information into the Customers table in the
	// database
	public static void addToCustomersTable(Statement statement, String CustomerData) throws SQLException {
		int rowsAffected = statement.executeUpdate(
				"INSERT INTO Customers (Customer_name, Customer_surname, Customer_number, Customer_email, Customer_address) VALUES ("
						+ CustomerData + ")");
		System.out.println("Successfully added " + rowsAffected + " rows");
		displayCustomerTable(statement);
	}

	/**
	 * This method prompts the user to select an attribute they want to update and
	 * enter the new value of that attribute.
	 * 
	 * @param scan a text scanner that takes in user input
	 * @return the selected attribute with its new value
	 */
	// This method asked the user what they would like to update then ask them to
	// enter the new value
	// This method determines what needs to be updated and the updated values
	// The method returns what is being updated and its new value in the MYSQL SET
	// syntax
	public static String attributeToUpdate(Scanner scan) {
		String updatedData = "";

		System.out.println("What do you want to change? Choose the letter of the attribute you want to change:" + "\n"
				+ "a. The Project name" + "\n" + "b. The Building type" + "\n" + "c. The Project address" + "\n"
				+ "d. The ERF number" + "\n" + "e. The total Project fee" + "\n" + "f. The total amount paid" + "\n"
				+ "g. The Project deadline" + "\n" + "h. The Architect_ID" + "\n" + "i. The Contractor_ID" + "\n"
				+ "j. The Customer_ID");
		String chosenAttribute = scan.next();

		boolean correctAttributeInput = false;
		while (correctAttributeInput == false) {
			if (!(chosenAttribute.equalsIgnoreCase("a") || chosenAttribute.equalsIgnoreCase("b")
					|| chosenAttribute.equalsIgnoreCase("c") || chosenAttribute.equalsIgnoreCase("d")
					|| chosenAttribute.equalsIgnoreCase("e") || chosenAttribute.equalsIgnoreCase("f")
					|| chosenAttribute.equalsIgnoreCase("g") || chosenAttribute.equalsIgnoreCase("h")
					|| chosenAttribute.equalsIgnoreCase("i") || chosenAttribute.equalsIgnoreCase("j"))) {

				System.out.println("Please enter a Letter from a to j");
				chosenAttribute = scan.next().trim();

			} else {
				correctAttributeInput = true;
			}
		}

		if (chosenAttribute.equalsIgnoreCase("a")) {
			scan.nextLine();
			System.out.println("Enter the new Project name");
			String newPName = scan.nextLine();

			updatedData = "Proj_name='" + newPName + "'";

		} else if (chosenAttribute.equalsIgnoreCase("b")) {
			scan.nextLine();
			System.out.println("Enter the new Building type");
			String newBType = scan.nextLine();

			updatedData = "Building_type='" + newBType + "'";

		} else if (chosenAttribute.equalsIgnoreCase("c")) {
			scan.nextLine();
			System.out.println("Enter the new Project address");
			String newPAddress = scan.nextLine();

			updatedData = "Proj_address='" + newPAddress + "'";

		} else if (chosenAttribute.equalsIgnoreCase("d")) {
			scan.nextLine();
			System.out.println("Enter the new ERF number");
			String newERF = scan.nextLine();

			updatedData = "ERF_num='" + newERF + "'";

		} else if (chosenAttribute.equalsIgnoreCase("e")) {
			scan.nextLine();
			System.out.println("Enter the new total Project fee");
			String newPTotal = scan.nextLine();

			updatedData = "Proj_total='" + newPTotal + "'";

		} else if (chosenAttribute.equalsIgnoreCase("f")) {
			scan.nextLine();
			System.out.println("Enter the new total amount paid");
			String newTPaid = scan.nextLine();

			updatedData = "Proj_total='" + newTPaid + "'";

		}
		// to enter the project dealine the user must first enter the year followed by
		// the month then the date of the deadline date
		else if (chosenAttribute.equalsIgnoreCase("g")) {
			scan.nextLine();
			System.out.println("Enter the year (YYYY) of the new Project deadline e.g 2023");
			String newYear = scan.nextLine();

			System.out.println("Enter the month (MM) of the new Project deadline e.g 02");
			String newMonth = scan.nextLine();

			System.out.println("Enter the day (DD) of the new Project deadline e.g 20");
			String newDay = scan.nextLine();

			String newDeadline = newYear + "-" + newMonth + "-" + newDay;

			updatedData = "Proj_deadline='" + newDeadline + "'";

		} else if (chosenAttribute.equalsIgnoreCase("h")) {
			System.out.println("Enter the new Architect_ID");
			int newArcID = scan.nextInt();

			updatedData = "Architect_ID=" + newArcID;

		} else if (chosenAttribute.equalsIgnoreCase("i")) {
			System.out.println("Enter the new Contractor_ID");
			int newConID = scan.nextInt();

			updatedData = "Contractor_ID=" + newConID;

		} else if (chosenAttribute.equalsIgnoreCase("j")) {
			System.out.println("Enter the new Customer_ID");
			int newCusID = scan.nextInt();

			updatedData = "Customer_ID=" + newCusID;

		}

		return updatedData;
	}

	/**
	 * This method updates the selected project with its new attribute value.
	 * 
	 * @param statement        a statement on an existing connection
	 * @param updatedAttribute this is the attribute that will be updated with its
	 *                         new value
	 * @param updateID         this is the id of the project that will be updated
	 * @throws SQLException
	 */
	// This method updates the projects table the project number is taken in
	// followed by the item to update
	public static void updateProjectsTable(Statement statement, String updatedAttribute, int updateID)
			throws SQLException {
		int rowsAffected = statement
				.executeUpdate("UPDATE Projects SET " + updatedAttribute + " WHERE Proj_number=" + updateID);
		System.out.println("Query complete, " + rowsAffected + " rows updated.");
		displayProjectsTable(statement);
	}

	/**
	 * This method deletes a project from the projects table in the database.
	 * 
	 * @param statement     a statement on an existing connection
	 * @param projectNumber the project number of the project to be deleted
	 * @throws SQLException
	 */
	// This project deletes a project by taking in the project number of the item
	// then deleting it in the database
	public static void deleteProject(Statement statement, int projectNumber) throws SQLException {
		int rowsAffected = statement.executeUpdate("DELETE FROM Projects WHERE Proj_number=" + projectNumber);
		System.out.println("Query complete, " + rowsAffected + " rows removed.");
		displayProjectsTable(statement);
	}

	/**
	 * This method finalises a project by updating the project status to finalized
	 * and adding the completion date
	 * 
	 * @param statement      a statement on an existing connection
	 * @param Completiondate this the completion date of the project
	 * @param projectNumber  the project number of the project that will be
	 *                       finalised
	 * @throws SQLException
	 */
	// This method finalises projects and updates the completion date column and
	// status
	// This method updates the project status to finalised and adds a completion
	// date
	public static void finaliseProject(Statement statement, String Completiondate, int projectNumber)
			throws SQLException {
		int rowsAffected = statement.executeUpdate("UPDATE Projects SET Completion_date=" + Completiondate
				+ ", Proj_status='Finalised' WHERE Proj_number=" + projectNumber);
		System.out.println("Query complete, " + rowsAffected + " rows updated.");
		displayProjectsTable(statement);
	}

	/**
	 * This method prints all projects that have not been finalised
	 * 
	 * @param statement a statement on an existing connection
	 * @throws SQLException
	 */
	// this method displays all projects that have not been finalised
	public static void locateOngingProjects(Statement statement) throws SQLException {
		ResultSet results = statement.executeQuery("SELECT * FROM projects WHERE Proj_status='Ongoing'");
		while (results.next()) {
			System.out.println(results.getInt("Proj_number") + ", " + results.getString("Proj_name") + ", "
					+ results.getString("Building_type") + ", " + results.getString("Proj_address") + ", "
					+ results.getInt("ERF_num") + ", " + results.getString("Proj_total") + ", "
					+ results.getString("Total_paid") + ", " + results.getString("Proj_deadline") + ", "
					+ results.getString("Completion_date") + ", " + results.getInt("Architect_ID") + ", "
					+ results.getInt("Contractor_ID") + ", " + results.getInt("Customer_ID") + ", "
					+ results.getString("Proj_status"));
		}
	}

	/**
	 * This method prints all projects that are past their due date
	 * 
	 * @param statement a statement on an existing connection
	 * @throws SQLException
	 */
	// This method displays projects that are past their due date
	// If the deadline is less than the current date then the project is past its
	// due date
	public static void locateLateProjects(Statement statement) throws SQLException {
		ResultSet results = statement.executeQuery("SELECT * FROM projects WHERE Proj_deadline < CURDATE()");
		while (results.next()) {
			System.out.println(results.getInt("Proj_number") + ", " + results.getString("Proj_name") + ", "
					+ results.getString("Building_type") + ", " + results.getString("Proj_address") + ", "
					+ results.getInt("ERF_num") + ", " + results.getString("Proj_total") + ", "
					+ results.getString("Total_paid") + ", " + results.getString("Proj_deadline") + ", "
					+ results.getString("Completion_date") + ", " + results.getInt("Architect_ID") + ", "
					+ results.getInt("Contractor_ID") + ", " + results.getInt("Customer_ID") + ", "
					+ results.getString("Proj_status"));
		}
	}

	/**
	 * This method locates and prints a project based on the given project number
	 * 
	 * @param statement     a statement on an existing connection
	 * @param projectNumber the project number of the project that will printed
	 * @throws SQLException
	 */
	// This method displays a project based on its project number
	// This method finds a project based on a given project number
	public static void locateWithProjectNumber(Statement statement, int projectNumber) throws SQLException {
		ResultSet results = statement.executeQuery("SELECT * FROM projects WHERE Proj_number=" + projectNumber);
		while (results.next()) {
			System.out.println(results.getInt("Proj_number") + ", " + results.getString("Proj_name") + ", "
					+ results.getString("Building_type") + ", " + results.getString("Proj_address") + ", "
					+ results.getInt("ERF_num") + ", " + results.getString("Proj_total") + ", "
					+ results.getString("Total_paid") + ", " + results.getString("Proj_deadline") + ", "
					+ results.getString("Completion_date") + ", " + results.getInt("Architect_ID") + ", "
					+ results.getInt("Contractor_ID") + ", " + results.getInt("Customer_ID") + ", "
					+ results.getString("Proj_status"));
		}
	}
}
