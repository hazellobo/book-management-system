<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>User Login</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body>
	<nav class="navbar navbar-default">
	  <div class="container-fluid">
	    <ul class="nav navbar-nav">
	    <div class="navbar-header">
	      <h1><a class="navbar-brand">Welcome to Book Application</a></h1>
	    </div>
	    </ul>
	  </div>
	</nav>
	<div class="container">
	<c:if test="${not empty errorMessage}">
		<font color="red"><c:out value = "${errorMessage}"/></font>
		<p onload="reset()"></p>
	</c:if>
		<form action="/login.htm" method="POST">
		<div class="form-group">
			<table>
				<tr>
				    <td>Email:</td>
				    <td><input type="email" name="email" size="30" required="required" /></td>
				</tr>
				<tr>
				<td></br></td><td><br/></td>
				</tr>
				<tr>
				    <td>Password:</td>
				    <td><input type="password" name="password" min="8" 
				    pattern="^(?=.*?[A-Za-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$" size="30" required="required"/></td>
				</tr>
				<tr>
					<td><br/></td>
					<td><br/></td>
				</tr>
				<tr>
				    <td colspan="2"><input type="submit" value="Login" /></td>
				</tr>	
			</table>
			</div>
		</form>
		<a href="/forgotpassword.htm">Forgot password?</a>
		<a href="/register.htm">Register User</a>
	</div>

</body>
</html>