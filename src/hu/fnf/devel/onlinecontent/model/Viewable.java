package hu.fnf.devel.onlinecontent.model;

import com.google.appengine.api.datastore.Key;

public interface Viewable {
	public String getDisplayName();
	public Key getNameKey();
	public String getThumbLocaleUrl();
	public boolean isKeptBack();
}
