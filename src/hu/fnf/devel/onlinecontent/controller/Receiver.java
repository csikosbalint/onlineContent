package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Entity;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class Receiver extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4652372274004877168L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log("dopost");
		ObjectInputStream ois = new ObjectInputStream(req.getInputStream());
		try {
			Entity e = (Entity)ois.readObject();
			log("received: " + e.getCica());
			System.out.println("received: " + e.getCica());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log("errpost: " + e.getMessage());
		}
		log("endpost");
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
