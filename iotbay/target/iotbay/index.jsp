<html>
    <% 
        // Variables go here (none needed for landing page)
    %>

    <head>
        <title>IoTBay - Landing Page</title>
        <link href="css.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <%@ include file="header.jsp" %> <!-- Site title and nav please do not remove -->

        <main> <!-- Please put all content inside the main tags -->
            <section style="text-align: center; padding: 50px;">
                <h1>Welcome to IoTBay!</h1>
                <p>Your one-stop shop for IoT devices and solutions.</p>

                <div style="margin-top: 30px;">
                    <a href="login.jsp">
                        <button style="padding: 12px 24px; font-size: 16px;">Login</button>
                    </a>
                    &nbsp;
                    <a href="register.jsp">
                        <button style="padding: 12px 24px; font-size: 16px;">Register</button>
                    </a>
                </div>
            </section>
        </main>

        <%@ include file="footer.jsp" %> <!-- Site footer please do not remove -->
    </body>
</html>
