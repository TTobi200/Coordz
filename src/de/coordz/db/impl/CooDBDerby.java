/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db.impl;

import java.sql.*;
import java.util.Objects;

import de.coordz.CooSystem;
import de.coordz.db.CooDBValTypes;
import de.util.log.CooLog;

public class CooDBDerby extends CooDBSimple
{
	private static final String APACHE_CLIENT_DRIVER = "org.apache.derby.jdbc.ClientDriver";

	@Override
	public void connect(String dbHost, String dbPort, String dbName,
		String usrName, String password, boolean create)
	{
		CooLog.debug("Establish connection to derby database <" + dbName + ">");
		
		// TODO $TO: Check why host and port not working on derby
		// Create the database connection url
		dbURL = new StringBuilder("jdbc:derby:");
		// Add the database host
//		dbURL.append("//").append(dbHost).append(":");
		// Add the database port
//		dbURL.append(dbPort).append("/");
		// Add the database name
		dbURL.append(dbName).append(";");
		// Add the database create flag
		dbURL.append("create=").append(String.valueOf(create)).append(";");
		
		try
		{
			Class.forName(APACHE_CLIENT_DRIVER).newInstance();
			conn = DriverManager.getConnection(dbURL.toString(), usrName, password);
			stmt = conn.createStatement();
		}
		catch(Exception e)
		{
			CooLog.error("Error while establish "
				+ "derby DB connection", e);
		}
	}

	@Override
	public void shutdown()
	{
		try
		{
			super.shutdown();
		}
		catch(SQLException e)
		{
			// Don't log this exception, always 
			// thrown on derby db shutdown
		}
	}
	
	@Override
	public void cleanUp(boolean dropTable) throws SQLException
	{
		CooLog.debug("Clean up derby database instance");
		
		// Get all available user tables from database
		DatabaseMetaData md = conn.getMetaData();
		ResultSet rs = md.getTables(null, CooSystem.getModel()
			.dbUserProperty().get().toUpperCase(), "%", null);
		
		while(rs.next())
		{
			// Get the name of this table
			String name = rs.getString(3);
			String type = rs.getString(4);
			
			if(Objects.nonNull(name))
			{
				// Delete or clean the table/view/sequence from database
				execUpdate(dropTable ? "DROP " + type + " " + name : 
					"TRUNCATE " + type + " " + name);
			}
		}
	}
	
	@Override
	public String getDataType(CooDBValTypes type, String column)
	{
		String retType = null;
		switch(type)
		{
			case BOOLEAN:
				retType = "BOOLEAN";
				break;
			case INTEGER:
				retType = "INTEGER";
				break;
			case DOUBLE:
				retType = "DOUBLE";
				break;
			case TIMESTAMP:
				retType = "TIMESTAMP";
				break;
			default:
			case VARCHAR:
				retType = "VARCHAR";
				break;
		}
		return retType;
	}
}