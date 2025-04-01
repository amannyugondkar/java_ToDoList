// ReminderService.java
import java.time.LocalDateTime;

public class ReminderService implements Notifiable {
    public void scheduleReminder(Task task) {}
    
    @Override
    public void notifyUser(User user, String message) {}
}
