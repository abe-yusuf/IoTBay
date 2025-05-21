<footer class="site-footer">
    <div class="footer-container">
        <div class="footer-grid">
            <div class="footer-section">
                <h3>About IoT<span class="dot">.</span>Bay</h3>
                <p>Your trusted destination for IoT devices and smart home solutions. We bring innovation to your doorstep.</p>
            </div>
            
            <div class="footer-section">
                <h3>Quick Links</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/products">Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/cart">Shopping Cart</a></li>
                    <li><a href="${pageContext.request.contextPath}/orders">My Orders</a></li>
                    <li><a href="${pageContext.request.contextPath}/account">My Account</a></li>
                </ul>
            </div>
            
            <div class="footer-section">
                <h3>Contact Us</h3>
                <ul>
                    <li>Email: GROUP7ISD@iotbay.com</li>
                    <li>Phone: 1300 IOT BAY</li>
                    <li>Address: 123 Tech Street</li>
                    <li>Sydney, NSW 2000</li>
                </ul>
            </div>
            
            <div class="footer-section">
                <h3>Legal</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/privacy">Privacy Policy</a></li>
                    <li><a href="${pageContext.request.contextPath}/terms">Terms of Service</a></li>
                    <li><a href="${pageContext.request.contextPath}/shipping">Shipping Policy</a></li>
                    <li><a href="${pageContext.request.contextPath}/returns">Returns Policy</a></li>
                </ul>
            </div>
        </div>
        
        <div class="footer-bottom">
            <p>&copy; 2025 IoT<span class="dot">.</span>Bay. All rights reserved.</p>
        </div>
    </div>
</footer>

<style>
.site-footer {
    background-color: #333;
    color: #fff;
    padding: 3rem 0 1.5rem;
    margin-top: 4rem;
}

.footer-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 1rem;
}

.footer-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 2rem;
    margin-bottom: 2rem;
}

.footer-section h3 {
    color: #F96E46;
    font-size: 1.2rem;
    margin-bottom: 1rem;
    font-weight: 600;
}

.footer-section h3 .dot {
    color: #694A38;
}

.footer-section p {
    line-height: 1.6;
    color: #ccc;
}

.footer-section ul {
    list-style: none;
    padding: 0;
    margin: 0;
}

.footer-section ul li {
    margin-bottom: 0.5rem;
    color: #ccc;
}

.footer-section ul li a {
    color: #ccc;
    text-decoration: none;
    transition: color 0.2s;
}

.footer-section ul li a:hover {
    color: #F96E46;
}

.footer-bottom {
    text-align: center;
    padding-top: 2rem;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.footer-bottom p {
    color: #ccc;
    font-size: 0.9rem;
}

.footer-bottom .dot {
    color: #694A38;
}

@media (max-width: 768px) {
    .footer-grid {
        grid-template-columns: 1fr;
        gap: 2rem;
    }
    
    .footer-section {
        text-align: center;
    }
    
    .site-footer {
        padding: 2rem 0 1rem;
    }
}
</style> 