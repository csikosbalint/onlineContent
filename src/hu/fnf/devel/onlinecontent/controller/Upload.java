package hu.fnf.devel.onlinecontent.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class Upload extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7498345221976837247L;
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);// getUploadedBlobs(req);
		List<BlobKey> bkList = blobs.get("file");
		BlobKey blobKey = bkList.get(0); //only one file

		if (blobKey == null) {
			res.sendRedirect("/");
			log("Blob null");
		} else {
			res.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());
			log("Blob is " + blobKey.getKeyString());
//			res.setContentType("plain/text");
//			res.getWriter().print(blobKey.getKeyString());
		}
	}
}
