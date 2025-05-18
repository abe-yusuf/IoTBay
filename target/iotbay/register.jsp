<html>
    <% 
        // Variables go here
    %> 

    <header>
        <title>Register</title>
        <link href="/webapp/css.css" rel="stylesheet" type="text/css" >
        
    </header>

    <body>
            <%@ include file="header.jsp" %><!-- Site title and nav please do not remove -->

        
        <main><!-- Please put all content inside the main tags -->
            <section class="register-form-container">
                <h2>Register</h2>

                <form class="register-form" action="/webapp/welcome.jsp" method="post">
                
                <div class="form-group">
                    <label for="fname">First Name: </label>
                    <input type="text" id="fname" name="fname" required/>
                </div>
                
                <div class="form-group">
                    <label for="lname">Last Name: </label>
                    <input type="text" id="lname" name="lname" required/>
                </div>

                <div class="form-group">
                    <label for="email">Email: </label>
                    <input type="text" id="email" name="email" required>
                </div>

                <div class="form-group">
                    <label for="password">Password: </label>
                    <input type="password" id="password" name="password" required/>
                </div>
                
                <div class="checkbox-group">
                    <label for="TOS">TOS: </label>
                    <input type="checkbox" name="TOS" required/>
                </div>

                <button type="submit" class="form-button">Register</button>

                <div class="form-group">Already have an account? <a href="/webapp/login.jsp">Log in</a>.</div>
            </form>
            </section>
                
        </main>
        

            <%@ include file="footer.jsp" %><!-- Site footer please do not remove -->
    </body>
</html>
