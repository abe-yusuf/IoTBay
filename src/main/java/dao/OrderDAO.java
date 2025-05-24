package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.Cart;
import model.CartItem;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import model.Product;
import util.DatabaseUtil;

public class OrderDAO {
    private Connection conn;
    private boolean isExternalConnection;
    
    public OrderDAO(Connection conn) {
        this.conn = conn;
        this.isExternalConnection = true;
    }
    
    public OrderDAO() {
        this.isExternalConnection = false;
    }
    
    private Connection getConnection() throws SQLException {
        if (isExternalConnection) {
            return conn;
        }
        return DatabaseUtil.getConnection();
    }
    
    private void closeConnection(Connection conn) throws SQLException {
        if (!isExternalConnection && conn != null) {
            conn.close();
        }
    }
    
    public List<Order> getOrdersByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.first_name, u.last_name, u.phone, u.address " +
                    "FROM orders o " +
                    "JOIN users u ON o.user_id = u.user_id " +
                    "WHERE o.user_id = ? " +
                    "ORDER BY o.order_date DESC";
        
        Connection localConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            localConn = getConnection();
            stmt = localConn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("order_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("status")
                );
                
                // Set user details
                order.setUserFirstName(rs.getString("first_name"));
                order.setUserLastName(rs.getString("last_name"));
                order.setUserPhone(rs.getString("phone"));
                order.setUserAddress(rs.getString("address"));
                
                // Load order items
                order.setOrderItems(getOrderItems(order.getOrderId()));
                
