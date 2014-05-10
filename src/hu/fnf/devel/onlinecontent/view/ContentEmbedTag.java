package hu.fnf.devel.onlinecontent.view;

import hu.fnf.devel.onlinecontent.model.Content;
import hu.fnf.devel.onlinecontent.model.PMF;

import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ContentEmbedTag extends SimpleTagSupport {
	private static PersistenceManager pm = PMF.getInstance().getPersistenceManager();
	private Content content;

	public void setContent(String name) {
		try {
			content = (Content)pm.getObjectById(Content.class, name);
		} catch ( JDOObjectNotFoundException e ) {
			content = null;
		}
	}

	@Override
	public void doTag() throws JspException, IOException {
		if ( content != null ) {
			getJspContext()
				.getOut()
				.print("<embed src=\""
						+ content.getContentSourceUrl()
						+ "\"quality=high pluginspage=\"http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash\" type=\"application/x-shockwave-flash\" width=\"720\" height=\"540\"></embed>");
		} else {
			getJspContext()
			.getOut()
			.print("<img src=\"http://placehold.it/500x500&text=Not found><br>");
		}
	}

}
