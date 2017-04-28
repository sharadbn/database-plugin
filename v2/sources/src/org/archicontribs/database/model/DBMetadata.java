/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */

package org.archicontribs.database.model;

import java.sql.Timestamp;

import org.archicontribs.database.DBPlugin;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.INameable;

/**
 * This class defines the metadata attached to every model components.
 * 
 * @author Herve Jouin 
 * @see org.archicontribs.database.model.IDBMetadata
 */
public class DBMetadata  {
	/**
	 * What to do with the component in case of conflict.<br>
	 * Beware, the preferences have precedence over this local variable
	 */
	public enum CONFLICT_CHOICE {askUser, doNotExport, exportToDatabase, importFromDatabase};
	private CONFLICT_CHOICE conflictChoice = CONFLICT_CHOICE.askUser;
	
	/**
	 * Keeps the information about the status of the component in the database
	 */
	public enum DATABASE_STATUS {isSynced, isUpdated, isNew};
	private DATABASE_STATUS databaseStatus = DATABASE_STATUS.isNew;
	
	/**
	 * Current version of the component.<br>
	 * This property is set when the component is imported or after a successful export.
	 */
	private int currentVersion = 0;
	
	/**
	 * Exported version of the component.<br>
	 * This property is set during the export process and copied to the current version if the transaction is committed to the database .
	 */
	private int exportedVersion = 0;
	
	/**
	 * Version of the component in the database.<br>
	 * This property is retrieved from the database each time a connection is made to a database.
	 */
	private int databaseVersion = 0;
	
	/**
	 * Checksum of the component<br>
	 * This property is set when the component is imported or before an export.
	 */
	private String currentChecksum = "";
	
	/**
	 * Checksum of the component in the database<br>
	 * This property is retrieved from the database each time a connection is made to a database.
	 */
	private String databaseChecksum = "";
	
	/**
	 * Username of the last person who exported the component in the database
	 */
	private String databaseCreatedBy = null;
	
	/**
	 * Timestamp of the component in the database
	 */
	private Timestamp databaseCreatedOn = null;
	
	/**
	 * diagram 
	 */
	private IDiagramModelComponent parentDiagram = null;
	
	/**
	 * Component that contains the DBMetadata<br>
	 * This property is set during the component initialization and is used to calculate the component checksum
	 */
	private EObject component;
	
	/**
	 * If the component is a folder, stores the type of its root folder<br>
	 * This is needed to determine what class of eObjects it is able to store<br>
	 * Folders created by the user have got a type of 0 (zero) but the root folder they are in limit the kind of objects they can store   
	 */
	private int rootFolderType;
	
	public DBMetadata(EObject component) {
		assert ( component instanceof IIdentifier );
		this.component = component;
	}
	
	public int getCurrentVersion() {
		return currentVersion;
	}
	
	public void setCurrentVersion(int version) {
		currentVersion = version;
	}
	
	public int getExportedVersion() {
	    if ( component!=null && parentDiagram!=null && !(component instanceof IDiagramModel) && (component instanceof IDiagramModelComponent || component instanceof IDiagramModelConnection) ) {
	        return ((IDBMetadata)parentDiagram).getDBMetadata().getExportedVersion();
	    }
    
	    return exportedVersion;
	}
	
	public void setExportedVersion(int version) {
		exportedVersion = version;
	}
	
	public int getDatabaseVersion() {
		return databaseVersion;
	}
	
	public void setDatabaseVersion(int version) {
		databaseVersion = version;
	}
	
	public String getDatabaseCreatedBy() {
		return databaseCreatedBy;
	}
	
	public void setDatabaseCreatedBy(String username) {
		databaseCreatedBy = username;
	}
	
	public Timestamp getDatabaseCreatedOn() {
		return databaseCreatedOn;
	}
	
	public void setDatabaseCreatedOn(Timestamp timestamp) {
		databaseCreatedOn = timestamp;
	}
	
	public CONFLICT_CHOICE getConflictChoice() {
		return conflictChoice;
	}
	
	public void setConflictChoice(CONFLICT_CHOICE choice) {
		conflictChoice = choice;
	}
	
	public void setDatabaseStatus(DATABASE_STATUS status) {
		databaseStatus = status;
	}
	
	public DATABASE_STATUS getDatabaseStatus() {
		return databaseStatus;
	}
	
	public String getCurrentChecksum() {
		return currentChecksum;
	}
	
	public void setCurrentChecksum(String checksum) {
		currentChecksum = checksum;
	}
	
	public String getDatabaseChecksum() {
		return databaseChecksum;
	}
	
	public void setDatabaseChecksum(String checksum) {
		databaseChecksum = checksum;
	}
	
	public IDiagramModelComponent getParentDiagram() {
	    return parentDiagram;
	}
	
	public void setParentdiagram(IDiagramModelComponent parent) {
	    parentDiagram = parent;
	}
	
	public int getRootFolderType() {
		return rootFolderType;
	}
	
	public void setRootFolderType(int type) {
		rootFolderType = type;
	}
	
	public boolean isUpdated() {
	    if ( component != null && !(component instanceof IDiagramModel) && (component instanceof IDiagramModelComponent || component instanceof IDiagramModelConnection) ) {
	        return (parentDiagram == null) ? true : ((IDBMetadata)parentDiagram).getDBMetadata().isUpdated();
	    }

		return !DBPlugin.areEqual(getCurrentChecksum(), databaseChecksum);	// this method is more accurate than using the databaseStatus
	}
	
	private String fullName = null;
	/**
	 * Calculates the full name of the component 
	 * @return getclass().getSimpleName()+":\""+getName()+"\""
	 */
	public String getFullName() {
		if ( fullName == null ) {
			StringBuilder objName = new StringBuilder(component.getClass().getSimpleName());
			objName.append(":\""+((INameable)component).getName()+"\"");
			fullName = objName.toString();
		}
		return fullName;
	}
	
	private String debugName = null;
	/**
	 * Calculates the debug name of the component 
	 * @return getclass().getSimpleName()+":\""+getName()+"\"("+getId()+")"
	 */
	public String getDebugName() {
		if ( debugName == null ) {
			StringBuilder objName = new StringBuilder(getFullName());
			objName.append("("+((IIdentifier)component).getId()+")");
			debugName = objName.toString();
		}
		return debugName;
	}
}
