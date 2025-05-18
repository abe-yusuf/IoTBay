<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Product" %>
<html>
    <head>
        <title>IoT Device Catalog - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <%@ include file="header.jsp" %>

        <main>
            <section>
                <h2>IoT Device Catalog</h2>

                <!-- Search Form -->
                <div style="margin-bottom: 20px;">
                    <form action="products" method="get">
                        <div style="display: flex; max-width: 500px; margin: 0 auto;">
                            <input type="text" name="search" placeholder="Search by name or description" 
                                   value="${searchTerm}" style="flex: 1; padding: 8px;">
                            <button type="submit" style="padding: 8px 16px; background-color: #F96E46; color: white; border: none;">
                                Search
                            </button>
                        </div>
                    </form>
                </div>

                <!-- Messages -->
                <% if (request.getParameter("message") != null) { %>
                    <div style="background-color: #d4edda; color: #155724; padding: 10px; margin-bottom: 15px; border-radius: 4px;">
                        <%= request.getParameter("message") %>
                    </div>
                <% } %>
                
                <% if (request.getParameter("error") != null) { %>
                    <div style="background-color: #f8d7da; color: #721c24; padding: 10px; margin-bottom: 15px; border-radius: 4px;">
                        <%= request.getParameter("error") %>
                    </div>
                <% } %>

                <!-- Admin Controls -->
                <% if (session.getAttribute("user") != null) { %>
                    <div style="margin-bottom: 20px; text-align: right;">
                        <a href="products?action=add" class="form-button" style="display: inline-block; max-width: 200px; text-align: center; text-decoration: none;">
                            Add New Device
                        </a>
                    </div>
                <% } %>

                <!-- Products List -->
                <div style="display: flex; flex-wrap: wrap; gap: 20px; justify-content: center;">
                    <% 
                        ArrayList<Product> products = (ArrayList<Product>)request.getAttribute("products");
                        if (products != null && !products.isEmpty()) {
                            for (Product product : products) {
                    %>
                        <div style="border: 1px solid #ddd; border-radius: 8px; padding: 15px; width: 250px; background-color: white;">
                            <% if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) { %>
                                <img src="<%= product.getImageUrl() %>" alt="<%= product.getName() %>" style="width: 100%; height: 150px; object-fit: cover; border-radius: 4px;">
                            <% } else { %>
                                <div style="width: 100%; height: 150px; background-color: #f5f5f5; display: flex; align-items: center; justify-content: center; border-radius: 4px;">
                                    No Image
                                </div>
                            <% } %>
                            
                            <h3 style="margin-top: 10px;"><%= product.getName() %></h3>
                            <p style="color: #F96E46; font-weight: bold; font-size: 1.2rem;">$<%= String.format("%.2f", product.getPrice()) %></p>
                            <p><strong>Stock:</strong> <%= product.getQuantity() %></p>
                            
                            <div style="display: flex; justify-content: space-between; margin-top: 15px;">
                                <a href="products?action=details&id=<%= product.getProductID() %>" style="text-decoration: none; background-color: #694A38; color: white; padding: 8px 12px; border-radius: 4px;">
                                    View Details
                                </a>
                                
                                <% if (session.getAttribute("user") != null) { %>
                                    <a href="products?action=edit&id=<%= product.getProductID() %>" style="text-decoration: none; background-color: #333; color: white; padding: 8px 12px; border-radius: 4px;">
                                        Edit
                                    </a>
                                <% } %>
                            </div>
                        </div>
                    <% 
                            }
                        } else {
                    %>
                        <p>No products found.</p>
                    <% } %>
                </div>
            </section>
        </main>

        <%@ include file="footer.jsp" %>
    </body>
</html>