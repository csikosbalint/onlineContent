<%@page language="java" %>
<%@page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>

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
	<input type="hidden" name="createCategory" value="true" /> <input
		type="text" name="categoryName"></input> <input type="text"
		name="categoryKeywords">
	<button type="submit">Add category</button>
</form>
<form action="/admin/servlet">
	<input type="hidden" name="modifyCategory" value="true" /> <input
		type="text" name="categoryName" /> <input type="text"
		name="addCategoryKeywords" />
	<button type="submit">Add keyword(s)</button>
</form>
<form action="/admin/servlet">
	<input type="hidden" name="createLanguageEntry" value="true" /> <input
		type="text" name="languageName" /> <input type="text" name="langKey" />
	<input type="text" name="textValue" />
	<button type="submit">Add translation</button>
</form>
<form action="<%= BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/upload") %>" method="post"
	enctype="multipart/form-data">
	<input type="hidden" name="manualUpload" value="true"/> <input type="file"
		name="file"> <input type="submit" value="Upload">
</form>
</html>