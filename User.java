// Base class for system entities
abstract class SystemEntity {
    private int id;
    private String name;
    
    public SystemEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    // Abstract method to be implemented by all system entities
    public abstract String getDisplayName();
    
    // Common method for all entities
    public String getIdAndName() {
        return "ID: " + id + " | Name: " + name;
    }
}

// Base class for user accounts
abstract class Account extends SystemEntity {
    private String username;
    protected String password; // In a real app, this would be hashed
    
    // Custom exceptions
    public static class InvalidUsernameException extends Exception {
        public InvalidUsernameException(String message) {
            super(message);
        }
    }
    
    public static class WeakPasswordException extends Exception {
        public WeakPasswordException(String message) {
            super(message);
        }
    }
    
    public Account(int id, String name, String username, String password) 
            throws InvalidUsernameException, WeakPasswordException {
        super(id, name);
        
        if (username == null || username.trim().length() < 3) {
            throw new InvalidUsernameException("Username must be at least 3 characters long");
        }
        
        if (password == null || password.length() < 8 || !containsSpecialChar(password)) {
            throw new WeakPasswordException("Password must be at least 8 characters and contain special characters");
        }
        
        this.username = username;
        this.password = password;
    }
    
    protected static boolean containsSpecialChar(String password) {
        return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
    }
    
    public String getUsername() {
        return username;
    }
    
    // Abstract method for authentication to be implemented by subclasses
    public abstract boolean authenticate(String password);
    
    // Override the abstract method from parent
    @Override
    public String getDisplayName() {
        return username;
    }
}

// User class that extends Account
public class User extends Account {
    private UserRole role;
    private boolean active;
    
    // Enum for user roles
    public enum UserRole {
        REGULAR_USER, MANAGER, ADMIN
    }
    
    // Constructor
    public User(int id, String name, String username, String password) 
            throws InvalidUsernameException, WeakPasswordException {
        super(id, name, username, password);
        this.role = UserRole.REGULAR_USER;
        this.active = true;
    }
    
    // Overloaded constructor - polymorphism
    public User(int id, String name, String username, String password, UserRole role) 
            throws InvalidUsernameException, WeakPasswordException {
        super(id, name, username, password);
        this.role = role;
        this.active = true;
    }
    
    // Implementation of abstract method from Account
    @Override
    public boolean authenticate(String password) {
        if (password == null || !active) {
            return false;
        }
        return this.password.equals(password);
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    // Method to change password with exception handling
    public void changePassword(String oldPassword, String newPassword) 
            throws WeakPasswordException, SecurityException {
        if (!authenticate(oldPassword)) {
            throw new SecurityException("Current password is incorrect");
        }
        
        if (newPassword == null || newPassword.length() < 8 || !containsSpecialChar(newPassword)) {
            throw new WeakPasswordException("New password must be at least 8 characters and contain special characters");
        }
        
        this.password = newPassword;
    }
    
    // Polymorphic method - different implementation based on user role
    public String getPermissionInfo() {
        switch (role) {
            case ADMIN:
                return "Has all system privileges";
            case MANAGER:
                return "Can manage tasks and regular users";
            case REGULAR_USER:
                return "Can manage own tasks only";
            default:
                return "Unknown permissions";
        }
    }
    
    // Override toString method
    @Override
    public String toString() {
        return "User: " + getUsername() + 
               " | Name: " + getName() +
               " | Role: " + role +
               " | Active: " + (active ? "Yes" : "No");
    }
}
