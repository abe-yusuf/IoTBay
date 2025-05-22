<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Shopping Cart - IoT Bay</title>
    <style>
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        th {
            background-color: #333;
            color: white;
        }
        
        .quantity-input {
            width: 60px;
            padding: 5px;
        }
        
        .update-btn {
            padding: 5px 10px;
            background-color: #795548;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        
        .remove-btn {
            padding: 8px 16px;
            background-color: #dc3545;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            width: 100%;
        }
        
        .cart-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 20px;
        }
        
        .continue-shopping {
            padding: 10px 20px;
            background-color: #F96E46;
            color: white;
            text-decoration: none;
            border-radius: 3px;
        }
        
        .checkout-btn {
            padding: 10px 20px;
            background-color: #F96E46;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        
        .clear-cart {
            padding: 10px 20px;
            background-color: #6c757d;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        
        .total {
            font-size: 1.2em;
            font-weight: bold;
            text-align: right;
            margin: 20px 0;
        }
        
        .empty-cart {
            text-align: center;
            padding: 50px;
            background-color: #f8f9fa;
            border-radius: 5px;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <jsp:include page="../shared/header.jsp" />
    
    <div class="container">
        <h1>Shopping Cart</h1>
        
        <c:choose>
            <c:when test="${empty cart.items}">
                <div class="empty-cart">
                    <h2>Your cart is empty</h2>
                    <p>Start shopping to add items to your cart!</p>
                    <a href="<c:url value='/products'/>" class="continue-shopping">Browse Products</a>
                </div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Total</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${cart.items}">
                            <tr>
                                <td>${item.productName}</td>
                                <td>$<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></td>
                                <td>
                                    <form action="<c:url value='/cart/update'/>" method="post" style="display: flex; align-items: center; gap: 10px;">
                                        <input type="hidden" name="productId" value="${item.productId}">
                                        <input type="number" name="quantity" value="${item.quantity}" min="1" class="quantity-input">
                                        <button type="submit" class="update-btn">Update</button>
                                    </form>
                                </td>
                                <td>$<fmt:formatNumber value="${item.price * item.quantity}" pattern="#,##0.00"/></td>
                                <td>
                                    <form action="<c:url value='/cart/remove'/>" method="post">
                                        <input type="hidden" name="productId" value="${item.productId}">
                                        <button type="submit" class="remove-btn">Remove</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                
                <div class="total">
                    Total: $<fmt:formatNumber value="${cart.totalAmount}" pattern="#,##0.00"/>
                </div>
                
                <div class="cart-actions">
                    <a href="<c:url value='/products'/>" class="continue-shopping">Continue Shopping</a>
                    
                    <form action="<c:url value='/cart/clear'/>" method="post" style="display: inline;">
                        <button type="submit" class="clear-cart">Clear Cart</button>
                    </form>
                    
                    <form action="<c:url value='/checkout'/>" method="post" style="display: inline;">
                        <button type="submit" class="checkout-btn">Checkout</button>
                    </form>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <jsp:include page="../common/footer.jsp" />
</body>
</html> 