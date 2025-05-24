<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Access Logs - IoTBay</title>
    <link href="${pageContext.request.contextPath}/resources/css/css.css" rel="stylesheet" type="text/css">
    <style>
        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }

        h1 {
            color: #333;
            font-size: 2rem;
            margin-bottom: 2rem;
            text-align: center;
            font-weight: 600;
        }

        h1 span {
            color: #F96E46;
        }

        .user-info {
            background: white;
            padding: 1.5rem;
            border-radius: 8px;
            margin-bottom: 2rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .user-info h2 {
            color: #694A38;
            margin-bottom: 1rem;
            font-size: 1.2rem;
        }

        .search-container {
            background: white;
            padding: 1.5rem;
            border-radius: 8px;
            margin-bottom: 2rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .search-form {
            display: flex;
            gap: 1rem;
            align-items: flex-end;
            flex-wrap: wrap;
        }

        .form-group {
            flex: 1;
            min-width: 200px;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            color: #694A38;
            font-weight: 500;
        }

        .form-group input {
            width: 100%;
            padding: 0.5rem;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .btn {
            padding: 0.5rem 1rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: 500;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background-color: #694A38;
            color: white;
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .table-container {
            background: white;
            border-radius: 8px;
            padding: 1.5rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            overflow-x: auto;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
        }

        .table th,
        .table td {
            padding: 1rem;
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

        .back-link {
            margin-top: 2rem;
            display: block;
        }

        @media (max-width: 768px) {
            .search-form {
                flex-direction: column;
            }
            
            .form-group {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="../common/header.jsp" />
    
    <div class="container">
        <h1>Access Logs for <span>${user.firstName} ${user.lastName}</span></h1>
        
        <div class="user-info">
            <h2>User Information</h2>
            <p><strong>User ID:</strong> ${user.userId}</p>
            <p><strong>Email:</strong> ${user.email}</p>
            <p><strong>Status:</strong> ${user.active ? 'Active' : 'Inactive'}</p>
        </div>
        
        <div class="search-container">
            <form class="search-form" action="${pageContext.request.contextPath}/admin/users/logs" method="get">
                <input type="hidden" name="userId" value="${user.userId}">
                
                <div class="form-group">
                    <label for="fromDate">From Date:</label>
                    <input type="date" id="fromDate" name="fromDate" value="${fromDate}">
                </div>
                
                <div class="form-group">
                    <label for="toDate">To Date:</label>
                    <input type="date" id="toDate" name="toDate" value="${toDate}">
                </div>
                
                <div class="form-group" style="flex: 0;">
                    <button type="submit" class="btn btn-primary">Search</button>
                    <c:if test="${not empty fromDate or not empty toDate}">
                        <a href="?userId=${user.userId}" class="btn btn-secondary">Clear</a>
                    </c:if>
                </div>
            </form>
        </div>

        <div class="table-container">
            <c:choose>
                <c:when test="${not empty accessLogs}">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Log ID</th>
                                <th>Login Time</th>
                                <th>Logout Time</th>
                                <th>IP Address</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="log" items="${accessLogs}">
                                <tr>
                                    <td>${log.logId}</td>
                                    <td><fmt:formatDate value="${log.loginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td>
                                        <c:if test="${not empty log.logoutTime}">
                                            <fmt:formatDate value="${log.logoutTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </c:if>
                                        <c:if test="${empty log.logoutTime}">
                                            <span style="color: #28a745;">Currently Active</span>
                                        </c:if>
                                    </td>
                                    <td>${log.ipAddress}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p style="text-align: center; padding: 2rem;">No access logs found for this user.</p>
                </c:otherwise>
            </c:choose>
        </div>
        
        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary back-link">Back to User Management</a>
    </div>
    
    <jsp:include page="../common/footer.jsp" />
</body>
</html> 