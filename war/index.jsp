<%@page import="hu.fnf.devel.onlinecontent.controller.OnlineContentServlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="WEB-INF/view.tld"%>
<%@page language="java"%>
<%@page import="hu.fnf.devel.onlinecontent.model.Content"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.*"%>
<%@page import="com.google.appengine.api.datastore.*"%>







<!DOCTYPE html>
<!-- saved from url=(0047)http://foundation.zurb.com/templates/store.html -->
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
	content="Documentation and reference library for ZURB Foundation. JavaScript, CSS, components, grid and more.">

<meta name="author"
	content="ZURB, inc. ZURB network also includes zurb.com">
<meta name="copyright" content="ZURB, inc. Copyright (c) 2013">

<link rel="stylesheet" href="static/foundation.css">
<script src="./static/google.js" </script>
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


	<div class="row">
		<div class="large-12 columns">
			<!-- Navigation -->

			<div class="row">
				<div class="large-12 columns">
					<nav class="top-bar" data-topbar="">
						<ul class="title-area">
							<!-- Title Area -->

							<li class="name">
								<h1>
									<a href="/">Home</a>
								</h1>
							</li>

							<li class="toggle-topbar menu-icon"><a href="#"><span>menu</span></a>
							</li>
						</ul>


						<section class="top-bar-section">
							<!-- Right Nav Section -->

							<ul class="right">
								<li class="divider"></li>

								<li class="has-dropdown not-click"><a href="#">Main
										Item 1</a>

									<ul class="dropdown">
										<li class="title back js-generated"><h5>
												<a href="javascript:void(0)">Back</a>
											</h5></li>
										<li><label>Section Name</label></li>

										<li class="has-dropdown not-click"><a class="" href="#">Has
												Dropdown, Level 1</a>

											<ul class="dropdown">
												<li class="title back js-generated"><h5>
														<a href="javascript:void(0)">Back</a>
													</h5></li>
												<li><a href="#">Dropdown Options</a></li>

												<li><a href="#">Dropdown Options</a></li>

												<li><a href="#">Level 2</a></li>

												<li><a href="#">Subdropdown Option</a></li>

												<li><a href="#">Subdropdown Option</a></li>

												<li><a href="#">Subdropdown Option</a></li>
											</ul></li>

										<li><a href="#">Dropdown Option</a></li>

										<li><a href="#">Dropdown Option</a></li>

										<li class="divider"></li>

										<li><label>Section Name</label></li>

										<li><a href="#">Dropdown Option</a></li>

										<li><a href="#">Dropdown Option</a></li>

										<li><a href="#">Dropdown Option</a></li>

										<li class="divider"></li>

										<li><a href="#">See all →</a></li>
									</ul></li>

								<li class="divider"></li>

								<li><a href="#">Main Item 2</a></li>

								<li class="divider"></li>

								<li class="has-dropdown not-click"><a href="#">Main
										Item 3</a>

									<ul class="dropdown">
										<li class="title back js-generated"><h5>
												<a href="javascript:void(0)">Back</a>
											</h5></li>
										<li><a href="#">Dropdown Option</a></li>

										<li><a href="#">Dropdown Option</a></li>

										<li><a href="#">Dropdown Option</a></li>

										<li class="divider"></li>

										<li><a href="#">See all →</a></li>
									</ul></li>
							</ul>
						</section>
					</nav>
					<!-- End Top Bar -->
				</div>
			</div>
			<!-- End Navigation -->

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
							<h6>99&nbsp; items in your cart</h6>
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
									<h5><%= ((Content) pageContext.getAttribute("content")).getDisplayName() %></h5>
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
							<p>© Copyright no one at all. Go to town.</p>
						</div>

						<div class="large-6 columns">
							<ul class="inline-list right">
							    <li>Pages:</li>
							<c:forEach var="i" begin="1" end="${requestScope.listSize}">
   								<c:choose>
    							    <c:when test="${i eq requestScope.pageActual}">
            							<li underline><c:out value="${i}"/></li>
        							</c:when>
        							<c:otherwise>
            							<li><a href="?page=<c:out value="${i}"/>"><c:out value="${i}"/></a></li>
        							</c:otherwise>
    							</c:choose>
							</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</footer>
			<!-- End Footer -->
		</div>
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