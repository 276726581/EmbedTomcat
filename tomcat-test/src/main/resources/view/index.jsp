<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Test</title>
</head>
<body>
Test JSP
<br>
msg: ${msg}
<br>
<c:out value="out: ${msg}"/>
</body>
</html>