                orders.add(order);
            }
            return orders;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            closeConnection(localConn);
        }
    }
    
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.first_name, u.last_name, u.phone, u.address " +
                    "FROM orders o " +
                    "JOIN users u ON o.user_id = u.user_id " +
                    "ORDER BY o.order_date DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("order_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("status")
                );
                
                // Set user details
                order.setUserFirstName(rs.getString("first_name"));
                order.setUserLastName(rs.getString("last_name"));
                order.setUserPhone(rs.getString("phone"));
                order.setUserAddress(rs.getString("address"));
                
                // Load order items
                order.setOrderItems(getOrderItems(order.getOrderId()));
                
                orders.add(order);
            }
        }
        return orders;
    }
    
    public Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT o.*, u.first_name, u.last_name, u.phone, u.address " +
                    "FROM orders o " +
                    "JOIN users u ON o.user_id = u.user_id " +
                    "WHERE o.order_id = ?";
        
        Connection localConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            localConn = getConnection();
            stmt = localConn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Order order = new Order(
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("order_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("status")
                );
                
                // Set user details
                order.setUserFirstName(rs.getString("first_name"));
                order.setUserLastName(rs.getString("last_name"));
                order.setUserPhone(rs.getString("phone"));
                order.setUserAddress(rs.getString("address"));
                
                // Load order items
                order.setOrderItems(getOrderItems(orderId));
                return order;
            }
            return null;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            closeConnection(localConn);
        }
    }
    
    public List<OrderItem> getOrderItems(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.name as product_name " +
                    "FROM order_items oi " +
                    "JOIN products p ON oi.product_id = p.product_id " +
                    "WHERE oi.order_id = ?";
        
        Connection localConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            localConn = getConnection();
            stmt = localConn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                OrderItem item = new OrderItem(
                    rs.getInt("order_item_id"),
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                );
                items.add(item);
            }
            return items;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            closeConnection(localConn);
        }
    }
    
    public int createOrder(Order order, Connection conn) throws SQLException {
        String sql = "INSERT INTO orders (user_id, total_amount, status, order_date) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getUserId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setString(3, order.getStatus());
            stmt.setTimestamp(4, order.getOrderDate());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
    
    public int createOrder(Order order) throws SQLException {
        try (Connection conn = getConnection()) {
            return createOrder(order, conn);
        }
    }
    
    public void addOrderItems(int orderId, List<OrderItem> items, Connection conn) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (OrderItem item : items) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getPrice());
                stmt.executeUpdate();
            }
        }
    }
    
    public void addOrderItems(int orderId, List<OrderItem> items) throws SQLException {
        try (Connection conn = getConnection()) {
            addOrderItems(orderId, items, conn);
        }
    }
    
    public Order createOrderFromCart(int userId) throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Get cart items
            CartDAO cartDAO = new CartDAO();
            Cart cart = cartDAO.getCart(userId);
            
            if (cart.getItems().isEmpty()) {
                throw new SQLException("Cart is empty");
            }
            
            // Verify stock availability and update product quantities
            ProductDAO productDAO = new ProductDAO(conn);
            for (CartItem cartItem : cart.getItems()) {
                Product product = productDAO.getProductById(cartItem.getProductId());
                if (product == null) {
                    throw new SQLException("Product not found: " + cartItem.getProductId());
                }
                if (product.getQuantity() < cartItem.getQuantity()) {
                    throw new SQLException("Insufficient stock for product: " + product.getName());
                }
                // Decrease product quantity
                product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                productDAO.updateProduct(product);
            }
            
            // Create order
            Order order = new Order(
                0, // ID will be generated
                userId,
                new Timestamp(System.currentTimeMillis()),
                cart.getTotalAmount(),
                "PENDING"
            );
            
            // Insert order
            int orderId = createOrder(order, conn);
            order.setOrderId(orderId);
            
            // Convert cart items to order items
            List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> new OrderItem(0, orderId, cartItem.getProductId(), 
                     cartItem.getProductName(), cartItem.getQuantity(), cartItem.getPrice()))
                .collect(Collectors.toList());
            
            // Insert order items
            addOrderItems(orderId, orderItems, conn);
            
            // Clear cart
            cartDAO.clearCart(userId);
            
            conn.commit();
            return order;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    closeConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void cancelOrder(int orderId) throws SQLException {
        String sql = "UPDATE orders SET status = 'CANCELLED' WHERE order_id = ? AND status = 'PENDING'";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }
    
    public void addOrderItem(OrderItem item, int userId) throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Verify order belongs to user and is pending
            String checkSql = "SELECT status, user_id FROM orders WHERE order_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, item.getOrderId());
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next() || !rs.getString("status").equals("PENDING") || rs.getInt("user_id") != userId) {
                    throw new SQLException("Cannot add item to this order");
                }
            }
            
            // Check product stock
            ProductDAO productDAO = new ProductDAO(conn);
            Product product = productDAO.getProductById(item.getProductId());
            
            if (product.getQuantity() < item.getQuantity()) {
                throw new SQLException("Insufficient stock for product: " + product.getName());
            }
            
            // Update product stock
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productDAO.updateProduct(product);
            
            // Add the item
            String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, item.getOrderId());
                stmt.setInt(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getPrice());
                stmt.executeUpdate();
            }
            
            // Update order total
            String updateOrderSql = "UPDATE orders o SET total_amount = " +
                                  "(SELECT SUM(oi.quantity * oi.price) FROM order_items oi WHERE oi.order_id = ?) " +
                                  "WHERE order_id = ?";
            try (PreparedStatement updateOrderStmt = conn.prepareStatement(updateOrderSql)) {
                updateOrderStmt.setInt(1, item.getOrderId());
                updateOrderStmt.setInt(2, item.getOrderId());
                updateOrderStmt.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                closeConnection(conn);
            }
        }
    }
    
    public void updateOrderTotal(int orderId, double newTotal) throws SQLException {
        String sql = "UPDATE orders SET total_amount = ? WHERE order_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newTotal);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        }
    }
    
    public OrderItem getOrderItemById(int orderItemId) throws SQLException {
        String sql = "SELECT oi.*, p.name as product_name FROM order_items oi " +
                    "JOIN products p ON oi.product_id = p.product_id " +
                    "WHERE oi.order_item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderItemId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new OrderItem(
                    rs.getInt("order_item_id"),
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                );
            }
            return null;
        }
    }
    
    public void updateOrderItem(OrderItem item) throws SQLException {
        String sql = "UPDATE order_items SET product_id = ?, quantity = ?, price = ? WHERE order_item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getProductId());
            stmt.setInt(2, item.getQuantity());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, item.getOrderItemId());
            stmt.executeUpdate();
        }
    }
    
    public void deleteOrderItem(int orderItemId, int userId) throws SQLException {
        // First verify that this order item belongs to a pending order owned by this user
        String checkSql = "SELECT o.status, o.user_id, oi.product_id, oi.quantity FROM orders o " +
                         "JOIN order_items oi ON o.order_id = oi.order_id " +
                         "WHERE oi.order_item_id = ?";
                         
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Get current order item details
            int productId;
            int quantity;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, orderItemId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next() || !rs.getString("status").equals("PENDING") || rs.getInt("user_id") != userId) {
                    throw new SQLException("Cannot delete this order item");
                }
                productId = rs.getInt("product_id");
                quantity = rs.getInt("quantity");
            }
            
            // Restore product stock
            ProductDAO productDAO = new ProductDAO(conn);
            Product product = productDAO.getProductById(productId);
            product.setQuantity(product.getQuantity() + quantity);
            productDAO.updateProduct(product);
            
            // Delete the item
            String deleteSql = "DELETE FROM order_items WHERE order_item_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, orderItemId);
                deleteStmt.executeUpdate();
            }
            
            // Update the order total
            String updateOrderSql = "UPDATE orders o SET total_amount = " +
                                  "(SELECT COALESCE(SUM(oi.quantity * oi.price), 0) FROM order_items oi WHERE oi.order_id = o.order_id) " +
                                  "WHERE order_id = (SELECT order_id FROM order_items WHERE order_item_id = ?)";
            try (PreparedStatement updateOrderStmt = conn.prepareStatement(updateOrderSql)) {
                updateOrderStmt.setInt(1, orderItemId);
                updateOrderStmt.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                closeConnection(conn);
            }
        }
    }
    
    public void updateOrderCustomerDetails(int orderId, String firstName, String lastName, String phone, String address) throws SQLException {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, phone = ?, address = ? " +
                    "WHERE user_id = (SELECT user_id FROM orders WHERE order_id = ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setInt(5, orderId);
            stmt.executeUpdate();
        }
    }
    
    public void updateOrderStatus(int orderId, OrderStatus status) throws SQLException {
        Connection localConn = null;
        try {
            localConn = getConnection();
            if (!isExternalConnection) {
                localConn.setAutoCommit(false);
            }
            
            // Get current order status
            String getStatusSql = "SELECT status FROM orders WHERE order_id = ?";
            try (PreparedStatement stmt = localConn.prepareStatement(getStatusSql)) {
                stmt.setInt(1, orderId);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("Order not found");
                }
                String currentStatus = rs.getString("status");
                
                // If cancelling a non-cancelled order, restore product quantities
                if (status == OrderStatus.CANCELLED && !currentStatus.equals("CANCELLED")) {
                    ProductDAO productDAO = new ProductDAO(localConn);
                    List<OrderItem> items = getOrderItems(orderId);
                    for (OrderItem item : items) {
                        Product product = productDAO.getProductById(item.getProductId());
                        product.setQuantity(product.getQuantity() + item.getQuantity());
                        productDAO.updateProduct(product);
                    }
                }
            }
            
            // Update order status
            String updateSql = "UPDATE orders SET status = ? WHERE order_id = ?";
            try (PreparedStatement stmt = localConn.prepareStatement(updateSql)) {
                stmt.setString(1, status.toString());
                stmt.setInt(2, orderId);
                stmt.executeUpdate();
            }
            
            if (!isExternalConnection) {
                localConn.commit();
            }
        } catch (SQLException e) {
            if (!isExternalConnection && localConn != null) {
                try {
                    localConn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (!isExternalConnection && localConn != null) {
                try {
                    localConn.setAutoCommit(true);
                    localConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public List<Order> searchUserOrders(int userId, String orderId, String fromDate, String toDate) throws SQLException {
        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT o.*, u.first_name, u.last_name, u.phone, u.address " +
            "FROM orders o " +
            "JOIN users u ON o.user_id = u.user_id " +
            "WHERE o.user_id = ? "
        );
        
        List<Object> params = new ArrayList<>();
        params.add(userId);
        
        // Add order ID filter if provided
        if (orderId != null && !orderId.trim().isEmpty()) {
            sql.append("AND o.order_id = ? ");
            params.add(Integer.parseInt(orderId.trim()));
        }
        
        // Add date range filter if provided
        if (fromDate != null && !fromDate.trim().isEmpty()) {
            sql.append("AND o.order_date >= ? ");
            params.add(java.sql.Date.valueOf(fromDate.trim()));
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            sql.append("AND o.order_date < {fn TIMESTAMPADD(SQL_TSI_DAY, 1, ?)} ");
            params.add(java.sql.Date.valueOf(toDate.trim()));
        }
        
        sql.append("ORDER BY o.order_date DESC");
        
        Connection localConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            localConn = getConnection();
            stmt = localConn.prepareStatement(sql.toString());
            
            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("order_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("status")
                );
                
                // Set user details
                order.setUserFirstName(rs.getString("first_name"));
                order.setUserLastName(rs.getString("last_name"));
                order.setUserPhone(rs.getString("phone"));
                order.setUserAddress(rs.getString("address"));
                
                // Load order items
                order.setOrderItems(getOrderItems(order.getOrderId()));
                
                orders.add(order);
            }
            return orders;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            closeConnection(localConn);
        }
    }
    
    public boolean isOrderEditableByCustomer(int orderId, int userId) throws SQLException {
        String sql = "SELECT status FROM orders WHERE order_id = ? AND user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String status = rs.getString("status");
                return status.equals("PENDING");
            }
            return false;
        }
    }
    
    public void updateOrderItemQuantity(int orderItemId, int quantity, int userId) throws SQLException {
        // First verify that this order item belongs to a pending order owned by this user
        String checkSql = "SELECT o.status, o.user_id, oi.product_id, oi.quantity as old_quantity FROM orders o " +
                         "JOIN order_items oi ON o.order_id = oi.order_id " +
                         "WHERE oi.order_item_id = ?";
                         
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Get current order item details
            int productId;
            int oldQuantity;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, orderItemId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next() || !rs.getString("status").equals("PENDING") || rs.getInt("user_id") != userId) {
                    throw new SQLException("Cannot modify this order item");
                }
                productId = rs.getInt("product_id");
                oldQuantity = rs.getInt("old_quantity");
            }
            
            // Check product stock
            ProductDAO productDAO = new ProductDAO(conn);
            Product product = productDAO.getProductById(productId);
            int stockDifference = quantity - oldQuantity;
            
            if (product.getQuantity() < stockDifference) {
                throw new SQLException("Insufficient stock for product: " + product.getName());
            }
            
            // Update product stock
            product.setQuantity(product.getQuantity() - stockDifference);
            productDAO.updateProduct(product);
            
            // Update the quantity
            String updateSql = "UPDATE order_items SET quantity = ? WHERE order_item_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, orderItemId);
                updateStmt.executeUpdate();
            }
            
            // Update the order total
            String updateOrderSql = "UPDATE orders o SET total_amount = " +
                                  "(SELECT SUM(oi.quantity * oi.price) FROM order_items oi WHERE oi.order_id = o.order_id) " +
                                  "WHERE order_id = (SELECT order_id FROM order_items WHERE order_item_id = ?)";
            try (PreparedStatement updateOrderStmt = conn.prepareStatement(updateOrderSql)) {
                updateOrderStmt.setInt(1, orderItemId);
                updateOrderStmt.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                closeConnection(conn);
            }
        }
    }
    
    public void cancelOrderByCustomer(int orderId, int userId) throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Verify order belongs to user and is pending
            String checkSql = "SELECT status FROM orders WHERE order_id = ? AND user_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, orderId);
                checkStmt.setInt(2, userId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next() || !rs.getString("status").equals("PENDING")) {
                    throw new SQLException("Cannot cancel this order. Orders can only be cancelled when in PENDING status.");
                }
            }
            
            // Get all order items to restore stock
            List<OrderItem> items = getOrderItems(orderId);
            ProductDAO productDAO = new ProductDAO(conn);
            
            // Restore stock for each item
            for (OrderItem item : items) {
                Product product = productDAO.getProductById(item.getProductId());
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productDAO.updateProduct(product);
            }
            
            // Update order status to CANCELLED
            String updateSql = "UPDATE orders SET status = 'CANCELLED' WHERE order_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, orderId);
                updateStmt.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                closeConnection(conn);
            }
        }
    }
} 