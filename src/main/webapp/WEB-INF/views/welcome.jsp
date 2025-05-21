<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html>
    <% 
        String fname = request.getParameter("fname"); 
        String lname = request.getParameter("lname"); 
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        User user = new User(fname, lname, email, password);
        session.setAttribute("user", user);
        %>

    <head>
        <title>Welcome - IoTBay</title>
        <link href="${pageContext.request.contextPath}/resources/css/css.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <%@ include file="common/header.jsp" %>
        
        <main>
            <section class="welcome-container">
                <% if (user.getFname() == null) { %>
                    <h2>Welcome to IoTBay</h2>
                    <p>Please <a href="${pageContext.request.contextPath}/auth/register">register</a> or <a href="${pageContext.request.contextPath}/auth/login">login</a> to continue.</p>
                <% } else { %>
                    <h2>Welcome <%= user.getFname() %>!</h2>
                    <p>Thank you for joining IoTBay. Please explore our catalog of IoT devices.</p>
                    <div class="welcome-actions">
                        <a href="${pageContext.request.contextPath}/products" class="btn-primary">Browse Products</a>
                        <a href="${pageContext.request.contextPath}/account" class="btn-secondary">My Account</a>
                    </div>
                <% } %>
            </section>
        </main>
        
        <%@ include file="common/footer.jsp" %>
    </body>
</html>

<style>
.welcome-container {
    max-width: 800px;
    margin: 4rem auto;
    padding: 2rem;
    text-align: center;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.welcome-container h2 {
    color: #694A38;
    font-size: 2rem;
    margin-bottom: 1.5rem;
}

.welcome-container p {
    color: #666;
    font-size: 1.1rem;
    line-height: 1.6;
    margin-bottom: 2rem;
}

.welcome-container a {
    color: #F96E46;
    text-decoration: none;
    transition: color 0.2s;
}

.welcome-container a:hover {
    color: #e85e35;
}

.welcome-actions {
    display: flex;
    gap: 1rem;
    justify-content: center;
}

.btn-primary {
    background-color: #F96E46;
    color: white;
    padding: 0.8rem 1.5rem;
    border-radius: 4px;
    text-decoration: none;
    transition: all 0.2s;
}

.btn-primary:hover {
    background-color: #e85e35;
    transform: translateY(-2px);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.btn-secondary {
    background-color: #694A38;
    color: white;
    padding: 0.8rem 1.5rem;
    border-radius: 4px;
    text-decoration: none;
    transition: all 0.2s;
}

.btn-secondary:hover {
    background-color: #553a2d;
    transform: translateY(-2px);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
    .welcome-container {
        margin: 2rem 1rem;
        padding: 1.5rem;
    }

    .welcome-actions {
        flex-direction: column;
    }

    .btn-primary,
    .btn-secondary {
        display: block;
        text-align: center;
    }
}
</style>
