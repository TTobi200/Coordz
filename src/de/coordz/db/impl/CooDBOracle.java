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

public class CooDBOracle extends CooDBSimple
{
	private static final String ORACLE_CLIENT_DRIVER = "oracle.jdbc.driver.OracleDriver";

	@Override
	public void connect(String dbHost, String dbPort, String dbName,
		String usrName, String password, boolean create)
	{
		CooLog.debug("Establish connection to oracle database <" + dbName + ">");
		
		// Create the database connection url
		dbURL = new StringBuilder("jdbc:oracle:thin:@");
		// Add the database host
		dbURL.append(dbHost).append(":");
		// Add the database port
		dbURL.append(dbPort).append(":");
		// Add the database name
		dbURL.append(dbName);

		try
		{
			Class.forName(ORACLE_CLIENT_DRIVER).newInstance();
			conn = DriverManager.getConnection(dbURL.toString(), usrName, password);
			stmt = conn.createStatement();

			// TODO $TO: Use the same constants for timestamp formatting
			// Set the timestamp format for this db session
			exec("ALTER SESSION SET NLS_TIMESTAMP_FORMAT = 'yyyy-mm-dd hh24:mi:ss.FF'");
		}
		catch(Exception e)
		{
			CooLog.error("Error while establish oracle"
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
			CooLog.error("Error While closing oracle "
				+ "DB connection", e);
		}
	}
	
	@Override
	public void cleanUp(boolean dropTable) throws SQLException
	{
		CooLog.debug("Clean up oracle database instance");
		
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
				execUpdate(dropTable ? "DROP " + type + " " + name + " CASCADE CONSTRAINTS" : 
					"TRUNCATE " + type + " " + name);
			}
		}
	}
	
	@Override
	public String getDataType(CooDBValTypes type, String column)
	{
		String retType = super.getDataType(type, column);
		
		switch(type)
		{
			case BOOLEAN:
				// Oracle 11gR2 has no boolean data type. Use a 
				// char and check if set 0 = false or 1 = true
				retType = "char check (" + column + " in (0, 1))";
				break;
			case DOUBLE:
				retType = "DOUBLE PRECISION";
				break;
			default:
		}
		return retType;
	}
}