<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Error - IoTBay</title>
        <link href="${pageContext.request.contextPath}/resources/css/css.css" rel="stylesheet" type="text/css">
        <style>
            .error-container {
                max-width: 600px;
                margin: 40px auto;
                padding: 20px;
                background-color: #fff;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            
            .error-message {
                background-color: #f8d7da;
                color: #721c24;
                padding: 15px;
                margin-bottom: 20px;
                border-radius: 4px;
                border: 1px solid #f5c6cb;
            }
            
            .back-button {
                display: inline-block;
                padding: 10px 20px;
                background-color: #d66b43;
                color: white;
                text-decoration: none;
                border-radius: 4px;
                transition: background-color 0.3s ease;
            }
            
            .back-button:hover {
                background-color: #c55a32;
            }
        </style>
    </head>
    <body>
        <%@ include file="common/header.jsp" %>
        
        <main>
            <div class="error-container">
                <h2>Error</h2>
                <div class="error-message">
                    <%= request.getAttribute("error") != null ? request.getAttribute("error") : "An unexpected error occurred." %>
                </div>
                <a href="${pageContext.request.contextPath}/products" class="back-button">Return to Products</a>
            </div>
        </main>
        
        <%@ include file="common/footer.jsp" %>
    </body>
</html>