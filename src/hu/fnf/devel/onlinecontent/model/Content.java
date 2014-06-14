package hu.fnf.devel.onlinecontent.model;

import hu.fnf.devel.onlinecontent.controller.OnlineContentServlet;
import hu.fnf.devel.onlinecontent.controller.Receiver;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

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

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key nameKey;
	@Persistent
	private String displayName;
	@Persistent
	private String contentSourceUrl;
	@Persistent
	private String thumbRemoteUrl;
	@Persistent
	private String thumbLocaleUrl;
	@Persistent
	private java.util.List<Category> categories;
	@Persistent
	private String description;
	@Persistent
	private java.util.List<String> thumbSearchKeyWords;
	@Persistent
	private Date contentCreation;
	@Persistent
	private boolean keptBack;
	
	private Content() {
		super();
	}
	
	public Content(Key nameKey) {
		this();
		this.nameKey = nameKey;
	}
	
	public Content(Key nameKey, String contentSourceUrl, java.util.List<String> thumbSearchKeyWords,
			Date contentCreation) {
		this();
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

	public Key getNameKey() {
		return nameKey;
	}
	
	public String getThumbSourceUrl() {
		return thumbRemoteUrl;
	}

	public void setThumbSourceUrl(String thumbSourceUrl) {
		this.thumbRemoteUrl = thumbSourceUrl;
	}

	public String getThumbBlobUrl() {

		if (thumbLocaleUrl == null || thumbLocaleUrl.contains("noimage")) {
			String thumbnail = OnlineContentServlet.searchThumbnail(this);
			thumbLocaleUrl = Receiver.srcUri(thumbnail, this);
		}
		return thumbLocaleUrl;
	}

	public void setThumbBlobUrl(String thumbBlobUrl) {
		this.thumbLocaleUrl = thumbBlobUrl;
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
	
	public void setKeptBack(boolean keptBack) {
		this.keptBack = keptBack;
	}
	
	public boolean isKeptBack() {
		return keptBack;
	}

	@Override
	public int compareTo(Content o) {
		return o.contentCreation.compareTo(this.contentCreation);
	}
}