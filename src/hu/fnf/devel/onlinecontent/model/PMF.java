package hu.fnf.devel.onlinecontent.model;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {
    private static PersistenceManagerFactory pmfInstance;

    private PMF() {}

    public static PersistenceManagerFactory getInstance() {
    	if ( pmfInstance == null ) {
    		pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
    	}
        return pmfInstance;
    }
}