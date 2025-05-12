package model;

public class OrderLineTest {
    public static void main(String[] args) {
        System.out.println("Testing OrderLine class...");
        
        // Test creating a new order line with constructor
        int productID = 1;
        int quantity = 3;
        double price = 24.99;
        String productName = "Smart Plug";
        
        OrderLine orderLine = new OrderLine(productID, quantity, price, productName);
        
        // Display the created order line details
        System.out.println("\nCreated OrderLine:");
        System.out.println("Product ID: " + orderLine.getProductID());
        System.out.println("Product Name: " + orderLine.getProductName());
        System.out.println("Quantity: " + orderLine.getQuantity());
        System.out.println("Price: $" + orderLine.getProductPrice());
        System.out.println("Subtotal: $" + orderLine.getSubtotal());
        
        // Test changing quantity and verify subtotal recalculation
        System.out.println("\nUpdating quantity from 3 to 5:");
        orderLine.setQuantity(5);
        System.out.println("New Quantity: " + orderLine.getQuantity());
        System.out.println("New Subtotal: $" + orderLine.getSubtotal());
        
        // Test changing price and verify subtotal recalculation
        System.out.println("\nUpdating price from $24.99 to $19.99:");
        orderLine.setProductPrice(19.99);
        System.out.println("New Price: $" + orderLine.getProductPrice());
        System.out.println("New Subtotal: $" + orderLine.getSubtotal());
        
        // Test creating with database constructor
        System.out.println("\nCreating OrderLine with database constructor:");
        OrderLine dbOrderLine = new OrderLine(1, 100, productID, quantity, 74.97);
        System.out.println("OrderLine ID: " + dbOrderLine.getOrderLineID());
        System.out.println("Order ID: " + dbOrderLine.getOrderID());
        System.out.println("Product ID: " + dbOrderLine.getProductID());
        System.out.println("Quantity: " + dbOrderLine.getQuantity());
        System.out.println("Subtotal: $" + dbOrderLine.getSubtotal());
        
        // Test toString method
        System.out.println("\nOrderLine toString output:");
        System.out.println(orderLine);
        
        System.out.println("\nOrderLine tests completed successfully!");
    }
}