<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Admin Dashboard - IoT Bay</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css.css">
    <style>
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 2rem;
            margin-top: 2rem;
        }
        .dashboard-card {
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            text-align: center;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        .dashboard-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }
        .dashboard-card h3 {
            color: #333;
            margin-bottom: 1rem;
        }
        .dashboard-card p {
            color: #666;
            margin-bottom: 1.5rem;
            min-height: 3em; /* Keep descriptions aligned */
        }
        .card-actions {
            display: flex;
            flex-direction: column;
            gap: 0.75rem;
        }
        .dashboard-card .btn {
            background-color: #F96E46;
            color: white;
            text-decoration: none;
            padding: 0.8rem 1.5rem;
            border-radius: 4px;
            display: inline-block;
            width: 100%;
            box-sizing: border-box;
            border: none;
            cursor: pointer;
            transition: background-color 0.2s ease;
        }
        .dashboard-card .btn:hover {
            background-color: #e55a3e;
            color: white;
        }
        .dashboard-card .btn-secondary {
            background-color: #694A38;
        }
        .dashboard-card .btn-secondary:hover {
            background-color: #5a3e2f;
        }
        .welcome-section {
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .welcome-text h1 {
            margin-top: 0;
        }
        .welcome-text p {
            margin-bottom: 0;
            color: #666;
        }
        .date-time {
            text-align: right;
            color: #666;
        }
        .date-time .date {
            font-size: 1.2rem;
            font-weight: bold;
            margin-bottom: 0.25rem;
        }
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
            margin-top: 2rem;
        }
        .stat-card {
            background-color: #f9f9f9;
            padding: 1.5rem;
            border-radius: 8px;
            text-align: center;
            border-left: 4px solid #F96E46;
            transition: transform 0.2s ease;
        }
        .stat-card:hover {
            transform: translateY(-3px);
        }
        .stat-card h4 {
            color: #333;
            margin-bottom: 0.5rem;
        }
        .stat-card .stat-number {
            font-size: 2rem;
            font-weight: bold;
            color: #F96E46;
        }
        .error-message, .success-message {
            padding: 1rem;
            border-radius: 4px;
            margin-bottom: 1rem;
        }
        .error-message {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .success-message {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .stat-card.primary {
            border-left-color: #F96E46;
        }
        .stat-card.primary .stat-number {
            color: #F96E46;
        }
        .stat-card.secondary {
            border-left-color: #694A38;
        }
        .stat-card.secondary .stat-number {
            color: #694A38;
        }
        .stat-card.info {
            border-left-color: #17a2b8;
        }
        .stat-card.info .stat-number {
            color: #17a2b8;
        }
        .stat-card.success {
            border-left-color: #28a745;
        }
        .stat-card.success .stat-number {
            color: #28a745;
        }
        .recent-activity {
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            margin-top: 2rem;
        }
        .recent-activity h3 {
            margin-top: 0;
            margin-bottom: 1.5rem;
            color: #333;
        }
        .activity-list {
            list-style: none;
            padding: 0;
        }
        .activity-item {
            display: flex;
            align-items: flex-start;
            margin-bottom: 1rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #eee;
        }
        .activity-icon {
            background-color: #f5f5f5;
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 1rem;
            color: #666;
            font-size: 1.2rem;
        }
        .activity-content {
            flex: 1;
        }
        .activity-content p {
            margin: 0;
        }
        .activity-time {
            font-size: 0.8rem;
            color: #999;
            margin-top: 0.25rem;
        }
        .view-all-link {
            display: block;
            text-align: center;
            margin-top: 1rem;
            color: #F96E46;
            text-decoration: none;
        }
        .view-all-link:hover {
            text-decoration: underline;
        }
        @media (max-width: 768px) {
            .welcome-section {
                flex-direction: column;
                text-align: center;
            }
            .date-time {
                text-align: center;
                margin-top: 1rem;
            }
            .dashboard-card .btn {
                width: 100%;
                margin: 0.25rem 0;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="/header.jsp" />
    
    <main>
        <div class="welcome-section">
            <div class="welcome-text">
                <h1>Admin Dashboard</h1>
                <p>Welcome back, ${user.fullName}! Here's an overview of your system.</p>
            </div>
            <div class="date-time">
                <div class="date" id="current-date"></div>
                <div class="time" id="current-time"></div>
            </div>
        </div>
        
        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                <p>${errorMessage}</p>
            </div>
        </c:if>
        
        <c:if test="${not empty successMessage}">
            <div class="success-message">
                <p>${successMessage}</p>
            </div>
        </c:if>
        
        <!-- Quick Actions -->
        <div class="dashboard-grid">
            <div class="dashboard-card">
                <h3>User Management</h3>
                <p>Manage user accounts and permissions in the system.</p>
                <div class="card-actions">
                    <a href="${pageContext.request.contextPath}/admin/listUsers" class="btn">View All Users</a>
                    <a href="${pageContext.request.contextPath}/admin/createUser" class="btn btn-secondary">Create New User</a>
                </div>
            </div>
            
            <div class="dashboard-card">
                <h3>Order Management</h3>
                <p>Monitor and manage customer orders and transactions.</p>
                <div class="card-actions">
                    <a href="${pageContext.request.contextPath}/admin/orders" class="btn">View All Orders</a>
                    <a href="${pageContext.request.contextPath}/admin/orderReports" class="btn btn-secondary">Order Reports</a>
                </div>
            </div>
            
            <div class="dashboard-card">
                <h3>Product Management</h3>
                <p>Add, edit, and manage product inventory and catalog.</p>
                <div class="card-actions">
                    <a href="${pageContext.request.contextPath}/admin/products" class="btn">Manage Products</a>
                    <a href="${pageContext.request.contextPath}/admin/inventory" class="btn btn-secondary">Inventory Reports</a>
                </div>
            </div>
            
            <div class="dashboard-card">
                <h3>System Monitoring</h3>
                <p>View logs, access records, and system performance.</p>
                <div class="card-actions">
                    <a href="${pageContext.request.contextPath}/admin/accessLogs" class="btn">Access Logs</a>
                    <a href="${pageContext.request.contextPath}/admin/settings" class="btn btn-secondary">System Settings</a>
                </div>
            </div>
        </div>
        
        <!-- System Stats -->
        <div class="stats-grid">
            <div class="stat-card primary">
                <h4>Total Users</h4>
                <div class="stat-number">${totalUsers != null ? totalUsers : '--'}</div>
            </div>
            <div class="stat-card secondary">
                <h4>Active Orders</h4>
                <div class="stat-number">${activeOrders != null ? activeOrders : '--'}</div>
            </div>
            <div class="stat-card info">
                <h4>Products</h4>
                <div class="stat-number">${totalProducts != null ? totalProducts : '--'}</div>
            </div>
            <div class="stat-card success">
                <h4>Revenue This Month</h4>
                <div class="stat-number">$${monthlyRevenue != null ? monthlyRevenue : '--'}</div>
            </div>
        </div>
        
        <!-- Recent Activity Section -->
        <div class="recent-activity">
            <h3>Recent System Activity</h3>
            <ul class="activity-list">
                <c:if test="${not empty recentLogs}">
                    <c:forEach var="log" items="${recentLogs}">
                        <li class="activity-item">
                            <div class="activity-icon">
                                <i class="fa fa-user"></i>
                            </div>
                            <div class="activity-content">
                                <p><strong>User ID: ${log.userID}</strong> - ${log.action}</p>
                                <div class="activity-time">
                                    <fmt:formatDate value="${log.timestamp}" pattern="MMM dd, yyyy HH:mm:ss" />
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                </c:if>
                <c:if test="${empty recentLogs}">
                    <li class="activity-item">
                        <div class="activity-content">
                            <p>No recent activity to display.</p>
                        </div>
                    </li>
                </c:if>
            </ul>
            <a href="${pageContext.request.contextPath}/admin/accessLogs" class="view-all-link">View All Activity Logs →</a>
        </div>
        
        <div style="margin-top: 2rem; text-align: center;">
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">Back to Main Site</a>
        </div>
    </main>
    
    <jsp:include page="/footer.jsp" />
    
    <script>
        // Update date and time
        function updateDateTime() {
            const now = new Date();
            const dateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
            const timeOptions = { hour: '2-digit', minute: '2-digit', second: '2-digit' };
            
            document.getElementById('current-date').textContent = now.toLocaleDateString('en-US', dateOptions);
            document.getElementById('current-time').textContent = now.toLocaleTimeString('en-US', timeOptions);
        }
        
        // Update time initially and every second
        updateDateTime();
        setInterval(updateDateTime, 1000);
    </script>
</body>
</html>