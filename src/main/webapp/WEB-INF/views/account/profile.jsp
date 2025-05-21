<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Account - IoTBay</title>
    <link href="${pageContext.request.contextPath}/resources/css/css.css" rel="stylesheet" type="text/css">
    <style>
        .profile-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 2rem;
        }

        .page-title {
            color: #333;
            font-size: 2rem;
            margin-bottom: 2rem;
            text-align: center;
        }

        .page-title span {
            color: #F96E46;
        }

        .profile-section {
            background: white;
            border-radius: 8px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .profile-section h2 {
            color: #694A38;
            margin-bottom: 1.5rem;
            font-size: 1.5rem;
            border-bottom: 2px solid #F96E46;
            padding-bottom: 0.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .profile-section h2::before {
            content: '';
            display: inline-block;
            width: 4px;
            height: 1.2em;
            background-color: #F96E46;
            margin-right: 0.5rem;
            border-radius: 2px;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            color: #694A38;
            font-weight: 600;
        }

        .form-group input,
        .form-group textarea {
            width: 100%;
            padding: 0.8rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 1rem;
            transition: all 0.2s;
        }

        .form-group input:focus,
        .form-group textarea:focus {
            border-color: #F96E46;
            outline: none;
            box-shadow: 0 0 0 2px rgba(249, 110, 70, 0.1);
        }

        .form-group input[readonly] {
            background-color: #f5f5f5;
            cursor: not-allowed;
            color: #666;
        }

        .btn-primary {
            background-color: #F96E46;
            color: white;
            padding: 0.8rem 1.5rem;
            border: none;
            border-radius: 4px;
            font-size: 1rem;
            cursor: pointer;
            transition: all 0.2s;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
            min-width: 150px;
        }

        .btn-primary:hover {
            background-color: #e85e35;
            transform: translateY(-1px);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .btn-primary:active {
            transform: translateY(0);
        }

        .alert {
            padding: 1rem;
            border-radius: 4px;
            margin-bottom: 1.5rem;
            animation: slideIn 0.3s ease-out;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .quick-links {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
            margin-top: 1rem;
        }

        .quick-links a {
            display: flex;
            padding: 1rem;
            align-items: center;
            justify-content: center;
            background-color: #694A38;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            transition: all 0.2s;
            font-weight: 500;
        }

        .quick-links a:hover {
            background-color: #553a2d;
            transform: translateY(-2px);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .quick-links a:active {
            transform: translateY(0);
        }

        @keyframes slideIn {
            from {
                transform: translateY(-10px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }

        @media (max-width: 768px) {
            .profile-container {
                padding: 1rem;
            }

            .profile-section {
                padding: 1.5rem;
            }

            .quick-links {
                grid-template-columns: 1fr;
            }

            .page-title {
                font-size: 1.5rem;
                margin-bottom: 1.5rem;
            }
        }

        /* Add styles for access logs table */
        .table-container {
            margin-top: 1rem;
            overflow-x: auto;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 1rem;
        }

        .table th,
        .table td {
            padding: 0.75rem;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        .table th {
            background-color: #f8f9fa;
            font-weight: 600;
            color: #694A38;
        }

        .table tr:hover {
            background-color: #f8f9fa;
        }

        .table td {
            color: #333;
        }

        .no-logs {
            text-align: center;
            padding: 1rem;
            color: #666;
            font-style: italic;
        }

        .currently-active {
            color: #28a745;
            font-weight: 500;
        }

        .btn-danger {
            background-color: #dc3545;
            color: white;
            padding: 0.8rem 1.5rem;
            border: none;
            border-radius: 4px;
            font-size: 1rem;
            cursor: pointer;
            transition: all 0.2s;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }

        .btn-danger:hover {
            background-color: #c82333;
            transform: translateY(-1px);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .danger-zone {
            margin-top: 2rem;
            padding: 1.5rem;
            border: 1px solid #dc3545;
            border-radius: 4px;
            background-color: #fff5f5;
        }

        .danger-zone h3 {
            color: #dc3545;
            margin-bottom: 1rem;
            font-size: 1.2rem;
        }

        .danger-zone p {
            color: #666;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <jsp:include page="../common/header.jsp" />
    
    <main>
        <div class="profile-container">
            <h1 class="page-title">My <span>Account</span></h1>
            
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            
            <div class="profile-section">
                <h2>Profile Information</h2>
                <form action="${pageContext.request.contextPath}/account" method="post">
                    <input type="hidden" name="action" value="update">
                    
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" value="${user.email}" readonly class="form-control">
                    </div>
                    
                    <div class="form-group">
                        <label for="firstName">First Name</label>
                        <input type="text" id="firstName" name="firstName" value="${user.firstName}" required class="form-control">
                    </div>
                    
                    <div class="form-group">
                        <label for="lastName">Last Name</label>
                        <input type="text" id="lastName" name="lastName" value="${user.lastName}" required class="form-control">
                    </div>
                    
                    <div class="form-group">
                        <label for="phone">Phone</label>
                        <input type="tel" id="phone" name="phone" value="${user.phone}" class="form-control">
                    </div>
                    
                    <div class="form-group">
                        <label for="address">Address</label>
                        <textarea id="address" name="address" class="form-control" rows="3">${user.address}</textarea>
                    </div>
                    
                    <button type="submit" class="btn-primary">Update Profile</button>
                </form>

                <div class="danger-zone">
                    <h3>Danger Zone</h3>
                    <p>Deactivating your account will prevent you from accessing IoTBay services. This action can be reversed by contacting support.</p>
                    <form action="${pageContext.request.contextPath}/account" method="post" onsubmit="return confirm('Are you sure you want to deactivate your account? This action can be reversed by contacting support.');">
                        <input type="hidden" name="action" value="deactivate">
                        <button type="submit" class="btn-danger">Deactivate Account</button>
                    </form>
                </div>
            </div>
            
            <div class="profile-section">
                <h2>Change Password</h2>
                <form action="${pageContext.request.contextPath}/account" method="post">
                    <input type="hidden" name="action" value="change-password">
                    
                    <div class="form-group">
                        <label for="currentPassword">Current Password</label>
                        <input type="password" id="currentPassword" name="currentPassword" required class="form-control">
                    </div>
                    
                    <div class="form-group">
                        <label for="newPassword">New Password</label>
                        <input type="password" id="newPassword" name="newPassword" required class="form-control">
                    </div>
                    
                    <div class="form-group">
                        <label for="confirmPassword">Confirm New Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required class="form-control">
                    </div>
                    
                    <button type="submit" class="btn-primary">Update Password</button>
                </form>
            </div>
            
            <div class="profile-section">
                <h2>Access History</h2>
                <div class="table-container">
                    <c:choose>
                        <c:when test="${not empty accessLogs}">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Login Time</th>
                                        <th>Logout Time</th>
                                        <th>IP Address</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="log" items="${accessLogs}">
                                        <tr>
                                            <td><fmt:formatDate value="${log.loginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                            <td>
                                                <c:if test="${not empty log.logoutTime}">
                                                    <fmt:formatDate value="${log.logoutTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                                </c:if>
                                                <c:if test="${empty log.logoutTime}">
                                                    <span class="currently-active">Currently Active</span>
                                                </c:if>
                                            </td>
                                            <td>${log.ipAddress}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="no-logs">
                                No access history found.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <div class="profile-section">
                <h2>Quick Links</h2>
                <div class="quick-links">
                    <c:choose>
                        <c:when test="${sessionScope.isStaff}">
                            <a href="${pageContext.request.contextPath}/admin/users">User Management</a>
                            <a href="${pageContext.request.contextPath}/admin/products">Product Management</a>
                            <a href="${pageContext.request.contextPath}/admin/orders">Order Management</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/orders">My Orders</a>
                            <a href="${pageContext.request.contextPath}/cart">My Cart</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </main>
    
    <jsp:include page="../common/footer.jsp" />
</body>
</html> 