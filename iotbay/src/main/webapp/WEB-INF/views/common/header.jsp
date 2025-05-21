<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<header class="site-header">
    <div class="header-container">
        <div class="logo">
            <a href="${pageContext.request.contextPath}/" class="logo-link">
                <span class="logo-text">
                    <span class="iot-text">I</span><!--
                    --><span class="iot-text">o</span><!--
                    --><span class="iot-text">T</span><!--
                    --><span class="dot">.</span><!--
                    --><span class="bay-text">Bay</span>
                </span>
            </a>
        </div>
        <nav class="main-nav">
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/products">Products</a></li>
                
                <c:choose>
                    <c:when test="${not empty sessionScope.userId}">
                        <c:if test="${not sessionScope.isStaff}">
                            <li><a href="${pageContext.request.contextPath}/cart">Cart</a></li>
                            <li><a href="${pageContext.request.contextPath}/orders">My Orders</a></li>
                        </c:if>
                        
                        <c:if test="${sessionScope.isStaff}">
                            <li class="dropdown">
                                <button class="dropbtn">Management</button>
                                <div class="dropdown-content">
                                    <a href="${pageContext.request.contextPath}/admin/users">User Management</a>
                                    <a href="${pageContext.request.contextPath}/products?action=manage">Product Management</a>
                                    <a href="${pageContext.request.contextPath}/admin/orders">Order Management</a>
                                </div>
                            </li>
                        </c:if>
                        
                        <li><a href="${pageContext.request.contextPath}/account">My Account</a></li>
                        <li>
                            <form action="${pageContext.request.contextPath}/auth/logout" method="get" style="display: inline;">
                                <button type="submit" class="btn-link">Logout</button>
                            </form>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageContext.request.contextPath}/auth/login">Login</a></li>
                        <li><a href="${pageContext.request.contextPath}/auth/register">Register</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
    </div>
</header>

<style>
.site-header {
    background-color: #333;
    color: white;
    padding: 1rem 0;
    margin-bottom: 2rem;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.header-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 1rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.logo-link {
    text-decoration: none;
}

.logo-text {
    font-size: 2.5rem;
    font-weight: bold;
    letter-spacing: 1px;
}

.iot-text {
    color: #ffffff;
}

.dot {
    color: #F96E46;
    margin: 0 2px;
    position: relative;
    top: -2px;
}

.bay-text {
    color: #ffffff;
}

.logo-link:hover .logo-text {
    opacity: 0.9;
    transition: opacity 0.2s ease;
}

.main-nav ul {
    list-style: none;
    margin: 0;
    padding: 0;
    display: flex;
    align-items: center;
    gap: 1.5rem;
}

.main-nav a {
    color: white;
    text-decoration: none;
    font-size: 1rem;
    transition: color 0.2s;
}

.main-nav a:hover {
    color: #F96E46;
}

.btn-link {
    background: none;
    border: none;
    color: white;
    cursor: pointer;
    font-size: 1rem;
    padding: 0;
    transition: color 0.2s;
}

.btn-link:hover {
    color: #F96E46;
}

.dropdown {
    position: relative;
    display: inline-block;
}

.dropbtn {
    background: none;
    border: none;
    color: white;
    cursor: pointer;
    font-size: 1rem;
    padding: 0;
    display: inline-block;
    transition: color 0.2s;
}

.dropbtn:hover {
    color: #F96E46;
}

.dropdown-content {
    display: none;
    position: absolute;
    background-color: #333;
    min-width: 200px;
    box-shadow: 0 8px 16px rgba(0,0,0,0.2);
    z-index: 100;
    border-radius: 4px;
    margin-top: 0.5rem;
    left: 50%;
    transform: translateX(-50%);
}

.dropdown-content a {
    color: white;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
    text-align: center;
    transition: background-color 0.2s;
}

.dropdown-content a:hover {
    background-color: #694A38;
    color: white;
}

.dropdown.active .dropdown-content {
    display: block;
}

@media (max-width: 768px) {
    .header-container {
        flex-direction: column;
        text-align: center;
        gap: 1rem;
    }

    .main-nav ul {
        flex-direction: column;
        gap: 0.5rem;
    }

    .dropdown-content {
        position: static;
        width: 100%;
        margin-top: 0.5rem;
        box-shadow: none;
        transform: none;
        left: 0;
    }

    .dropdown-content a {
        text-align: center;
    }
}
</style>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const dropdowns = document.querySelectorAll('.dropdown');
    
    dropdowns.forEach(dropdown => {
        const btn = dropdown.querySelector('.dropbtn');
        const content = dropdown.querySelector('.dropdown-content');
        
        // Toggle on button click
        btn.addEventListener('click', function(e) {
            e.stopPropagation();
            dropdowns.forEach(d => {
                if (d !== dropdown) d.classList.remove('active');
            });
            dropdown.classList.toggle('active');
        });
        
        // Close when clicking outside
        document.addEventListener('click', function(e) {
            if (!dropdown.contains(e.target)) {
                dropdown.classList.remove('active');
            }
        });
        
        // Prevent closing when clicking inside dropdown content
        content.addEventListener('click', function(e) {
            e.stopPropagation();
        });
    });
});
</script> 