<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Orders - IoTBay</title>
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

        h1 {
            color: #333;
            font-size: 2rem;
            margin-bottom: 2rem;
            text-align: center;
        }

        .orders-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 2rem;
            background: white;
        }

        .orders-table th {
            background-color: #333;
            color: white;
            padding: 1rem;
            text-align: left;
        }

        .orders-table td {
            padding: 1rem;
            border-bottom: 1px solid #eee;
            color: #333;
        }

        .orders-table tr:hover {
            background-color: #f8f9fa;
        }

        .btn {
            display: inline-block;
            padding: 0.5rem 1rem;
            border-radius: 4px;
            text-decoration: none;
            font-weight: 500;
            transition: all 0.2s;
        }

        .btn-small {
            background-color: #F96E46;
            color: white;
            margin-right: 0.5rem;
        }

        .btn-small:hover {
            background-color: #e85e36;
            transform: translateY(-1px);
        }

        .actions {
            margin-top: 2rem;
            text-align: center;
        }

        .actions .btn {
            background-color: #F96E46;
            color: white;
            padding: 0.8rem 1.5rem;
            font-size: 1rem;
        }

        .actions .btn:hover {
            background-color: #e85e36;
            transform: translateY(-1px);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .no-orders {
            text-align: center;
            padding: 2rem;
            color: #666;
            font-size: 1.1rem;
        }

        .order-items {
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        .order-item {
            display: inline-block;
            background: #f8f9fa;
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            font-size: 0.9rem;
            color: #333;
        }

        .status {
            display: inline-block;
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            font-size: 0.875rem;
            font-weight: 500;
        }

        .status-pending {
            background-color: #fff3cd;
            color: #856404;
        }

        .status-processing {
            background-color: #cce5ff;
            color: #004085;
        }

        .status-shipped {
            background-color: #d4edda;
            color: #155724;
        }

        .status-delivered {
            background-color: #d4edda;
            color: #155724;
        }

        .status-cancelled {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
    <jsp:include page="../common/header.jsp" />
    
    <div class="container">
        <h1>My Orders</h1>
        
        <c:if test="${empty orders}">
            <div class="no-orders">
                <p>You haven't placed any orders yet.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn">Start Shopping</a>
            </div>
        </c:if>
        
        <c:if test="${not empty orders}">
            <table class="orders-table">
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Date</th>
                        <th>Products</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <td>#${order.orderId}</td>
                            <td><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>
                                <div class="order-items">
                                    <c:forEach var="item" items="${order.orderItems}">
                                        <span class="order-item">
                                            ${item.productName} (x${item.quantity})
                                        </span>
                                    </c:forEach>
                                </div>
                            </td>
                            <td>$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></td>
                            <td>
                                <span class="status status-${order.status.toLowerCase()}">${order.status}</span>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/orders/${order.orderId}" class="btn btn-small">View Details</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        
        <div class="actions">
            <a href="${pageContext.request.contextPath}/products" class="btn">Continue Shopping</a>
        </div>
    </div>
    
    <jsp:include page="../common/footer.jsp" />
</body>
</html> 