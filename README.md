
# Poised Management System

This project is a project management system for a small structural engineering firm called “Poised”.Poised has tasked me with creating a program
that they can use to keep track of the many projects on which they work.


## Motivation
I have taken on this project since I have experience in JAVA from my food Quick Delivery project. To complete the task I have used JAVA for the functions of the program such as adding, updating, and deleting projects. For storage, I used MySql to store the projects. To connect all of this I used Java Database Connectivity.



## Features
This project was made using:
- JAVA
- MySQL
- JDBC - Java Database Connectivity
- Relational database - created using normalisation
- included an ERD Diagram to show the relationships between the tables in the database

This system has the following features:
- Read and write data about projects and people associated with projects from your database.
- Capture information about new projects and add these to the database.
- Update information about existing projects.
- Delete data about projects and people associated with them.
- Finalise existing projects - when finalized the project should be marked as “finalized” and the completion date should be added.
- Find all projects that still need to be completed from the database.
- Find all projects that are past the due date from the database.

Poised stores the following information for each project that they work on:
- Project number.
- Project name.
- What type of building is being designed? E.g House, apartment block or store, etc.
- The physical address for the project.
- ERF number.
- The total fee being charged for the project.
- The total amount paid to date.
- Deadline for the project.
- The name, telephone number, email address and physical address of the architect for the project.
- The name, telephone number, email address and physical address of the contractor for the project.
- The name, telephone number, email address and physical address of the customer for the project.


## Installation

To run this project you will need the following:

+ SQL Server
+ Microsoft JDBC Driver
+ JDE You will need to create a database in SQL   Server based on the db_export Excel file and adapt the database connections in the code to that database.

+ Copy the project to a repository and run the JAVA project.
+ The Main class runs the project.
