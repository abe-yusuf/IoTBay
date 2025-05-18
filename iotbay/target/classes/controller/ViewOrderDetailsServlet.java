package controller;

import model.Order;
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

@WebServlet(name = "ViewOrderDetailsServlet", urlPatterns = {"/viewOrderDetails"})
public class ViewOrderDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (user == null) {
            request.setAttribute("errorMessage", "You must be logged in to view order details.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        String orderIDParam = request.getParameter("orderID");
        if (orderIDParam == null || orderIDParam.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Order ID is required.");
            request.getRequestDispatcher("/viewOrders.jsp").forward(request, response);
            return;
        }
        
        try {
            int orderID = Integer.parseInt(orderIDParam);
            
            Connection conn = DBConnector.getConnection();
            OrderDAO orderDAO = new OrderDAO(conn);
            
            // Get the order details
            Order order = orderDAO.getOrderById(orderID);
            
            if (order == null) {
                request.setAttribute("errorMessage", "Order not found.");
                request.getRequestDispatcher("/viewOrders.jsp").forward(request, response);
                return;
            }
            
            // Check if this order belongs to the logged-in user
            if (order.getUserID() != user.getUserID()) {  // Changed from getCustomerID to getUserID
                request.setAttribute("errorMessage", "You do not have permission to view this order.");
                request.getRequestDispatcher("/viewOrders.jsp").forward(request, response);
                return;
            }
            
            request.setAttribute("order", order);
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            request.setAttribute("errorMessage", "Database error: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            request.setAttribute("errorMessage", "Invalid order ID.");
        }
        
        request.getRequestDispatcher("/orderDetails.jsp").forward(request, response);
    }
}