package controller;

import controller.DBConnector;
import controller.UserDAO;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/createUser")
public class CreateUserServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");
        
        if (admin == null || !admin.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        request.getRequestDispatcher("/createUser.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");
        
        if (admin == null || !admin.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phoneNumber = request.getParameter("phoneNumber");
        String userType = request.getParameter("userType");

        List<String> errors = new ArrayList<>();

        if (fname == null || fname.trim().isEmpty()) {
            errors.add("First name is required");
        } else if (fname.length() > 100) {
            errors.add("First name must be less than 100 characters");
        }

        if (lname == null || lname.trim().isEmpty()) {
            errors.add("Last name is required");
        } else if (lname.length() > 100) {
            errors.add("Last name must be less than 100 characters");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.add("Email is required");
        } else {
            User tempUser = new User();
            tempUser.setEmail(email.trim());
            if (!tempUser.isValidEmail()) {
                errors.add("Please enter a valid email address");
            }
        }

        if (password == null || password.trim().isEmpty()) {
            errors.add("Password is required");
        } else if (password.length() < 6) {
            errors.add("Password must be at least 6 characters long");
        }

        if (confirmPassword == null || !password.equals(confirmPassword)) {
            errors.add("Password confirmation does not match");
        }

        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            User tempUser = new User();
            tempUser.setPhoneNumber(phoneNumber.trim());
            if (!tempUser.isValidPhone()) {
                errors.add("Please enter a valid Australian phone number");
            }
        }

        if (userType == null || (!userType.equals("customer") && !userType.equals("staff") && !userType.equals("admin"))) {
            errors.add("Please select a valid user type");
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("fname", fname);
            request.setAttribute("lname", lname);
            request.setAttribute("email", email);
            request.setAttribute("phoneNumber", phoneNumber);
            request.setAttribute("userType", userType);
            request.getRequestDispatcher("/createUser.jsp").forward(request, response);
            return;
        }

        try {
            Connection conn = DBConnector.getConnection();
            UserDAO userDAO = new UserDAO(conn);

            if (userDAO.emailExists(email.trim(), 0)) {
                errors.add("Email address already exists");
                request.setAttribute("errors", errors);
                request.setAttribute("fname", fname);
                request.setAttribute("lname", lname);
                request.setAttribute("email", email);
                request.setAttribute("phoneNumber", phoneNumber);
                request.setAttribute("userType", userType);
                request.getRequestDispatcher("/createUser.jsp").forward(request, response);
                conn.close();
                return;
            }

            User newUser = new User(fname.trim(), lname.trim(), email.trim(), password,
                    phoneNumber != null ? phoneNumber.trim() : null, userType);
            newUser.setActive(true);

            boolean success = userDAO.addUser(newUser);

            conn.close();

            if (success) {
                request.setAttribute("successMessage", "User created successfully!");
                response.sendRedirect(request.getContextPath() + "/admin/listUsers");
            } else {
                errors.add("Failed to create user. Please try again.");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/createUser.jsp").forward(request, response);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            errors.add("Database error: " + e.getMessage());
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/createUser.jsp").forward(request, response);
        }
    }
}
