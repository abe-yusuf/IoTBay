<html>
    <head>
        <title>Add New Device - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <%@ include file="header.jsp" %>

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

                    <button type="submit" class="form-button">Add Device</button>
                    
                    <div class="form-group" style="text-align: center;">
                        <a href="products" style="color: #F96E46; text-decoration: none;">Cancel and return to catalog</a>
                    </div>
                </form>
            </section>
        </main>

        <%@ include file="footer.jsp" %>
    </body>
</html>