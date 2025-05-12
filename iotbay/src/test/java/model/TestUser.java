package model;

public class TestUser {
    public static void main(String[] args) {
        System.out.println("Testing User class...");
        
        // Test creating a user with constructor without ID
        User user1 = new User("John", "Doe", "john.doe@example.com", "password123");
        System.out.println("\nCreated user without ID:");
        System.out.println("First Name: " + user1.getFname());
        System.out.println("Last Name: " + user1.getLname());
        System.out.println("Full Name: " + user1.getFullName());
        System.out.println("Email: " + user1.getEmail());
        System.out.println("Password: " + user1.getPassword());
        
        // Set ID manually (this would normally come from the database)
        user1.setUserID(101);
        System.out.println("User ID set: " + user1.getUserID());
        
        // Test creating a user with constructor with ID
        User user2 = new User(102, "Jane", "Smith", "jane.smith@example.com", "securepass456");
        System.out.println("\nCreated user with ID:");
        System.out.println("User ID: " + user2.getUserID());
        System.out.println("First Name: " + user2.getFname());
        System.out.println("Last Name: " + user2.getLname());
        System.out.println("Full Name: " + user2.getFullName());
        System.out.println("Email: " + user2.getEmail());
        
        // Test updating user properties
        System.out.println("\nUpdating user details:");
        user2.setFname("Janet");
        user2.setEmail("janet.smith@example.com");
        System.out.println("Updated First Name: " + user2.getFname());
        System.out.println("Updated Full Name: " + user2.getFullName());
        System.out.println("Updated Email: " + user2.getEmail());
        
        System.out.println("\nUser tests completed successfully!");
    }
}