<%@page language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="view" uri="WEB-INF/view.tld" %>
<%@page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach" %>
<%@page import="java.lang.*" %>
<%@page import="java.util.*" %>
<html>
<!-- Use of this code assumes agreement with the Google Custom Search Terms of Service. -->
<!-- The terms of service are available at http://www.google.com/cse/docs/tos.html -->
<form name="cse" id="searchbox_demo" action="http://www.google.com/cse">
	<input type="hidden" name="cref" value="" /> <input type="hidden"
		name="ie" value="utf-8" /> <input type="hidden" name="hl" value="" />
	<input name="q" type="text" size="40" /> <input type="submit"
		name="sa" value="Search" />
</form>
<script type="text/javascript"
	src="https://www.google.com/cse/publicurl?cx=004811520739431370780:ggegf7qshxe"></script>
<form action="/admin/servlet">
    <h3>createCategory</h3>
	<input type="hidden" name="createCategory" value="true" /> </br> categoryName <input
		type="text" name="categoryName"></input> </br> categoryKeywords <input type="text"
		name="categoryKeywords">
	<button type="submit">Add category</button>
</form>
<form action="/admin/servlet">
    <h3>modifyCategory</h3>
	<input type="hidden" name="modifyCategory" value="true" /> </br> categoryName <input
		type="text" name="categoryName" /> </br> CategoryKeywords <input type="text"
		name="addCategoryKeywords" />
	<button type="submit">Add keyword(s)</button>
</form>
<form action="/admin/servlet">
    <h3>createLanguageEntry</h3>
	<input type="hidden" name="createLanguageEntry" value="true" /> languageName <input
		type="text" name="languageName" /> </br> langKey <input type="text" name="langKey" /> </br> textValue 
	<input type="text" name="textValue" />
	<button type="submit">Add translation</button>
</form>
<form action="<%= BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/upload") %>" method="post"
	enctype="multipart/form-data">
	<h3>uploadThumb</h3>
	<input type="hidden" name="manualUpload" value="true"/> <input type="file"
		name="file"> <input type="submit" value="Upload">
</form>
<form action="/admin/servlet">
    <h3>changeCronState</h3>
    <input type="hidden" name="changeCronState" value="true" />
    CRON state: <b><%= request.getAttribute("cronState") %></b></br>
    <button type="submit">Change cron state</button>
    (click on Change if this is null)
</form>
<form action="/admin/servlet">
    <input type="text" name="setHourlyLoadContent" value="<%= request.getAttribute("HOURLY_LOAD_CONTENT") %>" />
    <button type="submit">Set Hourly Load Content</button>
</form>
<form action="/admin/servlet">
    <h3>reloadCategories</h3>
    <input type="text" name="reloadCategories" value="<%= request.getAttribute("HOURLY_LOAD_CONTENT") %>" />
    <button type="submit">Hourly reLoad categories</button>
</form>
</html>