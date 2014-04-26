package hu.fnf.devel.onlinecontent.model;

import java.io.Serializable;
import java.util.Map;

public class Content implements Serializable {
	/**
	 * Uploader uploads serialized content
	 */
	private static final long serialVersionUID = 1985L;
	
	public static final String THUMBNAIL_SOURCE_URL = "thumbSourceUrl";
	public static final String THUMBNAIL_STORED_URL = "thumbBlobUrl";
	public static final String CATEGORIES_CSV = "categories";
	public static final String DESCRIPTION = "description";
	public static final String CONTENT_EMBED_URL = "contentEmbedUrl";
	public static final String CONTENT_SOURCE_URL= "contentSourceUrl";

	private String nameKey;
	private Map<String, Object> attributes;

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
}