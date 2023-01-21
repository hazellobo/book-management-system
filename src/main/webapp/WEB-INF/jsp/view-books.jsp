<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>View Issued Books</title>
</head>
<body>
	<nav class="navbar navbar-default">
	  <div class="container-fluid">
	    <ul class="nav navbar-nav">
	      <li class="active"><a href="customer.htm">Home</a></li>
	      <li><a href="logout.htm">Logout</a></li>
	    </ul>
	  </div>
	</nav>
	<div class="container">
	View books
 	<c:set var = "books" value = "${books}"/>
 	<c:set var = "view" value = "${view}"/>
 	<c:if test = "${empty books}">
 		<c:out value = "No Books Available"/>
 	</c:if>
      <c:if test = "${not empty books}">
       <form:form method="POST" action="/return-books.htm">
        <table border="1" cellpadding="5" cellspacing="5" class="table table-hover">
           <thead>
            <tr>
            <c:if test = "${view.equals('return')}">
            	<td></td>
            </c:if>
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
                <c:if test = "${view.equals('return')}">
                	<td><input name="bookId" type="checkbox" value="${book.id}"></td>
                </c:if>
                    <td >${book.title}</td>
                    <td>${book.author}</td>
                    <td>${book.isbn}</td>
                    <c:if test = "${view.equals('history')}">
                    	<c:set var = "status" value = "${book.bookStatus}"/>
                    	 <c:if test = "${status=='AVAILABLE'}">
                    	 	<td>RETURNED</td>
                    	 </c:if>
                    	 <c:if test = "${status=='ISSUED'}">
                    	 	<td>PENDING RETURN</td>
                    	 </c:if>
                    </c:if>
                    
                    <c:if test = "${view.equals('return')}">
                    	 <td>${book.bookStatus}</td>
                    </c:if>
                    <td>${book.genre.name}</td>
                    <td>${book.createdDate}</td>
                    <td><img width="100" height="100" src="data:image/jpeg;base64,${book.imagePath}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test = "${view.equals('return')}">
        	<input type="submit" value="Return">
        </c:if>
		</form:form>
		</c:if>
		</div>
</body>
</html>