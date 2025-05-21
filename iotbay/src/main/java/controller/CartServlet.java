package controller;

import java.io.IOException;
import java.sql.SQLException;

import dao.CartDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.CartItem;

@WebServlet("/cart/*")
public class CartServlet extends HttpServlet {
    private CartDAO cartDAO;

    @Override
    public void init() throws ServletException {
        cartDAO = new CartDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Check if user is logged in
        if (userId == null) {
            // Store the intended destination URL in the session
            session.setAttribute("redirectUrl", request.getRequestURI());
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        try {
            Cart cart = cartDAO.getCart(userId);
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving cart", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Check if user is logged in
        if (userId == null) {
            // Store the intended action in the session
            session.setAttribute("redirectUrl", request.getRequestURI());
            session.setAttribute("cartAction", request.getParameter("action"));
            session.setAttribute("productId", request.getParameter("productId"));
            session.setAttribute("productName", request.getParameter("productName"));
            session.setAttribute("quantity", request.getParameter("quantity"));
            session.setAttribute("price", request.getParameter("price"));
            
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        String action = request.getParameter("action");
        try {
            switch (action) {
                case "add":
                    addToCart(request, userId);
                    break;
                case "update":
                    updateCartItem(request, userId);
                    break;
                case "remove":
                    removeFromCart(request, userId);
                    break;
                case "clear":
                    clearCart(userId);
                    break;
            }
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (SQLException e) {
            throw new ServletException("Error processing cart operation", e);
        }
    }

    private void addToCart(HttpServletRequest request, int userId) throws SQLException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        String productName = request.getParameter("productName");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        double price = Double.parseDouble(request.getParameter("price"));

        CartItem item = new CartItem(productId, productName, quantity, price);
        cartDAO.addToCart(userId, item);
    }

    private void updateCartItem(HttpServletRequest request, int userId) throws SQLException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        cartDAO.updateCartItemQuantity(userId, productId, quantity);
    }

    private void removeFromCart(HttpServletRequest request, int userId) throws SQLException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        cartDAO.removeFromCart(userId, productId);
    }

    private void clearCart(int userId) throws SQLException {
        cartDAO.clearCart(userId);
    }
} 