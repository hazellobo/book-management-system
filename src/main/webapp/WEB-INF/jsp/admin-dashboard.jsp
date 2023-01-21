<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Admin dashboard</title>
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
	
	<ul class="list-group">
	  <li class="list-group-item"><a href="/manage-users.htm">Manage Users</a></li>
	  <li class="list-group-item"><a href="/manage-genre.htm">Manage Genre</a></li>
	  <li class="list-group-item"><a href="/manage-books.htm">Manage Books</a></li>
	</ul>
</body>
</html>