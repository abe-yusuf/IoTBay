<%@ page import="model.Product" %>
<html>
    <head>
        <title>Edit Device - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <%@ include file="header.jsp" %>

        <main>
            <section class="register-form-container">
                <h2>Edit IoT Device</h2>
                
                <% if (request.getAttribute("error") != null) { %>
                    <div style="background-color: #f8d7da; color: #721c24; padding: 10px; margin-bottom: 15px; border-radius: 4px;">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <form class="register-form" action="products" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="productId" value="${product.productID}">
                    
                    <div class="form-group">
                        <label for="name">Device Name: </label>
                        <input type="text" id="name" name="name" value="${product.name}" required/>
                    </div>
                    
                    <div class="form-group">
                        <label for="imageUrl">Image URL: </label>
                        <input type="text" id="imageUrl" name="imageUrl" value="${product.imageUrl}" />
                    </div>
                    
                    <div class="form-group">
                        <label for="description">Description: </label>
                        <textarea id="description" name="description" rows="4" style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">${product.description}</textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="price">Price ($): </label>
                        <input type="number" id="price" name="price" step="0.01" min="0" value="${product.price}" required/>
                    </div>
                    
                    <div class="form-group">
                        <label for="quantity">Stock Quantity: </label>
                        <input type="number" id="quantity" name="quantity" min="0" value="${product.quantity}" required/>
                    </div>

                    <button type="submit" class="form-button">Update Device</button>
                    
                    <div class="form-group" style="text-align: center; margin-top: 10px;">
                        <a href="products?action=delete&id=${product.productID}" style="color: #721c24; text-decoration: none;" 
                           onclick="return confirm('Are you sure you want to delete this device?');">
                            Delete this device
                        </a>
                    </div>
                    
                    <div class="form-group" style="text-align: center;">
                        <a href="products" style="color: #F96E46; text-decoration: none;">Cancel and return to catalog</a>
                    </div>
                </form>
            </section>
        </main>

        <%@ include file="footer.jsp" %>
    </body>
</html>