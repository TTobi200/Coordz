/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db.xml;

import static de.util.CooXmlDomUtil.*;

import java.util.*;

import org.w3c.dom.*;

import de.coordz.db.CooDBTypes;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

public class CooDBModel implements CooDBXML
{
	/** {@link ObjectProperty} with {@link CooDBTypes} */
	protected ObjectProperty<CooDBTypes> dbType;
	
	/** {@link StringProperty} for the database host */
	protected StringProperty dbHost;
	/** {@link StringProperty} for the database port */
	protected StringProperty dbPort;
	/** {@link StringProperty} for the database name */
	protected StringProperty dbName;
	/** {@link StringProperty} for the database user */
	protected StringProperty dbUser;
	/** {@link StringProperty} for the database password */
	protected StringProperty dbPassword;
	/** {@link BooleanProperty} for the database create flag */
	protected BooleanProperty dbCreate;
	
	/** {@link List} with all {@link CooDBTable} */
	protected List<CooDBTable> tables;
	
	public CooDBModel()
	{
		dbType = new SimpleObjectProperty<>();
		dbHost = new SimpleStringProperty();
		dbPort = new SimpleStringProperty();
		dbName = new SimpleStringProperty();
		dbUser = new SimpleStringProperty();
		dbPassword = new SimpleStringProperty();
		dbCreate = new SimpleBooleanProperty();
		tables = FXCollections.observableArrayList();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		
	}

	@Override
	public void fromXML(Element model)
	{
		if(Objects.nonNull(model))
		{
			// Get the database preferences
			this.dbType.set(CooDBTypes.valueOf(
				model.getAttribute("DBTYPE")));
			this.dbHost.set(model.getAttribute("DBHOST"));
			this.dbPort.set(model.getAttribute("DBPORT"));
			this.dbName.set(model.getAttribute("DBNAME"));
			this.dbUser.set(model.getAttribute("DBUSER"));
			this.dbPassword.set(model.getAttribute("DBPASSWORD"));
			this.dbCreate.set(Boolean.valueOf(model.getAttribute("DBCREATE")));
			
			// Get the tables
			Element tables = getSingleElement(model, "TABLES");
			addToList("TABLE", tables,
				CooDBTable.class, this.tables);
		}
	}
	
	public ObjectProperty<CooDBTypes> dbTypeProperty()
	{
		return dbType;
	}
	
	public StringProperty dbHostProperty()
	{
		return dbHost;
	}
	
	public StringProperty dbPortProperty()
	{
		return dbPort;
	}
	
	public StringProperty dbNameProperty()
	{
		return dbName;
	}
	
	public StringProperty dbUserProperty()
	{
		return dbUser;
	}
	
	public StringProperty dbPasswordProperty()
	{
		return dbPassword;
	}
	
	public BooleanProperty dbCreateProperty()
	{
		return dbCreate;
	}
	
	public List<CooDBTable> getTables()
	{
		return tables;
	}
}