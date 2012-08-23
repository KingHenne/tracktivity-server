<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Tracktivity - Register</title>
<link rel="stylesheet" href="/css/normalize.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css" />
</head>
<body>

<h1>Register</h1>

<sf:form method="POST" modelAttribute="user">
	<p>
		<label for="username">User Name:</label><br/>
		<sf:errors path="username" cssClass="error" htmlEscape="false" />
		<sf:input id="username" path="username" size="15" />
	</p>
	<p>
		<label for="firstname">First Name:</label><br/>
		<sf:errors path="firstname" cssClass="error" />
		<sf:input id="firstname" path="firstname" size="15" />
	</p>
	<p>
		<label for="lastname">Last Name:</label><br/>
		<sf:errors path="lastname" cssClass="error" />
		<sf:input id="lastname" path="lastname" size="15" />
	</p>
	<p><input type="submit" value="Register" /></p>
</sf:form>

</body>
</html>