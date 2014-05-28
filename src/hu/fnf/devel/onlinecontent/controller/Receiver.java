package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Content;
import hu.fnf.devel.onlinecontent.model.PMF;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

@SuppressWarnings("deprecation")
public class Receiver extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4652372274004877168L;
	private static PersistenceManager pm = PMF.getInstance().getPersistenceManager();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectInputStream ois = new ObjectInputStream(req.getInputStream());
		try {
			Content content = (Content) ois.readObject();

			log("received: " + content.getNameKey());
			Query q = new Query(Content.class.getSimpleName()).setFilter(FilterOperator.EQUAL.of(
					com.google.appengine.api.datastore.Entity.KEY_RESERVED_PROPERTY,
					KeyFactory.createKey(Content.class.getSimpleName(), content.getNameKey())));

			if (DatastoreServiceFactory.getDatastoreService().prepare(q).asList(FetchOptions.Builder.withDefaults())
					.size() == 0) {
				
				content.setThumbBlobUrl(srcUri(content.getThumbSourceUrl(), content));

				pm.makePersistent(content);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			log("errpost: " + e.getMessage());
		}
	}

	public static String srcUri(String thumbUrl, Content content) {
		if ( thumbUrl == null || !thumbUrl.contains("http") ) {
			return "/static/noimage.gif";
		}
		URL surl;
		FileService fileService = null;
		AppEngineFile file = null;
		FileWriteChannel writeChannel;
		byte[] byteArray;
		try {
			surl = new URL(thumbUrl);

			byteArray = URLFetchServiceFactory.getURLFetchService().fetch(surl).getContent();
			// resize image
			Image image = ImagesServiceFactory.makeImage(byteArray);

			com.google.appengine.api.images.Transform resize = ImagesServiceFactory.makeResize(250, 250);

			Image image250 = ImagesServiceFactory.getImagesService().applyTransform(resize, image);

			fileService = FileServiceFactory.getFileService();
			file = fileService.createNewBlobFile("image/jpg", content.getDisplayName() + " thumb");

			writeChannel = fileService.openWriteChannel(file, true);

			// resized image
			writeChannel.write(ByteBuffer.wrap(image250.getImageData()));
			writeChannel.closeFinally();
			return "/static/serve?blob-key=" + fileService.getBlobKey(file).getKeyString();
		} catch (Exception e) {
			e.printStackTrace();
			return "/static/noimage.gif";
		}
	}
}
