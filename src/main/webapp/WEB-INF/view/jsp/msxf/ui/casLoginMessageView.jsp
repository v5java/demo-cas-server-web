<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:directive.include file="includes/top.jsp"/>

<div id="msg" class="warn">
	<h2>Authentication Succeeded with Warnings</h2>
	<c:forEach items="${messages}" var="message">
		<p class="message">${message.text}</p>
	</c:forEach>
</div>

<div id="big-buttons">
	<a class="button" href="login?execution=${flowExecutionKey}&_eventId=proceed">Continue</a>
</div>

<jsp:directive.include file="includes/bottom.jsp"/>