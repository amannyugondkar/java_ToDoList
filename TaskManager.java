import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public class TaskManager implements DatabaseOperations {
    private Map<Integer, Task> tasks = new HashMap<>();
    private ReminderService reminderService;
    private int nextTaskId = 1;

    public TaskManager(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Override
    public boolean addTask(Task task) {
        tasks.put(task.getId(), task);
        reminderService.scheduleReminder(task);
        return true;
    }

    @Override
    public boolean editTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return false;
        }
        tasks.put(task.getId(), task);
        reminderService.scheduleReminder(task);
        return true;
    }

    @Override
    public boolean deleteTask(int taskId) {
        return tasks.remove(taskId) != null;
    }

    @Override
    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    @Override
    public List<Task> getAllTasks(User user) {
        return tasks.values().stream()
                .filter(task -> task.getOwner().getId() == user.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getTasksByPriority(User user, Task.Priority priority) {
        return tasks.values().stream()
                .filter(task -> task.getOwner().getId() == user.getId() && task.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public int createTask(String title, String description, LocalDateTime deadline,
                          Task.Priority priority, User owner) {
        int id = nextTaskId++;
        Task task = new Task(id, title, description, deadline, priority, owner);
        addTask(task);
        return id;
    }
}
