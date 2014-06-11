package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Content;
import hu.fnf.devel.onlinecontent.model.PMF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.logging.Logger;


@SuppressWarnings("serial")
public class OnlineContentServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(Content.class.getName());
	private static final String LIST = "entityList";
	private static final String LISTSIZE = "listSize";
	private static final String PAGEACTUAL = "pageActual";
	private static final int pageSize = 12;
	public static boolean search = false;
	
	private static PersistenceManager pm = PMF.getInstance().getPersistenceManager();
	@SuppressWarnings("unchecked")
	private static TreeSet<Content> list = new TreeSet<>((List<Content>) pm.newQuery(Content.class).execute());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getParameter("user").equals("admin") && req.getParameter("pass").equals("Macska8")) {
			HttpSession session = req.getSession(true);
			if (session.getAttribute("admin") == null) {
				session.setAttribute("admin", "admin: " + req.getRemoteAddr());
				log.warning("login: " + session.getAttribute("admin") );
			}
		}
		resp.sendRedirect("/?admin");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain; charset=utf-8");
		int pageActual = req.getParameter("page") == null ? 1 : Integer.valueOf(req.getParameter("page"));
		/*
		 * Session
		 */
		RequestDispatcher view;
		HttpSession session = req.getSession(true);
		if (session.getAttribute("admin") != null) {
			/*
			 * Admin functions
			 */
			if (req.getParameter("resetContent") != null) {
				resetContent(req.getParameter("resetContent"));
			}
			if (req.getParameter("forceReload") != null) {
				forceReload();
			}
			if ( req.getParameter("changeAndSearch") != null ) {
				changeAndSearch(req.getParameter("searchKeyWords"), req.getParameter("contentname"));
			}
		}
		req.setAttribute("session", session);

		if (req.getParameter("contentname") != null) {
			view = req.getRequestDispatcher("entity.jsp");
			Content content = pm.getObjectById(Content.class, req.getParameter("contentname"));
			req.setAttribute("content", content);
		} else if ( req.getParameter("admin") != null ) {
			view = req.getRequestDispatcher("admin.jsp");
		} else {
			// got to the current element
			Iterator<Content> it = list.iterator();

			for (int i = 0; i < (pageSize * (pageActual - 1)); i++) {
				it.next();
			}
			// create subList from pageSize*pageActual to
			// pageSize*(pageActual+1)
			TreeSet<Content> contents = new TreeSet<>();
			for (int i = 0; i < pageSize; i++) {
				if (it.hasNext()) {
					Content content = it.next();
					contents.add(content);
				}
			}
			view = req.getRequestDispatcher("index.jsp");
			req.setAttribute(OnlineContentServlet.LIST, contents);
			req.setAttribute(OnlineContentServlet.LISTSIZE, (list.size() / pageSize) + 1);
			req.setAttribute(OnlineContentServlet.PAGEACTUAL, pageActual);
		}

		try {
			view.forward(req, resp);
		} catch (ServletException e1) {
			e1.printStackTrace();
		}
	}

	private void changeAndSearch(String searchKeyWords, String nameKey) {
		Content content = pm.getObjectById(Content.class, nameKey);
		content.setSearchKeyWords(new ArrayList<String>(Arrays.asList(searchKeyWords.split(" "))));
	}

	@SuppressWarnings("unchecked")
	private void forceReload() {
		list = new TreeSet<Content>((List<Content>) pm.newQuery(Content.class).execute());
		log.info(list.size() + " elements have been reloaded!");
	}

	private void resetContent(String parameter) {
		// TODO Auto-generated method stub

	}
}
