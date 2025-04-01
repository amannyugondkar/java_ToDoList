// TaskManager.java
import java.time.LocalDateTime;
import java.util.List;

public class TaskManager implements DatabaseOperations {
    // Constructor
    public TaskManager(ReminderService reminderService) {}
    
    @Override
    public boolean addTask(Task task) {}
    
    @Override
    public boolean editTask(Task task) {}
    
    @Override
    public boolean deleteTask(int taskId) {}
    
    @Override
    public Task getTaskById(int taskId) {}
    
    @Override
    public List<Task> getAllTasks(User user) {}
    
    @Override
    public List<Task> getTasksByPriority(User user, Task.Priority priority) {}
    
    public int createTask(String title, String description, LocalDateTime deadline, 
                        Task.Priority priority, User owner) {}
}
