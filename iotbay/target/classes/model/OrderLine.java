
package model;

import java.io.Serializable;

public class OrderLine implements Serializable {
    private int orderLineID;
    private int orderID;
    private int productID;
    private int quantity;
    private double subtotal;
    private String productName; // For display purposes
    private double productPrice; // For display purposes
    
    // Default constructor
    public OrderLine() {
    }
    
    // Constructor for creating a new order line
    public OrderLine(int productID, int quantity, double productPrice, String productName) {
        this.productID = productID;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.productName = productName;
        this.subtotal = quantity * productPrice;
    }
    
    // Constructor for reading from database
    public OrderLine(int orderLineID, int orderID, int productID, int quantity, double subtotal) {
        this.orderLineID = orderLineID;
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }
    
    // Method to calculate subtotal
    public void calculateSubtotal() {
        this.subtotal = this.quantity * this.productPrice;
    }
    
    // Getters and Setters
    public int getOrderLineID() {
        return orderLineID;
    }
    
    public void setOrderLineID(int orderLineID) {
        this.orderLineID = orderLineID;
    }
    
    public int getOrderID() {
        return orderID;
    }
    
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
    
    public int getProductID() {
        return productID;
    }
    
    public void setProductID(int productID) {
        this.productID = productID;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public double getProductPrice() {
        return productPrice;
    }
    
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
        calculateSubtotal();
    }
    
    @Override
    public String toString() {
        return "OrderLine{" +
                "orderLineID=" + orderLineID +
                ", orderID=" + orderID +
                ", productID=" + productID +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", productPrice=" + productPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}