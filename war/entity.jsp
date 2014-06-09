<%@page import="hu.fnf.devel.onlinecontent.controller.OnlineContentServlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page language="java"%>
<%@page import="hu.fnf.devel.onlinecontent.model.Content"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.*"%>
<%@page import="com.google.appengine.api.datastore.*"%>
<!DOCTYPE html>

<html class=" js flexbox flexboxlegacy canvas canvastext webgl no-touch geolocation postmessage websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers applicationcache svg inlinesvg smil svgclippaths" lang="en" data-useragent="Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Foundation Template | So Boxy</title>

    <link rel="stylesheet" href="./static/foundation.css">
    <script src="./static/modernizr.js"></script>
    <script src="./static/google.js"></script>
  <meta class="foundation-data-attribute-namespace"><meta class="foundation-mq-xxlarge"><meta class="foundation-mq-xlarge"><meta class="foundation-mq-large"><meta class="foundation-mq-medium"><meta class="foundation-mq-small"><style></style><meta class="foundation-mq-topbar"><style type="text/css"></style></head>
  

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
	 <img src="/static/banner_v.gif"/>
	</div>
	<div id="middle_content" class="mid">
	<div id="right_side" class="left column_size_25">

	</div>
<div class="row">
    
        <div class="row">
 
          <div class="large-4 columns">
<!--            <img src="http://placehold.it/500x500&text=<%= ((Content) request.getAttribute("content")).getNameKey() %>"><br> -->
			<embed src="<%= ((Content) request.getAttribute("content")).getContentSourceUrl()%>" quality=high pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" type="application/x-shockwave-flash" width="720" height="540"></embed>
 
          </div>
 
 
<!--           <div class="large-6 columns"> -->
 
<!--             <h3 class="show-for-small">Header<hr></h3> -->
 
<!--             <div class="panel"> -->
<!--               <h4 class="hide-for-small">Header<hr></h4> -->
<!--             <h5 class="subheader">Fusce ullamcorper mauris in eros dignissim molestie posuere felis blandit. Aliquam erat volutpat. Mauris ultricies posuere vehicula. Sed sit amet posuere erat. Quisque in ipsum non augue euismod dapibus non et eros. Pellentesque consectetur tempus mi iaculis bibendum. Ut vel dolor sed eros tincidunt volutpat ac eget leo.</h5> -->
<!--             </div> -->

<!-- 					<div class="row"> -->
<!-- 						<div class="large-6 small-6 columns"> -->
<!-- 							<div class="panel"> -->
<!-- 								<h5>Header</h5> -->
<!-- 								<h6 class="subheader">Praesent placerat dui tincidunt elit -->
<!-- 									suscipit sed.</h6> -->
<!-- 								<a href="#" class="small button">BUTTON TIME!</a> -->
<!-- 							</div> -->
<!-- 						</div> -->

<!-- 						<div class="large-6 small-6 columns"> -->
<!-- 							<div class="panel"> -->
<!-- 								<h5>Header</h5> -->
<!-- 								<h6 class="subheader">Praesent placerat dui tincidunt elit -->
<!-- 									suscipit sed.</h6> -->
<!-- 								<a href="#" class="small button">BUTTON TIME!</a> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->

<!-- 				</div> -->
 
        </div>
 
      <!-- End Header Content -->
 
 
      <!-- Search Bar -->
 
<!--         <div class="row"> -->
 
<!--           <div class="large-12 columns"> -->
<!--             <div class="radius panel"> -->
 
<!--             <form> -->
<!--               <div class="row collapse"> -->
 
<!--                 <div class="large-10 small-8 columns"> -->
<!--                   <input type="text"> -->
<!--                 </div> -->
 
<!--                 <div class="large-2 small-3 columns"> -->
<!--                   <a href="#" class="postfix button expand">Search</a> -->
<!--                 </div> -->
 
<!--               </div> -->
<!--             </form> -->
 
<!--           </div> -->
<!--           </div> -->
 
<!--         </div> -->
 
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
  <div id="foot">
	<p>� Copyright Onlinejatek.fnf.hu @ 2014</p>
	</div>
    <script src="./static/jquery.js"></script>
    <script src="./static/foundation.js"></script>
    <script>
      $(document).foundation();

      var doc = document.documentElement;
      doc.setAttribute('data-useragent', navigator.userAgent);
    </script>
  
</body></html>