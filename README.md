# Core Task & User Management

## Overview
This module handles the core functionality of task creation and user authentication in the Java To-Do List Application. It consists of two main classes:
- `Task` - Represents a task with attributes like title, description, priority, and deadline.
- `User` - Represents a user with authentication logic.

## Files in This Module
1. **Task.java** - Defines the `Task` class with encapsulation.
2. **User.java** - Defines the `User` class and handles authentication.

## Task Class Implementation
The `Task` class is responsible for storing and managing task-related information.

### **Attributes**
- `id` (int) - Unique identifier for the task.
- `title` (String) - Title of the task.
- `description` (String) - Detailed information about the task.
- `deadline` (LocalDateTime) - Due date and time for the task.
- `priority` (enum Priority) - Priority level (**LOW, MEDIUM, HIGH**).
- `status` (enum Status) - Task status (**NOT_STARTED, IN_PROGRESS, COMPLETED**).
- `owner` (User) - The user who created the task.

### **Methods**
```java
public int getId();
public String getTitle();
public void setTitle(String title);
public String getDescription();
public void setDescription(String description);
public LocalDateTime getDeadline();
public void setDeadline(LocalDateTime deadline);
public Priority getPriority();
public void setPriority(Priority priority);
public Status getStatus();
public void setStatus(Status status);
public User getOwner();
@Override
public String toString();
```

## User Class Implementation
The `User` class handles user authentication and data storage.

### **Attributes**
- `id` (int) - Unique user identifier.
- `username` (String) - Username for login.
- `password` (String) - User password (should be stored securely in a real application).

### **Methods**
```java
public int getId();
public String getUsername();
public boolean authenticate(String password);
```

## Setup and Usage
1. **Include `Task.java` and `User.java` in your project.**
2. Create instances of `Task` and `User` as needed.
3. Use getter and setter methods to modify attributes safely.
4. Call `authenticate(String password)` in `User` class to validate login.

## Example Usage
```java
User user1 = new User(1, "JohnDoe", "password123");
Task task1 = new Task(101, "Complete Java Project", "Finish coding the To-Do List", LocalDateTime.now().plusDays(2), Task.Priority.HIGH, user1);

if (user1.authenticate("password123")) {
    System.out.println("Welcome, " + user1.getUsername());
    System.out.println("Task: " + task1.getTitle() + " - Priority: " + task1.getPriority());
}
```

## Future Improvements
- Implement password hashing for better security.
- Add user roles (e.g., Admin, Regular User).
- Enhance task management with tags and categories.

---
This module is essential for managing tasks and users securely using **Encapsulation and OOP Principles**.

