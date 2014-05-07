package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Content;
import hu.fnf.devel.onlinecontent.model.PMF;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class OnlineContentServlet extends HttpServlet {
	private static final String LIST = "entityList";
	private static final String LISTSIZE = "listSize";
	private static final int pageSize = 15;
	private static PersistenceManager pm = PMF.get().getPersistenceManager();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession(true);
		if (session.getAttribute("cica") == null) {
			session.setAttribute("cica", "kutya");
		}
		resp.setContentType("text/plain; charset=utf-8");
		
		@SuppressWarnings("unchecked")
		List<Content> list = (List<Content>) pm.newQuery(Content.class).execute();
		int listSize = (list.size()/pageSize)+1;
		if ( list.size() > pageSize ) {
			String pageNum = req.getParameter("page") == null ? "1" : req.getParameter("page");
			int page = Integer.parseInt(pageNum);
			int start = -1 + page * pageSize;
			if ( page * pageSize > list.size() ) {
				start = 0;
			}
			int stop = start+pageSize > list.size() ? list.size() : start+pageSize;
			list = list.subList(start, stop);
		}
		
		RequestDispatcher view = req.getRequestDispatcher("index.jsp");
		req.setAttribute(OnlineContentServlet.LIST, list);
		req.setAttribute(OnlineContentServlet.LISTSIZE, listSize);

		try {
			view.forward(req, resp);
		} catch (ServletException e1) {
			e1.printStackTrace();
		}
	}
}
