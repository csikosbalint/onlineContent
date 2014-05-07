package hu.fnf.devel.onlinecontent.view;

import hu.fnf.devel.onlinecontent.model.Content;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ContentShortDescrition extends SimpleTagSupport {
	private Content content;

	public void setContent(Content content) {
		this.content = content;
	}
	@Override
	public void doTag() throws JspException, IOException {
		getJspContext().getOut().print(content.getDescription());
	}
}
