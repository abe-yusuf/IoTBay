package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.DBConnector;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import model.User;

public class ProductServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        
        try {
            Connection conn = DBConnector.getConnection();
            ProductDAO productDAO = new ProductDAO(conn);
            
            if (action == null) {
                // Default: show product list
                String searchTerm = request.getParameter("search");
                List<Product> products;
                
                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                    products = productDAO.searchProducts(searchTerm);
                    request.setAttribute("searchTerm", searchTerm);
                } else {
                    products = productDAO.getAllProducts();
                }
                
                request.setAttribute("products", products);
                request.getRequestDispatcher("/product-list.jsp").forward(request, response);
            } else if (action.equals("add")) {
                // Check if user is staff
                User user = (User) session.getAttribute("user");
                if (user == null) {
                    // User not logged in
                    response.sendRedirect("login.jsp?error=You must be logged in to access this feature");
                    return;
                }
                
                // In a real application, you should check if the user is staff
                // For this implementation, we'll assume all logged-in users can add products
                request.getRequestDispatcher("/product-add.jsp").forward(request, response);
            } else if (action.equals("edit")) {
                // Check if user is staff
                User user = (User) session.getAttribute("user");
                if (user == null) {
                    // User not logged in
                    response.sendRedirect("login.jsp?error=You must be logged in to access this feature");
                    return;
                }
                
                // Get product for editing
                int productId = Integer.parseInt(request.getParameter("id"));
                Product product = productDAO.getProductById(productId);
                
                if (product != null) {
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("/product-edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect("products?error=Product not found");
                }
            } else if (action.equals("details")) {
                // View product details
                int productId = Integer.parseInt(request.getParameter("id"));
                Product product = productDAO.getProductById(productId);
                
                if (product != null) {
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("/product-details.jsp").forward(request, response);
                } else {
                    response.sendRedirect("products?error=Product not found");
                }
            } else if (action.equals("delete")) {
                // Check if user is staff
                User user = (User) session.getAttribute("user");
                if (user == null) {
                    // User not logged in
                    response.sendRedirect("login.jsp?error=You must be logged in to access this feature");
                    return;
                }
                
                // Delete product
                int productId = Integer.parseInt(request.getParameter("id"));
                productDAO.deleteProduct(productId);
                
                response.sendRedirect("products?message=Product deleted successfully");
            }
            
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            request.setAttribute("error", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        
        try {
            Connection conn = DBConnector.getConnection();
            ProductDAO productDAO = new ProductDAO(conn);
            
            // Check if user is staff
            User user = (User) session.getAttribute("user");
            if (user == null) {
                // User not logged in
                response.sendRedirect("login.jsp?error=You must be logged in to access this feature");
                return;
            }
            
            if (action.equals("add")) {
                // Add new product
                String name = request.getParameter("name");
                String imageUrl = request.getParameter("imageUrl");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                
                // Server-side validation
                if (name == null || name.trim().isEmpty()) {
                    request.setAttribute("error", "Product name is required");
                    request.getRequestDispatcher("/product-add.jsp").forward(request, response);
                    return;
                }
                
                if (price < 0) {
                    request.setAttribute("error", "Price must be a positive number");
                    request.getRequestDispatcher("/product-add.jsp").forward(request, response);
                    return;
                }
                
                if (quantity < 0) {
                    request.setAttribute("error", "Quantity must be a positive number");
                    request.getRequestDispatcher("/product-add.jsp").forward(request, response);
                    return;
                }
                
                Product product = new Product();
                product.setName(name);
                product.setImageUrl(imageUrl);
                product.setDescription(description);
                product.setPrice(price);
                product.setQuantity(quantity);
                product.setFavourited(false);
                
                productDAO.addProduct(product);
                
                response.sendRedirect("products?message=Product added successfully");
            } else if (action.equals("update")) {
                // Update existing product
                int productId = Integer.parseInt(request.getParameter("productId"));
                String name = request.getParameter("name");
                String imageUrl = request.getParameter("imageUrl");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                
                // Server-side validation
                if (name == null || name.trim().isEmpty()) {
                    request.setAttribute("error", "Product name is required");
                    request.getRequestDispatcher("/product-edit.jsp").forward(request, response);
                    return;
                }
                
                if (price < 0) {
                    request.setAttribute("error", "Price must be a positive number");
                    request.getRequestDispatcher("/product-edit.jsp").forward(request, response);
                    return;
                }
                
                if (quantity < 0) {
                    request.setAttribute("error", "Quantity must be a positive number");
                    request.getRequestDispatcher("/product-edit.jsp").forward(request, response);
                    return;
                }
                
                Product product = new Product();
                product.setProductID(productId);
                product.setName(name);
                product.setImageUrl(imageUrl);
                product.setDescription(description);
                product.setPrice(price);
                product.setQuantity(quantity);
                product.setFavourited(false);
                
                productDAO.updateProduct(product);
                
                response.sendRedirect("products?message=Product updated successfully");
            }
            
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            request.setAttribute("error", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (NumberFormatException ex) {
            request.setAttribute("error", "Invalid number format. Please enter valid numbers for price and quantity.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}