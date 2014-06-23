package hu.fnf.devel.onlinecontent.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Language implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 345569423630154017L;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key nameKey;
	@Persistent
	private Map<String, String> translate;
	
	public Key getNameKey() {
		return nameKey;
	}
	private Language() {
		this.translate = new HashMap<String, String>();
	}
	
	public Language(String nameKey) {
		this();
		this.nameKey = KeyFactory.createKey(Language.class.getSimpleName(), nameKey);
	}
	
	public void addTranslation(String langKey, String textValue) {
		translate.put(langKey, textValue);
	}
	
	public String getTranslation(String lang) {
		if ( translate.containsKey(lang) ) {
			return translate.get(lang);
		}
		return this.nameKey.getName().replace('_', ' ');
	}
	
}
