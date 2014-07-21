package hu.fnf.devel.onlinecontent.controller;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class Serve extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3851971972841879503L;
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// res.setHeader("Cache-Control", "public, max-age=3600");
		if (req.getParameter("noimage") != null) {
			// TODO: create random noimage thumbs
			//blobstoreService.serve(new BlobKey("1092OaDyeuZuosiC1cmKKA"), res);
			res.sendRedirect("http://placehold.it/250x180&text=" + req.getParameter("noimage"));
			return;
		}
		blobstoreService.serve(new BlobKey(req.getParameter("blob-key")), res);
	}
}
