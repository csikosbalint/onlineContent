package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Content;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;

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

public class Receiver extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4652372274004877168L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectInputStream ois = new ObjectInputStream(req.getInputStream());
		try {
			Content e = (Content) ois.readObject();
			log("received: " + e.getNameKey());
			Query q = new Query("Entity").setFilter(FilterOperator.EQUAL.of(
					com.google.appengine.api.datastore.Entity.KEY_RESERVED_PROPERTY,
					KeyFactory.createKey("Entity", e.getNameKey())));
			
			if (DatastoreServiceFactory.getDatastoreService().prepare(q).asList(FetchOptions.Builder.withDefaults())
					.size() == 0) {
				com.google.appengine.api.datastore.Entity entity = new com.google.appengine.api.datastore.Entity(
						"Entity", e.getNameKey());
				for (Map.Entry<String, Object> row : e.getAttributes().entrySet()) {
					entity.setProperty(row.getKey(), row.getValue());
				}
				if (entity.getProperty(Content.THUMBNAIL_SOURCE_URL) == null) {
					entity.setProperty(Content.THUMBNAIL_STORED_URL,
							saveBlobFromUrl(null));
				} else {
					entity.setProperty(Content.THUMBNAIL_STORED_URL,
							saveBlobFromUrl((String) entity.getProperty(Content.THUMBNAIL_SOURCE_URL)));
				}
				DatastoreServiceFactory.getDatastoreService().put(entity);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			log("errpost: " + e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	private String saveBlobFromUrl(String thumbUrl) {
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
			
			com.google.appengine.api.images.Transform resize = ImagesServiceFactory.makeResize(500, 500);
			
			Image image500 = ImagesServiceFactory.getImagesService().applyTransform(resize, image);
			
			fileService = FileServiceFactory.getFileService();
			file = fileService.createNewBlobFile("image/jpg");
			writeChannel = fileService.openWriteChannel(file, true);

			// resized image
			writeChannel.write(ByteBuffer.wrap(image500.getImageData()));
			writeChannel.closeFinally();
			return "/serve?blob-key=" + fileService.getBlobKey(file).getKeyString();
		} catch (Exception e) {
			e.printStackTrace();
			return "/static/noimage.gif";
		}
	}
}
