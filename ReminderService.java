//ReminderService.java
package todo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ReminderService implements Notifiable {
    private Map<Integer, Timer> scheduledReminders = new HashMap<>();

    public void scheduleReminder(Task task) {
        // Cancel any existing reminder for this task
        if (scheduledReminders.containsKey(task.getId())) {
            scheduledReminders.get(task.getId()).cancel();
            scheduledReminders.remove(task.getId());
        }

        // Calculate delay until reminder (5 minutes before deadline for example)
        LocalDateTime reminderTime = task.getDeadline().minusMinutes(5);
        LocalDateTime now = LocalDateTime.now();

        long delay = java.time.Duration.between(now, reminderTime).toMillis();

        // Only schedule if the reminder is in the future
        if (delay > 0) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    notifyUser(task.getOwner(), "Reminder: Task '" + task.getTitle() + "' is due in 5 minutes!");
                    scheduledReminders.remove(task.getId());
                }
            }, delay);

            scheduledReminders.put(task.getId(), timer);
        }
    }

    @Override
    public void notifyUser(User user, String message) {
        // In a real application, this might send an email, push notification, etc.
        System.out.println("NOTIFICATION for " + user.getUsername() + ": " + message);
    }
}
