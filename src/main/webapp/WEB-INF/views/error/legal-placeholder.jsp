<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Legal Document - Coming Soon</title>
    <style>
        .container {
            max-width: 800px;
            margin: 50px auto;
            text-align: center;
            font-family: Arial, sans-serif;
        }
        .message {
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 5px;
            margin: 20px 0;
        }
        .back-button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #F96E46;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-top: 20px;
        }
        .back-button:hover {
            background-color: #e85e35;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Coming Soon</h1>
        <div class="message">
            <p>This legal document is currently being drafted and reviewed by our legal team.</p>
            <p>We appreciate your patience and understanding.</p>
            <p>Please check back later for updates.</p>
        </div>
        <a href="<c:url value='/'/>" class="back-button">Return to Home</a>
    </div>
</body>
</html> 