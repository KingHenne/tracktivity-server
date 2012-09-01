<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Tracktivity - ${username}</title>
<link rel="stylesheet" href="/css/normalize.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css" />
</head>
<body>

<c:choose>
	<c:when test="${empty user}">
		<h2>The user <em>${username}</em> does not exist.</h2>
	</c:when>
	<c:otherwise>
		<h1>Profile for <em>${user.username}</em></h1>
		<p>${user.firstname} ${user.lastname}</p>
		<c:choose>
			<c:when test="${empty user.activities}">
				<h2>${user.firstname} has not yet recorded or uploaded any activities.</h2>
			</c:when>
			<c:otherwise>
				<h2>Activities</h2>
				<ul>
				<c:forEach var="activity" items="${user.activities}">
					<s:url value="../activity/${activity.id}" var="activtyUrl" />
					<fmt:formatDate value="${activity.created}" var="activityDate" type="date" dateStyle="full" />
					<fmt:formatDate value="${activity.created}" var="activityTime" type="time" timeStyle="full" />
					<li><a href="${activtyUrl}">${activity.name}</a> &ndash; ${activityDate}, ${activityTime}</li>
				</c:forEach>
				</ul>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

</body>
</html>