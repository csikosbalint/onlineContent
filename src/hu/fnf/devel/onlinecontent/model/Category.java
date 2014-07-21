package hu.fnf.devel.onlinecontent.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.annotations.Unowned;

@PersistenceCapable
public class Category implements Serializable, Viewable, Comparable<Category> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2661356804940299020L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key nameKey;
	@Persistent
	private String thumbLocaleUrl;
	@Persistent
	private java.util.List<String> keyWords;
	@Persistent
	@Unowned
	private Set<Content> members;
	
	private Category() {
		super();
	}
	
	public Key getNameKey() {
		return nameKey;
	}
	
	public Category(String nameKey) {
		this();
		this.nameKey = KeyFactory.createKey(Category.class.getSimpleName(), nameKey);
		this.members = new HashSet<Content>();
	}
	public Set<Content> getMembers() {
		return members;
	}
	
	public void setMembers(Set<Content> members) {
		this.members = members;
	}
	
	public void addMember(Content content) {
		this.members.add(content);
	}
	public java.util.List<String> getKeyWords() {
		return keyWords;
	}
	
	public void setKeyWords(java.util.List<String> keyWords) {
		this.keyWords = keyWords;
	}
	
	public void addKeyWord(String keyWord) {
		this.keyWords.add(keyWord);
	}

	@Override
	public String getDisplayName() {
		return getNameKey().getName();
	}

	@Override
	public String getThumbLocaleUrl() {
		if ( thumbLocaleUrl == null ) {
			return "/static/serve?noimage=" + getNameKey().getName();
		}
		return thumbLocaleUrl;
	}

	@Override
	public boolean isKeptBack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int compareTo(Category o) {
		return this.getDisplayName().compareTo(o.getDisplayName());
	}

}
