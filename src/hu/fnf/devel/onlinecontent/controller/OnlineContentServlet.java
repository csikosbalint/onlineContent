package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Content;
import hu.fnf.devel.onlinecontent.model.PMF;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
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

@SuppressWarnings("serial")
public class OnlineContentServlet extends HttpServlet {

	private static final String LIST = "entityList";
	private static final String LISTSIZE = "listSize";
	private static final String PAGEACTUAL = "pageActual";
	private static final int pageSize = 12;
	
	
	private static PersistenceManager pm = PMF.getInstance().getPersistenceManager();
	@SuppressWarnings("unchecked")
	private static TreeSet<Content> list = new TreeSet<>((List<Content>)pm.newQuery(Content.class).execute());

	private static int classCount = 0; // shared by all instances
	private static Hashtable<HttpServlet, HttpServlet> instances = new Hashtable<HttpServlet, HttpServlet>(); // also shared
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		int count = 0; // separate for each servlet
		int pageActual = req.getParameter("page") == null ? 1 : Integer.valueOf(req.getParameter("page"));
		HttpSession session = req.getSession(true);
		if (session.getAttribute("cica") == null) {
			session.setAttribute("cica", "kutya");
		}
		resp.setContentType("text/plain; charset=utf-8");
		PrintWriter out = resp.getWriter();

		count++;
		instances.put(this, this);
		classCount++;
		if (req.getParameter("debug") != null ) {
			out.println("<pre>There are currently " + instances.size() + " instances.</pre>");
			out.println("<pre>Instance has been accessed " + count + " times.?</pre>");
			out.println("<pre>Class has been accessed " + classCount + " times.</pre>");
			out.println("<pre>PageSize = " + pageSize + " </pre>");
		}
		if (req.getParameter("force-reload") != null ) {
			list = new TreeSet<Content>((List<Content>)pm.newQuery(Content.class).execute());
		}
		RequestDispatcher view;
		if ( req.getParameter("contentname") != null ) {
			view = req.getRequestDispatcher("entity.jsp");
			Content content = pm.getObjectById(Content.class, req.getParameter("contentname"));
			req.setAttribute("content", content);
		} else {
			// got to the current element
			Iterator<Content> it = list.iterator();
			
			for ( int i = 0; i<(pageSize*(pageActual-1)) ; i++ ) {
				it.next();
			}
			// create subList from pageSize*pageActual to  pageSize*(pageActual+1)
			TreeSet<Content> contents  = new TreeSet<>();
			for ( int i = 0; i < pageSize; i++) {
				if ( it.hasNext() ) {
					Content content = it.next();
					if ( content.getThumbBlobUrl() == null ) {
						content.setThumbBlobUrl("static/noimage.gif");
					}
					contents.add(content);
				}
			}
			view = req.getRequestDispatcher("index.jsp");
			req.setAttribute(OnlineContentServlet.LIST, contents);
			req.setAttribute(OnlineContentServlet.LISTSIZE, (list.size()/pageSize)+1);
			req.setAttribute(OnlineContentServlet.PAGEACTUAL, pageActual);
		}
		
		try {
			view.forward(req, resp);
		} catch (ServletException e1) {
			e1.printStackTrace();
		}
	}
}
