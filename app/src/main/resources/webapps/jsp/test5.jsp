<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Title</title>
</head>
<body>

<h1>
    JSP!
    <c:out value="${user.id}"/>

    <hr>
    <p>A trivial jstl example
    <hr>
    <c:forEach var="i" begin="1" end="10" step="1">
        <c:out value="${i}" />
        <br />
    </c:forEach>
</h1>
</body>
</html>
