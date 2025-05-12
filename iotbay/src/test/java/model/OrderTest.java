package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple test class for testing the Order model
 */
public class OrderTest {
    public static void main(String[] args) {
        System.out.println("Testing Order class...");
        
        // Test creating an order with default constructor
        System.out.println("\nCreating order with default constructor:");
        Order emptyOrder = new Order();
        System.out.println("Empty order created: " + emptyOrder);
        System.out.println("Order lines: " + emptyOrder.getOrderLines().size());
        
        // Test creating an order with user ID
        int userID = 1;
        System.out.println("\nCreating order for User ID " + userID + ":");
        Order userOrder = new Order(userID);
        
        System.out.println("Order Status: " + userOrder.getStatus());
        System.out.println("Order Date: " + userOrder.getOrderDate());
        System.out.println("Initial Total Amount: $" + userOrder.getTotalAmount());
        
        // Test adding order lines
        System.out.println("\nAdding order lines:");
        OrderLine line1 = new OrderLine(1, 2, 149.99, "Smart Thermostat");
        OrderLine line2 = new OrderLine(2, 3, 89.99, "Security Camera");
        
        userOrder.addOrderLine(line1);
        System.out.println("Added line 1 - New total: $" + userOrder.getTotalAmount());
        
        userOrder.addOrderLine(line2);
        System.out.println("Added line 2 - New total: $" + userOrder.getTotalAmount());
        
        // Test removing an order line
        System.out.println("\nTesting order line removal:");
        // Set an ID for the first line so we can remove it
        line1.setOrderLineID(1);
        userOrder.removeOrderLine(1);
        System.out.println("Removed line 1 - New total: $" + userOrder.getTotalAmount());
        System.out.println("Order lines count: " + userOrder.getOrderLines().size());
        
        // Test creating an order from database
        System.out.println("\nCreating order from database values:");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Order dbOrder = new Order(100, userID, now, "Saved", 269.97);
        System.out.println("Database Order ID: " + dbOrder.getOrderID());
        System.out.println("Database Order User ID: " + dbOrder.getUserID());
        System.out.println("Database Order Status: " + dbOrder.getStatus());
        System.out.println("Database Order Total: $" + dbOrder.getTotalAmount());
        
        // Test setOrderLines method
        System.out.println("\nTesting setOrderLines method:");
        List<OrderLine> newLines = new ArrayList<>();
        newLines.add(new OrderLine(3, 1, 199.99, "Smart Door Lock"));
        newLines.add(new OrderLine(4, 4, 24.99, "Smart Plug"));
        
        dbOrder.setOrderLines(newLines);
        System.out.println("Set new order lines - New total: $" + dbOrder.getTotalAmount());
        System.out.println("Order lines count: " + dbOrder.getOrderLines().size());
        
        // Test recalculating the total
        System.out.println("\nTesting total recalculation:");
        OrderLine firstLine = newLines.get(0);
        firstLine.setQuantity(2);
        dbOrder.calculateTotal();
        System.out.println("After changing quantity - New total: $" + dbOrder.getTotalAmount());
        
        // Test toString method
        System.out.println("\nOrder toString output:");
        System.out.println(dbOrder);
        
        System.out.println("\nOrder tests completed successfully!");
    }
}