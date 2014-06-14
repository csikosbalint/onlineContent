<%@page import="hu.fnf.devel.onlinecontent.controller.OnlineContentServlet"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="WEB-INF/view.tld"%>
<%@page language="java"%>
<%@page import="hu.fnf.devel.onlinecontent.model.Content"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.*"%>
<%@page import="com.google.appengine.api.datastore.*"%>







<!DOCTYPE html>

<html
	class=" js flexbox flexboxlegacy canvas canvastext webgl no-touch geolocation postmessage websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers applicationcache svg inlinesvg smil svgclippaths"
	lang="en"
	data-useragent="Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Foundation Template | Store | <%
	out.print(request.getSession(true).getAttribute("cica"));
%></title>
<meta name="description"
	content="Database of online flash games.">
<meta name="author"
	content="">
<meta name="copyright" content="Copyright (c) 2014">

<link rel="stylesheet" href="./static/foundation.css">
<script src="./static/google.js" ></script>
<script src="./static/modernizr.js"></script>
<style type="text/css"></style>
<meta class="foundation-data-attribute-namespace">
<meta class="foundation-mq-xxlarge">
<meta class="foundation-mq-xlarge">
<meta class="foundation-mq-large">
<meta class="foundation-mq-medium">
<meta class="foundation-mq-small">
<style></style>
<meta class="foundation-mq-topbar">
</head>
<body>
<div id="top_part">
	<div class="top_logo">
		<img src="./static/logo.png" />
	</div>
</div>
	<div id="menu_bar">		
	</div>
	<div id="content_frame">
	<div id="left_side" class="left column_size_25">
		<% if (request.getAttribute("session") != null && ((HttpSession)request.getAttribute("session")).getAttribute("admin") != null ) { %>
		<form action="/">
		<button type="submit" name="forceReload">Reload</button>
		</form>
		<% } else { %> 
		<img src="/static/banner_v.gif">
		<% } %>
	</div>
	<div id="middle_content" class="mid">
		<div class="row">
				<div class="row">
					<!-- Thumbnails -->
						<div class="three_x_three">
							<div class="row">
								<c:forEach items="${requestScope.entityList}" var="content">
									<div class="large-21 small-6 columns">
										<a href="?contentname=<%= ((Content) pageContext.getAttribute("content")).getNameKey() %>">
										<img src="<%= ((Content) pageContext.getAttribute("content")).getThumbBlobUrl() %>" /></a>							
											<!-- <h5><%= ((Content) pageContext.getAttribute("content")).getDisplayName() %></h5> -->
										<h6><view:ContentThumbnailImg content="${content}"/></h6>
									</div>
								</c:forEach>
							</div>
						<!-- End Thumbnails -->
						</div>
				</div>
				<!-- Footer -->

				<footer class="row">
				<div class="large-12 columns">
					<hr>
					<div class="row">
						<div class="large-6 columns">
							<ul class="inline-list right">
							<c:choose>
								<c:when test="${requestScope.pageActual > 4 &&  requestScope.pageActual <= requestScope.listSize-4}">
									<li><a href="/?page=${requestScope.pageActual - 1}"><-prev</a></li>
									<li><a href="/?page=1">1</a></li>
									<li>...</li>
									<c:forEach var="i" begin="${requestScope.pageActual-2}" end="${requestScope.pageActual+2}">
   										<c:choose>
    							  			<c:when test="${i == requestScope.pageActual}">
            									<li underline><c:out value="${i}"/></li>
        									</c:when>
        									<c:otherwise>
            									<li><a href="?page=<c:out value="${i}"/>"><c:out value="${i}"/></a></li>
        									</c:otherwise>
    									</c:choose>
									</c:forEach>
									<li>...</li>
									<li><a href="/?page=${requestScope.listSize}">${requestScope.listSize}</a></li>
									<li><a href="/?page=${requestScope.pageActual + 1}">next-></a></li>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${requestScope.pageActual <= 4}">
											<c:choose>
												<c:when test="${requestScope.pageActual != 1}">
													<li><a href="/?page=${requestScope.pageActual - 1}"><-prev</a></li>
												</c:when>
											</c:choose>				
											<c:forEach var="i" begin="1" end="${requestScope.pageActual+2}">
   												<c:choose>
    							    				<c:when test="${i == requestScope.pageActual}">
            											<li underline><c:out value="${i}"/></li>
        											</c:when>
        											<c:otherwise>
            											<li><a href="?page=<c:out value="${i}"/>"><c:out value="${i}"/></a></li>
        											</c:otherwise>
    											</c:choose>
											</c:forEach>
											<li>...</li>
											<li><a href="/?page=${requestScope.listSize}">${requestScope.listSize}</a></li>
											<li><a href="/?page=${requestScope.pageActual + 1}">next-></a></li>
										</c:when>
										<c:otherwise>
											<li><a href="/?page=${requestScope.pageActual - 1}"><-prev</a></li>
											<li><a href="/?page=1">1</a></li>
											<li>...</li>
											<c:forEach var="i" begin="${requestScope.pageActual-2}" end="${requestScope.listSize}">
	   											<c:choose>
    							  					<c:when test="${i == requestScope.pageActual}">
	            										<li underline><c:out value="${i}"/></li>
        											</c:when>
        											<c:otherwise>
	            										<li><a href="?page=<c:out value="${i}"/>"><c:out value="${i}"/></a></li>
        											</c:otherwise>
    											</c:choose>
											</c:forEach>
											<c:choose>
												<c:when test="${requestScope.pageActual != requestScope.listSize}">
													<li><a href="/?page=${requestScope.pageActual + 1}">next-></a></li>
												</c:when>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>			
							</ul>
						</div>
					</div>
				</div>
			</footer>
			<!-- End Footer -->
		</div>
	</div>
	<div id="right_side" class="right column_size_25">
	</div>
	</div>
	</div>
	<div id="foot">
	<p>Copyright Onlinejatek.fnf.hu @ 2014</p>
	</div>
	<script src="./static/jquery.js"></script>
	<script src="./static/foundation.js"></script>
	<script>
		$(document).foundation();

		var doc = document.documentElement;
		doc.setAttribute('data-useragent', navigator.userAgent);
	</script>

</body>
</html>