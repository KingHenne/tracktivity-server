<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Tracktivity - Activity ${activity.name}</title>

<!-- Mobile viewport optimized: j.mp/bplateviewport -->
<meta name="viewport" content="width=device-width, minimum-scale=1.0 maximum-scale=1.0 initial-scale=1.0" />

<link rel="stylesheet" href="/css/normalize.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/leaflet.css" type="text/css" />
</head>
<body>

<c:choose>
	<c:when test="${empty activity}">
		<h2>The activity with the ID <em>${activityId}</em> does not exist.</h2>
	</c:when>
	<c:otherwise>
		<s:url var="userUrl" value="../users/${activity.user.username}" />
		<fmt:formatDate value="${activity.created}" var="activityDate" type="both" dateStyle="full" timeStyle="short" />
		<h1><c:out value="${empty activity.name ? activityDate : activity.name}" /></h1>
		<dl class="activityData">
			<dt>Created by:</dt>
			<dd><a href="${userUrl}">${activity.user.fullname}</a></dd>
		<c:if test="${not empty activity.type}">
			<dt>Activity type:</dt>
			<dd>${activity.type.label}</dd>
		</c:if>
			<dt>Duration:</dt>
			<dd>${durationNetto} (${durationBrutto} incl. breaks)</dd>
			<dt>Distance:</dt>
			<dd><fmt:formatNumber maxFractionDigits="2" value="${activity.track.lengthInMeters / 1000}" /> km</dd>
		</dl>
		<div id="map"></div>
	</c:otherwise>
</c:choose>

<script src="/js/leaflet.js"></script>
<script type="text/javascript">
	var multiPolyline = ${activity.track.sparseMultiPolyline};
	var latLonBounds = ${activity.track.latLngBounds};
	
	var map = L.map('map');
	map.fitBounds(latLonBounds);
	
	L.tileLayer('http://{s}.tile.cloudmade.com/{key}/{styleId}/256/{z}/{x}/{y}.png', {
		key: '6eac6d67cf3f4fa8a18bbf5bec747cdc',
		styleId: 70963,
	    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery &copy; <a href="http://cloudmade.com">CloudMade</a>',
	    maxZoom: 18,
	    detectRetina: true
	}).addTo(map);
	
	var polyline = L.multiPolyline(multiPolyline, {color: '#0073E5', opacity: 0.8}).addTo(map);
</script>

</body>
</html>