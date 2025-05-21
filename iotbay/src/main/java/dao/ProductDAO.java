package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Product;
import util.DatabaseUtil;

public class ProductDAO {
    
    private Connection conn;
    private boolean isExternalConnection;
    
    public ProductDAO(Connection conn) {
        this.conn = conn;
        this.isExternalConnection = true;
    }
    
    public ProductDAO() {
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
    
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM PRODUCTS";
        Connection localConn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            localConn = getConnection();
            stmt = localConn.createStatement();
            rs = stmt.executeQuery(query);
            
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
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            closeConnection(localConn);
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
        Connection localConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            localConn = getConnection();
            stmt = localConn.prepareStatement(query);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();
            
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
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            closeConnection(localConn);
        }
    }
    
    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) VALUES (?, ?, ?, ?, ?, ?)";
        Connection localConn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        
        try {
            localConn = getConnection();
            stmt = localConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getImageUrl());
            stmt.setString(3, product.getDescription());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getQuantity());
            stmt.setBoolean(6, product.isFavourited());
            
            stmt.executeUpdate();
            
            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                product.setProductID(generatedKeys.getInt(1));
            }
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (stmt != null) stmt.close();
            closeConnection(localConn);
        }
    }
    
    public void updateProduct(Product product) throws SQLException {
        String query = "UPDATE PRODUCTS SET NAME = ?, IMAGE_URL = ?, DESCRIPTION = ?, PRICE = ?, QUANTITY = ?, FAVOURITED = ? WHERE PRODUCT_ID = ?";
        Connection localConn = null;
        PreparedStatement stmt = null;
        
        try {
            localConn = getConnection();
            stmt = localConn.prepareStatement(query);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getImageUrl());
            stmt.setString(3, product.getDescription());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getQuantity());
            stmt.setBoolean(6, product.isFavourited());
            stmt.setInt(7, product.getProductID());
            
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            closeConnection(localConn);
        }
    }
    
    public void deleteProduct(int productId) throws SQLException {
        String query = "DELETE FROM PRODUCTS WHERE PRODUCT_ID = ?";
        Connection localConn = null;
        PreparedStatement stmt = null;
        
        try {
            localConn = getConnection();
            stmt = localConn.prepareStatement(query);
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            closeConnection(localConn);
        }
    }
}