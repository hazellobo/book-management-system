<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>  

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Manage Users</title>

</head>
<body>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <ul class="nav navbar-nav">
      <li class="active"><a href="admin.htm">Home</a></li>
      <li><a href="logout.htm">Logout</a></li>
    </ul>
  </div>
</nav>
<div class="container">
	<h3>Manage User Accounts</h3>
	<table class="table-bordered">
						<tr>
							<th>First Name</th>
							<th>Last Name</th>
							<th>Email Id</th>
							<th>Status</th>
						</tr>
		<c:forEach items ="${userList}" var ="user">
	         <form:form method="POST" action="/update-status.htm" >
	         <input type=hidden name="email" value="${user.email}">
		         <tr>
		         <td>${user.fname}</td>
		         <td>${user.lname}</td>
		         <td>${user.email}</td>
		         <td><button>${user.status==0? ' Activate ': 'Inactivate'}</button></td>  
		         <td></td>
		         </tr>
	         </form:form>
	      </c:forEach>		
	</table>
</div>
</body>
</html>