# Database Operations & Task Management (feature-database)

## 📌 Branch Overview

This branch (`feature-database`) focuses on implementing the database operations and task management functionalities for the Java To-Do List Application. It includes interfaces and classes responsible for storing, retrieving, and managing tasks.

## 📂 Included Files

### 1️⃣ `DatabaseOperations.java` (Interface)

Defines methods for database interactions, including:
- `addTask(Task task)`: Adds a new task.
- `editTask(Task task)`: Updates an existing task.
- `deleteTask(int taskId)`: Deletes a task by ID.
- `getTaskById(int taskId)`: Retrieves a specific task.
- `getAllTasks(User user)`: Retrieves all tasks for a user.
- `getTasksByPriority(User user, Task.Priority priority)`: Retrieves tasks based on priority.

### 2️⃣ `TaskManager.java` (Class)

Implements `DatabaseOperations` to manage task creation, retrieval, and modification.  
Methods include:
- `createTask(String title, String description, LocalDateTime deadline, Task.Priority priority, User owner)`: Creates and stores tasks.
- Implements all methods from `DatabaseOperations.java`.

### 3️⃣ `Task.java` (Class)

Defines the Task object, including attributes like:
- `id`, `title`, `description`, `deadline`, `priority`, and `status`.
- Used by `TaskManager` to handle task operations.

