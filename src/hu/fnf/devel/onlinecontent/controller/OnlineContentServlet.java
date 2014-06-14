package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Category;
import hu.fnf.devel.onlinecontent.model.Content;
import hu.fnf.devel.onlinecontent.model.PMF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.Customsearch.Cse.List;
import com.google.api.services.customsearch.model.Search;

@SuppressWarnings("serial")
public class OnlineContentServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(Content.class.getName());
	private static final String LIST = "entityList";
	private static final String LISTSIZE = "listSize";
	private static final String PAGEACTUAL = "pageActual";
	private static final int PAGESIZE = 12;
	public static boolean search = true;

	private static PersistenceManager pm = PMF.getInstance().getPersistenceManager();
	@SuppressWarnings("unchecked")
	private static TreeSet<Content> list = new TreeSet<>((java.util.List<Content>) pm.newQuery(Content.class).execute());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getParameter("user").equals("admin") && req.getParameter("pass").equals("Macska8")) {
			HttpSession session = req.getSession(true);
			if (session.getAttribute("admin") == null) {
				session.setAttribute("admin", "admin: " + req.getRemoteAddr());
				log.warning("login: " + session.getAttribute("admin"));
			}
		}
		resp.sendRedirect("/?admin");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain; charset=utf-8");
		int pageActual = req.getParameter("page") == null ? 1 : Integer.valueOf(req.getParameter("page"));
		/*
		 * Session
		 */
		RequestDispatcher view;
		HttpSession session = req.getSession(true);

		if (session.getAttribute("admin") != null) {
			try {
				Category t = (Category) pm.getObjectById(Category.class, "Dance");
				Content c = t.getMembers2().get(0);
				System.out.println(c.getDisplayName());
			} catch (Exception e) {
				log.warning("no such element");
			}

			/*
			 * Admin functions
			 */
			if (req.getParameter("resetContent") != null) {
				resetContent(req.getParameter("resetContent"));
			}
			if (req.getParameter("forceReload") != null) {
				forceReload();
			}
			if (req.getParameter("changeAndSearch") != null) {
				changeAndSearch(req.getParameter("searchKeyWords"), req.getParameter("contentname"));
			}
			if (req.getParameter("createCategory") != null) {
				createCategory(req.getParameter("categoryName"), req.getParameter("categoryKeywords"));
			}
			if (req.getParameter("reCalculateContentArgs") != null) {
				reCalculateContentArgs();
			}
		}
		req.setAttribute("session", session);

		if (req.getParameter("contentname") != null) {
			view = req.getRequestDispatcher("entity.jsp");
			Content content = pm.getObjectById(Content.class, req.getParameter("contentname"));
			req.setAttribute("content", content);
		} else if (req.getParameter("admin") != null) {
			view = req.getRequestDispatcher("admin.jsp");
		} else {
			// got to the current element
			Iterator<Content> it = list.iterator();

			for (int i = 0; i < (PAGESIZE * (pageActual - 1)); i++) {
				it.next();
			}
			// create subList from pageSize*pageActual to
			// pageSize*(pageActual+1)
			TreeSet<Content> contents = new TreeSet<>();
			for (int i = 0; i < PAGESIZE; i++) {
				if (it.hasNext()) {
					Content content = it.next();
					contents.add(content);
				}
			}
			view = req.getRequestDispatcher("index.jsp");
			req.setAttribute(OnlineContentServlet.LIST, contents);
			req.setAttribute(OnlineContentServlet.LISTSIZE, (list.size() / PAGESIZE) + 1);
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
		content.setThumbBlobUrl("/static/noimage.gif");
	}

	@SuppressWarnings("unchecked")
	private void forceReload() {
		list = new TreeSet<Content>((java.util.List<Content>) pm.newQuery(Content.class).execute());
		log.info(list.size() + " elements have been reloaded!");
	}

	private void resetContent(String parameter) {
		// TODO Auto-generated method stub

	}

	public static String searchThumbnail(Content content) {
		if (OnlineContentServlet.search) {
			log.info("searching...(only once)");
			Customsearch thumbSearch = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), null)
					.setApplicationName("ThumbSearch").build();
			try {
				StringBuffer searchKeyWords = new StringBuffer();
				if (content.getSearchKeyWords().size() == 0) {
					searchKeyWords.append(content.getDisplayName());
				} else {
					for (String str : content.getSearchKeyWords()) {
						searchKeyWords.append(str + " ");
					}
				}

				searchKeyWords.append("online game");
				System.out.println("search keywords: " + (searchKeyWords));
				List l = thumbSearch.cse().list(searchKeyWords.toString());
				l.setCx("004811520739431370780:ggegf7qshxe");
				l.setSafe("high");
				l.setFilter("1");
				l.setSearchType("image");
				// l.setImgSize("large");
				l.setKey("AIzaSyDiZfaoVfU5FeORRwSuvBC3tk1UJQ5N-XI");

				Search imgResult = l.execute();
				System.out.println("result: " + imgResult.toPrettyString());
				if (imgResult.getItems() != null && imgResult.getItems().size() != 0) {
					// thumbsrc
					return imgResult.getItems().get(0).getLink();
				} else {
					log.warning("search was not successful: " + content.getNameKey());
					log.warning(imgResult.getSearchInformation().toPrettyString());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		log.warning("Useing default thumbnail for: " + content.getNameKey());
		return "/static/noimage.gif";
	}

	public static void createCategory(String categoryName, String words) {
		java.util.List<String> keyWords = new ArrayList<String>();
		for (String word : words.split(",")) {
			keyWords.add(word);
		}
		Category category = new Category(categoryName, keyWords);
		pm.makePersistent(category);

		Content c = (Content) pm.getObjectById(Content.class, "tánc_stúdió_boogy_bash");

		category.addMember(c.getNameKey());
		category.addMember2(c);

		c.addCategory(category);

	}
	
	private void reCalculateContentArgs() {
		
	}
	
}
