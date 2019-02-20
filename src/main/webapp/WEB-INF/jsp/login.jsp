<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Spring Security Example</title>
<!-- <link href="/bootstrap.min.css" rel="stylesheet">
    <script src="/jquery-2.2.1.min.js"></script>
    <script src="/bootstrap.min.js"></script> -->
</head>
<body>
	<div class="container" style="margin:50px">
		<h3>Spring Security Login Example</h3>
		
		 <c:if test="${result ne null}">
			<div style="color: red"><h3>${result}</h3></div> 
		</c:if>
		
		<form action="/sso/login" method="post">
			<div class="form-group">
				<label for="username">UserName: <input type="text" class="form-control" id="username" name="username" value="74515">
			</div>
			<div class="form-group">
				<label for="pwd">Password:</label> <input type="password" class="form-control" id="pwd" name="password" value="mypswd">
			</div>
			<c:if test="${client_id != null}">
				<input type="hidden" class="form-control" id="client_id" name="client_id" value="${client_id}">
				<div style="color: blue">${client_id}</div> 
		</c:if>

			<button type="submit" class="btn btn-success">Submit</button>

			<%-- <input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" /> --%>
		</form>
	</div>
</body>
</html>