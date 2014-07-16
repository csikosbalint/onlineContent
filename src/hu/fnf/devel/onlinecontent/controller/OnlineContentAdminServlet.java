package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Category;
import hu.fnf.devel.onlinecontent.model.Content;
import hu.fnf.devel.onlinecontent.model.Language;
import hu.fnf.devel.onlinecontent.model.PMF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;

public class OnlineContentAdminServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(Content.class.getName());

	private static int HOURLY_LOAD_CONTENT = 15;
	private static final String CRON_STATE = "cronState";

	private static PersistenceManager pm;
	private static java.util.List<Observer> observers;

	private static Map<String, Content> contents;
	private static boolean contentsChanged = false;
	private static Map<String, Language> languages;
	private static boolean languagesChanged = false;
	private static Map<String, Category> categories;
	private static boolean categoriesChanged = false;
	private static Map<String, BlobKey> noimages;
	private static boolean noimagesChanged = false;

	private boolean cronState;

	public OnlineContentAdminServlet() {
		log.info("Admin init.");
		pm = PMF.getInstance().getPersistenceManager();
		observers = Collections.synchronizedList(new ArrayList<Observer>());

		contents = new HashMap<>();
		languages = new HashMap<>();
		categories = new HashMap<>();
		noimages = new HashMap<>();

		cronState = true;
		initMemory();
	}

	public static void addObserver(Observer observer) {
		// this .add() is synchronized
		observers.add(observer);
		contentsChanged = true;
		categoriesChanged = true;
		languagesChanged = true;
		noimagesChanged = true;
		notifyObservers();
	}

	private static void notifyObservers() {
		notifyObservers(observers);
	}

	private static void notifyObservers(List<Observer> myObservers) {
		if (contentsChanged) {
			notifyObservers(contents, myObservers);
			contentsChanged = false;
		}
		if (categoriesChanged) {
			notifyObservers(categories, myObservers);
			categoriesChanged = false;
		}
		if (languagesChanged) {
			notifyObservers(languages, myObservers);
			languagesChanged = false;
		}
		if (noimagesChanged) {
			notifyObservers(noimages, myObservers);
			noimagesChanged = false;
		}
	}

	private static void notifyObservers(Map<String, ?> container, List<Observer> myObservers) {
		for (Observer observer : myObservers) {
			observer.update(null, container);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * CRON section
		 */
		if (req.getHeader("X-AppEngine-Cron") != null) {
			if (cronState) {
				if (req.getParameter("loadContent") != null) {
					log.info("performing cron actions requested by AppEngine");
					loadContent();
				} else if ( req.getParameter("notifyObservers") != null ) {
					log.info("notifiying observers");
					notifyObservers();
				}
			}
			return;
		}
		/*
		 * ADMIN section
		 */
		if (req.getParameter("setHourlyLoadContent") != null) {
			setHourlyLoadContent(req.getParameter("setHourlyLoadContent"));
		}
		if (req.getParameter("changeCronState") != null) {
			changeCronState();
		}
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

		if (req.getParameter("createLanguageEntry") != null) {
			createLanguageEntry(req.getParameter("languageName"), req.getParameter("langKey"),
					req.getParameter("textValue"));
		}
		RequestDispatcher view = req.getRequestDispatcher("/admin");
		req.setAttribute(OnlineContentAdminServlet.CRON_STATE, cronState);
		req.setAttribute("HOURLY_LOAD_CONTENT", HOURLY_LOAD_CONTENT);
		view.forward(req, resp);

		// resp.sendRedirect("/admin");
	}

	private void setHourlyLoadContent(String loadNumber) {
		HOURLY_LOAD_CONTENT = Integer.valueOf(loadNumber);
	}

	private void changeCronState() {
		if (cronState) {
			cronState = false;
		} else {
			cronState = true;
		}
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

	private void createCategory(String nameKey, String words) {
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

	private void searchAndChange(String parameter, String parameter2) {
		// TODO Auto-generated method stub

	}

	private void resetContent(String parameter) {
		// TODO Auto-generated method stub

	}

	private void initMemory() {
		loadContent();
		loadLanguage();
		loadCategory();

		notifyObservers();
	}

	// TODO: make Generic java method for loading
	@SuppressWarnings("unchecked")
	private void loadCategory() {
		/*
		 * CATEGORY
		 */
		categories.clear();
		for (Category category : (java.util.List<Category>) pm.newQuery(Category.class).execute()) {
			categories.put(category.getNameKey().getName(), category);
		}
		log.info(categories.size() + " categories have been loaded!");
		categoriesChanged = true;
	}

	@SuppressWarnings("unchecked")
	private void loadLanguage() {
		/*
		 * TRANSLATION
		 */
		languages.clear();
		for (Language language : (java.util.List<Language>) pm.newQuery(Language.class).execute()) {
			languages.put(language.getNameKey().getName(), language);
		}
		log.info(languages.size() + " translation(s) have been loaded!");
		languagesChanged = true;
	}

	@SuppressWarnings("unchecked")
	private void loadContent() {
		/*
		 * CONTENT
		 */
		Query contentq = pm.newQuery(Content.class);
		log.info("setting range: " + contents.size() + " - " + (contents.size() + HOURLY_LOAD_CONTENT));
		contentq.setRange(contents.size(), contents.size() + HOURLY_LOAD_CONTENT);
		java.util.List<Content> list = (java.util.List<Content>) contentq.execute();
		if (list != null) {
			if (list.size() == 0) {
				log.severe("Full content has been loaded: " + contents.size());
			} else {
				for (Content content : list) {
					contents.put(content.getNameKey().getName(), content);
				}
			}
			log.info(contents.size() + " content(s) have been loaded!");
			contentsChanged = true;
		}
	}

	public static synchronized Map<String, Content> getContents() {
		return contents;
	}

	public static synchronized Map<String, Language> getTranslations() {
		return languages;
	}

	public static synchronized Map<String, Category> getCategories() {
		return categories;
	}

	public static synchronized Map<String, BlobKey> getNoimages() {
		return noimages;
	}

}