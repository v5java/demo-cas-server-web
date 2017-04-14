<%@ page pageEncoding="UTF-8" session="false"%>
<%
	final String queryString = request.getQueryString();
	final String url = request.getContextPath() + "/login" + (queryString!=null ? "?"+queryString : "");
	response.sendRedirect(response.encodeURL(url));
%>