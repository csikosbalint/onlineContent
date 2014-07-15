package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Category;
import hu.fnf.devel.onlinecontent.model.Content;
import hu.fnf.devel.onlinecontent.model.Language;
import hu.fnf.devel.onlinecontent.model.PMF;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.Customsearch.Cse.List;
import com.google.api.services.customsearch.model.Search;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.apphosting.api.ApiProxy.OverQuotaException;

@SuppressWarnings("serial")
public class OnlineContentServlet extends HttpServlet implements Observer {
	private static final Logger log = Logger.getLogger(Content.class.getName());
	private static final String LIST = "entityList";
	private static final String CONTENT = "content";
	private static final String LISTSIZE = "listSize";
	private static final String PAGEACTUAL = "pageActual";
	private static final int PAGESIZE = 11;
	
//	private static PersistenceManager pm;
	private static java.util.Set<Content> sortedContents;
	private static Map<String, Content> contents;
	private static Map<String, Language> languages;
	private static Map<String, Category> categories;
	private static Map<String, BlobKey> noimages;
	
	public OnlineContentServlet() {
		log.info("Servlet init.");
		sortedContents = new TreeSet<>();
		contents = new HashMap<>();
		languages = new HashMap<>();
		categories = new HashMap<>();
		noimages = new HashMap<>();
		
		OnlineContentAdminServlet.addObserver(this);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO: make upload url secure
		resp.sendRedirect("/");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain; charset=utf-8");
		int pageActual = req.getParameter("page") == null ? 1 : Integer.valueOf(req.getParameter("page"));

		RequestDispatcher view;

		if (req.getParameter("contentname") != null) {
			Content content = contents.get(req.getParameter("contentname"));
			java.util.Set<Content> recommendations = OnlineContentServlet.getRecommendation(3, content);

			view = req.getRequestDispatcher("entity.jsp");
			req.setAttribute(OnlineContentServlet.CONTENT, content);
			req.setAttribute(OnlineContentServlet.LIST, recommendations);

		} else {
			Iterator<Content> it = sortedContents.iterator();
			for (int i = 0; i < (OnlineContentServlet.PAGESIZE * (pageActual - 1)); i++) {
				it.next();
			}
			// create subList from pageSize*pageActual to
			// pageSize*(pageActual+1)
			TreeSet<Content> pageContents = new TreeSet<>();
			while (it.hasNext() && pageContents.size() <= OnlineContentServlet.PAGESIZE) {
				pageContents.add(it.next());
			}
			view = req.getRequestDispatcher("index.jsp");
			req.setAttribute(OnlineContentServlet.LIST, pageContents);
			req.setAttribute(OnlineContentServlet.LISTSIZE, (contents.size() / PAGESIZE) + 1);
			req.setAttribute(OnlineContentServlet.PAGEACTUAL, pageActual);
		}

		try {
			view.forward(req, resp);
		} catch (ServletException e1) {
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static java.util.Set<Content> getRecommendation(int limit, Content content) {
		// TODO: recommend according to current content
		Random rand = new Random();
		// TODO: do some optimalization, this reorder is called on every entry
		// page view
		Map.Entry<String, Content>[] entries = (Entry<String, Content>[]) contents.entrySet().toArray();
		java.util.Set<Content> recommendations = new HashSet<>();
		while (recommendations.size() <= limit) {
			recommendations.add(entries[rand.nextInt(entries.length - 1)].getValue());
		}
		return recommendations;
	}
	/*
	 * STATIC CALLS
	 */
	public static Map<String, Language> getTranslations() {
		return languages;
	}

	public static Map<String, Category> getCategories() {
		return categories;
	}

	public static Map<String, Content> getContents() {
		return contents;
	}

	public static Map<String, BlobKey> getNoimages() {
		return noimages;
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
		return "/static/serve?noimage=" + content.getNameKey().getName();
	}

	public static Set<Category> searchCategories(Content content) {
		Set<Category> ret = new HashSet<Category>();
		try {
			for (Category category : categories.values() ) {
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

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object data) {
		if ( data instanceof Map<?, ?> ) {
			if ( ((Map<?, ?>) data).isEmpty() ) {
				return;
			}
			Object k = ((Map<?, ?>) data).values().iterator().next();
			if ( k instanceof Content ) {
				contents.clear();
				contents.putAll((Map<String,Content>) data);
				sortedContents.clear();
				sortedContents.addAll(contents.values());
			} else if ( k instanceof Language ) {
				languages.clear();
				languages.putAll((Map<String,Language>) data);
			} else if ( k instanceof Category ) {
				categories.clear();
				categories.putAll((Map<String,Category>) data);
			} else if ( k instanceof BlobKey ) {
				noimages.clear();
				noimages.putAll((Map<String,BlobKey>) data);
			} else {
				log.warning("Unknown object to update: " + k.getClass().getSimpleName());
			}
		}
	}
}
