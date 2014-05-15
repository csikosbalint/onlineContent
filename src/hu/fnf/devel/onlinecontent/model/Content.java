package hu.fnf.devel.onlinecontent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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
	private List<String> categories;
	@Persistent
	private String description;
	@Persistent
	private String[] thumbSearchKeyWords;
	@Persistent
	private Date contentCreation;

	public Content(String nameKey, String contentSourceUrl, String[] thumbSearchKeyWords, Date contentCreation) {
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

	public String[] getSearchKeyWords() {
		return thumbSearchKeyWords;
	}

	public void setSearchKeyWords(String[] searchKeyWords) {
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
		return thumbLocaleUrl;
	}

	public void setThumbBlobUrl(String thumbBlobUrl) {
		this.thumbLocaleUrl = thumbBlobUrl;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
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