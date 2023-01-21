<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Admin dashboard</title>
<script language="javascript" type="text/javascript">

</script>
</head>
<body>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <ul class="nav navbar-nav">
    <div class="navbar-header">
      <a class="navbar-brand">Welcome to Book Application</a>
    </div>
    </ul>
  </div>
</nav>
	<div class="container">
      <c:if test = "${error==false}">
        <font color="green"><c:out value="${msg}"/></font>
      </c:if>
		<h2>Forgot Password</h2>
	
		<form action="/forgotpassword.htm" method="post">
		<div class="form-group">
			<table>
			<tr>
			    <td>Email Id:</td>
			    <td><input type="email" name="email" size="30" required /></td>
			     
			</tr>
			<tr>
				<c:if test = "${error==true}">
			        <font color="red"><c:out value="${msg}"/></font>
			     </c:if>
					<td></br></td>
					<td><br/></td>
			</tr>
			
			<tr>
			    <td colspan="2"><input type="submit" value="Send Password" /></td>
			</tr>
			</table>
		</div>
		</form>
	</div>
</body>
</html>