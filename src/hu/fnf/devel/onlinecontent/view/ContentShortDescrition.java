package hu.fnf.devel.onlinecontent.view;

import hu.fnf.devel.onlinecontent.model.Content;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.google.appengine.api.datastore.Entity;

public class ContentShortDescrition extends SimpleTagSupport {
	private Entity entity;

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		getJspContext().getOut().print(entity.getProperty(Content.DESCRIPTION));
	}
}
