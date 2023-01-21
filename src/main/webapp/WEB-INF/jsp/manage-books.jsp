<%@page import="com.myapp.book.common.Constants"%>
<%@page import="com.myapp.book.pojo.User"%>
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
<title>Dashboard</title>
<script type="text/javascript">
function validateform(){
	if(document.getElementById("update").onClick){
		return validateBorrow();
	} else if(document.getElementById("borrow").onClick){
		return validateBorrow();
	}else if(document.getElementById("delete").onClick){
		return validateDelete();
	}
}
function validateBorrow(){
	var checkbox = document.querySelectorAll('input[type="checkbox"]:checked').length;
    if(checkbox<1){
    	alert("Please select atleast one book");
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
<%
User u = (User) session.getAttribute(Constants.LOGGED_IN_USER);
%>

<nav class="navbar navbar-default">
  <div class="container-fluid">
    <ul class="nav navbar-nav">
    <% if(u.getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)){%>
       		<li class="active"><a href="admin.htm">Home</a></li>
       		<li><a href="/book-report.pdf">Export PDF</a></li>
      <%} else if(u.getRole().equalsIgnoreCase(Constants.ROLE_CUSTOMER)){%>
      		<li class="active"><a href="customer.htm">Home</a></li>
      <%} %>
      <li><a href="logout.htm">Logout</a></li>
    </ul>
    	<form:form method="POST" action="/searchBook.htm">
	      <table>
	      <tr><td></td></tr>
	      <tr>
		      <td><input class="form-control" type="search" name="search" placeholder="Search book name" aria-label="Search"></td>
		      <td><button type="submit" class="btn btn-outline-success">Search</button></td>
	      </tr>
	      </table>
    	</form:form>
  </div>
</nav>
	<div class="container">
	
        <font color="red"><c:out value = "${checkbox}"/></font>

 	<% if(u.getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)){%>
	<c:set var = "msg" value = "${success}"/>
      <c:if test = "${not empty msg}">
        <font color="green">Book <c:out value = "${msg}"/> Added</font>
      </c:if>
		<h2>Manage Books</h2>
		<form:form action="/addbook.htm" modelAttribute="book" method="post" enctype="multipart/form-data">
		<div class="form-group">
            <table>

                <tr>
                    <td>Genre:</td>
                    <td>
                        <form:select path="genre">
                            <c:forEach var="genre" items="${genres}">
                                 <form:option value="${genre.id}">${genre.name}</form:option>
                            </c:forEach>
                        </form:select>
                    </td>
                </tr>
                
                <tr>
					<td></br></td>
					<td><br/></td>
				</tr>

                <tr>
                    <td>Book Title:</td>
                    <td><form:input path="title" size="30"/> <font color="red"><form:errors path="title"/></font></td>
                </tr>
                
                <tr>
					<td></br></td>
					<td><br/></td>
				</tr>

                <tr>
                    <td>Author:</td>
                    <td><form:input path="author" size="30"/> <font color="red"><form:errors path="author"/></font></td>
                </tr>
                
                <tr>
					<td></br></td>
					<td><br/></td>
				</tr>

                <tr>
                    <td>ISBN:</td>
                    <td><form:input path="isbn" size="30" /> <font color="red"><form:errors path="isbn"/></font></td>
                </tr>
                
                <tr>
					<td></br></td>
					<td><br/></td>
				</tr>
                
                <tr>
                    <td>Select Item Photo:</td>
                    <td><input type="file" name="imageFile" accept="image/png, image/jpeg" size="30"/> <font color="red"><form:errors path="imageFile"/></font></td>
                </tr>
                
                <tr>
					<td></br></td>
					<td><br/></td>
				</tr>

                <tr>
                    <td colspan="2"><input type="submit" value="Add Book" /></td>
                </tr>
            </table>
            </div>
        </form:form>
 <% }%>   
  	<div id="bookContainer">
 	<c:set var = "books" value = "${books}"/>
 	<c:if test = "${empty books}">
 		<c:out value = "No Books Available"/>
 	</c:if>
      <c:if test = "${not empty books}">
	     
       <form:form onsubmit="return validateform()" id="bookOperation" name="bookOperation" method="post" action="book-operation.htm">
        <table border="1" cellpadding="5" cellspacing="5" class="table table-hover">
           <thead>
            <tr>
            	<td></td>
                <td scope="col">Book Name</td>
                <td scope="col">Author</td>
                <td scope="col">ISBN</td>
                <td scope="col">Status</td>
                <td scope="col">Genre</td>
                <td scope="col">Created Date</td>
            </tr>
             </thead>
             <tbody>
            <c:forEach var="book" items="${books}">
                <tr>
                	<td><input name="bookId" type="checkbox" value="${book.id}"></td>
                    <td >${book.title}</td>
                    <td>${book.author}</td>
                    <td>${book.isbn}</td>
                    <td>${book.bookStatus}</td>
                    <td>${book.genre.name}</td>
                    <td>${book.createdDate}</td>
                    <td><img width="100" height="100" src="data:image/jpeg;base64,${book.imagePath}"/>
                </tr>
            </c:forEach>
            
            </tbody>
        </table>
        <% if(u.getRole().equalsIgnoreCase(Constants.ROLE_CUSTOMER)){ %>
        	<input type="submit" name="borrow" id="borrow" value="Borrow">
        <%}else if(u.getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)){ %>
	        <input type="submit" name="delete" id="delete" value="Delete">
	        <input type="submit" name="update" id="update" value="Update"">
        <%} %>
		</form:form>
		</c:if>
		</div>
	</div>
</body>
</html>