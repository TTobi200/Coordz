/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db.impl;

import java.sql.*;
import java.util.Objects;

import de.coordz.db.CooDBValTypes;
import de.util.CooNativeUtils;
import de.util.log.CooLog;

public class CooDBSQLServer extends CooDBSimple
{
	private static final String SQLSERVER_CLIENT_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	@Override
	public void connect(String dbHost, String dbPort, String dbName,
		String usrName, String password, boolean create)
	{
		CooLog.debug("Establish connection to SQLServer database <" + dbName + ">");
		
		// Create the database connection url
		dbURL = new StringBuilder("jdbc:sqlserver://");
		// Add the database host
		dbURL.append(dbHost).append(";");
		// Add the database name
		dbURL.append("databaseName=").append(dbName).append(";");
		// Add property to use windows authentication
		dbURL.append("integratedSecurity=true;");
		
		try
		{
			// Load the windows authentication native
			String bit = "x" + System.getProperty("sun.arch.data.model");
			CooNativeUtils.loadLibraryToPath("/include/jar/sqljdbc_6.4/enu"
				+ "/auth/" + bit + "/sqljdbc_auth.dll");
			
			// Connect to the database
			Class.forName(SQLSERVER_CLIENT_DRIVER).newInstance();
			conn = DriverManager.getConnection(dbURL.toString());
			stmt = conn.createStatement();
		}
		catch(Exception e)
		{
			CooLog.error("Error while establish SQLServer "
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
			CooLog.error("Error While closing SQLServer "
				+ "DB connection", e);
		}
	}
	
	@Override
	public void cleanUp(boolean dropTable) throws SQLException
	{
		CooLog.debug("Clean up SQLServer database instance");
		
		// Get all available user tables from database
		DatabaseMetaData md = conn.getMetaData();
		ResultSet rs = md.getTables("Coords", "dbo", "%", null);
		
		while(rs.next())
		{
			// Get the name of this table
//			String catalog = rs.getString(1);
//			String schema = rs.getString(2);
			String name = rs.getString(3);
			String type = rs.getString(4);
			
			if(Objects.nonNull(name))
			{
				// Delete or clean the table/view/sequence from database
				// TODO $TO: Implement the DROP CONSTRAINT
				execUpdate(dropTable ? "DROP " + type + " " + name : 
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
			case BLOB:
				retType = "varBinary(MAX)";
				break;
			case DOUBLE:
				retType = "decimal(5,2)";
				break;
			case TIMESTAMP:
				retType = "datetime";
			break;
			default:
		}
		return retType;
	}
}