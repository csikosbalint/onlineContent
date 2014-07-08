package hu.fnf.devel.onlinecontent.controller;

import java.io.IOException;
import java.util.Random;

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
//		res.setHeader("Cache-Control", "public, max-age=3600");
		if ( req.getParameter("noimage") != null ) {
			// TODO: create random noimage thumbs
			Random rand = new Random();
			int i = rand.nextInt(OnlineContentServlet.getNoimages().size()-1);
			blobstoreService.serve(new BlobKey("AMIfv94GA4_sky2UoY4rjhD1fJzJAs4afdrLuWHUe0_xvbf7xms_eSMl-5JBCtFdB3klCroEOhwysJ3X0Isqpa_KNczDqd-gGrCtLGQMoUfFH0ChI0vicJlFlxqxgjGknHgW-ycBeydmu9w_tllqfvxMnSqgdCxAmg"), res);
		}
		BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
		blobstoreService.serve(blobKey, res);
	}
}
