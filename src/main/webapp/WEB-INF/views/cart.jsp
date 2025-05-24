<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Shopping Cart - IoTBay</title>
    <link href="${pageContext.request.contextPath}/resources/css/css.css" rel="stylesheet" type="text/css">
    <style>
        .cart-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 2rem;
        }
        .cart-table th {
            background-color: #333;
            color: white;
            padding: 1rem;
            text-align: left;
        }
        .cart-table td {
            padding: 1rem;
            border-bottom: 1px solid #ddd;
        }
        .cart-total {
            text-align: right;
            font-size: 1.2rem;
            margin: 2rem 0;
        }
        .cart-total span {
            color: #F96E46;
            font-weight: bold;
        }
        .cart-actions {
            display: flex;
            justify-content: space-between;
            margin-top: 2rem;
            align-items: center;
        }
        .cart-actions-right {
            display: flex;
            gap: 1rem;
            align-items: center;
        }
        .btn {
            padding: 0.8rem 1.5rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: 500;
            text-decoration: none;
            transition: all 0.2s ease;
            font-size: 1rem;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 120px;
            height: 42px;
        }
        .btn-primary {
            background-color: #F96E46;
            color: white;
        }
        .btn-primary:hover {
            background-color: #e85e36;
            transform: translateY(-1px);
        }
        .btn-secondary {
            background-color: #694A38;
            color: white;
        }
        .btn-secondary:hover {
            background-color: #593a28;
            transform: translateY(-1px);
        }
        .btn-danger {
            background-color: #dc3545;
            color: white;
        }
        .btn-danger:hover {
            background-color: #bb2d3b;
            transform: translateY(-1px);
        }
        .quantity-input {
            width: 70px;
            padding: 0.5rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            text-align: center;
            margin-right: 0.5rem;
        }
        .update-btn {
            background-color: #694A38;
            color: white;
            padding: 0.5rem 1rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.2s ease;
            font-weight: 500;
            height: 36px;
            display: inline-flex;
            align-items: center;
        }
        .update-btn:hover {
            background-color: #593a28;
            transform: translateY(-1px);
        }
    </style>
</head>
<body>
    <%@ include file="common/header.jsp" %>
    
    <div class="container">
        <h1>Shopping Cart</h1>
        
        <c:if test="${empty cart.items}">
            <p>Your cart is empty.</p>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Continue Shopping</a>
        </c:if>
        
        <c:if test="${not empty cart.items}">
            <table class="cart-table">
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
                            <td>$${item.price}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="productId" value="${item.productId}">
                                    <input type="number" name="quantity" value="${item.quantity}" min="1" class="quantity-input">
                                    <button type="submit" class="update-btn">Update</button>
                                </form>
                            </td>
                            <td>$${item.price * item.quantity}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="productId" value="${item.productId}">
                                    <button type="submit" class="btn btn-danger">Remove</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <div class="cart-total">
                Total: <span>$${cart.totalAmount}</span>
            </div>
            
            <div class="cart-actions">
                <div class="cart-actions-left">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Continue Shopping</a>
                </div>
                <div class="cart-actions-right">
                    <form action="${pageContext.request.contextPath}/cart" method="post">
                        <input type="hidden" name="action" value="clear">
                        <button type="submit" class="btn btn-secondary">Clear Saved Cart</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/orders" method="post">
                        <input type="hidden" name="action" value="create">
                        <button type="submit" class="btn btn-primary">Checkout</button>
                    </form>
                </div>
            </div>
        </c:if>
    </div>
    
    <%@ include file="common/footer.jsp" %>
</body>
</html> 