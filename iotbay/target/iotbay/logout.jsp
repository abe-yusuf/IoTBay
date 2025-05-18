<html>
    <% 
        // Variables go here
    %>

    <header>
        <title>Log Out</title>
        <link href="/webapp/css.css" rel="stylesheet" type="text/css" >
         
    </header>

    <body>
            <%@ include file="header.jsp" %><!-- Site title and nav please do not remove -->

        
        <main><!-- Please put all content inside the main tags -->
            <section>
                <% session.invalidate() ;%>

                <p>You are now logged out. Click here to go to <a href="/webapp/index.jsp">home</a>.</p>
            </section>
                
        </main>
        

            <%@ include file="footer.jsp" %><!-- Site footer please do not remove -->
    </body>
</html>
