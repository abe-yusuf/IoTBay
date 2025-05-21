<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Order Management - IoTBay</title>
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

        h1 span {
            color: #F96E46;
        }

        .table-container {
            background: white;
            border-radius: 8px;
            padding: 1.5rem;
            overflow-x: auto;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 1rem;
        }

        .table th {
            background-color: #333;
            color: white;
            padding: 1rem;
            text-align: left;
            font-weight: 600;
        }

        .table td {
            padding: 1rem;
            border-bottom: 1px solid #eee;
            color: #333;
        }

        .table tr:hover {
            background-color: #f8f9fa;
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

        .status-select {
            padding: 0.375rem 0.75rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            background-color: white;
            color: #333;
            font-size: 0.875rem;
        }

        .btn {
            display: inline-block;
            padding: 0.375rem 0.75rem;
            background-color: #F96E46;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.875rem;
            transition: all 0.2s;
        }

        .btn:hover {
            background-color: #e85e36;
            transform: translateY(-1px);
        }

        .no-orders {
            text-align: center;
            padding: 2rem;
            color: #666;
            font-size: 1.1rem;
            background-color: #f8f9fa;
            border-radius: 4px;
        }

        .customer-info {
            display: flex;
            flex-direction: column;
        }

        .customer-details {
            font-size: 0.875rem;
            color: #666;
            margin-top: 0.25rem;
        }

        .order-items {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
            margin: -0.25rem;
        }

        .order-item {
            display: inline-block;
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            background-color: #f8f9fa;
            font-size: 0.875rem;
            color: #333;
            white-space: nowrap;
        }

        .search-section {
            margin-bottom: 2rem;
            background: white;
            padding: 1.5rem;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .search-form {
            display: flex;
            justify-content: center;
        }

        .search-group {
            display: flex;
            gap: 1rem;
            align-items: center;
        }

        .search-input {
            padding: 0.5rem 1rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 1rem;
            min-width: 200px;
        }

        .search-input:focus {
            outline: none;
            border-color: #F96E46;
            box-shadow: 0 0 0 2px rgba(249, 110, 70, 0.1);
        }

        .nowrap {
            white-space: nowrap;
        }
    </style>
</head>
<body>
    <jsp:include page="../common/header.jsp" />
    
    <main>
        <div class="container">
            <h1>Order <span>Management</span></h1>
            
            <div class="search-section">
                <form action="${pageContext.request.contextPath}/admin/orders" method="get" class="search-form">
                    <div class="search-group">
                        <input type="number" name="orderId" placeholder="Enter Order ID" class="search-input" min="1">
                        <button type="submit" class="btn">Search</button>
                        <a href="${pageContext.request.contextPath}/admin/orders" class="btn nowrap">Show All</a>
                    </div>
                </form>
            </div>
            
            <div class="table-container">
                <c:choose>
                    <c:when test="${not empty orders}">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th class="nowrap">Order ID</th>
                                    <th>Customer</th>
                                    <th>Date</th>
                                    <th>Products</th>
                                    <th>Total</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${orders}">
                                    <tr>
                                        <td>#${order.orderId}</td>
                                        <td>
                                            <div class="customer-info">
                                                <div>${order.userFirstName} ${order.userLastName}</div>
                                                <div class="customer-details">
                                                    <small>${order.userPhone}</small>
                                                </div>
                                            </div>
                                        </td>
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
                                            <form action="${pageContext.request.contextPath}/admin/orders" method="post" style="display: flex; align-items: center; gap: 0.5rem;">
                                                <input type="hidden" name="action" value="update-status">
                                                <input type="hidden" name="orderId" value="${order.orderId}">
                                                <select name="status" class="status-select">
                                                    <option value="PENDING" ${order.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                                                    <option value="PROCESSING" ${order.status == 'PROCESSING' ? 'selected' : ''}>Processing</option>
                                                    <option value="SHIPPED" ${order.status == 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                                                    <option value="DELIVERED" ${order.status == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                                    <option value="CANCELLED" ${order.status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                                </select>
                                                <button type="submit" class="btn">Update</button>
                                            </form>
                                            <a href="${pageContext.request.contextPath}/orders/${order.orderId}" class="btn" style="margin-top: 0.5rem;">View Details</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="no-orders">
                            No orders found.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>
    
    <jsp:include page="../common/footer.jsp" />
</body>
</html> 