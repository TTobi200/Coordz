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

public class CooDBMySQL extends CooDBSimple
{
	private static final String MYSQL_CLIENT_DRIVER = "com.mysql.jdbc.Driver";

	@Override
	public void connect(String dbHost, String dbPort, String dbName,
		String usrName, String password, boolean create)
	{
		CooLog.debug("Establish connection to MySQL database <" + dbName + ">");
		
		// Create the database connection url
		dbURL = new StringBuilder("jdbc:mysql://");
		// Add the database host
		dbURL.append(dbHost).append(":");
		// Add the database port
		dbURL.append(dbPort).append("/");
		// Add the database name
		dbURL.append(dbName);
		
		try
		{
			Class.forName(MYSQL_CLIENT_DRIVER).newInstance();
			conn = DriverManager.getConnection(dbURL.toString(), usrName, password);
			stmt = conn.createStatement();
		}
		catch(Exception e)
		{
			CooLog.error("Error while establish MySQL "
				+ "DB connection", e);
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
			CooLog.error("Error While closing MySQL "
				+ "DB connection", e);
		}
	}
	
	@Override
	public void cleanUp(boolean dropTable) throws SQLException
	{
		CooLog.debug("Clean up MySQL database instance");
		
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
				execUpdate(dropTable ? "DROP " + type + " " + name + " CASCADE" : 
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
				// MySQL will convert this to an TINYINT
				// Use the CooSQLUtil.escapeString(Property<?> p)
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