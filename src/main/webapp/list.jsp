<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
 
<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Born</th>
        <th>Died</th>
        <th>Level</th>
        <th>Update Agent</th>
        <th>Delete Agent</th>
    </tr>
    </thead>
    <c:forEach items="${agents}" var="agent">
        <tr>
            <form method="post" action="${pageContext.request.contextPath}/agents/update?id=${agent.id}"
                      style="margin-bottom: 0;">
            <td><c:out value="${agent.id}"/></td>
            <td><input type="text" name="name" value="<c:out value="${agent.name}"/>"/></td>
            <td><input type="text" name="born" value="<c:out value="${agent.born}"/>"/></td>
            <td><input type="text" name="died" value="<c:out value="${agent.died}"/>"/></td>
            <td><input type="text" name="level" value="<c:out value="${agent.level}"/>"/></td>
            <td><input type="submit" value="Update"></td>
            </form>
            <td><form method="post" action="${pageContext.request.contextPath}/agents/delete?id=${agent.id}"
                      style="margin-bottom: 0;"><input type="submit" value="Delete"></form></td>
        </tr>
    </c:forEach>
</table>
 
<h2>Create Agent</h2>
<c:if test="${not empty error}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
        <c:out value="${error}"/>
    </div>
</c:if>
<form action="${pageContext.request.contextPath}/agents/add" method="post">
    <table>
        <tr>
            <th>Name</th>
            <td><input type="text" name="name" value="<c:out value='${param.name}'/>"/></td>
        </tr>
        <tr>
            <th>Born</th>
            <td><input type="text" name="born" value="<c:out value='${param.born}'/>"/></td>
        </tr>
        <tr>
            <th>Died</th>
            <td><input type="text" name="died" value="<c:out value='${param.died}'/>"/></td>
        </tr>
        <tr>
            <th>Level</th>
            <td><input type="text" name="level" value="<c:out value='${param.level}'/>"/></td>
        </tr>
    </table>
    <input type="Submit" value="Create" />
</form>
</body>
</html>