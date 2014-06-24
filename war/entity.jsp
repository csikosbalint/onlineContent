<%@page language="java"%>
<%@page import="hu.fnf.devel.onlinecontent.controller.OnlineContentServlet"%>
<%@page import="hu.fnf.devel.onlinecontent.model.Content"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.*"%>
<%@page import="com.google.appengine.api.datastore.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="view" uri="WEB-INF/view.tld"%>
<!DOCTYPE html>

<html
	class=" js flexbox flexboxlegacy canvas canvastext webgl no-touch geolocation postmessage websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers applicationcache svg inlinesvg smil svgclippaths"
	lang="en"
	data-useragent="Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Foundation Template | So Boxy</title>

<link rel="stylesheet" href="./static/foundation.css">
<script src="./static/modernizr.js"></script>
<script src="./static/google.js"></script>
<meta class="foundation-data-attribute-namespace">
<meta class="foundation-mq-xxlarge">
<meta class="foundation-mq-xlarge">
<meta class="foundation-mq-large">
<meta class="foundation-mq-medium">
<meta class="foundation-mq-small">
<style></style>
<meta class="foundation-mq-topbar">
<style type="text/css"></style>
</head>


<body>

	<div id="top_part">
		<div class="top_logo">
			<img src="./static/logo.png" />
		</div>
	</div>
	<div id="menu_bar"></div>
	<div id="content_frame">
		<div id="left_side" class="left column_size_25">

			<%
				if (request.getAttribute("session") != null
						&& ((HttpSession) request.getAttribute("session"))
								.getAttribute("admin") != null) {
			%>
			<img
				src="<%=((Content) request.getAttribute("content"))
						.getThumbBlobUrl()%>" />
			<form action="/">
				<%=((Content) request.getAttribute("content"))
						.getDisplayName()%>
				<input type="hidden" name="contentname"
					value="<%=((Content) request.getAttribute("content"))
						.getNameKey()%>" />
				<input type="text" name="searchKeyWords"
					value="<%=((Content) request.getAttribute("content"))
						.getSearchKeyWords().toString()%>" />
				<button type="submit" name="changeAndSearch">Change and
					Search</button>
			</form>
			<%
				} else {
			%>
			<img src="/static/banner_v.gif">
			<%
				}
			%>
		</div>
		<div id="middle_content" class="mid">
			<div id="right_side" class="left column_size_25"></div>
			<div class="row">

				<div class="row">

					<div class="large-4 columns">
						<embed
							src="<%=((Content) request.getAttribute("content"))
					.getContentSourceUrl()%>"
							type="application/x-shockwave-flash" width="720" height="540"></embed>

					</div>

					<div class="large-6 columns"></div>

					<!-- End Header Content -->


					<!-- Search Bar -->

					<div class="row">
						<pre>categories</pre>
						<div class="large-12 columns">
							<div class="radius panel">

								<form>
									<div class="row collapse">

										<div class="large-10 small-8 columns">
											<input type="text">
										</div>

										<div class="large-2 small-3 columns">
											<a href="#" class="postfix button expand">Search</a>
										</div>
										<pre>
										<view:ContentCategories
												content="<%=(Content) request.getAttribute(\"content\")%>">
												</view:ContentCategories>
										</pre>
									</div>
								</form>

							</div>
						</div>

					</div>

					<!-- End Search Bar -->


					<!-- Thumbnails -->
				</div>
				<div class="row">
					<div class="mid">
						<div class="large-3 small-6 columns">
							<img src="./static/Thumbnail500.jpg">

							<p>Description</p>

						</div>

						<div class="large-3 small-6 columns">
							<img src="./static/Thumbnail500.jpg">

							<p>Description</p>

						</div>

						<div class="large-3 small-6 columns">
							<img src="./static/Thumbnail500.jpg">

							<p>Description</p>

						</div>

						<div class="large-3 small-6 columns">
							<img src="./static/Thumbnail500.jpg">

							<p>Description</p>

						</div>
					</div>
				</div>
				<!-- End Thumbnails -->
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