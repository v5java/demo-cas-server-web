<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="session"/>

<!DOCTYPE HTML>
<html lang="en">
<head>
	<meta charset="UTF-8"/>
	<title>CAS单点登录系统</title>
	<link rel="icon" href="${ctx}/favicon.ico" type="image/x-icon"/>
	<link rel="stylesheet" href="${ctx}/css/cas.css"/>
	<!--[if lt IE 9]>
		<script src="/js/html5shiv-3.7.2.min.js" type="text/javascript"></script>
	<![endif]-->
</head>
<body id="cas">
	<div id="container">
		<div id="content">