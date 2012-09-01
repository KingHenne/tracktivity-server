<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Tracktivity - Login</title>
<link rel="stylesheet" href="/css/normalize.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css" />
</head>
<body>

<h1>Login</h1>

<s:url var="authUrl" value="j_spring_security_check" />

<form method="post" action="${authUrl}">

	<c:if test="${error}">
		<div class="error">
			Your login attempt was not successful, try again.<br />
			Reason: ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>
	
	<p>
		<label for="username">Username:</label><br/>
		<input type="text" id="username" name="j_username" size="15" autofocus="autofocus" />
	</p>
	<p>
		<label for="j_password">Password:</label><br/>
		<input type="password" id="password" name="j_password" size="15" />
	</p>
	<p>
		<input type="checkbox" id="remember_me" name="_spring_security_remember_me" />
		<label for="remember_me">Remember me</label>
	</p>
	<p><input type="submit" value="Login" /></p>
</form>

</body>
</html>