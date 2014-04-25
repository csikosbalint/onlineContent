package hu.fnf.devel.onlinecontent.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tools.ant.taskdefs.Filter;

import com.google.appengine.api.ThreadManager;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

@SuppressWarnings("serial")
public class OnlineContentServlet extends HttpServlet {
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public OnlineContentServlet() {
		Vector<String> ents = new Vector<String>();
		ents.add("alma");
		ents.add("korte");
		ents.add("cica");
		ents.add("kutya");
		ents.add("kata");
		ents.add("fel");
		ents.add("kelt");
		
		for (String ent : ents) {
			Query q = new Query("Entity").addFilter(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL,KeyFactory.createKey("Entity", ent));
			if ( datastore.prepare(q).asList(FetchOptions.Builder.withDefaults()).size() == 0 ) {
				Entity e = new Entity("Entity", ent);
				String thumbUrl = "http://placehold.it/500x500&text=" + ent;
				String gameUrl = "http://www.freeonlinegames.com/game/heavy-crane-parking";
				e.setProperty("url", gameUrl);
				e.setProperty("thumbnail", thumbUrl);
				e.setProperty("thumb_src", saveBlobFromUrl(thumbUrl));

				datastore.put(e);
			}
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		

		HttpSession session = req.getSession(true);
		if (session.getAttribute("cica") == null) {
			session.setAttribute("cica", "kutya");
		}
		

		resp.setContentType("text/plain");

		List<Entity> entityList = DatastoreServiceFactory.getDatastoreService().prepare(new Query("Entity"))
				.asList(FetchOptions.Builder.withDefaults());
		
		RequestDispatcher view = req.getRequestDispatcher("index.jsp");
		req.setAttribute("entities", entityList);

		try {
			view.forward(req, resp);
		} catch (ServletException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private String saveBlobFromUrl(String thumbUrl) {
		URL surl;
		FileService fileService = null;
		AppEngineFile file = null;
		FileWriteChannel writeChannel;
		byte[] byteArray;
		try {
			surl = new URL(thumbUrl);

			byteArray = URLFetchServiceFactory.getURLFetchService().fetch(surl).getContent();

			fileService = FileServiceFactory.getFileService();

			file = fileService.createNewBlobFile("image/jpg");
			log("id: " + file.getFullPath());

			writeChannel = fileService.openWriteChannel(file, true);

			writeChannel.write(ByteBuffer.wrap(byteArray));
			writeChannel.closeFinally();
			return "/serve?blob-key=" + fileService.getBlobKey(file).getKeyString();
		} catch (Exception e) {
			e.printStackTrace();
			return "/static/noimage.gif";
		}
	}
}
