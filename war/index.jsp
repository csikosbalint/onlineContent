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

	<div class="row">
		<div class="large-12 columns">

			<div class="row">
				<!-- Side Bar -->

				<div class="large-4 small-12 columns">
					<img src="./static/Logo.jpg">

					<div class="hide-for-small panel">
						<h3>Header</h3>

						<h5 class="subheader">Risus ligula, aliquam nec fermentum
							vitae, sollicitudin eget urna. Donec dignissim nibh fermentum
							odio ornare sagittis.</h5>
					</div>
					<a href="#">
						<div class="panel callout radius">
							
						</div>
					</a>
				</div>		
				<!-- End Side Bar -->
				<!-- Thumbnails -->

				<div class="large-8 columns">
					<div class="row">
						<c:forEach items="${requestScope.entityList}" var="content">
							<div class="large-4 small-6 columns">
								<a href="?contentname=<%= ((Content) pageContext.getAttribute("content")).getNameKey() %>">
								<img src="<%= ((Content) pageContext.getAttribute("content")).getThumbBlobUrl() %>" /></a>
								<div class="panel">
									<!-- <h5><%= ((Content) pageContext.getAttribute("content")).getDisplayName() %></h5> -->
									<h5><view:ContentThumbnailImg content="${content}"/></h5>
								</div>
							</div>
						</c:forEach>
					</div>
					<!-- End Thumbnails -->
					<!-- Managed By -->

					<div class="row">
						<div class="large-12 columns">
							<div class="panel">
								<div class="row">
									<div class="large-2 small-6 columns">
										<img src="./static/SiteOwner.jpg">
									</div>

									<div class="large-10 small-6 columns">
										<strong>This Site Is Managed By</strong>
										<hr>
										Risus ligula, aliquam nec fermentum vitae, sollicitudin eget
										urna. Donec dignissim nibh fermentum odio ornare sagittis
									</div>
								</div>
							</div>
						</div>
						<!-- End Managed By -->
					</div>
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
									<li><a href="/?page=${requestScope.pageActual - 1}"><-</a></li>
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
									<li><a href="/?page=${requestScope.pageActual + 1}">-></a></li>
									
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${requestScope.pageActual <= 4}">
											<c:choose>
												<c:when test="${requestScope.pageActual != 1}">
													<li><a href="/?page=${requestScope.pageActual - 1}"><-</a></li>
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
											<li><a href="/?page=${requestScope.pageActual + 1}">-></a></li>
										</c:when>
										<c:otherwise>
											<li><a href="/?page=${requestScope.pageActual - 1}"><-</a></li>
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
													<li><a href="/?page=${requestScope.pageActual + 1}">-></a></li>
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
	</div>
	<div id="foot">
	<p>© Copyright no one at all. Go to town.</p>
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