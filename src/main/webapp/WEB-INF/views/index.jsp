<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <% 
        // Variables go here (none needed for landing page)
    %>

    <head>
        <title>Welcome to IoTBay</title>
        <link href="${pageContext.request.contextPath}/resources/css/css.css" rel="stylesheet" type="text/css">
        <style>
            .hero {
                background: linear-gradient(135deg, #333 0%, #1a1a1a 100%);
                color: white;
                padding: 60px 20px;
                text-align: center;
                margin-bottom: 40px;
            }
            
            .hero h1 {
                font-size: 3em;
                margin-bottom: 20px;
            }
            
            .hero p {
                font-size: 1.2em;
                max-width: 800px;
                margin: 0 auto 30px;
                line-height: 1.6;
            }
            
            .cta-button {
                display: inline-block;
                padding: 15px 30px;
                background-color: #d66b43;
                color: white;
                text-decoration: none;
                border-radius: 5px;
                font-size: 1.2em;
                transition: background-color 0.3s ease;
            }
            
            .cta-button:hover {
                background-color: #c55a32;
            }
            
            .features {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 30px;
                padding: 20px;
                max-width: 1200px;
                margin: 0 auto;
            }
            
            .feature-card {
                background: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                text-align: center;
                transition: transform 0.3s ease;
            }
            
            .feature-card:hover {
                transform: translateY(-5px);
            }
            
            .feature-card h3 {
                color: #333;
                margin-bottom: 15px;
            }
            
            .feature-card p {
                color: #666;
                line-height: 1.5;
            }
            
            .feature-icon {
                font-size: 2em;
                margin-bottom: 15px;
                color: #d66b43;
            }
        </style>
    </head>

    <body>
        <%@ include file="common/header.jsp" %> <!-- Site title and nav please do not remove -->

        <main> <!-- Please put all content inside the main tags -->
            <section class="hero">
                <h1>Welcome to IoTBay</h1>
                <p>Your one-stop shop for all Internet of Things devices. Discover smart home solutions that make your life easier, more efficient, and more connected.</p>
                <a href="${pageContext.request.contextPath}/products" class="cta-button">Browse Products</a>
            </section>
            
            <section class="features">
                <div class="feature-card">
                    <div class="feature-icon">🏠</div>
                    <h3>Smart Home</h3>
                    <p>Transform your home with our range of smart devices. Control lighting, temperature, and security from your smartphone.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-icon">🔒</div>
                    <h3>Security</h3>
                    <p>Keep your home safe with our advanced security systems. Monitor your property from anywhere in the world.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-icon">💡</div>
                    <h3>Energy Efficiency</h3>
                    <p>Save money and the environment with our energy-efficient smart devices and monitoring solutions.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-icon">🔧</div>
                    <h3>Easy Setup</h3>
                    <p>All our devices are designed for simple installation and come with comprehensive setup guides.</p>
                </div>
            </section>
        </main>

        <%@ include file="common/footer.jsp" %> <!-- Site footer please do not remove -->
    </body>
</html>
