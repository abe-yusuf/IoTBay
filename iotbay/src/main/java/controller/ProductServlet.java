package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import util.DatabaseUtil;

public class ProductServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        
        try {
            Connection conn = DatabaseUtil.getConnection();
            ProductDAO productDAO = new ProductDAO(conn);
            
            if (action == null) {
                // Default: show product list - publicly accessible
                String searchTerm = request.getParameter("search");
                List<Product> products;
                
                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                    products = productDAO.searchProducts(searchTerm);
                    request.setAttribute("searchTerm", searchTerm);
                } else {
                    products = productDAO.getAllProducts();
                }
                
                request.setAttribute("products", products);
                request.getRequestDispatcher("/WEB-INF/views/product-list.jsp").forward(request, response);
            } else if (action.equals("manage")) {
                // Check if user is staff
                Integer userId = (Integer) session.getAttribute("userId");
                Boolean isStaff = (Boolean) session.getAttribute("isStaff");
                
                if (userId == null) {
                    response.sendRedirect(request.getContextPath() + "/auth/login?error=You must be logged in to access this feature");
                    return;
                }
                
                if (isStaff == null || !isStaff) {
                    response.sendRedirect(request.getContextPath() + "/products?error=You must be a staff member to access this feature");
                    return;
                }
                
                // Get all products for management
                List<Product> products = productDAO.getAllProducts();
                request.setAttribute("products", products);
                request.getRequestDispatcher("/WEB-INF/views/product-management.jsp").forward(request, response);
            } else if (action.equals("add")) {
                // Check if user is staff
                Integer userId = (Integer) session.getAttribute("userId");
                Boolean isStaff = (Boolean) session.getAttribute("isStaff");
                
                if (userId == null) {
                    response.sendRedirect(request.getContextPath() + "/auth/login?error=You must be logged in to access this feature");
                    return;
                }
                
                if (isStaff == null || !isStaff) {
                    response.sendRedirect(request.getContextPath() + "/products?error=You must be a staff member to access this feature");
                    return;
                }
                
                request.getRequestDispatcher("/WEB-INF/views/product-add.jsp").forward(request, response);
            } else if (action.equals("edit")) {
                // Check if user is staff
                Integer userId = (Integer) session.getAttribute("userId");
                Boolean isStaff = (Boolean) session.getAttribute("isStaff");
                
                if (userId == null) {
                    response.sendRedirect(request.getContextPath() + "/auth/login?error=You must be logged in to access this feature");
                    return;
                }
                
                if (isStaff == null || !isStaff) {
                    response.sendRedirect(request.getContextPath() + "/products?error=You must be a staff member to access this feature");
                    return;
                }
                
                int productId = Integer.parseInt(request.getParameter("id"));
                Product product = productDAO.getProductById(productId);
                
                if (product != null) {
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("/WEB-INF/views/product-edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/products?error=Product not found");
                }
            } else if (action.equals("details")) {
                // View product details - publicly accessible
                int productId = Integer.parseInt(request.getParameter("id"));
                Product product = productDAO.getProductById(productId);
                
                if (product != null) {
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("/WEB-INF/views/product-details.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/products?error=Product not found");
                }
            } else if (action.equals("delete")) {
                try {
                    int productId = Integer.parseInt(request.getParameter("id"));
                    
                    try {
                        // Get a new connection specifically for this delete operation
                        Connection deleteConn = DatabaseUtil.getConnection();
                        try {
                            ProductDAO deleteDAO = new ProductDAO(deleteConn);
                            deleteDAO.deleteProduct(productId);
                            response.sendRedirect(request.getContextPath() + "/products?message=Product deleted successfully");
                        } finally {
                            if (deleteConn != null) {
                                try {
                                    deleteConn.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (SQLException e) {
                        String errorMessage;
                        if (e.getMessage().contains("Product not found")) {
                            errorMessage = "Product no longer exists.";
                        } else if (e.getMessage().contains("referenced in orders or cart")) {
                            errorMessage = "Cannot delete this product because it is referenced in orders or cart.";
                        } else {
                            errorMessage = "An error occurred while deleting the product. Please try again.";
                            e.printStackTrace();
                        }
                        
                        response.sendRedirect(request.getContextPath() + "/products?error=" + 
                            java.net.URLEncoder.encode(errorMessage, "UTF-8"));
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/products?error=Invalid product ID");
            }
            }
            
        } catch (SQLException ex) {
            System.err.println("Database error in ProductServlet: " + ex.getMessage());
            ex.printStackTrace();
            request.setAttribute("error", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } catch (NumberFormatException ex) {
            System.err.println("Invalid number format: " + ex.getMessage());
            request.setAttribute("error", "Invalid product ID format");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } catch (Exception ex) {
            System.err.println("Unexpected error in ProductServlet: " + ex.getMessage());
            ex.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred: " + ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        
        try {
            Connection conn = DatabaseUtil.getConnection();
            ProductDAO productDAO = new ProductDAO(conn);
            
            // Check if user is staff
            Integer userId = (Integer) session.getAttribute("userId");
            Boolean isStaff = (Boolean) session.getAttribute("isStaff");
            
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/auth/login?error=You must be logged in to access this feature");
                return;
            }
            
            if (isStaff == null || !isStaff) {
                response.sendRedirect(request.getContextPath() + "/products?error=You must be a staff member to access this feature");
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
                    request.getRequestDispatcher("/WEB-INF/views/product-add.jsp").forward(request, response);
                    return;
                }
                
                if (price < 0) {
                    request.setAttribute("error", "Price must be a positive number");
                    request.getRequestDispatcher("/WEB-INF/views/product-add.jsp").forward(request, response);
                    return;
                }
                
                if (quantity < 0) {
                    request.setAttribute("error", "Quantity must be a positive number");
                    request.getRequestDispatcher("/WEB-INF/views/product-add.jsp").forward(request, response);
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
                
                response.sendRedirect(request.getContextPath() + "/products?action=manage&message=Product added successfully");
            } else if (action.equals("edit")) {
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
                    request.getRequestDispatcher("/WEB-INF/views/product-edit.jsp").forward(request, response);
                    return;
                }
                
                if (price < 0) {
                    request.setAttribute("error", "Price must be a positive number");
                    request.getRequestDispatcher("/WEB-INF/views/product-edit.jsp").forward(request, response);
                    return;
                }
                
                if (quantity < 0) {
                    request.setAttribute("error", "Quantity must be a positive number");
                    request.getRequestDispatcher("/WEB-INF/views/product-edit.jsp").forward(request, response);
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
                
                response.sendRedirect(request.getContextPath() + "/products?action=manage&message=Product updated successfully");
            } else if (action.equals("delete")) {
                try {
                    int productId = Integer.parseInt(request.getParameter("id"));
                    
                    // Check if product exists
                    Product product = productDAO.getProductById(productId);
                    if (product == null) {
                        response.sendRedirect(request.getContextPath() + "/products?action=manage&error=Product not found");
                        return;
                    }
                    
                    // Try to delete the product
                    try {
                        productDAO.deleteProduct(productId);
                        response.sendRedirect(request.getContextPath() + "/products?action=manage&message=Product deleted successfully");
                    } catch (SQLException e) {
                        // Check if the error is due to foreign key constraint
                        if (e.getMessage().contains("foreign key constraint")) {
                            response.sendRedirect(request.getContextPath() + 
                                "/products?action=manage&error=Cannot delete this product because it is referenced in orders or cart items");
                        } else {
                            throw e; // Re-throw if it's a different kind of SQL error
                        }
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/products?action=manage&error=Invalid product ID");
                }
            }
            
        } catch (SQLException ex) {
            System.err.println("Database error in ProductServlet: " + ex.getMessage());
            ex.printStackTrace();
            request.setAttribute("error", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } catch (Exception ex) {
            System.err.println("Unexpected error in ProductServlet: " + ex.getMessage());
            ex.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred: " + ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}