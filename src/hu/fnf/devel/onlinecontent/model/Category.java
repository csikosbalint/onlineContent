package hu.fnf.devel.onlinecontent.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.annotations.Unowned;

@PersistenceCapable
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2661356804940299020L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key nameKey;
	@Persistent
	private java.util.List<String> keyWords;
	@Persistent
	private java.util.List<Key> members;
	@Persistent
	@Unowned
	private java.util.List<Content> members2;
	
	private Category() {
		super();
	}
	
	public Category(String nameKey, List<String> keyWords) {
		this();
		this.nameKey = KeyFactory.createKey(Category.class.getSimpleName(), nameKey);
		this.keyWords = keyWords;
		this.members = new ArrayList<Key>();
		this.members2 = new ArrayList<Content>();
	}
	
	public void addMember(Key member) {
		members.add(member);
	}
	
	public java.util.List<Key> getMembers() {
		return members;
	}
	
	public java.util.List<Content> getMembers2() {
		return members2;
	}
	
	public void addMember2(Content c) {
		members2.add(c);
	}
}
