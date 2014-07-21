<%@page language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="WEB-INF/view.tld" %>
<%@page import="hu.fnf.devel.onlinecontent.controller.OnlineContentServlet" %>
<%@page import="hu.fnf.devel.onlinecontent.model.Viewable" %>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach" %>
<%@page import="java.lang.*" %>
<%@page import="java.util.*" %>
<%@page import="com.google.appengine.api.datastore.*" %>
<!DOCTYPE HTML>
<html>
<head>
<title>Online Jatek FNF</title>
<meta name="copyright" content="Copyright (c) 2014" >
<meta name="description" content="Database of online flash games." >
<meta name="viewport" content="width=device-width, initial-scale=1.0" >
<meta charset="utf-8">
<link rel="stylesheet" href="static/screen.css" type="text/css" media="screen">
<link rel="stylesheet" href="static/style.css" type="text/css" media="screen">
</head>
<body>
<header class="clearfix">
  <div class="container">
    <div id="logo" class="span-2">
        <a href="/"><img src="static/logo.png" alt="Online Jatek FNF"></a>
    </div>
    <h1 class="span-6"></h1>
    <nav class="span-16 last" style="float:right">
      <ul class="right">
        <li class="${param.menu == null ? 'active' : 'inactive'}"><a href="/">Home</a></li>
        <li class="${param.menu == 'categories' ? 'active' : 'inactive'}"><a href="/?menu=categories">Categories</a></li>
        <li><a href="/?favorites">Favorites</a></li>
        <li><a href="#">Propose</a></li>
        <li><a href="#">Contact</a></li>
      </ul>
    </nav>
  </div>
</header>
<div class="separator"></div>
<section id="recentgames" class="container clearfix" style="margin:0 auto 0 auto;text-align:center;">
   <c:forEach items="${requestScope.entityList}" var="content">
	<div class="span-6" style="margin: 5px 10px 5px 10px;text-align:center;">
		<div class="image_wrapper " style="border-style:solid;border-width 5px;border-color:black" >
		  <a href="?contentname=${content.nameKey.name}">
		      <img class="rounded-corners"
		          src="${content.thumbLocaleUrl}"
		          alt="${content.nameKey.name}"
		          style="width:240px;height:119px"/>
		  </a>
		</div>
		<h6>${content.displayName}</h6>
    </div>
	</c:forEach>
</section>

<div class="page_nav">
					<hr> <!-- TODO: page numbering is buggy!!! -->
							<ul class="inline-list" >
							<c:choose>
								<c:when test="${requestScope.pageActual > 4 &&  requestScope.pageActual <= requestScope.listSize-4}">
									<li>
									   <a href="/?page=${requestScope.pageActual - 1}">
									       <view:Interpreter text="prev"></view:Interpreter>
									   </a>
                                    </li>
									<li><a href="/?page=1">1</a></li>
									<li>...</li>
									<c:forEach var="i" begin="${requestScope.pageActual-2}" end="${requestScope.pageActual+2}">
   										<c:choose>
    							  			<c:when test="${i == requestScope.pageActual}">
            									<li><c:out value="${i}"/></li>
        									</c:when>
        									<c:otherwise>
            									<li><a href="?page=<c:out value="${i}"/>"><c:out value="${i}"/></a></li>
        									</c:otherwise>
    									</c:choose>
									</c:forEach>
									<li>...</li>
									<li><a href="/?page=${requestScope.listSize}">${requestScope.listSize}</a></li>
									<li><a href="/?page=${requestScope.pageActual + 1}"><view:Interpreter text="next"></view:Interpreter></a></li>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${requestScope.pageActual <= 4}">
											<c:choose>
												<c:when test="${requestScope.pageActual != 1}">
													<li><a href="/?page=${requestScope.pageActual - 1}"><view:Interpreter text="prev"></view:Interpreter></a></li>
												</c:when>
											</c:choose>				
											<c:forEach var="i" begin="1" end="${requestScope.pageActual+2}">
   												<c:choose>
    							    				<c:when test="${i == requestScope.pageActual}">
            											<li><c:out value="${i}"/></li>
        											</c:when>
        											<c:otherwise>
            											<li><a href="?page=<c:out value="${i}"/>"><c:out value="${i}"/></a></li>
        											</c:otherwise>
    											</c:choose>
											</c:forEach>
											<li>...</li>
											<li><a href="/?page=${requestScope.listSize}">${requestScope.listSize}</a></li>
											<li><a href="/?page=${requestScope.pageActual + 1}"><view:Interpreter text="next"></view:Interpreter></a></li>
										</c:when>
										<c:otherwise>
											<li><a href="/?page=${requestScope.pageActual - 1}"><view:Interpreter text="prev"></view:Interpreter></a></li>
											<li><a href="/?page=1">1</a></li>
											<li>...</li>
											<c:forEach var="i" begin="${requestScope.pageActual-2}" end="${requestScope.listSize}">
	   											<c:choose>
    							  					<c:when test="${i == requestScope.pageActual}">
	            										<li><c:out value="${i}"/></li>
        											</c:when>
        											<c:otherwise>
	            										<li><a href="?page=<c:out value="${i}"/>"><c:out value="${i}"/></a></li>
        											</c:otherwise>
    											</c:choose>
											</c:forEach>
											<c:choose>
												<c:when test="${requestScope.pageActual != requestScope.listSize}">
													<li><a href="/?page=${requestScope.pageActual + 1}"><view:Interpreter text="next"></view:Interpreter></a></li>
												</c:when>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>			
							</ul>
</div>
<footer class="clearfix">
  <div class="container">
    <div class="about span-6 append-1">
      <h3>About the site's history</h3>
      <p>The basic idea was that to develope a website which is able to build its content without human resources. The project actually works for crawl flash games over the internet and collect them all, but it's do it just because the searching criteria defined for this.</p>
      <p class="social_icons" style="float:right">Follow us :<br/><a href="https://github.com/csikosbalint/onlineContent"><img src="static/git.png" alt=""></a><br/></p>
    </div>
    <div id="tweets" class="span-6 append-1">
      <h3>Latest updates</h3>
      <div class="one_tweet">
        <p>Upgraded design</p>
        <div class="date">27 June 2014</div>
      </div>
	  <div class="one_tweet">
        <p>Crawler starting to mine</p>
        <div class="date">25 April 2014</div>
      </div>
	  <div class="one_tweet">
        <p>Project borns</p>
        <div class="date">12 April 2014</div>
      </div>
    </div>
    <div id="footer_form" class="span-10 last clearfix">
      <h3>Registration</h3>
      <form action="#">
        <div class="row">
          <label>Nick</label>
          <input type="text" name="name" class="span-7 right last">
        </div>
        <div class="row" style="margin-top:20px">
          <label>Your e-Mail</label>
          <input type="text" name="email" class="span-7 right last">
        </div>
        <div class="span-7 right last">
          <input type="submit" class="right">
        </div>
      </form>
    </div>
    <hr>
    Copyright @ 2014 <span class="right last"><a href="#"><img src="static/fbs.png" alt=""></a><a href="#"><img src="static/ts.png" alt=""></a><a href="#"><img src="static/gs.png" alt=""></a></div>
</footer>
<script src="static/scripts/jquery.min.js"></script>
</body>
</html>