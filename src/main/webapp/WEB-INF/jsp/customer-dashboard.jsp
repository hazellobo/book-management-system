<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Customer Dashboard</title>
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
	<ul class="list-group">
	  <li class="list-group-item"><a href="/manage-books.htm">Borrow Books</a></li>
	  <li class="list-group-item"><a href="/manage-return.htm">Active Issues</a></li>
	  <li class="list-group-item"><a href="/viewHistory.htm">View History</a></li>
	  <!--  <li class="list-group-item"><a href="/raise-issue.htm">Raise Issue</a></li>-->
	</ul>
</body>
</html>