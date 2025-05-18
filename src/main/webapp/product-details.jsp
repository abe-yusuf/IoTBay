<%@ page import="model.Product" %>
<html>
    <head>
        <title>${product.name} - IoTBay</title>
        <link href="css.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <%@ include file="header.jsp" %>

        <main>
            <section style="background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);">
                <a href="products" style="color: #694A38; text-decoration: none; display: inline-block; margin-bottom: 20px;">
                    &larr; Back to Catalog
                </a>

                <div style="display: flex; flex-wrap: wrap; gap: 30px;">
                    <div style="flex: 1; min-width: 300px;">
                        <% if (((Product)request.getAttribute("product")).getImageUrl() != null && !((Product)request.getAttribute("product")).getImageUrl().isEmpty()) { %>
                            <img src="${product.imageUrl}" alt="${product.name}" style="width: 100%; border-radius: 8px; max-height: 400px; object-fit: contain;">
                        <% } else { %>
                            <div style="width: 100%; height: 300px; background-color: #f5f5f5; display: flex; align-items: center; justify-content: center; border-radius: 8px;">
                                No Image Available
                            </div>
                        <% } %>
                    </div>
                    
                    <div style="flex: 1; min-width: 300px;">
                        <h1 style="margin-top: 0; color: #333;">${product.name}</h1>
                        
                        <p style="color: #F96E46; font-weight: bold; font-size: 1.5rem; margin: 20px 0;">
                            $${product.price}
                        </p>
                        
                        <div style="background-color: ${product.quantity > 0 ? '#d4edda' : '#f8d7da'}; 
                             color: ${product.quantity > 0 ? '#155724' : '#721c24'}; 
                             padding: 10px; border-radius: 4px; display: inline-block; margin-bottom: 20px;">
                            <strong>Stock:</strong> 
                            ${product.quantity > 0 ? product.quantity.toString().concat(' units available') : 'Out of stock'}
                        </div>
                        
                        <h3>Description</h3>
                        <p style="line-height: 1.6;">${product.description}</p>
                        
                        <div style="margin-top: 30px; display: flex; gap: 15px;">
                            <% if (session.getAttribute("user") != null) { %>
                                <a href="products?action=edit&id=${product.productID}" 
                                   style="background-color: #333; color: white; padding: 10px 20px; border-radius: 4px; text-decoration: none;">
                                    Edit Device
                                </a>
                            <% } %>
                            
                            <% if (((Product)request.getAttribute("product")).getQuantity() > 0) { %>
                                <a href="#" style="background-color: #F96E46; color: white; padding: 10px 20px; border-radius: 4px; text-decoration: none;">
                                    Add to Cart
                                </a>
                            <% } else { %>
                                <button disabled style="background-color: #ddd; color: #666; padding: 10px 20px; border-radius: 4px; border: none; cursor: not-allowed;">
                                    Out of Stock
                                </button>
                            <% } %>
                        </div>
                    </div>
                </div>
            </section>
        </main>

        <%@ include file="footer.jsp" %>
    </body>
</html>