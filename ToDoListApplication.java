import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ToDoListApplication {
    private static TaskManager taskManager;
    private static User currentUser;
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        // Initialize services
        ReminderService reminderService = new ReminderService();
        taskManager = new TaskManager(reminderService);

        // For demo purposes, create a user
        currentUser = new User(1, "user1", "password");

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addNewTask();
                    break;
                case 2:
                    viewAllTasks();
                    break;
                case 3:
                    editTask();
                    break;
                case 4:
                    deleteTask();
                    break;
                case 5:
                    viewTasksByPriority();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n===== TO-DO LIST APPLICATION =====");
        System.out.println("1. Add New Task");
        System.out.println("2. View All Tasks");
        System.out.println("3. Edit Task");
        System.out.println("4. Delete Task");
        System.out.println("5. View Tasks by Priority");
        System.out.println("0. Exit");
        System.out.println("==================================");
    }

    private static void addNewTask() {
        System.out.println("\n----- ADD NEW TASK -----");
        String title = getStringInput("Enter task title: ");
        String description = getStringInput("Enter task description: ");

        LocalDateTime deadline = getDateTimeInput("Enter deadline (yyyy-MM-dd HH:mm): ");

        System.out.println("Select priority: ");
        System.out.println("1. LOW");
        System.out.println("2. MEDIUM");
        System.out.println("3. HIGH");
        int priorityChoice = getIntInput("Enter your choice: ");

        Task.Priority priority;
        switch (priorityChoice) {
            case 1:
                priority = Task.Priority.LOW;
                break;
            case 2:
                priority = Task.Priority.MEDIUM;
                break;
            case 3:
                priority = Task.Priority.HIGH;
                break;
            default:
                System.out.println("Invalid choice. Setting priority to MEDIUM by default.");
                priority = Task.Priority.MEDIUM;
        }

        int taskId = taskManager.createTask(title, description, deadline, priority, currentUser);
        System.out.println("Task created successfully with ID: " + taskId);
    }

    private static void viewAllTasks() {
        System.out.println("\n----- ALL TASKS -----");
        List<Task> tasks = taskManager.getAllTasks(currentUser);

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (Task task : tasks) {
            System.out.println("\n" + task);
            System.out.println("------------------");
        }
    }

    private static void editTask() {
        System.out.println("\n----- EDIT TASK -----");
        int taskId = getIntInput("Enter task ID to edit: ");

        Task task = taskManager.getTaskById(taskId);
        if (task == null || task.getOwner().getId() != currentUser.getId()) {
            System.out.println("Task not found or you don't have permission to edit it.");
            return;
        }

        System.out.println("Current task details:");
        System.out.println(task);

        System.out.println("\nWhat would you like to edit?");
        System.out.println("1. Title");
        System.out.println("2. Description");
        System.out.println("3. Deadline");
        System.out.println("4. Priority");
        System.out.println("5. Status");
        System.out.println("0. Cancel");

        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                String title = getStringInput("Enter new title: ");
                task.setTitle(title);
                break;
            case 2:
                String description = getStringInput("Enter new description: ");
                task.setDescription(description);
                break;
            case 3:
                LocalDateTime deadline = getDateTimeInput("Enter new deadline (yyyy-MM-dd HH:mm): ");
                task.setDeadline(deadline);
                break;
            case 4:
                System.out.println("Select new priority: ");
                System.out.println("1. LOW");
                System.out.println("2. MEDIUM");
                System.out.println("3. HIGH");
                int priorityChoice = getIntInput("Enter your choice: ");

                switch (priorityChoice) {
                    case 1:
                        task.setPriority(Task.Priority.LOW);
                        break;
                    case 2:
                        task.setPriority(Task.Priority.MEDIUM);
                        break;
                    case 3:
                        task.setPriority(Task.Priority.HIGH);
                        break;
                    default:
                        System.out.println("Invalid choice. Priority not changed.");
                }
                break;
            case 5:
                System.out.println("Select new status: ");
                System.out.println("1. NOT_STARTED");
                System.out.println("2. IN_PROGRESS");
                System.out.println("3. COMPLETED");
                int statusChoice = getIntInput("Enter your choice: ");

                switch (statusChoice) {
                    case 1:
                        task.setStatus(Task.Status.NOT_STARTED);
                        break;
                    case 2:
                        task.setStatus(Task.Status.IN_PROGRESS);
                        break;
                    case 3:
                        task.setStatus(Task.Status.COMPLETED);
                        break;
                    default:
                        System.out.println("Invalid choice. Status not changed.");
                }
                break;
            case 0:
                System.out.println("Edit cancelled.");
                return;
            default:
                System.out.println("Invalid choice. Task not modified.");
                return;
        }

        if (taskManager.editTask(task)) {
            System.out.println("Task updated successfully.");
        } else {
            System.out.println("Failed to update task.");
        }
    }

    private static void deleteTask() {
        System.out.println("\n----- DELETE TASK -----");
        int taskId = getIntInput("Enter task ID to delete: ");

        Task task = taskManager.getTaskById(taskId);
        if (task == null || task.getOwner().getId() != currentUser.getId()) {
            System.out.println("Task not found or you don't have permission to delete it.");
            return;
        }

        String confirm = getStringInput("Are you sure you want to delete this task? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            if (taskManager.deleteTask(taskId)) {
                System.out.println("Task deleted successfully.");
            } else {
                System.out.println("Failed to delete task.");
            }
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private static void viewTasksByPriority() {
        System.out.println("\n----- VIEW TASKS BY PRIORITY -----");
        System.out.println("Select priority: ");
        System.out.println("1. LOW");
        System.out.println("2. MEDIUM");
        System.out.println("3. HIGH");
        int priorityChoice = getIntInput("Enter your choice: ");

        Task.Priority priority;
        switch (priorityChoice) {
            case 1:
                priority = Task.Priority.LOW;
                break;
            case 2:
                priority = Task.Priority.MEDIUM;
                break;
            case 3:
                priority = Task.Priority.HIGH;
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        List<Task> tasks = taskManager.getTasksByPriority(currentUser, priority);

        if (tasks.isEmpty()) {
            System.out.println("No tasks found with " + priority + " priority.");
            return;
        }

        System.out.println("\nTasks with " + priority + " priority:");
        for (Task task : tasks) {
            System.out.println("\n" + task);
            System.out.println("------------------");
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt) {
        int input = -1;
        boolean valid = false;

        while (!valid) {
            try {
                System.out.print(prompt);
                input = Integer.parseInt(scanner.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return input;
    }

    private static LocalDateTime getDateTimeInput(String prompt) {
        LocalDateTime dateTime = null;
        boolean valid = false;

        while (!valid) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                dateTime = LocalDateTime.parse(input, formatter);
                valid = true;
            } catch (Exception e) {
                System.out.println("Invalid format. Please use yyyy-MM-dd HH:mm");
            }
        }

        return dateTime;
    }
}
