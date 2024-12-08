#I. Project Overview

This project is an Expense Tracker Application developed in Java. It aims to assist users in efficiently managing their finances by tracking expenses and generating insightful spending reports. The application provides a user-friendly interface that supports essential features like user registration (sign-up and login), adding expense records, deleting records, and generating total expense reports.
Users can categorize expenses (e.g., food, transport, entertainment), and the app stores these details for future use. The project also utilizes file-based storage to persist user data and expenses across sessions.

#SDG 12: Responsible Consumption and Production

Expense Tracker supports SDG Goal 12 by promoting responsible financial habits. 
This project: 
Encourages Awareness: Tracks and categorizes expenses, helping users monitor consumption patterns.
Fosters Discipline: avoid overspending, and align spending with financial goals.
Empowers Decisions: Generates monthly reports that provide insights into spending trends, enabling informed choices.
By addressing these aspects, Expense Tracker aligns with the global goal of encouraging sustainable and mindful consumption practices.

#II. Application of OOP Principles
The project makes use of key Object-Oriented Programming (OOP) principles to structure and manage the code efficiently.

Encapsulation
The Expense, User, and ExpenseTracker classes encapsulate data relevant to their respective entities. For example, the Expense class encapsulates properties such as description, amount, date, and category. Similarly, the User class encapsulates name, password, and budget data.
Methods like getAmount(), getDescription(), and getPassword() allow access to these private fields.

Polymorphism
Methods like addExpense(), viewExpenses(), and deleteExpense() can operate on different types of Expense objects. This provides flexibility in managing various kinds of expenses, even if new categories are added in the future.

Abstraction
The implementation of classes like ExpenseDatabase and UserDatabase abstracts the complex logic of saving and retrieving data from files, allowing the user to interact with simple methods (e.g., saveUser(), loadUsers()) without worrying about the underlying file operations.

