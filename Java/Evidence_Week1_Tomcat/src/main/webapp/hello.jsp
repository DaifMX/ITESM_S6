<html>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <body>
        <h2>Hello from JSP!</h2>

        <c:forEach items="${['Java','HTTP','Servlets']}" var="item">
          <p>${item}</p>
        </c:forEach>
    </body>
</html>

