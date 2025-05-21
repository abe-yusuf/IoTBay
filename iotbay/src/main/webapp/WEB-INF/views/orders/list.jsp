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
        }

        h1 {
            color: #333;
            font-size: 2rem;
            margin-bottom: 2rem;
            text-align: center;
        }

        .search-form {
            margin-bottom: 20px;
            padding: 20px;
            background-color: #f5f5f5;
            border-radius: 8px;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 20px;
        }

        .date-range-container {
            display: flex;
            gap: 20px;
            width: 100%;
            max-width: 900px;
            justify-content: center;
        }
        
        .form-group {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 8px;
            max-width: 250px;
        }
        
        .form-group label {
            display: block;
            font-weight: 600;
            color: #333;
            text-align: center;
        }
        
        .form-group input[type="date"],
        .form-group input[type="text"] {
            width: 100%;
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            height: 40px;
            font-size: 14px;
            text-align: center;
        }
        
        .search-form button {
            padding: 0 32px;
            background-color: #F96E46;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            height: 40px;
            font-weight: 500;
            font-size: 14px;
            transition: all 0.2s;
            min-width: 120px;
        }
        
        .search-form button:hover {
            background-color: #e85e36;
            transform: translateY(-1px);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        
        .clear-search {
            color: #666;
            text-decoration: none;
            font-size: 14px;
            display: inline-block;
            height: 40px;
            line-height: 40px;
            margin-left: 12px;
        }
        
        .clear-search:hover {
            text-decoration: underline;
        }

        .orders-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 2rem;
            background: white;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            overflow: hidden;
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

        .no-orders {
            text-align: center;
            padding: 2rem;
            color: #666;
            font-style: italic;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
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
        
        <form class="search-form" action="${pageContext.request.contextPath}/orders" method="get">
            <div class="date-range-container">
                <div class="form-group">
                    <label for="orderId">Order ID:</label>
                    <input type="text" id="orderId" name="orderId" value="${searchOrderId}" placeholder="Enter order ID">
                </div>
                
                <div class="form-group">
                    <label for="fromDate">From Date:</label>
                    <input type="date" id="fromDate" name="fromDate" value="${fromDate}">
                </div>
                
                <div class="form-group">
                    <label for="toDate">To Date:</label>
                    <input type="date" id="toDate" name="toDate" value="${toDate}">
                </div>
            </div>
            
            <div>
                <button type="submit">Search</button>
                <c:if test="${searchOrderId != null || fromDate != null || toDate != null}">
                    <a href="${pageContext.request.contextPath}/orders" class="clear-search">Clear</a>
                </c:if>
            </div>
        </form>

        <c:if test="${empty orders}">
            <div class="no-orders">
                <p>No orders found for the selected criteria.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-small">Start Shopping</a>
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
    </div>
    
    <jsp:include page="../common/footer.jsp" />
</body>
</html> 