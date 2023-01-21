<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>User Registration</title>
<script type="text/javascript">
 function reset(){
	 document.getElementById('myform').reset();
 }
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
	<c:if test="${error=='false'}">
		<font color="green"><c:out value = "${msg}"/></font>
		<p onload="reset()"></p>
	</c:if>
	<br/><br/>
	<form:form id="myform" action="/register.htm" modelAttribute="user" method="post">
		<div class="form-group">
			<table>
				<tr>
					    <td>First Name:</td>
					    <td><form:input path="fname" size="30" required="required" /></td>
					    <td><font color="red"><form:errors path="fname" cssClass="error"/></font></td>
					</tr>
					<tr>
						<td><br/></td>
						<td><br/></td>
				</tr>
				<tr>
				    <td>Last Name:</td>
				    <td><form:input path="lname" size="30" required="required" /></td>
				    <td><font color="red"><form:errors path="lname" cssClass="error"/></font></td>
				</tr>
				<tr>
					<td><br/></td>
					<td><br/></td>
				</tr>
				<tr>
				    <td>Email:</td>
				    <td><form:input path="email" size="30" required="required" /></td>
				    <td>
					    <font color="red">
					    	<form:errors path="email" cssClass="error"/>
					    	<c:if test="${error=='true' }">
								<c:out value = "${msg}"/>
							</c:if>
					    </font>
				    </td>
				</tr>
				<tr>
					<td><br/></td>
					<td><br/></td>
				</tr>
				<tr>
				    <td>Password:</td>
				    <td><form:input type="password" path="password"  size="30" required="required"/></td>
				    <td><font color="red"><form:errors path="password" cssClass="error"/></font></td>
				</tr>
				<tr>
					<td><br/></td>
					<td><br/></td>
				</tr>
				<tr>
				    <td colspan="2"><input type="submit" value="Register" /></td>
				</tr>	
			</table>
			</div>
		</form:form>
		</div>
</body>
</html>