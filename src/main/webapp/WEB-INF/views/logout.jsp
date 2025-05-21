<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Logged Out - IoTBay</title>
        <link href="${pageContext.request.contextPath}/resources/css/css.css" rel="stylesheet" type="text/css">
        <style>
            .logout-container {
                max-width: 600px;
                margin: 4rem auto;
                padding: 2rem;
                text-align: center;
                background: white;
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            }

            .logout-container h2 {
                color: #694A38;
                font-size: 2rem;
                margin-bottom: 1.5rem;
            }

            .logout-container p {
                color: #666;
                font-size: 1.1rem;
                line-height: 1.6;
                margin-bottom: 2rem;
            }

            .btn-home {
                display: inline-block;
                background-color: #F96E46;
                color: white;
                padding: 0.8rem 1.5rem;
                border-radius: 4px;
                text-decoration: none;
                transition: all 0.2s;
            }

            .btn-home:hover {
                background-color: #e85e35;
                transform: translateY(-2px);
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }

            @media (max-width: 768px) {
                .logout-container {
                    margin: 2rem 1rem;
                    padding: 1.5rem;
                }
            }
        </style>
    </head>

    <body>
        <%@ include file="common/header.jsp" %>
        
        <main>
            <section class="logout-container">
                <% session.invalidate(); %>
                <h2>Successfully Logged Out</h2>
                <p>Thank you for visiting IoTBay. We hope to see you again soon!</p>
                <a href="${pageContext.request.contextPath}/" class="btn-home">Return to Home</a>
            </section>
        </main>
        
        <%@ include file="common/footer.jsp" %>
    </body>
</html>
