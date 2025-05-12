package controller;

import model.Order;
import model.OrderLine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private Connection conn;
    
    public OrderDAO(Connection conn) {
        this.conn = conn;
    }
    
    // Create a new order
    public int createOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (userID, orderDate, status, totalAmount) VALUES (?, ?, ?, ?)";  // Changed customerID to userID
        
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, order.getUserID());  // Changed getCustomerID to getUserID
        stmt.setTimestamp(2, order.getOrderDate());
        stmt.setString(3, order.getStatus());
        stmt.setDouble(4, order.getTotalAmount());
        
        int affectedRows = stmt.executeUpdate();
        
        if (affectedRows == 0) {
            throw new SQLException("Creating order failed, no rows affected.");
        }
        
        int orderID;
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                orderID = generatedKeys.getInt(1);
                order.setOrderID(orderID);
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        }
        
        stmt.close();
        return orderID;
    }
    
    // Add order lines to an order
    public void addOrderLines(List<OrderLine> orderLines, int orderID) throws SQLException {
        String query = "INSERT INTO order_lines (orderID, productID, quantity, subtotal) VALUES (?, ?, ?, ?)";
        
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        
        for (OrderLine line : orderLines) {
            stmt.setInt(1, orderID);
            stmt.setInt(2, line.getProductID());
            stmt.setInt(3, line.getQuantity());
            stmt.setDouble(4, line.getSubtotal());
            stmt.addBatch();
        }
        
        stmt.executeBatch();
        
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        int i = 0;
        while (generatedKeys.next() && i < orderLines.size()) {
            orderLines.get(i).setOrderLineID(generatedKeys.getInt(1));
            orderLines.get(i).setOrderID(orderID);
            i++;
        }
        
        stmt.close();
    }
    
    // Get all orders for a user
    public List<Order> getOrdersByUserId(int userID) throws SQLException {  // Changed from getOrdersByCustomerId to getOrdersByUserId
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE userID = ? ORDER BY orderDate DESC";  // Changed customerID to userID
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userID);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Order order = new Order(
                rs.getInt("orderID"),
                rs.getInt("userID"),  // Changed customerID to userID
                rs.getTimestamp("orderDate"),
                rs.getString("status"),
                rs.getDouble("totalAmount")
            );
            orders.add(order);
        }
        
        rs.close();
        stmt.close();
        return orders;
    }
    
    // Get a specific order by ID
    public Order getOrderById(int orderID) throws SQLException {
        String query = "SELECT * FROM orders WHERE orderID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, orderID);
        ResultSet rs = stmt.executeQuery();
        
        Order order = null;
        if (rs.next()) {
            order = new Order(
                rs.getInt("orderID"),
                rs.getInt("userID"),  // Changed customerID to userID
                rs.getTimestamp("orderDate"),
                rs.getString("status"),
                rs.getDouble("totalAmount")
            );
            
            // Load order lines
            order.setOrderLines(getOrderLinesForOrder(orderID));
        }
        
        rs.close();
        stmt.close();
        return order;
    }
    
    // Get order lines for a specific order
    public List<OrderLine> getOrderLinesForOrder(int orderID) throws SQLException {
        List<OrderLine> orderLines = new ArrayList<>();
        String query = "SELECT ol.*, p.name, p.price FROM order_lines ol " +
                      "JOIN products p ON ol.productID = p.productID " +
                      "WHERE ol.orderID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, orderID);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            OrderLine line = new OrderLine(
                rs.getInt("orderLineID"),
                rs.getInt("orderID"),
                rs.getInt("productID"),
                rs.getInt("quantity"),
                rs.getDouble("subtotal")
            );
            line.setProductName(rs.getString("name"));
            line.setProductPrice(rs.getDouble("price"));
            orderLines.add(line);
        }
        
        rs.close();
        stmt.close();
        return orderLines;
    }
    
    // Update an order
    public void updateOrder(Order order) throws SQLException {
        String query = "UPDATE orders SET status = ?, totalAmount = ? WHERE orderID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, order.getStatus());
        stmt.setDouble(2, order.getTotalAmount());
        stmt.setInt(3, order.getOrderID());
        
        stmt.executeUpdate();
        stmt.close();
    }
    
    // Delete all order lines for an order
    public void deleteOrderLines(int orderID) throws SQLException {
        String query = "DELETE FROM order_lines WHERE orderID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, orderID);
        
        stmt.executeUpdate();
        stmt.close();
    }
    
    // Delete a specific order line
    public void deleteOrderLine(int orderLineID) throws SQLException {
        String query = "DELETE FROM order_lines WHERE orderLineID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, orderLineID);
        
        stmt.executeUpdate();
        stmt.close();
    }
    
    // Get order line by ID
    public OrderLine getOrderLineById(int orderLineID) throws SQLException {
        String query = "SELECT ol.*, p.name, p.price FROM order_lines ol " +
                      "JOIN products p ON ol.productID = p.productID " +
                      "WHERE ol.orderLineID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, orderLineID);
        ResultSet rs = stmt.executeQuery();
        
        OrderLine line = null;
        if (rs.next()) {
            line = new OrderLine(
                rs.getInt("orderLineID"),
                rs.getInt("orderID"),
                rs.getInt("productID"),
                rs.getInt("quantity"),
                rs.getDouble("subtotal")
            );
            line.setProductName(rs.getString("name"));
            line.setProductPrice(rs.getDouble("price"));
        }
        
        rs.close();
        stmt.close();
        return line;
    }
    
    // Update order line quantity
    public void updateOrderLineQuantity(int orderLineID, int quantity, double subtotal) throws SQLException {
        String query = "UPDATE order_lines SET quantity = ?, subtotal = ? WHERE orderLineID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, quantity);
        stmt.setDouble(2, subtotal);
        stmt.setInt(3, orderLineID);
        
        stmt.executeUpdate();
        stmt.close();
    }
    
    // Cancel an order
    public void cancelOrder(int orderID) throws SQLException {
        String query = "UPDATE orders SET status = 'Cancelled' WHERE orderID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, orderID);
        
        stmt.executeUpdate();
        stmt.close();
    }
}