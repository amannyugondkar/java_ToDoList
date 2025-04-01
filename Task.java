// Task.java
import java.time.LocalDateTime;

public class Task {
    // Enum for Priority
    public enum Priority {
        LOW, MEDIUM, HIGH
    }
    
    // Enum for Status
    public enum Status {
        NOT_STARTED, IN_PROGRESS, COMPLETED
    }
    
    // Constructor
    public Task(int id, String title, String description, LocalDateTime deadline, Priority priority, User owner) {}
    
    // Getters and Setters
    public int getId() {}
    public String getTitle() {}
    public void setTitle(String title) {}
    public String getDescription() {}
    public void setDescription(String description) {}
    public LocalDateTime getDeadline() {}
    public void setDeadline(LocalDateTime deadline) {}
    public Priority getPriority() {}
    public void setPriority(Priority priority) {}
    public Status getStatus() {}
    public void setStatus(Status status) {}
    public User getOwner() {}
    
    @Override
    public String toString() {}
}
