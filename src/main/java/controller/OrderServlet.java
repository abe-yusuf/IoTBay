package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.OrderDAO;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import model.Product;
import util.DatabaseUtil;

@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        Boolean isStaff = (Boolean) session.getAttribute("isStaff");
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get search parameters
                String searchOrderId = request.getParameter("orderId");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");
                
                // List orders with search
                List<Order> orders;
                if (searchOrderId != null || fromDate != null || toDate != null) {
                    orders = orderDAO.searchUserOrders(userId, searchOrderId, fromDate, toDate);
                } else {
                    orders = orderDAO.getOrdersByUserId(userId);
                }
                
                request.setAttribute("orders", orders);
                request.setAttribute("searchOrderId", searchOrderId);
                request.setAttribute("fromDate", fromDate);
                request.setAttribute("toDate", toDate);
                request.getRequestDispatcher("/WEB-INF/views/orders/list.jsp").forward(request, response);
            } else if (pathInfo.matches("/\\d+")) {
                // View specific order
                int orderId = Integer.parseInt(pathInfo.substring(1));
                Order order = orderDAO.getOrderById(orderId);
                
                // Verify order belongs to user or user is staff
                if (order != null && (order.getUserId() == userId || Boolean.TRUE.equals(isStaff))) {
                    request.setAttribute("order", order);
                    
                    // Load products list for staff or customers with pending orders
                    if (Boolean.TRUE.equals(isStaff) || 
                        (order.getStatus().equals("PENDING") && order.getUserId() == userId)) {
                        try (Connection conn = DatabaseUtil.getConnection()) {
                            ProductDAO productDAO = new ProductDAO(conn);
                            List<Product> products = productDAO.getAllProducts();
                            request.setAttribute("products", products);
                        }
                    }
                    
                    request.getRequestDispatcher("/WEB-INF/views/orders/view.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException("Error retrieving orders", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        Boolean isStaff = (Boolean) session.getAttribute("isStaff");
        String action = request.getParameter("action");

        // Check if user is logged in
        if (userId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            switch (action) {
                case "create":
                    // Allow any logged-in user to create an order
                    createOrder(request, userId);
                    response.sendRedirect(request.getContextPath() + "/orders");
                    break;
                    
                case "update-item-quantity":
                    // Allow customers to update quantity of pending orders
                    if (!Boolean.TRUE.equals(isStaff)) {
                        int orderItemId = Integer.parseInt(request.getParameter("orderItemId"));
                        int quantity = Integer.parseInt(request.getParameter("quantity"));
                        orderDAO.updateOrderItemQuantity(orderItemId, quantity, userId);
                        response.sendRedirect(request.getHeader("Referer"));
                        return;
                    }
                    break;
                    
                case "delete-item":
                    // Allow customers to delete items from pending orders
                    if (!Boolean.TRUE.equals(isStaff)) {
                        int orderItemId = Integer.parseInt(request.getParameter("orderItemId"));
                        orderDAO.deleteOrderItem(orderItemId, userId);
                        response.sendRedirect(request.getHeader("Referer"));
                        return;
                    }
                    break;

                case "cancel-order":
                    // Allow customers to cancel their pending orders
                    if (!Boolean.TRUE.equals(isStaff)) {
                        int orderId = Integer.parseInt(request.getParameter("orderId"));
                        try {
                            orderDAO.cancelOrderByCustomer(orderId, userId);
                            request.getSession().setAttribute("message", "Order cancelled successfully.");
                        } catch (SQLException e) {
                            request.getSession().setAttribute("error", e.getMessage());
                        }
                        response.sendRedirect(request.getHeader("Referer"));
                        return;
                    }
                    break;

                case "update-customer":
                    // Allow customers to update their details for pending orders
                    if (!Boolean.TRUE.equals(isStaff)) {
                        int orderId = Integer.parseInt(request.getParameter("orderId"));
                        // Verify the order belongs to the user and is pending
                        if (orderDAO.isOrderEditableByCustomer(orderId, userId)) {
                            String firstName = request.getParameter("firstName");
                            String lastName = request.getParameter("lastName");
                            String phone = request.getParameter("phone");
                            String address = request.getParameter("address");
                            orderDAO.updateOrderCustomerDetails(orderId, firstName, lastName, phone, address);
                            response.sendRedirect(request.getHeader("Referer"));
                            return;
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                            return;
                        }
                    }
                    break;

                case "add-item":
                    // Allow customers to add items to pending orders
                    if (!Boolean.TRUE.equals(isStaff)) {
                        int orderId = Integer.parseInt(request.getParameter("orderId"));
                        // Verify the order belongs to the user and is pending
                        if (orderDAO.isOrderEditableByCustomer(orderId, userId)) {
                            addOrderItem(request);
                            response.sendRedirect(request.getHeader("Referer"));
                            return;
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                            return;
                        }
                    }
                    break;
                    
                // Staff-only actions
                case "update-status":
                case "update-item":
                    if (!Boolean.TRUE.equals(isStaff)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    switch (action) {
                        case "update-status":
                            updateOrderStatus(request);
                            break;
                        case "update-item":
                            updateOrderItem(request);
                            break;
                    }
                    response.sendRedirect(request.getHeader("Referer"));
                    break;
                    
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new ServletException("Error processing order operation", e);
        }
    }

    private void createOrder(HttpServletRequest request, int userId) throws SQLException {
        orderDAO.createOrderFromCart(userId);
    }

    private void updateOrderStatus(HttpServletRequest request) throws SQLException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String status = request.getParameter("status");
        orderDAO.updateOrderStatus(orderId, OrderStatus.valueOf(status));
    }

    private void addOrderItem(HttpServletRequest request) throws SQLException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        // Get product details to get current price
        try (Connection conn = DatabaseUtil.getConnection()) {
            ProductDAO productDAO = new ProductDAO(conn);
            Product product = productDAO.getProductById(productId);
            
            if (product != null) {
                OrderItem item = new OrderItem(
                    0, // ID will be generated
                    orderId,
                    productId,
                    product.getName(),
                    quantity,
                    product.getPrice()
                );
                
                // Get user ID from session
                HttpSession session = request.getSession();
                Integer userId = (Integer) session.getAttribute("userId");
                
                orderDAO.addOrderItem(item, userId);
            }
        }
    }

    private void updateOrderItem(HttpServletRequest request) throws SQLException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        int orderItemId = Integer.parseInt(request.getParameter("orderItemId"));
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        double price = Double.parseDouble(request.getParameter("price"));
        
        // Get the old item to calculate total difference
        OrderItem oldItem = orderDAO.getOrderItemById(orderItemId);
        double oldSubtotal = oldItem.getQuantity() * oldItem.getPrice();
        double newSubtotal = quantity * price;
        
        // Update the item
        OrderItem item = new OrderItem(orderItemId, orderId, productId, "", quantity, price);
        orderDAO.updateOrderItem(item);
        
        // Update order total
        Order order = orderDAO.getOrderById(orderId);
        double newTotal = order.getTotalAmount() - oldSubtotal + newSubtotal;
        orderDAO.updateOrderTotal(orderId, newTotal);
    }
} 