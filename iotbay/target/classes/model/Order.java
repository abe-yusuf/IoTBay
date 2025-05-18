package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private int orderID;
    private int userID;  // Changed from customerID to userID
    private Timestamp orderDate;
    private String status;
    private double totalAmount;
    private List<OrderLine> orderLines;
    
    // Default constructor
    public Order() {
        this.orderLines = new ArrayList<>();
    }
    
    // Constructor for creating a new order
    public Order(int userID) {  // Changed from customerID to userID
        this.userID = userID;  // Changed from customerID to userID
        this.orderDate = new Timestamp(System.currentTimeMillis());
        this.status = "Saved";
        this.totalAmount = 0.0;
        this.orderLines = new ArrayList<>();
    }
    
    // Constructor for reading from database
    public Order(int orderID, int userID, Timestamp orderDate, String status, double totalAmount) {  // Changed from customerID to userID
        this.orderID = orderID;
        this.userID = userID;  // Changed from customerID to userID
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderLines = new ArrayList<>();
    }
    
    // Method to recalculate the total amount
    public void calculateTotal() {
        this.totalAmount = 0.0;
        for (OrderLine line : orderLines) {
            this.totalAmount += line.getSubtotal();
        }
    }
    
    // Method to add a new order line
    public void addOrderLine(OrderLine line) {
        orderLines.add(line);
        calculateTotal();
    }
    
    // Method to remove an order line
    public void removeOrderLine(int orderLineID) {
        orderLines.removeIf(line -> line.getOrderLineID() == orderLineID);
        calculateTotal();
    }
    
    // Getters and Setters
    public int getOrderID() {
        return orderID;
    }
    
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
    
    public int getUserID() {  // Changed from getCustomerID to getUserID
        return userID;  // Changed from customerID to userID
    }
    
    public void setUserID(int userID) {  // Changed from setCustomerID to setUserID
        this.userID = userID;  // Changed from customerID to userID
    }
    
    public Timestamp getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
    
    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
        calculateTotal();
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", userID=" + userID +  // Changed from customerID to userID
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                ", orderLines=" + orderLines.size() +
                '}';
    }
}