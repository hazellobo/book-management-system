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
<title>View Borrowed Books</title>
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
 	<c:set var = "issues" value = "${issues}"/>
 	<c:set var = "view" value = "${view}"/>
 	
 	<c:if test = "${empty issues}">
 		<c:out value = "No Issues Available"/>
 	</c:if>
      <c:if test = "${not empty issues}">
      	Manage Issues
       <form:form method="POST" action="/return-issues.htm">
        <table border="1" cellpadding="5" cellspacing="5" class="table table-hover">
           <thead>
            <tr>
               <c:if test = "${view.equals('return')}">
            	<td></td>
 				</c:if>
                <td scope="col">Issue Date</td>
                <td scope="col">Expected Return Date</td>
                <td scope="col">Status</td>
                <td scope="col">Books</td>
            </tr>
             </thead>
             <tbody>
            <c:forEach var="issue" items="${issues}">
                <tr>
                <c:if test = "${view.equals('return')}">
                	  <td><input name="issueId" type="checkbox" value="${issue.id}"></td>
 				</c:if>
                    <td >${issue.issueDate}</td>
                    <td>${issue.expectedReturnDate}</td>
                    <td>${issue.issueStatus}</td>
                    <c:if test = "${view.equals('return')}">
 				                    <td><a href="/viewIssuedBooks.htm?id=${issue.id}">view books</a></td>
 					</c:if>
 					<c:if test = "${view.equals('history')}">
 				                    <td><a href="/viewAllBooks.htm?id=${issue.id}">view books</a></td>
 					</c:if>
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