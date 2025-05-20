package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Order;
import model.OrderLine;
import model.User;

@WebServlet(name = "CancelOrderServlet", urlPatterns = {"/cancelOrder"})
public class CancelOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            request.setAttribute("errorMessage", "You must be logged in to cancel orders.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
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
            
            Order order = orderDAO.getOrderById(orderID);
            
            if (order == null) {
                request.setAttribute("errorMessage", "Order not found.");
                response.sendRedirect(request.getContextPath() + "/viewOrders");
                return;
            }
            
            if (order.getUserID() != user.getUserID()) {  
                request.setAttribute("errorMessage", "You do not have permission to cancel this order.");
                response.sendRedirect(request.getContextPath() + "/viewOrders");
                return;
            }
            
            if (!"Saved".equals(order.getStatus())) {
                request.setAttribute("errorMessage", "Only saved orders can be cancelled.");
                response.sendRedirect(request.getContextPath() + "/viewOrderDetails?orderID=" + orderID);
                return;
            }
            
            conn.setAutoCommit(false);
            try {
                orderDAO.cancelOrder(orderID);

                for (OrderLine line : order.getOrderLines()) {
                    productDAO.increaseStock(line.getProductID(), line.getQuantity());
                }

                // Access Log
                AccessLogServlet.logAction(request, "Cancelled order #" + orderID);
                
                conn.commit();
                request.setAttribute("successMessage", "Order cancelled successfully!");
                
            } catch (Exception ex) {
                conn.rollback();
                request.setAttribute("errorMessage", "Error cancelling order: " + ex.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
            
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            request.setAttribute("errorMessage", "Database error: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            request.setAttribute("errorMessage", "Invalid order ID.");
        }
        
        response.sendRedirect(request.getContextPath() + "/viewOrderDetails?orderID=" + orderIDParam);
    }
}
