<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Tracktivity - Live Tracking</title>
<link rel="stylesheet" href="/css/normalize.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<style type="text/css">
	input#chat {
		width: 410px
	}
	#console-container {
		width: 400px;
	}
	#console {
		border: 1px solid #ccc;
		border-right-color: #999;
		border-bottom-color: #999;
		height: 170px;
		overflow-y: scroll;
		padding: 5px;
		width: 100%;
	}
	#console p {
		padding: 0;
		margin: 0;
	}
</style>
</head>
<body>

<div>
	<p>
		<input type="text" placeholder="type and press enter to chat" id="chat">
	</p>
	<div id="console-container">
		<div id="console"></div>
	</div>
</div>

<script src="/js/chat.js"></script>
</body>
</html>
