package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Content;
import hu.fnf.devel.onlinecontent.model.PMF;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

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
	private static final Logger log = Logger.getLogger(Content.class.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectInputStream ois = new ObjectInputStream(req.getInputStream());
		try {
			Content content = (Content) ois.readObject();

			log("received: " + content.getDisplayName());
			content.setNameKey(KeyFactory.createKey(Content.class.getSimpleName(), content.getDisplayName()
					.toLowerCase().replace(' ', '_')));

			Query q = new Query(Content.class.getSimpleName()).setFilter(FilterOperator.EQUAL.of(
					com.google.appengine.api.datastore.Entity.KEY_RESERVED_PROPERTY, content.getNameKey()));

			if (DatastoreServiceFactory.getDatastoreService().prepare(q).asList(FetchOptions.Builder.withDefaults())
					.size() == 0) {

				content.setThumbBlobUrl(srcUri(content.getThumbSourceUrl(), content));

				pm.makePersistent(content);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			log.warning("errpost: " + e.getMessage());
		}
	}
//	if (thumbLocaleUrl == null || thumbLocaleUrl.contains("noimage")) {
//		String thumbnail = OnlineContentServlet.searchThumbnail(this);
//		thumbLocaleUrl = Receiver.srcUri(thumbnail, this);
//	}

	public static String srcUri(String thumbUrl, Content content) {
		if (thumbUrl == null || !thumbUrl.contains("http")) {
			return "/static/serve?noimage";
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
			return ("/static/serve?noimage=" + content.getNameKey().getName());
		}
	}
}
