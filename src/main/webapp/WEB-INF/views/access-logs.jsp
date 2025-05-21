<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Access Logs - IoTBay</title>
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
        }

        h1 span {
            color: #F96E46;
        }

        .table-container {
            background: white;
            border-radius: 8px;
            padding: 1.5rem;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            overflow-x: auto;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 1rem;
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

        .table td {
            color: #333;
        }

        .actions {
            margin-top: 2rem;
            text-align: center;
        }

        .button {
            display: inline-block;
            padding: 0.8rem 1.5rem;
            background-color: #F96E46;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            transition: all 0.2s;
        }

        .button:hover {
            background-color: #e85e35;
            transform: translateY(-1px);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .no-logs {
            text-align: center;
            padding: 2rem;
            color: #666;
            font-style: italic;
        }

        @media (max-width: 768px) {
            .container {
                padding: 1rem;
            }

            .table th,
            .table td {
                padding: 0.75rem;
            }

            h1 {
                font-size: 1.5rem;
                margin-bottom: 1.5rem;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="common/header.jsp" />
    
    <main>
        <div class="container">
            <h1>Access <span>Logs</span></h1>
            
            <div class="table-container">
                <c:choose>
                    <c:when test="${not empty accessLogs}">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Log ID</th>
                                    <c:if test="${sessionScope.isStaff}">
                                        <th>User ID</th>
                                    </c:if>
                                    <th>Login Time</th>
                                    <th>Logout Time</th>
                                    <th>IP Address</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="log" items="${accessLogs}">
                                    <tr>
                                        <td>${log.logId}</td>
                                        <c:if test="${sessionScope.isStaff}">
                                            <td>${log.userId}</td>
                                        </c:if>
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
                        <div class="no-logs">
                            No access logs found.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="actions">
                <a href="${pageContext.request.contextPath}/account" class="button">Back to My Account</a>
            </div>
        </div>
    </main>
    
    <jsp:include page="common/footer.jsp" />
</body>
</html> 