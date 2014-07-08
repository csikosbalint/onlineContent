package hu.fnf.devel.onlinecontent.controller;

import hu.fnf.devel.onlinecontent.model.Category;
import hu.fnf.devel.onlinecontent.model.Content;
import hu.fnf.devel.onlinecontent.model.Language;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OnlineContentAdminServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(Content.class.getName());
    
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("admin servlet called from " + req.toString());
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
			reInitMemory();
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
			language = OnlineContentServlet.getPm().getObjectById(Language.class, nameKey);
			log.fine("language(" + nameKey + ") has been found.");
		} catch (Exception e) {
			language = new Language(nameKey);
			log.fine("new language(" + nameKey + ") created.");
		}
		language.addTranslation(langKey, textValue);
		OnlineContentServlet.getPm().makePersistent(language);
	}

	private void createCategory(String nameKey, String words) {
		Category category = null;
		java.util.List<String> keyWords = new ArrayList<String>();
		for (String word : words.split(",")) {
			keyWords.add(word);
		}
		try {
			category = OnlineContentServlet.getPm().getObjectById(Category.class, nameKey);
		} catch (Exception e) {
			category = new Category(nameKey);
		}
		category.setKeyWords(keyWords);
		OnlineContentServlet.getPm().makePersistent(category);
	}

	private void searchAndChange(String parameter, String parameter2) {
		// TODO Auto-generated method stub

	}

	private void reInitMemory() {
		OnlineContentServlet.initMemory();
	}

	private void resetContent(String parameter) {
		// TODO Auto-generated method stub

	}
}
