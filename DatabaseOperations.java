// DatabaseOperations.java
package todo;
// DatabaseOperations.java
import java.util.List;

public interface DatabaseOperations {
    boolean addTask(Task task);
    boolean editTask(Task task);
    boolean deleteTask(int taskId);
    Task getTaskById(int taskId);
    List<Task> getAllTasks(User user);
    List<Task> getTasksByPriority(User user, Task.Priority priority);
}
