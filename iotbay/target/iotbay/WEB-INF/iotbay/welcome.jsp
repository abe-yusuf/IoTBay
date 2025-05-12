<%@ page import="model.User"%>

<html>
    <% 
        String fname = request.getParameter("fname"); 
        String lname = request.getParameter("lname"); 
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        User user = new User(fname, lname, email, password);
        session.setAttribute("user", user);
        %>

    <header>
        <title>Welcome</title>
        <link href="/webapp/css.css" rel="stylesheet" type="text/css" >
         
    </header>

    <body>
            <%@ include file="header.jsp" %><!-- Site title and nav please do not remove -->

        
        <main><!-- Please put all content inside the main tags -->
            <section>
                <%
                    // Where there is no user stored
                    if(false) { 
                    
                %>
                Hello guest, please register <a href="/webapp/register.jsp">here</a> or login <a href="/webapp/login.jsp">here</a>.
                <%
                    } else {
                %>
                   <h2>Welcome <%= user.getFname()%>!</h2> 
                   <p>Please explore our catalogue! <%= user.getEmail()%>.</p>

                    <a href="/webapp/logout.jsp">Log Out.</a>
                <%
                    }
                %>

            </section>
                
        </main>
        

            <%@ include file="footer.jsp" %><!-- Site footer please do not remove -->
    </body>
</html>
