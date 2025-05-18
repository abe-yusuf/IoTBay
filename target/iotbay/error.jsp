<html>
    <head>
        <title>Error - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <%@ include file="header.jsp" %>

        <main>
            <section style="text-align: center; padding: 50px;">
                <h1 style="color: #721c24;">Error</h1>
                
                <div style="background-color: #f8d7da; color: #721c24; padding: 20px; border-radius: 8px; max-width: 600px; margin: 20px auto;">
                    <%= request.getAttribute("error") != null ? request.getAttribute("error") : "An unknown error occurred." %>
                </div>
                
                <a href="index.jsp" style="display: inline-block; margin-top: 20px; background-color: #F96E46; color: white; padding: 10px 20px; border-radius: 4px; text-decoration: none;">
                    Return to Home
                </a>
            </section>
        </main>

        <%@ include file="footer.jsp" %>
    </body>
</html>