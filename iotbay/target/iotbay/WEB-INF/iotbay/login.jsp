<html>
    <%
        String errorMessage = request.getParameter("error");
    %>

    <head>
        <title>Log In</title>
        <link href="css.css" rel="stylesheet" type="text/css">
        <style>
            .form-container {
                max-width: 500px;
                margin: 50px auto;
                padding: 40px;
                background: white;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }

            .form-container h2 {
                text-align: center;
                margin-bottom: 30px;
                font-size: 24px;
            }

            .form-group {
                margin-bottom: 20px;
                text-align: left;
            }

            .form-group label {
                display: block;
                margin-bottom: 8px;
                font-weight: bold;
            }

            .form-group input {
                width: 100%;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
                font-size: 16px;
            }

            .btn-login {
                width: 100%;
                background-color: #d66b43;
                color: white;
                padding: 12px;
                font-size: 16px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                margin-top: 10px;
            }

            .btn-login:hover {
                background-color: #c65c38;
            }

            .form-footer {
                margin-top: 20px;
                font-size: 14px;
                text-align: center;
            }

            .form-footer a {
                color: #0077cc;
                text-decoration: none;
            }

            .form-footer a:hover {
                text-decoration: underline;
            }

            .error-message {
                color: red;
                margin-bottom: 15px;
                text-align: center;
                font-weight: bold;
            }
        </style>
    </head>

    <body>
        <%@ include file="header.jsp" %> <!-- Site title and nav please do not remove -->

        <main>
            <section class="form-container">
                <h2>Log In</h2>

                <% if (errorMessage != null) { %>
                    <div class="error-message"><%= errorMessage %></div>
                <% } %>

                <form action="welcome.jsp" method="post">
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="text" id="email" name="email" required>
                    </div>

                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="password" id="password" name="password" required>
                    </div>

                    <button type="submit" class="btn-login">Login</button>
                </form>

                <div class="form-footer">
                    <p><a href="register.jsp">Don't have an account? Register here</a></p>
                    <p><a href="forgotPassword.jsp">Forgot your password?</a></p>
                </div>
            </section>
        </main>

        <%@ include file="footer.jsp" %> <!-- Site footer please do not remove -->
    </body>
</html>
