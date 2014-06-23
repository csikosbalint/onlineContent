package hu.fnf.devel.onlinecontent.view;

import hu.fnf.devel.onlinecontent.controller.OnlineContentServlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class Interpreter extends SimpleTagSupport {
	private String text;

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		text = text.replace(' ', '_');
		getJspContext().getOut().print(
				OnlineContentServlet.getTranslations().get(text)
						.getTranslation(request.getHeader("Accept-Language").split(",")[0]));
	}
}
