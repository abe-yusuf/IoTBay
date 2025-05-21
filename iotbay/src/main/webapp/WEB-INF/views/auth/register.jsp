<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <% 
        String errorMessage = (String) request.getAttribute("error");
    %> 

    <head>
        <title>Register - IoTBay</title>
        <link href="${pageContext.request.contextPath}/resources/css/css.css" rel="stylesheet" type="text/css">
        <style>
            .container {
                max-width: 500px;
                margin: 2rem auto;
                padding: 2rem;
                background: white;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }

            .form-group {
                margin-bottom: 1rem;
            }

            .form-group label {
                display: block;
                margin-bottom: 0.5rem;
                color: #333;
                font-weight: 500;
            }

            .form-group input {
                width: 100%;
                padding: 0.5rem;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 1rem;
            }

            .form-group input:focus {
                outline: none;
                border-color: #F96E46;
                box-shadow: 0 0 0 2px rgba(249, 110, 70, 0.2);
            }

            .button {
                display: inline-block;
                padding: 0.8rem 1.5rem;
                background-color: #F96E46;
                color: white;
                text-decoration: none;
                border-radius: 4px;
                border: none;
                cursor: pointer;
                font-size: 1rem;
                transition: all 0.2s;
            }

            .button:hover {
                background-color: #e85e35;
                transform: translateY(-1px);
            }

            .error {
                color: #dc3545;
                font-size: 0.875rem;
                margin-top: 0.25rem;
            }

            .success {
                color: #28a745;
                text-align: center;
                margin-bottom: 1rem;
            }
        </style>
    </head>

    <body>
        <%@ include file="/WEB-INF/views/common/header.jsp" %>

        <main>
            <div class="container">
                <h1 style="text-align: center; color: #333; margin-bottom: 2rem;">Register</h1>
                
                <c:if test="${not empty error}">
                    <div class="error" style="text-align: center; margin-bottom: 1rem;">
                        ${error}
                    </div>
                </c:if>
                
                <form id="registerForm" action="${pageContext.request.contextPath}/auth/register" method="post" onsubmit="return validateForm('registerForm')">
                    <div class="form-group">
                        <label for="email">Email *</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="password">Password *</label>
                        <input type="password" id="password" name="password" required>
                        <small style="color: #666; font-size: 0.8rem;">
                            Must be at least 8 characters long and contain uppercase, lowercase, and numbers
                        </small>
                    </div>
                    
                    <div class="form-group">
                        <label for="firstName">First Name *</label>
                        <input type="text" id="firstName" name="firstName" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="lastName">Last Name *</label>
                        <input type="text" id="lastName" name="lastName" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="phone">Phone</label>
                        <input type="tel" id="phone" name="phone">
                        <small style="color: #666; font-size: 0.8rem;">
                            Australian format (e.g., 0412345678 or +61412345678)
                        </small>
                    </div>
                    
                    <div class="form-group">
                        <label for="address">Address</label>
                        <input type="text" id="address" name="address">
                    </div>
                    
                    <div style="text-align: center; margin-top: 2rem;">
                        <button type="submit" class="button">Register</button>
                    </div>
                    
                    <div style="text-align: center; margin-top: 1rem;">
                        Already have an account? <a href="${pageContext.request.contextPath}/auth/login" style="color: #F96E46;">Login here</a>
                    </div>
                </form>
            </div>
        </main>

        <%@ include file="/WEB-INF/views/common/footer.jsp" %>
        
        <!-- Include validation script -->
        <script src="${pageContext.request.contextPath}/resources/js/validation.js"></script>
    </body>
</html>
