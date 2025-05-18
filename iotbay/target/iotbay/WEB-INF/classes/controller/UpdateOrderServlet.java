package controller;

import model.Order;
import model.OrderLine;
import model.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "UpdateOrderServlet", urlPatterns = {"/updateOrder"})
public class UpdateOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (user == null) {
            request.setAttribute("errorMessage", "You must be logged in to update orders.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        String action = request.getParameter("action");
        String orderIDParam = request.getParameter("orderID");
        
        if (orderIDParam == null || orderIDParam.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Order ID is required.");
            response.sendRedirect(request.getContextPath() + "/viewOrders");
            return;
        }
        
        try {
            int orderID = Integer.parseInt(orderIDParam);
            Connection conn = DBConnector.getConnection();
            OrderDAO orderDAO = new OrderDAO(conn);
            ProductDAO productDAO = new ProductDAO(conn);
            
            // Get the order
            Order order = orderDAO.getOrderById(orderID);
            
            if (order == null) {
                request.setAttribute("errorMessage", "Order not found.");
                response.sendRedirect(request.getContextPath() + "/viewOrders");
                return;
            }
            
            // Check if this order belongs to the logged-in user
            if (order.getUserID() != user.getUserID()) {
                request.setAttribute("errorMessage", "You do not have permission to update this order.");
                response.sendRedirect(request.getContextPath() + "/viewOrders");
                return;
            }
            
            // Check if the order is in a state that can be updated
            if (!"Saved".equals(order.getStatus())) {
                request.setAttribute("errorMessage", "Only saved orders can be updated.");
                response.sendRedirect(request.getContextPath() + "/viewOrderDetails?orderID=" + orderID);
                return;
            }
            
            conn.setAutoCommit(false);
            try {
                if ("updateQuantity".equals(action)) {
                    // Update order line quantity
                    String orderLineIDParam = request.getParameter("orderLineID");
                    String quantityParam = request.getParameter("quantity");
                    
                    if (orderLineIDParam == null || quantityParam == null) {
                        throw new IllegalArgumentException("Order line ID and quantity are required.");
                    }
                    
                    int orderLineID = Integer.parseInt(orderLineIDParam);
                    int newQuantity = Integer.parseInt(quantityParam);
                    
                    if (newQuantity <= 0) {
                        throw new IllegalArgumentException("Quantity must be greater than zero.");
                    }
                    
                    // Get the order line
                    OrderLine orderLine = orderDAO.getOrderLineById(orderLineID);
                    
                    if (orderLine == null || orderLine.getOrderID() != orderID) {
                        throw new IllegalArgumentException("Order line not found or does not belong to this order.");
                    }
                    
                    // Check if we need to allocate more stock
                    int quantityDifference = newQuantity - orderLine.getQuantity();
                    
                    if (quantityDifference > 0) {
                        // Need to check if additional stock is available
                        if (!productDAO.checkStock(orderLine.getProductID(), quantityDifference)) {
                            throw new IllegalArgumentException("Not enough stock available.");
                        }
                        
                        // Decrease product stock for the additional quantity
                        productDAO.decreaseStock(orderLine.getProductID(), quantityDifference);
                    } else if (quantityDifference < 0) {
                        // Increase product stock for the reduced quantity
                        productDAO.increaseStock(orderLine.getProductID(), -quantityDifference);
                    }
                    
                    // Update the order line
                    double newSubtotal = orderLine.getProductPrice() * newQuantity;
                    orderDAO.updateOrderLineQuantity(orderLineID, newQuantity, newSubtotal);
                    
                    // Recalculate order total
                    order = orderDAO.getOrderById(orderID);
                    double newTotal = 0;
                    for (OrderLine line : order.getOrderLines()) {
                        newTotal += line.getSubtotal();
                    }
                    
                    order.setTotalAmount(newTotal);
                    orderDAO.updateOrder(order);
                    
                } else if ("removeItem".equals(action)) {
                    // Remove item from order
                    String orderLineIDParam = request.getParameter("orderLineID");
                    
                    if (orderLineIDParam == null) {
                        throw new IllegalArgumentException("Order line ID is required.");
                    }
                    
                    int orderLineID = Integer.parseInt(orderLineIDParam);
                    
                    // Get the order line
                    OrderLine orderLine = orderDAO.getOrderLineById(orderLineID);
                    
                    if (orderLine == null || orderLine.getOrderID() != orderID) {
                        throw new IllegalArgumentException("Order line not found or does not belong to this order.");
                    }
                    
                    // Restore stock
                    productDAO.increaseStock(orderLine.getProductID(), orderLine.getQuantity());
                    
                    // Delete the order line
                    orderDAO.deleteOrderLine(orderLineID);
                    
                    // Recalculate order total
                    order = orderDAO.getOrderById(orderID);
                    double newTotal = 0;
                    for (OrderLine line : order.getOrderLines()) {
                        newTotal += line.getSubtotal();
                    }
                    
                    order.setTotalAmount(newTotal);
                    orderDAO.updateOrder(order);
                }
                
                conn.commit();
                request.setAttribute("successMessage", "Order updated successfully!");
                
            } catch (Exception ex) {
                conn.rollback();
                request.setAttribute("errorMessage", "Error updating order: " + ex.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
            
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            request.setAttribute("errorMessage", "Database error: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            request.setAttribute("errorMessage", "Invalid input: " + ex.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/viewOrderDetails?orderID=" + orderIDParam);
    }
}