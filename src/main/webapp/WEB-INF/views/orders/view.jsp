<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Order Details - IoTBay</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/css.css">
    <style>
        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 2rem;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        h1, h2 {
            color: #333;
            margin-bottom: 1.5rem;
        }

        h1 {
            font-size: 2rem;
            text-align: center;
        }

        h2 {
            font-size: 1.5rem;
            border-bottom: 2px solid #F96E46;
            padding-bottom: 0.5rem;
            margin-top: 2rem;
        }

        .order-info, .customer-info {
            background: #f8f9fa;
            padding: 1.5rem;
            border-radius: 8px;
            margin-bottom: 2rem;
        }

        .order-info p, .customer-info p {
            margin: 0.5rem 0;
            color: #333;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 2rem;
            background: white;
        }

        .table th {
            background-color: #333;
            color: white;
            padding: 1rem;
            text-align: left;
        }

        .table td {
            padding: 1rem;
            border-bottom: 1px solid #eee;
            color: #333;
        }

        .table tr:hover {
            background-color: #f8f9fa;
        }

        .actions {
            text-align: center;
            margin-top: 2rem;
        }

        .button {
            display: inline-block;
            padding: 0.8rem 1.5rem;
            background-color: #F96E46;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            border: none;
            cursor: pointer;
            font-size: 1rem;
            margin: 0 0.5rem;
            transition: all 0.2s;
        }

        .button:hover {
            background-color: #e85e36;
            transform: translateY(-1px);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .staff-actions {
            background: #f8f9fa;
            padding: 1.5rem;
            border-radius: 8px;
            margin: 2rem 0;
        }

        .staff-actions h3 {
            color: #333;
            margin-bottom: 1rem;
        }

        select {
            padding: 0.5rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin-right: 1rem;
        }

        .edit-form {
            margin-bottom: 1rem;
        }
        
        .form-group {
            margin-bottom: 1rem;
        }
        
        .form-input {
            padding: 0.375rem 0.75rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin: 0.25rem;
            font-size: 0.875rem;
        }
        
        .button-danger {
            background-color: #dc3545;
        }
        
        .button-danger:hover {
            background-color: #c82333;
        }
        
        .add-item-form {
            background: #f8f9fa;
            padding: 1rem;
            border-radius: 4px;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Order Details</h1>
        
        <div class="order-info">
            <p><strong>Order ID:</strong> ${order.orderId}</p>
            <p><strong>Date:</strong> <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
            <p><strong>Status:</strong> ${order.status}</p>
            <p><strong>Total Amount:</strong> $<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></p>
        </div>
        
        <div class="customer-info">
            <h2>Customer Details</h2>
            <c:if test="${sessionScope.isStaff}">
                <form action="${pageContext.request.contextPath}/orders" method="post" class="edit-form">
                    <input type="hidden" name="action" value="update-customer">
                    <input type="hidden" name="orderId" value="${order.orderId}">
                    <div class="form-group">
                        <label><strong>Full Name:</strong></label>
                        <input type="text" name="firstName" value="${order.userFirstName}" class="form-input" required>
                        <input type="text" name="lastName" value="${order.userLastName}" class="form-input" required>
                    </div>
                    <div class="form-group">
                        <label><strong>Phone:</strong></label>
                        <input type="text" name="phone" value="${order.userPhone}" class="form-input" required>
                    </div>
                    <div class="form-group">
                        <label><strong>Address:</strong></label>
                        <input type="text" name="address" value="${order.userAddress}" class="form-input" required>
                    </div>
                    <button type="submit" class="button">Update Customer Details</button>
                </form>
            </c:if>
            <c:if test="${not sessionScope.isStaff}">
                <p><strong>Full Name:</strong> ${order.userFirstName} ${order.userLastName}</p>
                <p><strong>Phone:</strong> ${order.userPhone}</p>
                <p><strong>Address:</strong> ${order.userAddress}</p>
            </c:if>
        </div>
        
        <h2>Order Items</h2>
        <c:if test="${sessionScope.isStaff}">
            <div class="add-item-form">
                <form action="${pageContext.request.contextPath}/orders" method="post" class="edit-form">
                    <input type="hidden" name="action" value="add-item">
                    <input type="hidden" name="orderId" value="${order.orderId}">
                    <div class="form-group">
                        <label>Add Product:</label>
                        <select name="productId" class="form-input" required>
                            <c:forEach var="product" items="${products}">
                                <option value="${product.productID}">${product.name} ($${product.price})</option>
                            </c:forEach>
                        </select>
                        <input type="number" name="quantity" value="1" min="1" class="form-input" required>
                        <button type="submit" class="button">Add Item</button>
                    </div>
                </form>
            </div>
        </c:if>
        <table class="table">
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Subtotal</th>
                    <c:if test="${sessionScope.isStaff}">
                        <th>Actions</th>
                    </c:if>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${order.orderItems}">
                    <tr>
                        <c:if test="${sessionScope.isStaff}">
                            <form action="${pageContext.request.contextPath}/orders" method="post" class="edit-form">
                                <input type="hidden" name="action" value="update-item">
                                <input type="hidden" name="orderId" value="${order.orderId}">
                                <input type="hidden" name="orderItemId" value="${item.orderItemId}">
                                <td>
                                    <select name="productId" class="form-input" required>
                                        <c:forEach var="product" items="${products}">
                                            <option value="${product.productID}" ${product.productID == item.productId ? 'selected' : ''}>
                                                ${product.name} ($${product.price})
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td><input type="number" name="quantity" value="${item.quantity}" min="1" class="form-input" required></td>
                                <td><input type="number" name="price" value="${item.price}" min="0" step="0.01" class="form-input" required></td>
                                <td>$<fmt:formatNumber value="${item.price * item.quantity}" pattern="#,##0.00"/></td>
                                <td>
                                    <button type="submit" class="button">Update</button>
                                    <button type="submit" name="action" value="delete-item" class="button button-danger">Delete</button>
                                </td>
                            </form>
                        </c:if>
                        <c:if test="${not sessionScope.isStaff}">
                            <td>${item.productName}</td>
                            <td>${item.quantity}</td>
                            <td>$<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></td>
                            <td>$<fmt:formatNumber value="${item.price * item.quantity}" pattern="#,##0.00"/></td>
                        </c:if>
                    </tr>
                </c:forEach>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="${sessionScope.isStaff ? '4' : '3'}" style="text-align: right;"><strong>Total:</strong></td>
                    <td>$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></td>
                </tr>
            </tfoot>
        </table>
        
        <c:if test="${sessionScope.isStaff}">
            <div class="staff-actions">
                <h3>Update Order Status</h3>
                <form action="${pageContext.request.contextPath}/orders" method="post">
                    <input type="hidden" name="action" value="update-status">
                    <input type="hidden" name="orderId" value="${order.orderId}">
                    <select name="status">
                        <option value="PENDING" ${order.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                        <option value="PROCESSING" ${order.status == 'PROCESSING' ? 'selected' : ''}>Processing</option>
                        <option value="SHIPPED" ${order.status == 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                        <option value="DELIVERED" ${order.status == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                        <option value="CANCELLED" ${order.status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                    </select>
                    <button type="submit" class="button">Update Status</button>
                </form>
            </div>
        </c:if>
        
        <div class="actions">
            <c:choose>
                <c:when test="${sessionScope.isStaff}">
                    <a href="${pageContext.request.contextPath}/admin/orders" class="button">Back to Orders</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/orders" class="button">Back to Orders</a>
                </c:otherwise>
            </c:choose>
            <a href="${pageContext.request.contextPath}/" class="button">Back to Home</a>
        </div>
    </div>
</body>
</html>