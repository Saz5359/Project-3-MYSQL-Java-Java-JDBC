# My Third Project

## This is the third project of my bootcamp which instructs us to create a project management system.
In this project, I was tasked with designing and implementing a database and also writing the code needed to interact with the database so I was
tasked with creating a Java program that can be used to keep track of the many projects
The database should store the following information for each project:
+ Project number.
+ Project name.
+ What type of building is being designed? E.g. House, apartment, block, store, etc.
+ The physical address for the project.
+ ERF number.
+ The total fee being charged for the project.
+ The total amount paid to date.
+ Deadline for the project.
+ The name, telephone number, email address, and physical address of the architect for the project.
+ The name, telephone number, email address, and physical address of the contractor for the project.
+ The name, telephone number, email address, and physical address of the customer for the project.

Using the above information I was tasked with creating a relational database so I used normalization to create the MySQL database and used Java(JDBC), to interact with the database.
The system needs to do the following:
+ Capture information about new projects. If a project name is not provided when the information is captured, name the project using the customer's surname (e.g. Mike Tyson - House Tyson).
+ List all projects and people associated with projects from the database.
+ Update information about existing projects
+ Delete data about projects and people associated with them
+ Finalise existing projects - when finalized the project should be marked as 'finalized' and the completion date should be added
+ Find all projects that still need to be completed from the database
+ Find all projects that are past the due date from the database

This project was created using the following:
+ MySql
+ Java
+ Java Database Connectivity - JDBC

## How to install project
I had issues pushing the project to GitHub
+ Copy the main class in src/poised/ to your computer
+ Make you copy only the main class
+ Start a new Java project
+ Replace add the main class or copy the code to the new Java project if all else fails

