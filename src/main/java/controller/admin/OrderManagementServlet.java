package controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.OrderStatus;

@WebServlet("/admin/orders")
public class OrderManagementServlet extends HttpServlet {
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check authentication and authorization
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        Boolean isStaff = (Boolean) session.getAttribute("isStaff");
        
        // Check if user is logged in
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        // Check if user is staff
        if (!Boolean.TRUE.equals(isStaff)) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        try {
            String orderIdParam = request.getParameter("orderId");
            List<Order> orders;

            if (orderIdParam != null && !orderIdParam.trim().isEmpty()) {
                try {
                    int orderId = Integer.parseInt(orderIdParam);
                    Order order = orderDAO.getOrderById(orderId);
                    orders = order != null ? List.of(order) : List.of();
                } catch (NumberFormatException e) {
                    orders = orderDAO.getAllOrders();
                }
            } else {
                orders = orderDAO.getAllOrders();
            }

            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving orders", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check authentication and authorization
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        Boolean isStaff = (Boolean) session.getAttribute("isStaff");
        
        // Check if user is logged in
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        // Check if user is staff
        if (!Boolean.TRUE.equals(isStaff)) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String action = request.getParameter("action");
        
        if ("update-status".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String status = request.getParameter("status");
                
                orderDAO.updateOrderStatus(orderId, OrderStatus.valueOf(status));
                response.sendRedirect(request.getContextPath() + "/admin/orders");
            } catch (SQLException e) {
                throw new ServletException("Error updating order status", e);
            }
        }
    }
} 