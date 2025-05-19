package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Order;
import model.OrderLine;
import model.Product;
import model.User;

@WebServlet(name = "CreateOrderServlet", urlPatterns = {"/createOrder"})
public class CreateOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            Connection conn = DBConnector.getConnection();
            ProductDAO productDAO = new ProductDAO(conn);
            
            // Get all available products
            java.util.List<Product> products = productDAO.getAllProducts();
            request.setAttribute("products", products);
            
            // Check if there's an order in progress
            Order currentOrder = (Order) session.getAttribute("currentOrder");
            if (currentOrder != null) {
                request.setAttribute("currentOrder", currentOrder);
            }
            
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            request.setAttribute("errorMessage", "Database error: " + ex.getMessage());
        }
        
        request.getRequestDispatcher("/createOrder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        
        try {
            Connection conn = DBConnector.getConnection();
            ProductDAO productDAO = new ProductDAO(conn);
            OrderDAO orderDAO = new OrderDAO(conn);
            
            // Get or create the current order
            Order currentOrder = (Order) session.getAttribute("currentOrder");
            if (currentOrder == null) {
                // Get the logged-in user
                User user = (User) session.getAttribute("user");
                if (user == null) {
                    // For anonymous users, use a placeholder user ID
                    currentOrder = new Order(0);
                } else {
                    currentOrder = new Order(user.getUserID());
                }
                session.setAttribute("currentOrder", currentOrder);
            }
            
            if ("addProduct".equals(action)) {
                // Add product to order
                int productID = Integer.parseInt(request.getParameter("productID"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                
                // Validate quantity
                if (quantity <= 0) {
                    request.setAttribute("errorMessage", "Quantity must be greater than zero.");
                    doGet(request, response);
                    return;
                }
                
                // Get product details
                Product product = productDAO.getProductById(productID);
                
                if (product == null) {
                    request.setAttribute("errorMessage", "Product not found.");
                    doGet(request, response);
                    return;
                }
                
                // Check stock
                if (!productDAO.checkStock(productID, quantity)) {
                    request.setAttribute("errorMessage", "Not enough stock available for " + product.getName());
                    doGet(request, response);
                    return;
                }
                
                // Check if product already exists in the cart
                boolean productExists = false;
                for (OrderLine existingLine : currentOrder.getOrderLines()) {
                    if (existingLine.getProductID() == productID) {
                        // Update existing order line quantity
                        int newQuantity = existingLine.getQuantity() + quantity;
                        if (!productDAO.checkStock(productID, newQuantity)) {
                            request.setAttribute("errorMessage", "Not enough stock available. You already have " + 
                                existingLine.getQuantity() + " of this item in your cart.");
                            doGet(request, response);
                            return;
                        }
                        existingLine.setQuantity(newQuantity);
                        existingLine.setSubtotal(existingLine.getQuantity() * existingLine.getProductPrice());
                        productExists = true;
                        break;
                    }
                }
                
                if (!productExists) {
                    // Create a new order line
                    OrderLine newLine = new OrderLine(
                        productID,
                        quantity,
                        product.getPrice(),
                        product.getName()
                    );
                    currentOrder.addOrderLine(newLine);
                } else {
                    // Recalculate total for existing product update
                    currentOrder.calculateTotal();
                }
                
                session.setAttribute("currentOrder", currentOrder);
                request.setAttribute("successMessage", product.getName() + " added to your cart.");
                
            } else if ("removeProduct".equals(action)) {
                // Remove product from cart
                String productIDParam = request.getParameter("productID");
                if (productIDParam != null) {
                    int productID = Integer.parseInt(productIDParam);
                    currentOrder.getOrderLines().removeIf(line -> line.getProductID() == productID);
                    currentOrder.calculateTotal();
                    session.setAttribute("currentOrder", currentOrder);
                    request.setAttribute("successMessage", "Product removed from your cart.");
                }
                
            } else if ("saveOrder".equals(action)) {
                // Check if user is logged in
                User user = (User) session.getAttribute("user");
                if (user == null) {
                    request.setAttribute("errorMessage", "You must be logged in to save an order.");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                    return;
                }
                
                // Save the order to database
                if (currentOrder.getOrderLines().isEmpty()) {
                    request.setAttribute("errorMessage", "Cannot save an empty order.");
                    doGet(request, response);
                    return;
                }
                
                // Update order with logged-in user ID if it was created anonymously
                if (currentOrder.getUserID() == 0) {
                    currentOrder.setUserID(user.getUserID());
                }
                
                conn.setAutoCommit(false);
                try {
                    // Create the order
                    int orderID = orderDAO.createOrder(currentOrder);
                    
                    // Add order lines
                    orderDAO.addOrderLines(currentOrder.getOrderLines(), orderID);
                    
                    // Decrease product stock
                    for (OrderLine line : currentOrder.getOrderLines()) {
                        productDAO.decreaseStock(line.getProductID(), line.getQuantity());
                    }
                    
                    conn.commit();
                    
                    // Clear the session order
                    session.removeAttribute("currentOrder");
                    
                    // Redirect to order details
                    response.sendRedirect(request.getContextPath() + "/viewOrderDetails?orderID=" + orderID);
                    return;
                    
                } catch (SQLException ex) {
                    conn.rollback();
                    request.setAttribute("errorMessage", "Error saving order: " + ex.getMessage());
                } finally {
                    conn.setAutoCommit(true);
                }
            }
            
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            request.setAttribute("errorMessage", "Database error: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            request.setAttribute("errorMessage", "Invalid input: " + ex.getMessage());
        }
        
        doGet(request, response);
    }
}