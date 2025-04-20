//TaskManager.java
package todo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskManager implements DatabaseOperations {
    // JDBC connection parameters — adjust user/password as needed
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/todo_db";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "123456";

    // Custom unchecked exceptions for cleaner error handling
    public static class DatabaseOperationException extends RuntimeException {
        public DatabaseOperationException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class TaskNotFoundException extends RuntimeException {
        public TaskNotFoundException(String msg) {
            super(msg);
        }
    }

    private final ReminderService reminderService;

    // Constructor loads driver and sets reminder service
    public TaskManager(ReminderService reminderService) {
        this.reminderService = reminderService;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseOperationException("MySQL JDBC Driver not found.", e);
        }
    }

    // Helper to obtain a DB connection
    private Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to connect to database.", e);
        }
    }

    @Override
    public boolean addTask(Task task) {
        String sql = "INSERT INTO tasks (title, description, deadline, priority, status, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(task.getDeadline()));
            stmt.setString(4, task.getPriority().name());
            stmt.setString(5, task.getStatus().name());
            stmt.setInt(6, task.getOwner().getId());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new DatabaseOperationException("Inserting task failed, no rows affected.", null);
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    task.setId(keys.getInt(1));
                }
            }

            reminderService.scheduleReminder(task);
            return true;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error adding task.", e);
        }
    }

    @Override
    public boolean editTask(Task task) {
        String sql = "UPDATE tasks SET title=?, description=?, deadline=?, priority=?, status=? WHERE id=? AND user_id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(task.getDeadline()));
            stmt.setString(4, task.getPriority().name());
            stmt.setString(5, task.getStatus().name());
            stmt.setInt(6, task.getId());
            stmt.setInt(7, task.getOwner().getId());

            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new TaskNotFoundException("Task not found or no permission to edit.");
            }

            reminderService.scheduleReminder(task);
            return true;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error editing task.", e);
        }
    }

    @Override
    public boolean deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            int deleted = stmt.executeUpdate();
            if (deleted == 0) {
                throw new TaskNotFoundException("Task ID " + taskId + " not found.");
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting task.", e);
        }
    }

    @Override
    public Task getTaskById(int taskId) {
        String sql = "SELECT t.id, t.title, t.description, t.deadline, " +
                "t.priority, t.status, u.id AS uid, u.username, u.password " +
                "FROM tasks t JOIN users u ON t.user_id=u.id WHERE t.id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new TaskNotFoundException("Task ID " + taskId + " not found.");
                }
                User owner = new User(
                        rs.getInt("uid"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTimestamp("deadline").toLocalDateTime(),
                        Task.Priority.valueOf(rs.getString("priority")),
                        owner
                );
                task.setStatus(Task.Status.valueOf(rs.getString("status")));
                return task;
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching task.", e);
        } catch (Exception ie) {
            throw new DatabaseOperationException("Error instantiating Task.", ie);
        }
    }

    @Override
    public List<Task> getAllTasks(User user) {
        String sql = "SELECT * FROM tasks WHERE user_id=?";
        List<Task> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task t = new Task(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getTimestamp("deadline").toLocalDateTime(),
                            Task.Priority.valueOf(rs.getString("priority")),
                            user
                    );
                    t.setStatus(Task.Status.valueOf(rs.getString("status")));
                    list.add(t);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error listing tasks.", e);
        } catch (Exception ie) {
            throw new DatabaseOperationException("Error instantiating Task.", ie);
        }
    }

    @Override
    public List<Task> getTasksByPriority(User user, Task.Priority priority) {
        String sql = "SELECT * FROM tasks WHERE user_id=? AND priority=?";
        List<Task> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.getId());
            stmt.setString(2, priority.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task t = new Task(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getTimestamp("deadline").toLocalDateTime(),
                            priority,
                            user
                    );
                    t.setStatus(Task.Status.valueOf(rs.getString("status")));
                    list.add(t);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error filtering tasks.", e);
        } catch (Exception ie) {
            throw new DatabaseOperationException("Error instantiating Task.", ie);
        }
    }

    /**
     * Convenience method to create and insert a new Task
     * @return the generated task ID
     */
    public int createTask(String title,
                          String description,
                          LocalDateTime deadline,
                          Task.Priority priority,
                          User owner) {
        try {
            Task task = new Task(0, title, description, deadline, priority, owner);
            addTask(task);
            return task.getId();
        } catch (Exception e) {
            throw new DatabaseOperationException("Error creating task.", e);
        }
    }
}
