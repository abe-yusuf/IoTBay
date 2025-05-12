package controller;

import model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection conn;
    
    public ProductDAO(Connection conn) {
        this.conn = conn;
    }
    
    // Get all products
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE quantity > 0";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Product product = new Product(
                rs.getInt("productID"),
                rs.getString("name"),
                rs.getString("imageUrl"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getBoolean("favourited")
            );
            products.add(product);
        }
        
        rs.close();
        stmt.close();
        return products;
    }
    
    // Get product by ID
    public Product getProductById(int productID) throws SQLException {
        String query = "SELECT * FROM products WHERE productID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, productID);
        ResultSet rs = stmt.executeQuery();
        
        Product product = null;
        if (rs.next()) {
            product = new Product(
                rs.getInt("productID"),
                rs.getString("name"),
                rs.getString("imageUrl"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getBoolean("favourited")
            );
        }
        
        rs.close();
        stmt.close();
        return product;
    }
    
    // Get products by search term
    public List<Product> searchProducts(String searchTerm) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE name LIKE ? OR description LIKE ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, "%" + searchTerm + "%");
        stmt.setString(2, "%" + searchTerm + "%");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Product product = new Product(
                rs.getInt("productID"),
                rs.getString("name"),
                rs.getString("imageUrl"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getBoolean("favourited")
            );
            products.add(product);
        }
        
        rs.close();
        stmt.close();
        return products;
    }
    
    // Check if product has enough stock
    public boolean checkStock(int productID, int requestedQuantity) throws SQLException {
        String query = "SELECT quantity FROM products WHERE productID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, productID);
        ResultSet rs = stmt.executeQuery();
        
        boolean hasStock = false;
        if (rs.next()) {
            int availableQuantity = rs.getInt("quantity");
            hasStock = availableQuantity >= requestedQuantity;
        }
        
        rs.close();
        stmt.close();
        return hasStock;
    }
    
    // Update product stock (decrease)
    public void decreaseStock(int productID, int quantity) throws SQLException {
        String query = "UPDATE products SET quantity = quantity - ? WHERE productID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, quantity);
        stmt.setInt(2, productID);
        stmt.executeUpdate();
        stmt.close();
    }
    
    // Update product stock (increase)
    public void increaseStock(int productID, int quantity) throws SQLException {
        String query = "UPDATE products SET quantity = quantity + ? WHERE productID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, quantity);
        stmt.setInt(2, productID);
        stmt.executeUpdate();
        stmt.close();
    }
    
    // Toggle product favourite status
    public void toggleFavorite(int productID, boolean favourited) throws SQLException {
        String query = "UPDATE products SET favourited = ? WHERE productID = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setBoolean(1, favourited);
        stmt.setInt(2, productID);
        stmt.executeUpdate();
        stmt.close();
    }
    
    // Get all favourite products
    public List<Product> getFavouriteProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE favourited = TRUE";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Product product = new Product(
                rs.getInt("productID"),
                rs.getString("name"),
                rs.getString("imageUrl"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getBoolean("favourited")
            );
            products.add(product);
        }
        
        rs.close();
        stmt.close();
        return products;
    }
}