<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Tracktivity - Upload Activity</title>
<link rel="stylesheet" href="/css/normalize.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css" />
</head>
<body>

<h1>Upload a new activity.</h1>

<sf:form method="POST" enctype="multipart/form-data" modelAttribute="uploadItem">
	<sf:errors path="fileData" element="p" cssClass="error" />
	<p>
		<label for="activityTypes">Activity type:</label><br/>
		<sf:select path="activityType" id="activityTypes" items="${uploadItem.activityTypes}" itemLabel="label" />
	</p>
	<p>
		<label for="fileData">GPX File:</label><br/>
		<input type="file" name="fileData" />
	</p>
	<p><input type="submit" value="Upload" onclick="this.disabled='disabled'; this.form.submit();" /></p>
</sf:form>

</body>
</html>