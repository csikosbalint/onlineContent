package hu.fnf.devel.onlinecontent.view;

import hu.fnf.devel.onlinecontent.controller.Receiver;
import hu.fnf.devel.onlinecontent.model.Content;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.Customsearch.Cse.List;
import com.google.api.services.customsearch.model.Search;

public class ContentThumbnailImg extends SimpleTagSupport {
	private Content content;

	private String searchThumbnail() {

//		Customsearch thumbSearch = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), null)
//				.setApplicationName("ThumbSearch").build();

//		try {
//			String keywords = null;
//			if ( content.getSearchKeyWords() == null ) {
//				keywords = content.getDisplayName() + " online game";
//			} else {
//				keywords = content.getSearchKeyWords().toString();
//			}
//			List l = thumbSearch.cse().list(keywords);
//			l.setNum(1L);
//			l.setCx("004811520739431370780:ggegf7qshxe");
//			l.setSafe("high");
//			l.setFilter("1");
//			l.setSearchType("image");
//			l.setImgSize("large");
//			l.setKey("AIzaSyDiZfaoVfU5FeORRwSuvBC3tk1UJQ5N-XI");
//			
//			Search imgResult = l.execute();
//			// System.out.println("result: " + imgResult.toPrettyString());
//			if (imgResult.getItems() != null) {
//				System.out.println(imgResult.getSearchInformation().toPrettyString());
//				// thumbsrc
//				System.out.println("found image: " + imgResult.getItems().size());
//				return imgResult.getItems().get(0).getLink();
//			}
//
//		} catch (GoogleJsonResponseException e) {
//			//System.out.println(e.getCause().getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return "/static/noimage.gif";
	}

	@Override
	public void doTag() throws JspException, IOException {
		if (content.getThumbBlobUrl() == null || content.getThumbBlobUrl().contains("noimage")) {
			String thumbnail = searchThumbnail();
			content.setThumbBlobUrl(Receiver.srcUri(thumbnail, content));

			getJspContext().getOut().print("<img src=\"" + content.getThumbBlobUrl() + "\"/>");
		} else {
			getJspContext().getOut().print("<img src=\"" + content.getThumbBlobUrl() + "\"/>");
		}
	}

	public void setContent(Content content) {
		this.content = content;
	}
}
