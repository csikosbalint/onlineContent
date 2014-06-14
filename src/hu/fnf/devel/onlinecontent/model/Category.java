package hu.fnf.devel.onlinecontent.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

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
	
	private Category() {
		super();
	}
	
	public Category(Key nameKey, List<String> keyWords) {
		this();
		this.nameKey = nameKey;
		this.keyWords = keyWords;
		this.members = new ArrayList<Key>();
	}
	
	public void addMember(Key member) {
		members.add(member);
	}
	
	public java.util.List<Key> getMembers() {
		return members;
	}
}
