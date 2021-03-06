package org.archicontribs.database.model.impl;

import org.archicontribs.database.model.DBMetadata;
import org.archicontribs.database.model.IDBMetadata;

/**
 * extends ApplicationComponent<br>
 * implements IDBMetadata
 * 
 * @author Herve Jouin 
 * @see com.archimatetool.model.impl.ApplicationComponent
 * @see org.archicontribs.database.model.IDBMetadata
 */
public class ApplicationComponent extends com.archimatetool.model.impl.ApplicationComponent implements IDBMetadata {
	private DBMetadata dbMetadata;
	
	public ApplicationComponent() {
		super();
		
		this.dbMetadata = new DBMetadata(this);
	}
	
	/**
	 * Gets the DBMetadata of the object
	 */
	@Override
    public DBMetadata getDBMetadata() {
		return this.dbMetadata;
	}
}
