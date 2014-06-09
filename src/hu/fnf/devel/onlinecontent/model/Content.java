package hu.fnf.devel.onlinecontent.model;

import hu.fnf.devel.onlinecontent.controller.OnlineContentServlet;
import hu.fnf.devel.onlinecontent.controller.Receiver;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.Customsearch.Cse.List;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

@PersistenceCapable
public class Content implements Serializable, Comparable<Content> {
	/**
	 * Uploader uploads serialized content
	 */
	private static final long serialVersionUID = 1990L;

	public static final String THUMBNAIL_SOURCE_URL = "thumbRemoteUrl";
	public static final String THUMBNAIL_STORED_URL = "thumbLocaleUrl";
	public static final String CATEGORIES_CSV = "categories";
	public static final String DESCRIPTION = "description";
	public static final String CONTENT_SOURCE_URL = "contentSourceUrl";
	public static final String CONTENT_CREATION = "contentCreation";
	/*
	 * com.google.appengine.api.datastore.QueryResultsSourceImpl
	 * logChunkSizeWarning: This query does not have a chunk size set in
	 * FetchOptions and has returned over 1000 results. If result sets of this
	 * size are common for this query, consider setting a chunk size to improve
	 * performance. To disable this warning set the following system property in
	 * appengine-web.xml (the value of the property doesn't matter):
	 * 'appengine.datastore.disableChunkSizeWarning'
	 */

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String nameKey;
	@Persistent
	private String displayName;
	@Persistent
	private String contentSourceUrl;
	@Persistent
	private String thumbRemoteUrl;
	@Persistent
	private String thumbLocaleUrl;
	@Persistent
	private java.util.List<String> categories;
	@Persistent
	private String description;
	@Persistent
	private java.util.List<String> thumbSearchKeyWords;
	@Persistent
	private Date contentCreation;

	private String searchThumbnail() {
		if (OnlineContentServlet.search) {
			System.out.println("searching...(only once)");
			Customsearch thumbSearch = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), null)
					.setApplicationName("ThumbSearch").build();
			try {
				StringBuffer searchKeyWords = new StringBuffer();
				searchKeyWords.append("\"");
				if (this.getSearchKeyWords().size() == 0) {
					searchKeyWords.append(this.getDisplayName());
				} else {
					for (String str : this.getSearchKeyWords()) {
						searchKeyWords.append(str);
					}
				}
				searchKeyWords.append("\" online game");
				System.out.println("search keywords: " + (searchKeyWords));
				List l = thumbSearch.cse().list(searchKeyWords.toString());
				// l.setNum(1L);
				l.setCx("004811520739431370780:ggegf7qshxe");
				l.setSafe("high");
				// l.setFilter("1");
				l.setSearchType("image");
				// l.setImgSize("large");
				l.setKey("AIzaSyDiZfaoVfU5FeORRwSuvBC3tk1UJQ5N-XI");

				Search imgResult = l.execute();
				System.out.println("result: " + imgResult.toPrettyString());
				if (imgResult.getItems() != null) {
					System.out.println(imgResult.getSearchInformation().toPrettyString());
					// thumbsrc
					int found = 0;
					while ( !isImageOk(imgResult.getItems().get(found)) ) {
						found++;
					}
					System.out.println("found image: " + imgResult.getItems().size());
					return imgResult.getItems().get(0).getLink();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "/static/noimage.gif";
	}

	private boolean isImageOk(Result result) {
		String[] bannedUris = {"youtube"};
		if ( Arrays.asList(bannedUris).contains(result.getDisplayLink()) ) {
			return false;
		}
		return true;
	}

	public Content(String nameKey, String contentSourceUrl, java.util.List<String> thumbSearchKeyWords,
			Date contentCreation) {
		super();
		this.nameKey = nameKey;
		this.contentSourceUrl = contentSourceUrl;
		this.thumbSearchKeyWords = thumbSearchKeyWords;
		this.contentCreation = contentCreation;
	}

	public Date getContentCreation() {
		return contentCreation;
	}

	public void setContentCreation(Date contentCreation) {
		this.contentCreation = contentCreation;
	}

	public java.util.List<String> getSearchKeyWords() {
		return thumbSearchKeyWords;
	}

	public void setSearchKeyWords(java.util.List<String> searchKeyWords) {
		this.thumbSearchKeyWords = searchKeyWords;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	private Content() {
		// TODO Auto-generated constructor stub
	}

	public Content(String name) {
		this();
		this.nameKey = name;
	}

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

	public String getThumbSourceUrl() {
		return thumbRemoteUrl;
	}

	public void setThumbSourceUrl(String thumbSourceUrl) {
		this.thumbRemoteUrl = thumbSourceUrl;
	}

	public String getThumbBlobUrl() {

		if (thumbLocaleUrl == null || thumbLocaleUrl.contains("noimage")) {
			String thumbnail = searchThumbnail();
			System.out.println("Found thumbnail: " + thumbnail);
			thumbLocaleUrl = Receiver.srcUri(thumbnail, this);
		}
		return thumbLocaleUrl;
	}

	public void setThumbBlobUrl(String thumbBlobUrl) {
		this.thumbLocaleUrl = thumbBlobUrl;
	}

	public java.util.List<String> getCategories() {
		return categories;
	}

	public void setCategories(java.util.List<String> categories) {
		this.categories = categories;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContentSourceUrl() {
		return contentSourceUrl;
	}

	public void setContentSourceUrl(String contentSourceUrl) {
		this.contentSourceUrl = contentSourceUrl;
	}

	@Override
	public int compareTo(Content o) {
		return o.contentCreation.compareTo(this.contentCreation);
	}
}