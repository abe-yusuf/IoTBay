<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.net.URLEncoder" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>IoT Devices - IoTBay</title>
        <link href="${pageContext.request.contextPath}/resources/css/css.css" rel="stylesheet" type="text/css">
        <style>
            main {
                width: 100%;
                max-width: none;
                margin: 0;
                padding: 0;
            }
            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }
            .header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 30px;
                flex-wrap: wrap;
                gap: 20px;
            }
            .header h1 {
                color: #333;
                margin: 0;
                font-size: 2em;
            }
            .search-box {
                padding: 10px 15px;
                border: 1px solid #ddd;
                border-radius: 4px;
                width: 300px;
                font-size: 14px;
            }
            .search-box:focus {
                outline: none;
                border-color: #F96E46;
            }
            .product-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
                gap: 30px;
                margin-top: 20px;
            }
            .product-card {
                background: white;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                transition: transform 0.2s, box-shadow 0.2s;
                display: flex;
                flex-direction: column;
                height: 100%;
            }
            .product-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 5px 15px rgba(0,0,0,0.2);
            }
            .product-image-container {
                position: relative;
                width: 100%;
                padding-top: 75%; /* 4:3 Aspect Ratio */
                background-color: #f5f5f5;
            }
            .product-image {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                object-fit: contain;
                padding: 10px;
            }
            .no-image {
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                color: #999;
                font-size: 14px;
            }
            .product-details {
                padding: 20px;
                flex-grow: 1;
                display: flex;
                flex-direction: column;
            }
            .product-name {
                font-size: 1.2em;
                color: #333;
                margin: 0 0 10px 0;
                font-weight: 600;
                line-height: 1.4;
            }
            .product-price {
                font-size: 1.3em;
                color: #F96E46;
                font-weight: bold;
                margin: 10px 0;
            }
            .product-stock {
                color: #4CAF50;
                margin: 5px 0 15px 0;
                font-size: 0.9em;
            }
            .out-of-stock {
                color: #f44336;
            }
            .btn-view {
                background-color: #694A38;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 4px;
                display: inline-block;
                transition: background-color 0.2s;
                text-align: center;
                margin-top: auto;
                border: none;
                cursor: pointer;
            }
            .btn-view:hover {
                background-color: #523728;
            }
            .error {
                background-color: #ffebee;
                color: #c62828;
                padding: 15px;
                border-radius: 4px;
                margin-bottom: 20px;
            }
            @media (max-width: 768px) {
                .header {
                    flex-direction: column;
                    align-items: stretch;
                }
                .search-box {
                    width: 100%;
                }
                .product-grid {
                    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
                    gap: 20px;
                }
            }
        </style>
    </head>

    <body>
        <%@ include file="common/header.jsp" %>

        <main>
            <div class="container">
                <div class="header">
                    <h1>IoT Device Catalog</h1>
                    <form action="products" method="get" style="margin: 0;">
                        <input type="text" name="search" class="search-box" placeholder="Search devices..." 
                               value="${searchTerm != null ? searchTerm : ''}" />
                    </form>
                </div>

                <% if (request.getAttribute("error") != null) { %>
                    <div class="error">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>
                
                <div class="product-grid">
                    <% 
                    List<Product> products = (List<Product>) request.getAttribute("products");
                    DecimalFormat df = new DecimalFormat("0.00");
                        if (products != null && !products.isEmpty()) {
                            for (Product product : products) {
                            String imageUrl = product.getImageUrl();
                            boolean hasImage = imageUrl != null && !imageUrl.trim().isEmpty();
                    %>
                        <div class="product-card">
                            <div class="product-image-container">
                                <% if (hasImage) { %>
                                    <img src="<%= imageUrl %>" 
                                         alt="<%= product.getName() %>" 
                                         class="product-image"
                                         loading="lazy"
                                         crossorigin="anonymous"
                                         onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/resources/images/no-image.png';">
                            <% } else { %>
                                    <div class="no-image">No Image Available</div>
                                <% } %>
                                </div>
                            <div class="product-details">
                                <h2 class="product-name"><%= product.getName() %></h2>
                                <p class="product-price">$<%= df.format(product.getPrice()) %></p>
                                <% if (product.getQuantity() > 0) { %>
                                    <p class="product-stock"><%= product.getQuantity() %> in stock</p>
                                <% } else { %>
                                    <p class="product-stock out-of-stock">Out of stock</p>
                            <% } %>
                                <a href="${pageContext.request.contextPath}/products?action=details&id=<%= product.getProductID() %>" class="btn-view">
                                    View Details
                                </a>
                            </div>
                        </div>
                    <% 
                            }
                        } else {
                    %>
                        <div style="grid-column: 1 / -1; text-align: center; padding: 40px;">
                        <p>No products found.</p>
                        </div>
                    <% } %>
                </div>
            </div>
        </main>

        <%@ include file="common/footer.jsp" %>
    </body>
</html>