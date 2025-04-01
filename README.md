📝 To-Do List Application

This Java-based To-Do List Application allows users to efficiently manage their tasks. It provides essential features like adding, editing, deleting, and viewing tasks based on priority. The application follows OOP principles, integrates exception handling, and interacts with a MySQL database via JDBC.

🚀 Features

✅ User Authentication – Each task is linked to a user.
✅ Task Management – Add, edit, delete, and view tasks.
✅ Task Prioritization – Categorize tasks as LOW, MEDIUM, or HIGH priority.
✅ Reminder Service – Ensures users stay updated with deadlines.
✅ Exception Handling – Handles invalid inputs and database-related issues.
✅ Interactive Console Menu – Provides a simple CLI interface for user interaction.

⚙️ Tech Stack

Java (Core Application Logic)
JDBC & MySQL (Database Operations)
OOP Concepts (Encapsulation, Inheritance, Polymorphism)
Exception Handling (Custom & Built-in Exceptions)
📂 Project Structure

📦 ToDoListApplication  
 ┣ 📜 ToDoListApplication.java   # Main application logic  
 ┣ 📜 TaskManager.java           # Manages task-related operations  
 ┣ 📜 Task.java                  # Task class with properties & methods  
 ┣ 📜 User.java                  # User details & authentication  
 ┣ 📜 ReminderService.java       # Task reminder service  
 ┣ 📜 DatabaseOperations.java    # Handles database CRUD operations  
 ┗ 📜 CustomExceptions.java      # Defines custom exceptions  

📌 How to Run

1️⃣ Clone the repository:
git clone https://github.com/your-username/todo-list-app.git
cd todo-list-app
2️⃣ Compile and run:

javac ToDoListApplication.java  
java ToDoListApplication  
