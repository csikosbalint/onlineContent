<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page language="java" %>
<%@page import="hu.fnf.devel.onlinecontent.model.Content" %>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach" %>
<%@page import="java.lang.*" %>
<%@page import="java.util.*" %>
<%@page import="com.google.appengine.api.datastore.*" %>
<!DOCTYPE HTML>
<html>
<head>
<title>Online Jatek FNF: ${content.displayName}</title>
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
    <div id="logo" class="span-2"><img src="static/logo.png" alt=""></div>
    <h1 class="span-6"></h1>
    <nav class="span-16 last" style="float:right">
      <ul class="right">
      <!-- TODO: jquery to inclde menu.html -->
        <li><a href="/">Home</a></li>
        <li><a href="#">Categories</a></li>
        <li><a href="#">Favorites</a></li>
        <li><a href="#">Propose</a></li>
        <li><a href="#">Contact</a></li>
      </ul>
    </nav>
  </div>
</header>
<div class="separator"></div>
<section id="recentgames" class="container clearfix" style="margin:0 auto 0 auto;text-align:center;">
			<embed src="${content.contentSourceUrl}" type="application/x-shockwave-flash" width="950" height="540" />
<div style="position:relative;width:100%;margin:20px auto 0 auto; text-align:center; padding-left:90px;">
 <c:forEach items="${requestScope.entityList}" var="contents">
		<div class="span-6" style="margin: 5px 10px 5px 10px;text-align:center;">
		<div class="image_wrapper " style="border-style:solid;border-width 5px;border-color:black" > <a href="?contentname=${contents.nameKey.name}"> 
		<img class="rounded-corners" src="${contents.thumbLocaleUrl}" alt="" style="width:240px;height:119px"></a></div>
		<h6>${contents.displayName}</h6></div>
	</c:forEach>
</div>
</section>
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