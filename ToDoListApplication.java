import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

// Main class for the To-Do List Application
public class ToDoListApplication {
    private static TaskManager taskManager; // Manages tasks
    private static User currentUser; // Stores currently logged-in user
    private static Scanner scanner = new Scanner(System.in); // Scanner for user input
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // Date format for input

    public static void main(String[] args) {
        // Initialize required services
        ReminderService reminderService = new ReminderService();
        taskManager = new TaskManager(reminderService);
        
        // Creating a demo user
        currentUser = new User(1, "user1", "password");
        
        boolean running = true;
        while (running) {
            displayMenu(); // Show menu options
            int choice = getIntInput("Enter your choice: ");

            // Handle user choices
            switch (choice) {
                case 1:
                    addNewTask(); // Add new task
                    break;
                case 2:
                    viewAllTasks(); // View all tasks
                    break;
                case 3:
                    editTask(); // Edit an existing task
                    break;
                case 4:
                    deleteTask(); // Delete a task
                    break;
                case 5:
                    viewTasksByPriority(); // View tasks filtered by priority
                    break;
                case 0:
                    running = false; // Exit the application
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close(); // Close scanner when done
    }

    // Displays the menu options
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

    // Adds a new task
    private static void addNewTask() {
        System.out.println("\n----- ADD NEW TASK -----");
        String title = getStringInput("Enter task title: ");
        String description = getStringInput("Enter task description: ");
        LocalDateTime deadline = getDateTimeInput("Enter deadline (yyyy-MM-dd HH:mm): ");

        // Select task priority
        System.out.println("Select priority: ");
        System.out.println("1. LOW\n2. MEDIUM\n3. HIGH");
        int priorityChoice = getIntInput("Enter your choice: ");
        
        Task.Priority priority = switch (priorityChoice) {
            case 1 -> Task.Priority.LOW;
            case 2 -> Task.Priority.MEDIUM;
            case 3 -> Task.Priority.HIGH;
            default -> {
                System.out.println("Invalid choice. Setting priority to MEDIUM by default.");
                yield Task.Priority.MEDIUM;
            }
        };

        int taskId = taskManager.createTask(title, description, deadline, priority, currentUser);
        System.out.println("Task created successfully with ID: " + taskId);
    }

    // Displays all tasks for the current user
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

    // Edits an existing task
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
        System.out.println("1. Title\n2. Description\n3. Deadline\n4. Priority\n5. Status\n0. Cancel");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1 -> task.setTitle(getStringInput("Enter new title: "));
            case 2 -> task.setDescription(getStringInput("Enter new description: "));
            case 3 -> task.setDeadline(getDateTimeInput("Enter new deadline (yyyy-MM-dd HH:mm): "));
            case 4 -> {
                System.out.println("Select new priority: \n1. LOW\n2. MEDIUM\n3. HIGH");
                int priorityChoice = getIntInput("Enter your choice: ");
                task.setPriority(priorityChoice == 1 ? Task.Priority.LOW : (priorityChoice == 2 ? Task.Priority.MEDIUM : Task.Priority.HIGH));
            }
            case 5 -> {
                System.out.println("Select new status: \n1. NOT_STARTED\n2. IN_PROGRESS\n3. COMPLETED");
                int statusChoice = getIntInput("Enter your choice: ");
                task.setStatus(statusChoice == 1 ? Task.Status.NOT_STARTED : (statusChoice == 2 ? Task.Status.IN_PROGRESS : Task.Status.COMPLETED));
            }
            case 0 -> {
                System.out.println("Edit cancelled.");
                return;
            }
            default -> System.out.println("Invalid choice. Task not modified.");
        }
        
        if (taskManager.editTask(task)) {
            System.out.println("Task updated successfully.");
        } else {
            System.out.println("Failed to update task.");
        }
    }

    // Handles user input for string values
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // Handles user input for integer values with validation
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

    // Handles user input for date-time values with validation
    private static LocalDateTime getDateTimeInput(String prompt) {
        LocalDateTime dateTime = null;
        boolean valid = false;
        while (!valid) {
            try {
                System.out.print(prompt);
                dateTime = LocalDateTime.parse(scanner.nextLine(), formatter);
                valid = true;
            } catch (Exception e) {
                System.out.println("Invalid format. Please use yyyy-MM-dd HH:mm");
            }
        }
        return dateTime;
    }
}
