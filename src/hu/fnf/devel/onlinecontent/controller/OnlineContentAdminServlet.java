package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Category;
import hu.fnf.devel.onlinecontent.model.Content;
import hu.fnf.devel.onlinecontent.model.Language;
import hu.fnf.devel.onlinecontent.model.PMF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;

public class OnlineContentAdminServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final int HOURLY_LOAD_CONTENT = 25;
	private static final Logger log = Logger.getLogger(Content.class.getName());
	
	private static PersistenceManager pm;
	private static java.util.List<Observer> observers;
	
	private static Map<String, Content> contents;
	private static Map<String, Language> translations;
	private static Map<String, Category> categories;
	private static Map<String, BlobKey> noimages;
	
	public OnlineContentAdminServlet() {
		log.info("Admin instanciated.");
		pm = PMF.getInstance().getPersistenceManager();
		observers = Collections.synchronizedList(new ArrayList<Observer>());
		
		contents = new HashMap<>();
		translations = new HashMap<>();
		categories = new HashMap<>();
		noimages = new HashMap<>();
		
		initMemory();
	}
    
	public static void addObserver(Observer observer) {
		// this .add is synchronized
    	observers.add(observer);
    	notifyObservers(observer);
    }
	
	private static void notifyObservers(Observer observer) {
		observer.update(null, contents);
		observer.update(null, categories);
		observer.update(null, translations);
		observer.update(null, noimages);
	}
	
	private static void notifyObservers() {
		for (Observer observer : observers) {
			notifyObservers(observer);
		}
	}
    
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * CRON section
		 */
		if ( req.getHeader("X-AppEngine-Cron") != null ) {
			log.info("performing cron actions requested by AppEngine");
			return;
		}
		/*
		 * ADMIN section 
		 */
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

		resp.sendRedirect("/admin");
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
	
	@SuppressWarnings("unchecked")
	private void initMemory() {
		/*
		 * CONTENT
		 */
		Query contentq = pm.newQuery(Content.class);
		contentq.setRange(contents.size(), contents.size() + HOURLY_LOAD_CONTENT);
		for (Content content : (java.util.List<Content>) contentq.execute()) {
			contents.put(content.getNameKey().getName(), content);
		}
		log.info(contents.size() + " content(s) have been loaded!");
		/*
		 * TRANSLATION
		 */
		translations.clear();
		for (Language language : (java.util.List<Language>) pm.newQuery(Language.class).execute()) {
			translations.put(language.getNameKey().getName(), language);
		}
		log.info(translations.size() + " translation(s) have been loaded!");
		/*
		 * CATEGORY
		 */
		categories.clear();
		for (Category category : (java.util.List<Category>) pm.newQuery(Category.class).execute()) {
			categories.put(category.getNameKey().getName(), category);
		}
		log.info(categories.size() + " categories have been loaded!");
		notifyObservers();
	}

}