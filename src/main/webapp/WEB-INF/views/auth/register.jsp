<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <% 
        String errorMessage = (String) request.getAttribute("error");
    %> 

    <head>
        <title>Register</title>
        <link href="${pageContext.request.contextPath}/resources/css/css.css" rel="stylesheet" type="text/css">
        <style>
            .staff-section {
                margin-top: 20px;
                padding-top: 20px;
                border-top: 1px solid #ddd;
            }
            
            .toggle-staff {
                background: none;
                border: none;
                color: #d66b43;
                cursor: pointer;
                font-size: 14px;
                text-decoration: underline;
                padding: 0;
                margin-bottom: 10px;
                display: block;
            }
            
            .hidden {
                display: none;
            }
        </style>
    </head>

    <body>
        <%@ include file="/WEB-INF/views/common/header.jsp" %>

        <main>
            <section class="form-container">
                <h2>Register</h2>
                
                <% if (errorMessage != null) { %>
                    <div class="error-message"><%= errorMessage %></div>
                <% } %>

                <form action="${pageContext.request.contextPath}/auth/register" method="post">
                
                <div class="form-group">
                    <label for="firstName">First Name: </label>
                    <input type="text" id="firstName" name="firstName" required/>
                </div>
                
                <div class="form-group">
                    <label for="lastName">Last Name: </label>
                    <input type="text" id="lastName" name="lastName" required/>
                </div>

                <div class="form-group">
                    <label for="email">Email: </label>
                    <input type="email" id="email" name="email" required>
                </div>

                <div class="form-group">
                    <label for="password">Password: </label>
                    <input type="password" id="password" name="password" required/>
                </div>
                
                <div class="form-group">
                    <label for="phone">Phone: </label>
                    <input type="tel" id="phone" name="phone"/>
                </div>
                
                <div class="form-group">
                    <label for="address">Address: </label>
                    <input type="text" id="address" name="address"/>
                </div>
                
                <button type="button" class="toggle-staff" onclick="toggleStaffSection()">Register as Staff</button>
                
                <div id="staff-section" class="staff-section hidden">
                    <h3>Admin Authentication</h3>
                    <p>Please enter admin credentials to register a staff account</p>
                    
                    <div class="form-group">
                        <label for="adminEmail">Admin Email: </label>
                        <input type="email" id="adminEmail" name="adminEmail" value="admin@iotbay.com">
                    </div>
                    
                    <div class="form-group">
                        <label for="adminPassword">Admin Password: </label>
                        <input type="password" id="adminPassword" name="adminPassword">
                    </div>
                    
                    <div class="checkbox-group">
                        <label for="isStaff">Register as Staff: </label>
                        <input type="checkbox" id="isStaff" name="isStaff" value="true">
                    </div>
                </div>
                
                <div class="checkbox-group">
                    <label for="TOS">I agree to the Terms of Service: </label>
                    <input type="checkbox" id="TOS" name="TOS" required/>
                </div>

                <button type="submit" class="btn">Register</button>

                <div class="form-footer">
                    <p>Already have an account? <a href="${pageContext.request.contextPath}/auth/login">Log in</a></p>
                </div>
            </form>
            </section>
        </main>

        <%@ include file="/WEB-INF/views/common/footer.jsp" %>
        
        <script>
            function toggleStaffSection() {
                const staffSection = document.getElementById('staff-section');
                if (staffSection.classList.contains('hidden')) {
                    staffSection.classList.remove('hidden');
                } else {
                    staffSection.classList.add('hidden');
                    document.getElementById('isStaff').checked = false;
                    document.getElementById('adminEmail').value = 'admin@iotbay.com';
                    document.getElementById('adminPassword').value = '';
                }
            }
        </script>
    </body>
</html>
