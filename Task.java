import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Priority priority;
    private Status status;
    private User owner;

    // Enum for Priority
    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    // Enum for Status
    public enum Status {
        NOT_STARTED, IN_PROGRESS, COMPLETED
    }

    public static class InvalidTaskTitleException extends Exception {
        public InvalidTaskTitleException(String message) {
            super(message);
        }
    }

    

    
    // Constructor
    public Task(int id, String title, String description, LocalDateTime deadline, Priority priority, User owner) throws InvalidTaskTitleException, {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.status = Status.NOT_STARTED;
        this.owner = owner;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getOwner() {
        return owner;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Task ID: " + id +
                "\nTitle: " + title +
                "\nDescription: " + description +
                "\nDeadline: " + deadline.format(formatter) +
                "\nPriority: " + priority +
                "\nStatus: " + status +
                "\nOwner: " + owner.getUsername();
    }
}
