/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data;

import java.sql.SQLException;

import de.coordz.CooSystem;
import de.coordz.data.init.*;
import de.coordz.db.*;
import de.coordz.db.xml.*;
import de.util.log.CooLog;

public class CooDataInit extends CooDBInit
{
	@Override
	public void init() throws SQLException
	{
		// Create the DB Tables
		createDBTables(CooSystem.getModel());
		
		// Initializes database tables
		new CooInitTblUsers().init();
		new CooInitTblStatistic().init();
		new CooInitTblSettings().init();
	}
	
	private void createDBTables(CooDBModel model) throws SQLException
	{
		CooLog.debug("Create the database tables");
		CooDB database = CooSystem.getDatabase();
		
		// Loop through loaded tables and create them
		for(CooDBTable table : model.getTables())
		{
			CooLog.debug("Creating table <" + table.nameProperty().get() + ">");
			database.exec(table.toSQL());
		}
	}
}