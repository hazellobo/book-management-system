<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Error</title>
</head>
<body>
<div>
	<h1>Error Page</h1>
	<p>${errorMessage}</p>
	<c:if test = "${resendLink== true}" >
		<form:form action="/resendemail.htm" method="POST">
			User Email:<input type="text" name="username" size="30" required="required" />
			<br/>
			<input type="submit" value="Resend Email" />
		</form:form>	
	</c:if>
</div>

</body>
</html>