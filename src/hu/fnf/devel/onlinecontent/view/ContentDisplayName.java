package hu.fnf.devel.onlinecontent.view;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.google.appengine.api.datastore.Entity;

public class ContentDisplayName extends SimpleTagSupport {
	private Entity entity;

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void doTag() throws JspException, IOException {
		getJspContext().getOut().print(entity.getKey().getName());
	}

}
