<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Tracktivity - Activity ${activity.name}</title>
<link rel="stylesheet" href="/css/normalize.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css" />
</head>
<body>

<c:choose>
	<c:when test="${empty activity}">
		<h2>The activity with the ID <em>${activityId}</em> does not exist.</h2>
	</c:when>
	<c:otherwise>
		<h1>Activity <em>${activity.name}</em></h1>
		<p>created by ${activity.user.username}</p>
	</c:otherwise>
</c:choose>

</body>
</html>