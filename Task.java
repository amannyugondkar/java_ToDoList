package todo;

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

    // Instance variables
    private int id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Priority priority;
    private Status status;
    private User owner;

    // Constructor
    public Task(int id, String title, String description, LocalDateTime deadline, Priority priority, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.status = Status.NOT_STARTED;  // Default status
        this.owner = owner;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    // toString method to display Task information
    @Override
    public String toString() {
        return "Task ID: " + id + "\n" +
                "Title: " + title + "\n" +
                "Description: " + description + "\n" +
                "Deadline: " + deadline + "\n" +
                "Priority: " + priority + "\n" +
                "Status: " + status + "\n" +
                "Owner: " + owner.getUsername(); // Assuming User has a getUsername method
    }
}
