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
<!-- saved from url=(0046)http://foundation.zurb.com/templates/boxy.html -->
<html class=" js flexbox flexboxlegacy canvas canvastext webgl no-touch geolocation postmessage websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers applicationcache svg inlinesvg smil svgclippaths" lang="en" data-useragent="Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Foundation Template | So Boxy</title>

    
    <meta name="description" content="Documentation and reference library for ZURB Foundation. JavaScript, CSS, components, grid and more.">
    
    <meta name="author" content="ZURB, inc. ZURB network also includes zurb.com">
    <meta name="copyright" content="ZURB, inc. Copyright (c) 2013">

    <link rel="stylesheet" href="http://foundation.zurb.com/assets/css/templates/foundation.css">
    <script src="./static/modernizr.js"></script>
    <script src="./static/google.js"></script>
  <meta class="foundation-data-attribute-namespace"><meta class="foundation-mq-xxlarge"><meta class="foundation-mq-xlarge"><meta class="foundation-mq-large"><meta class="foundation-mq-medium"><meta class="foundation-mq-small"><style></style><meta class="foundation-mq-topbar"><style type="text/css"></style></head>
  

 <body>
    

 <div class="row">
    <div class="large-12 columns">
  	<!-- NAV -->
  	<div class="nav_top">
  	
  	
  	
  	</div>
    <!-- NAV -->
<div class="row">
      <div class="large-12 columns">
        <nav class="top-bar" data-topbar="">
          <ul class="title-area">
            <!-- Title Area -->

            <li class="name">
              <h1><a href="#">Top Bar Title</a></h1>
            </li>

            <li class="toggle-topbar menu-icon">
              <a href="#"><span>menu</span></a>
            </li>
          </ul>

          
        <section class="top-bar-section">
            <!-- Right Nav Section -->

            <ul class="right">
              <li class="divider"></li>

              <li class="has-dropdown not-click">
                <a href="#">Main Item 1</a>

                <ul class="dropdown"><li class="title back js-generated"><h5><a href="javascript:void(0)">Back</a></h5></li>
                  <li><label>Section Name</label></li>

                  <li class="has-dropdown not-click">
                    <a class="" href="#">Has Dropdown, Level 1</a>

                    <ul class="dropdown"><li class="title back js-generated"><h5><a href="javascript:void(0)">Back</a></h5></li>
                      <li>
                        <a href="#">Dropdown Options</a>
                      </li>

                      <li>
                        <a href="#">Dropdown Options</a>
                      </li>

                      <li>
                        <a href="#">Level 2</a>
                      </li>

                      <li>
                        <a href="#">Subdropdown Option</a>
                      </li>

                      <li>
                        <a href="#">Subdropdown Option</a>
                      </li>

                      <li>
                        <a href="#">Subdropdown Option</a>
                      </li>
                    </ul>
                  </li>

                  <li>
                    <a href="#">Dropdown Option</a>
                  </li>

                  <li>
                    <a href="#">Dropdown Option</a>
                  </li>

                  <li class="divider"></li>

                  <li><label>Section Name</label></li>

                  <li>
                    <a href="#">Dropdown Option</a>
                  </li>

                  <li>
                    <a href="#">Dropdown Option</a>
                  </li>

                  <li>
                    <a href="#">Dropdown Option</a>
                  </li>

                  <li class="divider"></li>

                  <li>
                    <a href="#">See all →</a>
                  </li>
                </ul>
              </li>

              <li class="divider"></li>

              <li>
                <a href="#">Main Item 2</a>
              </li>

              <li class="divider"></li>

              <li class="has-dropdown not-click">
                <a href="#">Main Item 3</a>

                <ul class="dropdown"><li class="title back js-generated"><h5><a href="javascript:void(0)">Back</a></h5></li>
                  <li>
                    <a href="#">Dropdown Option</a>
                  </li>

                  <li>
                    <a href="#">Dropdown Option</a>
                  </li>

                  <li>
                    <a href="#">Dropdown Option</a>
                  </li>

                  <li class="divider"></li>

                  <li>
                    <a href="#">See all →</a>
                  </li>
                </ul>
              </li>
            </ul>
          </section></nav><!-- End Top Bar -->
      </div>
    </div>
        <div class="row">
 
          <div class="large-6 columns">
 
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
 
        <div class="row">
 
          <div class="large-12 show-for-small columns">
          <h3>Header</h3><hr>
        </div>
 
          <div class="large-3 small-6 columns">
            <img src="./static/Thumbnail500.jpg">
            <div class="panel">
              <p>Description</p>
            </div>
          </div>
 
          <div class="large-3 small-6 columns">
            <img src="./static/Thumbnail500.jpg">
            <div class="panel">
              <p>Description</p>
            </div>
          </div>
 
          <div class="large-3 small-6 columns">
            <img src="./static/Thumbnail500.jpg">
            <div class="panel">
              <p>Description</p>
            </div>
          </div>
 
          <div class="large-3 small-6 columns">
            <img src="./static/Thumbnail500.jpg">
            <div class="panel">
              <p>Description</p>
            </div>
          </div>
 
        </div>
 
      <!-- End Thumbnails -->
 
 
      <!-- Footer -->
 
        <footer class="row">
        <div class="large-12 columns"><hr>
            <div class="row">
 
              <div class="large-6 columns">
                  <p>© Copyright no one at all. Go to town.</p>
              </div>
 
              <div class="large-6 columns">
                  <ul class="inline-list right">
                    <li><a href="#">Link 1</a></li>
                    <li><a href="#">Link 2</a></li>
                    <li><a href="#">Link 3</a></li>
                    <li><a href="#">Link 4</a></li>
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
  
</body></html>