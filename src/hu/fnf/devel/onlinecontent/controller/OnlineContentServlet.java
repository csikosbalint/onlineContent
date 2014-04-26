package hu.fnf.devel.onlinecontent.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class OnlineContentServlet extends HttpServlet {
	public static final String LIST = "entitylist";

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession(true);
		if (session.getAttribute("cica") == null) {
			session.setAttribute("cica", "kutya");
		}
		resp.setContentType("text/plain; charset=utf-8");

		List<Entity> entityList = DatastoreServiceFactory.getDatastoreService().prepare(new Query("Entity"))
				.asList(FetchOptions.Builder.withDefaults());
		
		RequestDispatcher view = req.getRequestDispatcher("index.jsp");
		req.setAttribute(OnlineContentServlet.LIST, entityList);

		try {
			view.forward(req, resp);
		} catch (ServletException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
