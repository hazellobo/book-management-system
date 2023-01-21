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
<script type="text/javascript">
function validateform(){
	if(document.getElementById("update").onClick){
		return validateUpdate();
	}else if(document.getElementById("delete").onClick){
		return validateDelete();
	}
}
function validateUpdate(){
	var checkbox = document.querySelectorAll('input[type="checkbox"]:checked').length;
    if(checkbox!=1){
    	alert("Please select only one book");
    	return false;
    }else{
    	return true;
        }
}
function validateDelete(){
	var checkbox = document.querySelectorAll('input[type="checkbox"]:checked').length;
    if(checkbox<=0){
    	alert("Please select atleast one book");
    	return false;
    }else{
    	return true;
        }
}
function validateUpdate(){
	var checkbox = document.querySelectorAll('input[type="checkbox"]:checked').length;
    if(checkbox!=1){
    	alert("Please select only one book");
    	return false;
    }else{
    	return true;
        }
}
</script>
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
	 <font color="red"><c:out value = "${checkbox}"/></font>
	<c:set var = "msg" value = "${msg}"/>
	<c:set var = "error" value = "${error}"/>
      <c:if test = "${not empty msg}">
        <font color="green"><c:out value = "${msg}"/></font>
      </c:if>
      
		<h2>Manage Genre</h2>
	
		<form:form modelAttribute="genre" method="post" action="/addgenre.htm">
		<div class="form-group">
			<table>
			<tr>
			    <td>Genre Name:</td>
			    <td><form:input path="name" size="30" /> <font color="red"><form:errors path="name"/></font>
			    </td>
			</tr>
			<tr>
					<td><br/></td>
					<td><br/></td>
			</tr>
			
			<tr>
			    <td colspan="2"><input type="submit" value="Create" /></td>
			</tr>
			</table>
		</div>
		</form:form>
		
		
 	<c:set var = "genre" value = "${genreList}"/>
 	<c:if test = "${empty genre}">
 		<c:out value = "No genre available"/>
 	</c:if>
      <c:if test = "${not empty genre}">
     <br/>	<br/>
		<h2>Genre List</h2>
       <form:form onsubmit="return validateform()" method="post" action="/genre-operation.htm">
        <table border="1" cellpadding="2" cellspacing="2" class="table table-hover">
           <thead>
            <tr>
            	<td></td>
                <td scope="col">Genre</td>
                <td scope="col">Created date</td>
            </tr>
             </thead>
             <tbody>
           
            <c:forEach var="genre" items="${genre}">
                <tr>
                	<td><input name="genreId" type="checkbox" value="${genre.id}"></td>
                    <td >${genre.name}</td>
                    <td>${genre.createdDate}</td>
                </tr>
            </c:forEach>
            </tbody>
             </table>
            <input type="submit" value="Update" id="update" name="update"  />
            <input type="submit" value="Delete" id="delete" name="delete"  />
           </form:form>	
		</c:if>
</div>
</body>
</html>