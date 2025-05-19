<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Manage Users - IoT Bay Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css.css">
    <style>
        .admin-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 2rem;
        }
        .admin-header h1 {
            color: #333;
            margin: 0;
        }
        .admin-header .btn {
            background-color: #F96E46;
            color: white;
            text-decoration: none;
            padding: 0.8rem 1.5rem;
            border-radius: 4px;
            border: none;
            cursor: pointer;
        }
        .admin-header .btn:hover {
            background-color: #e55a3e;
            color: white;
        }
        .users-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 1rem;
            background-color: white;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            overflow: hidden;
        }
        .users-table th,
        .users-table td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid #DDD;
        }
        .users-table th {
            background-color: #f9f9f9;
            font-weight: bold;
            color: #333;
        }
        .users-table tbody tr:hover {
            background-color: #f5f5f5;
        }
        .action-buttons {
            display: flex;
            gap: 0.5rem;
        }
        .btn-small {
            padding: 0.5rem 1rem;
            font-size: 0.9rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            color: white;
            display: inline-block;
        }
        .btn-edit {
            background-color: #F96E46;
        }
        .btn-edit:hover {
            background-color: #e55a3e;
            color: white;
        }
        .btn-delete {
            background-color: #dc3545;
        }
        .btn-delete:hover {
            background-color: #c82333;
        }
        .empty-state {
            text-align: center;
            padding: 3rem 2rem;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            margin-top: 2rem;
        }
        .empty-state h3 {
            color: #666;
            margin-bottom: 1rem;
        }
        .empty-state p {
            color: #999;
            margin-bottom: 2rem;
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
        .users-stats {
            background-color: white;
            padding: 1.5rem;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            margin-top: 2rem;
            text-align: center;
        }
        .navigation-links {
            text-align: center;
            margin-top: 2rem;
        }
        .navigation-links a {
            background-color: #694A38;
            color: white;
            text-decoration: none;
            padding: 0.8rem 1.5rem;
            border-radius: 4px;
            margin: 0.5rem;
            display: inline-block;
        }
        .navigation-links a:hover {
            background-color: #5a3e2f;
            color: white;
        }
        .table-container {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            margin-top: 1rem;
        }
        @media (max-width: 768px) {
            .admin-header {
                flex-direction: column;
                align-items: stretch;
                gap: 1rem;
            }
            .users-table {
                font-size: 0.9rem;
            }
            .users-table th,
            .users-table td {
                padding: 0.5rem;
            }
            .action-buttons {
                flex-direction: column;
                gap: 0.25rem;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="/header.jsp" />
    
    <main>
        <div class="admin-header">
            <h1>Manage Users</h1>
            <a href="${pageContext.request.contextPath}/admin/createUser" class="btn">
                + Create New User
            </a>
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
        
        <c:choose>
            <c:when test="${empty users}">
                <div class="empty-state">
                    <h3>No users found</h3>
                    <p>There are currently no users in the system.</p>
                    <a href="${pageContext.request.contextPath}/admin/createUser" class="btn">
                        Create First User
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-container">
                    <table class="users-table">
                        <thead>
                            <tr>
                                <th>User ID</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Email</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${users}">
                                <tr>
                                    <td>${user.userID}</td>
                                    <td>${user.fname}</td>
                                    <td>${user.lname}</td>
                                    <td>${user.email}</td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/admin/editUser?id=${user.userID}" 
                                               class="btn-small btn-edit">Edit</a>
                                            
                                            <form action="${pageContext.request.contextPath}/admin/deleteUser" 
                                                  method="post" style="display: inline;"
                                                  onsubmit="return confirm('Are you sure you want to delete user: ${user.fullName}? This action cannot be undone.');">
                                                <input type="hidden" name="userID" value="${user.userID}">
                                                <button type="submit" class="btn-small btn-delete">Delete</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <div class="users-stats">
                    <strong>Total Users: ${users.size()}</strong>
                </div>
            </c:otherwise>
        </c:choose>
        
        <div class="navigation-links">
            <a href="${pageContext.request.contextPath}/admin/adminDashboard.jsp">Admin Dashboard</a>
            <a href="${pageContext.request.contextPath}/index.jsp">Main Site</a>
        </div>
    </main>
    
    <jsp:include page="/footer.jsp" />
</body>
</html>