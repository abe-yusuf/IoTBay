package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Product;

public class ProductDAO {
    
    private Connection conn;
    
    public ProductDAO(Connection conn) {
        this.conn = conn;
    }
    
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM PRODUCTS";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            Product product = new Product();
            product.setProductID(rs.getInt("PRODUCT_ID"));
            product.setName(rs.getString("NAME"));
            product.setImageUrl(rs.getString("IMAGE_URL"));
            product.setDescription(rs.getString("DESCRIPTION"));
            product.setPrice(rs.getDouble("PRICE"));
            product.setQuantity(rs.getInt("QUANTITY"));
            product.setFavourited(rs.getBoolean("FAVOURITED"));
            products.add(product);
        }
        return products;
    }
    
    public List<Product> searchProducts(String searchTerm) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM PRODUCTS WHERE LOWER(NAME) LIKE ? OR LOWER(DESCRIPTION) LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, "%" + searchTerm.toLowerCase() + "%");
        stmt.setString(2, "%" + searchTerm.toLowerCase() + "%");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Product product = new Product();
            product.setProductID(rs.getInt("PRODUCT_ID"));
            product.setName(rs.getString("NAME"));
            product.setImageUrl(rs.getString("IMAGE_URL"));
            product.setDescription(rs.getString("DESCRIPTION"));
            product.setPrice(rs.getDouble("PRICE"));
            product.setQuantity(rs.getInt("QUANTITY"));
            product.setFavourited(rs.getBoolean("FAVOURITED"));
            products.add(product);
        }
        return products;
    }
    
    public Product getProductById(int productId) throws SQLException {
        String query = "SELECT * FROM PRODUCTS WHERE PRODUCT_ID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, productId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            Product product = new Product();
            product.setProductID(rs.getInt("PRODUCT_ID"));
            product.setName(rs.getString("NAME"));
            product.setImageUrl(rs.getString("IMAGE_URL"));
            product.setDescription(rs.getString("DESCRIPTION"));
            product.setPrice(rs.getDouble("PRICE"));
            product.setQuantity(rs.getInt("QUANTITY"));
            product.setFavourited(rs.getBoolean("FAVOURITED"));
            return product;
        }
        return null;
    }
    
    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, product.getName());
        stmt.setString(2, product.getImageUrl());
        stmt.setString(3, product.getDescription());
        stmt.setDouble(4, product.getPrice());
        stmt.setInt(5, product.getQuantity());
        stmt.setBoolean(6, product.isFavourited());
        
        stmt.executeUpdate();
        
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            product.setProductID(generatedKeys.getInt(1));
        }
    }
    
    public void updateProduct(Product product) throws SQLException {
        String query = "UPDATE PRODUCTS SET NAME = ?, IMAGE_URL = ?, DESCRIPTION = ?, PRICE = ?, QUANTITY = ?, FAVOURITED = ? WHERE PRODUCT_ID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, product.getName());
        stmt.setString(2, product.getImageUrl());
        stmt.setString(3, product.getDescription());
        stmt.setDouble(4, product.getPrice());
        stmt.setInt(5, product.getQuantity());
        stmt.setBoolean(6, product.isFavourited());
        stmt.setInt(7, product.getProductID());
        
        stmt.executeUpdate();
    }
    
    public void deleteProduct(int productId) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        // Set lock timeout to 60 seconds (default is 60000 milliseconds)
        try {
            conn.createStatement().execute("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.locks.waitTimeout', '60')");
        } catch (SQLException e) {
            // Log the error but continue - not critical if this fails
            System.err.println("Warning: Could not set lock timeout: " + e.getMessage());
        }
        
        // Start transaction
        boolean originalAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        
        try {
            // First check if product exists
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM products WHERE product_id = ?");
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                throw new SQLException("Product not found");
            }
            
            // Check for references in cart items
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM cart_items WHERE product_id = ?");
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Cannot delete: Product is in users' carts");
            }
            
            // Check for references in order items
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM order_items WHERE product_id = ?");
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Cannot delete: Product is referenced in orders");
            }
            
            // If we get here, it's safe to delete
            stmt = conn.prepareStatement("DELETE FROM products WHERE product_id = ?");
        stmt.setInt(1, productId);
        
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to delete product");
            }
            
            // Commit the transaction
            conn.commit();
        } catch (SQLException e) {
            // Rollback on error
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                // Log rollback error
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            throw e;
        } finally {
            // Restore original auto-commit state
            try {
                conn.setAutoCommit(originalAutoCommit);
            } catch (SQLException e) {
                System.err.println("Error restoring auto-commit: " + e.getMessage());
            }
            
            // Close resources
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignore */ }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }
}