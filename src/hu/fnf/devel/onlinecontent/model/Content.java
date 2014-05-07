package hu.fnf.devel.onlinecontent.model;

import java.io.Serializable;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Content implements Serializable {
	/**
	 * Uploader uploads serialized content
	 */
	private static final long serialVersionUID = 1988L;

	public static final String THUMBNAIL_SOURCE_URL = "thumbSourceUrl";
	public static final String THUMBNAIL_STORED_URL = "thumbBlobUrl";
	public static final String CATEGORIES_CSV = "categories";
	public static final String DESCRIPTION = "description";
	public static final String CONTENT_EMBED_URL = "contentEmbedUrl";
	public static final String CONTENT_SOURCE_URL = "contentSourceUrl";

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String nameKey;
	@Persistent
	private String thumbSourceUrl;
	@Persistent
	private String thumbBlobUrl;
	@Persistent
	private List<String> categories;
	@Persistent
	private String description;
	@Persistent
	private String contentEmbedUrl;
	@Persistent
	private String contentSourceUrl;
	@Persistent
	private String displayName;
	@Persistent
	private byte[] thumbData;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public byte[] getThumbData() {
		return thumbData;
	}

	public void setThumbData(byte[] thumbData) {
		this.thumbData = thumbData;
	}

	public Content(String name) {
		this.nameKey = name;
	}

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

	public String getThumbSourceUrl() {
		return thumbSourceUrl;
	}

	public void setThumbSourceUrl(String thumbSourceUrl) {
		this.thumbSourceUrl = thumbSourceUrl;
	}

	public String getThumbBlobUrl() {
		return thumbBlobUrl;
	}

	public void setThumbBlobUrl(String thumbBlobUrl) {
		this.thumbBlobUrl = thumbBlobUrl;
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

	public String getContentEmbedUrl() {
		return contentEmbedUrl;
	}

	public void setContentEmbedUrl(String contentEmbedUrl) {
		this.contentEmbedUrl = contentEmbedUrl;
	}

	public String getContentSourceUrl() {
		return contentSourceUrl;
	}

	public void setContentSourceUrl(String contentSourceUrl) {
		this.contentSourceUrl = contentSourceUrl;
	}
}