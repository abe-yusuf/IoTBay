<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Add New Device - IoTBay</title>
        <link href="${pageContext.request.contextPath}/resources/css/css.css" rel="stylesheet" type="text/css">
        <style>
            .btn-container {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-top: 30px;
            }
            .btn-primary {
                background-color: #694A38;
                color: white;
                padding: 12px 24px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 16px;
                font-weight: 500;
                transition: all 0.3s ease;
                text-transform: uppercase;
                letter-spacing: 0.5px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .btn-primary:hover {
                background-color: #523728;
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            }
            .back-link {
                color: #694A38;
                text-decoration: none;
                font-weight: 500;
                transition: all 0.3s ease;
                padding: 12px 24px;
                border: 2px solid #694A38;
                border-radius: 6px;
            }
            .back-link:hover {
                background-color: #f8f9fa;
                color: #523728;
                border-color: #523728;
            }
        </style>
    </head>

    <body>
        <%@ include file="common/header.jsp" %>

        <main>
            <section class="register-form-container">
                <h2>Add New IoT Device</h2>
                
                <% if (request.getAttribute("error") != null) { %>
                    <div style="background-color: #f8d7da; color: #721c24; padding: 10px; margin-bottom: 15px; border-radius: 4px;">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <form class="register-form" action="products" method="post">
                    <input type="hidden" name="action" value="add">
                    
                    <div class="form-group">
                        <label for="name">Device Name: </label>
                        <input type="text" id="name" name="name" required/>
                    </div>
                    
                    <div class="form-group">
                        <label for="imageUrl">Image URL: </label>
                        <input type="text" id="imageUrl" name="imageUrl" />
                    </div>
                    
                    <div class="form-group">
                        <label for="description">Description: </label>
                        <textarea id="description" name="description" rows="4" style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="price">Price ($): </label>
                        <input type="number" id="price" name="price" step="0.01" min="0" required/>
                    </div>
                    
                    <div class="form-group">
                        <label for="quantity">Stock Quantity: </label>
                        <input type="number" id="quantity" name="quantity" min="0" required/>
                    </div>

                    <div class="btn-container">
                        <a href="products?action=manage" class="back-link">Back to Management</a>
                        <button type="submit" class="btn-primary">Add Device</button>
                    </div>
                </form>
            </section>
        </main>

        <%@ include file="common/footer.jsp" %>
    </body>
</html>