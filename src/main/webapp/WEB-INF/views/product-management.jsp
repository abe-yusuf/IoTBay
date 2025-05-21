<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DecimalFormat" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product Management - IoTBay</title>
    <link href="${pageContext.request.contextPath}/resources/css/css.css" rel="stylesheet" type="text/css">
    <style>
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
        }
        .header-actions {
            display: flex;
            gap: 15px;
            align-items: center;
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
        .btn {
            padding: 10px 20px;
            border-radius: 4px;
            text-decoration: none;
            font-weight: 500;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            transition: all 0.2s;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-add {
            background-color: #4CAF50;
            color: white;
        }
        .btn-add:hover {
            background-color: #45a049;
        }
        .btn-edit {
            background-color: #2196F3;
            color: white;
        }
        .btn-edit:hover {
            background-color: #1976D2;
        }
        .btn-delete {
            background-color: #f44336;
            color: white;
        }
        .btn-delete:hover {
            background-color: #d32f2f;
        }
        .product-table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .product-table th {
            background-color: #694A38;
            color: white;
            padding: 15px;
            text-align: left;
            font-weight: 500;
        }
        .product-table td {
            padding: 15px;
            border-bottom: 1px solid #eee;
            color: #333;
        }
        .product-table tr:last-child td {
            border-bottom: none;
        }
        .product-table tr:hover {
            background-color: #f8f9fa;
        }
        .actions {
            display: flex;
            gap: 10px;
        }
        .error {
            background-color: #ffebee;
            color: #c62828;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .message {
            background-color: #e8f5e9;
            color: #2e7d32;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        @media (max-width: 768px) {
            .header {
                flex-direction: column;
                align-items: stretch;
            }
            .header-actions {
                flex-direction: column;
            }
            .search-box {
                width: 100%;
            }
            .product-table {
                display: block;
                overflow-x: auto;
            }
        }
    </style>
</head>
<body>
    <%@ include file="common/header.jsp" %>
    
    <div class="container">
        <div class="header">
            <h1>Product Management</h1>
            <div class="header-actions">
                <input type="text" id="searchInput" class="search-box" placeholder="Search products..." onkeyup="searchProducts()">
                <a href="products?action=add" class="btn btn-add">Add New Product</a>
            </div>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        
        <% if (request.getAttribute("message") != null) { %>
            <div class="message">
                <%= request.getAttribute("message") %>
            </div>
        <% } %>

        <table class="product-table" id="productTable">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% 
                List<Product> products = (List<Product>) request.getAttribute("products");
                DecimalFormat df = new DecimalFormat("0.00");
                if (products != null && !products.isEmpty()) {
                    for (Product product : products) {
                %>
                    <tr>
                        <td><%= product.getProductID() %></td>
                        <td><%= product.getName() %></td>
                        <td><%= product.getDescription() %></td>
                        <td>$<%= df.format(product.getPrice()) %></td>
                        <td><%= product.getQuantity() %></td>
                        <td class="actions">
                            <a href="products?action=edit&id=<%= product.getProductID() %>" class="btn btn-edit">Edit</a>
                            <form action="products" method="post" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete this product? This action cannot be undone.');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%= product.getProductID() %>">
                                <button type="submit" class="btn btn-delete">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% 
                    }
                } else {
                %>
                    <tr>
                        <td colspan="6" style="text-align: center;">No products found.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <script>
    function searchProducts() {
        var input = document.getElementById("searchInput");
        var filter = input.value.toLowerCase();
        var table = document.getElementById("productTable");
        var tr = table.getElementsByTagName("tr");

        for (var i = 1; i < tr.length; i++) {
            var td = tr[i].getElementsByTagName("td");
            var found = false;
            
            for (var j = 0; j < td.length - 1; j++) {
                var cell = td[j];
                if (cell) {
                    var text = cell.textContent || cell.innerText;
                    if (text.toLowerCase().indexOf(filter) > -1) {
                        found = true;
                        break;
                    }
                }
            }
            
            tr[i].style.display = found ? "" : "none";
        }
    }
    </script>

    <%@ include file="common/footer.jsp" %>
</body>
</html> 