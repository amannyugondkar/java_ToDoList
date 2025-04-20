import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

// Base class for all trackable items
abstract class TrackableItem {
    private int id;
    private String title;
    protected Status status;

    // Common Status enum moved to parent class
    public enum Status {
        NOT_STARTED, IN_PROGRESS, COMPLETED
    }

    // Constructor
    public TrackableItem(int id, String title) throws IOException {
        if (title == null || title.trim().isEmpty()) {
            throw new IOException("Title cannot be empty");
        }
        this.id = id;
        this.title = title;
        this.status = Status.NOT_STARTED;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws IOException {
        if (title == null || title.trim().isEmpty()) {
            throw new IOException("Title cannot be empty");
        }
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Abstract method to be implemented by subclasses
    public abstract boolean isOverdue();

    // Common method to display basic info
    public String getBasicInfo() {
        return "ID: " + id + " | Title: " + title + " | Status: " + status;
    }
}

// Task class inherits from TrackableItem
public class Task extends TrackableItem {
    private String description;
    private LocalDateTime deadline;
    private Priority priority;
    private User owner;

    // Enum for Priority
    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    // Custom exception
    public static class InvalidTaskTitleException extends Exception {
        public InvalidTaskTitleException(String message) {
            super(message);
        }
    }

    // Constructor
    public Task(int id, String title, String description, LocalDateTime deadline, 
                Priority priority, User owner) throws IOException, InvalidTaskTitleException {
        super(id, title); // Call parent constructor which handles common validation
        
        if (title != null && title.length() > 200) {
            throw new InvalidTaskTitleException("Task title cannot exceed 200 characters");
        }
        
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.owner = owner;
    }

    // Implementation of abstract method
    @Override
    public boolean isOverdue() {
        return deadline != null && deadline.isBefore(LocalDateTime.now()) && status != Status.COMPLETED;
    }

    // Additional getters and setters specific to Task
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public User getOwner() {
        return owner;
    }

    // Polymorphic method - overrides toString()
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Task ID: " + getId() +
                "\nTitle: " + getTitle() +
                "\nDescription: " + description +
                "\nDeadline: " + (deadline != null ? deadline.format(formatter) : "Not set") +
                "\nPriority: " + priority +
                "\nStatus: " + getStatus() +
                "\nOverdue: " + (isOverdue() ? "Yes" : "No") +
                "\nOwner: " + owner.getUsername();
    }
    
    // Method overloading - polymorphism
    public String getFormattedInfo() {
        return getBasicInfo();
    }
    
    public String getFormattedInfo(boolean includeDescription) {
        String info = getBasicInfo();
        if (includeDescription && description != null && !description.isEmpty()) {
            info += " | Description: " + description;
        }
        return info;
    }
    
    public String getFormattedInfo(boolean includeDescription, boolean includeDeadline) {
        String info = getFormattedInfo(includeDescription);
        if (includeDeadline && deadline != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            info += " | Deadline: " + deadline.format(formatter);
        }
        return info;
    }
}
