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
                    
                    // Load products list for staff view
                    if (Boolean.TRUE.equals(isStaff)) {
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
                    
                // Staff-only actions
                case "update-status":
                case "add-item":
                case "update-item":
                case "delete-item":
                case "update-customer":
                    if (!Boolean.TRUE.equals(isStaff)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    switch (action) {
                        case "update-status":
                            updateOrderStatus(request);
                            break;
                        case "add-item":
                            addOrderItem(request);
                            break;
                        case "update-item":
                            updateOrderItem(request);
                            break;
                        case "delete-item":
                            deleteOrderItem(request);
                            break;
                        case "update-customer":
                            updateCustomerDetails(request);
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
                
                orderDAO.addOrderItem(item);
                
                // Update order total
                Order order = orderDAO.getOrderById(orderId);
                double newTotal = order.getTotalAmount() + (quantity * product.getPrice());
                orderDAO.updateOrderTotal(orderId, newTotal);
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

    private void deleteOrderItem(HttpServletRequest request) throws SQLException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        int orderItemId = Integer.parseInt(request.getParameter("orderItemId"));
        
        // Get the item to subtract its total
        OrderItem item = orderDAO.getOrderItemById(orderItemId);
        double subtotal = item.getQuantity() * item.getPrice();
        
        // Delete the item
        orderDAO.deleteOrderItem(orderItemId);
        
        // Update order total
        Order order = orderDAO.getOrderById(orderId);
        double newTotal = order.getTotalAmount() - subtotal;
        orderDAO.updateOrderTotal(orderId, newTotal);
    }

    private void updateCustomerDetails(HttpServletRequest request) throws SQLException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        orderDAO.updateOrderCustomerDetails(orderId, firstName, lastName, phone, address);
    }
} 