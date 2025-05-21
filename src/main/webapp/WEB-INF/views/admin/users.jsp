<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Management - IoTBay</title>
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
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
        }

        h1 span {
            color: #F96E46;
        }

        .search-container {
            margin-bottom: 2rem;
            display: flex;
            justify-content: center;
            gap: 1rem;
        }

        .search-box {
            padding: 0.8rem 1rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            width: 300px;
            font-size: 1rem;
        }

        .search-box:focus {
            outline: none;
            border-color: #F96E46;
            box-shadow: 0 0 0 2px rgba(249, 110, 70, 0.1);
        }

        .search-button {
            background-color: #694A38;
            color: white;
            padding: 0.8rem 1.5rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 1rem;
            transition: all 0.2s;
        }

        .search-button:hover {
            background-color: #553a2d;
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
            vertical-align: middle;
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
            display: flex;
            gap: 0.5rem;
            align-items: center;
            justify-content: flex-start;
            flex-wrap: nowrap;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 0.5rem 1rem;
            border-radius: 4px;
            text-decoration: none;
            cursor: pointer;
            border: none;
            font-size: 0.9rem;
            min-width: 100px;
            max-width: 100px;
            height: 36px;
            white-space: nowrap;
            transition: all 0.2s;
        }

        .btn-primary {
            background-color: #694A38;
            color: white;
        }

        .btn-danger {
            background-color: #dc3545;
            color: white;
        }

        .btn:hover {
            opacity: 0.9;
            transform: translateY(-1px);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .status {
            display: inline-flex;
            align-items: center;
            padding: 0.25rem 0.75rem;
            border-radius: 4px;
            font-size: 0.875rem;
            font-weight: 500;
            min-width: 80px;
            justify-content: center;
        }

        .status-active {
            background-color: #d4edda;
            color: #155724;
        }

        .status-inactive {
            background-color: #f8d7da;
            color: #721c24;
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

            .search-container {
                flex-direction: column;
            }

            .search-box {
                width: 100%;
            }
        }

        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.4);
        }

        .modal-content {
            background-color: white;
            margin: 15% auto;
            padding: 20px;
            border-radius: 8px;
            width: 80%;
            max-width: 500px;
            position: relative;
        }

        .close {
            position: absolute;
            right: 20px;
            top: 10px;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }

        .form-group {
            margin-bottom: 1rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            color: #333;
            font-weight: 500;
        }

        .form-group input {
            width: 100%;
            padding: 0.5rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 1rem;
        }

        .form-group input:focus {
            border-color: #694A38;
            outline: none;
            box-shadow: 0 0 0 2px rgba(105, 74, 56, 0.1);
        }
    </style>
</head>
<body>
    <jsp:include page="../common/header.jsp" />
    
    <main>
        <div class="container">
            <h1>User <span>Management</span></h1>
            
            <div class="search-container">
                <form action="${pageContext.request.contextPath}/admin/users" method="get" style="display: flex; gap: 1rem; width: 100%; max-width: 500px;">
                    <input type="text" name="search" class="search-box" placeholder="Search by name or email..." value="${param.search}">
                    <button type="submit" class="search-button">Search</button>
                </form>
            </div>

            <div class="table-container">
                <c:choose>
                    <c:when test="${not empty users}">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="user" items="${users}">
                                    <tr>
                                        <td>${user.userId}</td>
                                        <td>${user.firstName} ${user.lastName}</td>
                                        <td>${user.email}</td>
                                        <td>${user.staff ? 'Staff' : 'Customer'}</td>
                                        <td>
                                            <span class="status ${user.active ? 'status-active' : 'status-inactive'}">
                                                ${user.active ? 'Active' : 'Inactive'}
                                            </span>
                                        </td>
                                        <td>
                                            <div class="actions">
                                                <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display: inline;">
                                                    <input type="hidden" name="action" value="update">
                                                    <input type="hidden" name="userId" value="${user.userId}">
                                                    <input type="hidden" name="isActive" value="${!user.active}">
                                                    <button type="submit" class="btn btn-primary">
                                                        ${user.active ? 'Deactivate' : 'Activate'}
                                                    </button>
                                                </form>
                                                
                                                <button type="button" class="btn btn-primary" onclick="openEditModal(${user.userId}, '${user.firstName}', '${user.lastName}', '${user.phone}', '${user.address}')">
                                                    Edit
                                                </button>
                                                
                                                <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display: inline;">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="userId" value="${user.userId}">
                                                    <button type="submit" class="btn btn-danger" 
                                                            onclick="return confirm('Are you sure you want to delete this user?')">
                                                        Delete
                                                    </button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="no-users">
                            No users found.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>
    
    <div id="editModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <h2>Edit User</h2>
            <form id="editUserForm" action="${pageContext.request.contextPath}/admin/users" method="post">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="userId" id="editUserId">
                
                <div class="form-group">
                    <label for="editFirstName">First Name:</label>
                    <input type="text" id="editFirstName" name="firstName" required>
                </div>
                
                <div class="form-group">
                    <label for="editLastName">Last Name:</label>
                    <input type="text" id="editLastName" name="lastName" required>
                </div>
                
                <div class="form-group">
                    <label for="editPhone">Phone:</label>
                    <input type="tel" id="editPhone" name="phone">
                </div>
                
                <div class="form-group">
                    <label for="editAddress">Address:</label>
                    <input type="text" id="editAddress" name="address">
                </div>
                
                <button type="submit" class="btn btn-primary">Save Changes</button>
            </form>
        </div>
    </div>

    <jsp:include page="../common/footer.jsp" />

    <script>
    function openEditModal(userId, firstName, lastName, phone, address) {
        document.getElementById('editUserId').value = userId;
        document.getElementById('editFirstName').value = firstName;
        document.getElementById('editLastName').value = lastName;
        document.getElementById('editPhone').value = phone || '';
        document.getElementById('editAddress').value = address || '';
        document.getElementById('editModal').style.display = 'block';
    }

    // Close modal when clicking the X button
    document.querySelector('.close').onclick = function() {
        document.getElementById('editModal').style.display = 'none';
    }

    // Close modal when clicking outside
    window.onclick = function(event) {
        var modal = document.getElementById('editModal');
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    }
    </script>
</body>
</html> 