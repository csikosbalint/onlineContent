<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload</title>
</head>
<body>

	<form enctype="multipart/form-data" method="post"
		action="<%=BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/upload")%>">
		<input type="file" name="file" size="30" /> <input type="submit" />
	</form>

</body>
</html>