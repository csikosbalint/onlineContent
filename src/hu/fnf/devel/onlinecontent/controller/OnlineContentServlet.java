package hu.fnf.devel.onlinecontent.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

@SuppressWarnings("serial")
public class OnlineContentServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		HttpSession session = req.getSession(true);
		if (session.getAttribute("cica") == null) {
			session.setAttribute("cica", "kutya");
		}
		log("session: " + session.getId());

		if (datastore.prepare(new Query("Entity")).countEntities(FetchOptions.Builder.withDefaults()) == 0) {
			Vector<String> ents = new Vector<String>();
			ents.add("alma");
			ents.add("korte");
			ents.add("cica");
			ents.add("kutya");

			for (String ent : ents) {
				Entity e = new Entity("Entity", ent);
				String thumbUrl = "http://placehold.it/500x500&text=" + ent;
				String gameUrl = "http://www.freeonlinegames.com/game/heavy-crane-parking";
				e.setProperty("url", gameUrl);
				e.setProperty("thumbnail", thumbUrl);
				e.setProperty("thumb_src", saveBlobFromUrl(thumbUrl));

				datastore.put(e);
			}
		}

		resp.setContentType("text/plain");
		
		List<String> thumbList = new ArrayList<String>();

		for (Entity e : DatastoreServiceFactory.getDatastoreService().prepare(new Query("Entity")).asIterable()) {
			String a = (String) e.getProperty("thumb_src");
			thumbList.add(a);
		}

		RequestDispatcher view = req.getRequestDispatcher("index.jsp");
		req.setAttribute("thumbList", thumbList);

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
