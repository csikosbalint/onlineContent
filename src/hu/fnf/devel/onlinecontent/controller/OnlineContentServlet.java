package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Category;
import hu.fnf.devel.onlinecontent.model.Content;
import hu.fnf.devel.onlinecontent.model.Language;
import hu.fnf.devel.onlinecontent.model.PMF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
import com.google.apphosting.api.ApiProxy.OverQuotaException;

@SuppressWarnings("serial")
public class OnlineContentServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(Content.class.getName());
	private static final String LIST = "entityList";
	private static final String LISTSIZE = "listSize";
	private static final String PAGEACTUAL = "pageActual";
	private static final int PAGESIZE = 12;

	private static PersistenceManager pm;
	private static TreeSet<Content> list;
	private static Map<String, Language> translations;

	public OnlineContentServlet() {
		initMemory();
	}

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
		try {
			HttpSession session = req.getSession(false);

			if (session != null && session.getAttribute("admin") != null) {
				/*
				 * Admin functions
				 */
				log.fine("admin: " + req.toString());
				if (req.getParameter("resetContent") != null) {
					resetContent(req.getParameter("resetContent"));
				}
				if (req.getParameter("forceReload") != null) {
					initMemory();
				}
				if (req.getParameter("changeAndSearch") != null) {
					searchAndChange(req.getParameter("searchKeyWords"), req.getParameter("contentname"));
				}
				if (req.getParameter("createCategory") != null) {
					createCategory(req.getParameter("categoryName"), req.getParameter("categoryKeywords"));
				}
				if (req.getParameter("reCalculateContentArgs") != null) {
					reCalculateContentArgs();
				}
				if (req.getParameter("createLanguageEntry") != null) {
					createLanguageEntry(req.getParameter("languageName"), req.getParameter("langKey"),
							req.getParameter("textValue"));
				}
				req.setAttribute("session", session);
				// reinit
				initMemory();
			}
		} catch (OverQuotaException e) {
			req.setAttribute("session", null);
		}
		if (req.getParameter("contentname") != null) {
			view = req.getRequestDispatcher("entity.jsp");
			Content content = OnlineContentServlet.searchContent(req.getParameter("contentname"));
			content.incViewCount();
			req.setAttribute("content", content);
		} else if (req.getParameter("admin") != null || req.getParameter("createLanguageEntry") != null
				|| req.getParameter("createCategory") != null) {
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

	private static Content searchContent(String nameKey) {
		for (Content content : list) {
			if (content.getNameKey().getName().equals(nameKey)) {
				return content;
			}
		}
		return null;
	}

	private void searchAndChange(String searchKeyWords, String nameKey) {
		Content content = OnlineContentServlet.searchContent(nameKey);
		content.setSearchKeyWords(new ArrayList<String>(Arrays.asList(searchKeyWords.split(" "))));
		content.setThumbBlobUrl("/static/noimage.gif");
	}

	@SuppressWarnings("unchecked")
	private void initMemory() {
		pm = PMF.getInstance().getPersistenceManager();

		list = new TreeSet<>((java.util.List<Content>) pm.newQuery(Content.class).execute());
		log.info(list.size() + " elements have been reloaded!");

		translations = new HashMap<String, Language>();
		for (Language language : (java.util.List<Language>) pm.newQuery(Language.class).execute()) {
			translations.put(language.getNameKey().getName(), language);
		}
		log.info(translations.size() + " translations have been reloaded!");
	}

	private void resetContent(String parameter) {
		// TODO Auto-generated method stub

	}

	/*
	 * STATIC CALLS
	 */

	public static Map<String, Language> getTranslations() {
		return translations;
	}

	public static String searchThumbnail(Content content) {
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
			log.warning("search keywords: " + (searchKeyWords));
			List l = thumbSearch.cse().list(searchKeyWords.toString());
			l.setCx("004811520739431370780:ggegf7qshxe");
			l.setSafe("high");
			l.setFilter("1");
			l.setSearchType("image");
			// l.setImgSize("large");
			l.setKey("AIzaSyDiZfaoVfU5FeORRwSuvBC3tk1UJQ5N-XI");

			Search imgResult = l.execute();
			log.warning("result: " + imgResult.toPrettyString());
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

		log.warning("Useing default thumbnail for: " + content.getNameKey());
		return "/static/noimage.gif";
	}

	private void createLanguageEntry(String nameKey, String langKey, String textValue) {
		Language language = null;
		nameKey = nameKey.replace(' ', '_');
		try {
			language = pm.getObjectById(Language.class, nameKey);
			log.fine("language(" + nameKey + ") has been found.");
		} catch (Exception e) {
			language = new Language(nameKey);
			log.fine("new language(" + nameKey + ") created.");
		}
		language.addTranslation(langKey, textValue);
		pm.makePersistent(language);
	}

	public static void createCategory(String nameKey, String words) {
		Category category = null;
		java.util.List<String> keyWords = new ArrayList<String>();
		for (String word : words.split(",")) {
			keyWords.add(word);
		}
		try {
			category = pm.getObjectById(Category.class, nameKey);
		} catch (Exception e) {
			category = new Category(nameKey);
		}
		category.setKeyWords(keyWords);
		pm.makePersistent(category);
	}

	private void reCalculateContentArgs() {

	}

	@SuppressWarnings("unchecked")
	public static Set<Category> searchCategories(Content content) {
		Set<Category> ret = new HashSet<Category>();
		try {
			for (Category category : (java.util.List<Category>) pm.newQuery(Category.class).execute()) {
				for (String keyword : category.getKeyWords()) {
					if (content.getDisplayName().toLowerCase().contains(keyword)) {
						category.addMember(content);
						ret.add(category);
						log.fine(category.getNameKey().getName() + " has been added to " + content.getDisplayName());
						break;
					}
				}
			}
		} catch (OverQuotaException e) {
			log.warning("datastore read quota exceeded!");
		}
		return ret;
	}
}
/*
 * kutya
*/
