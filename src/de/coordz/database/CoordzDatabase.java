/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.database;

import java.sql.*;

import de.util.log.CoordzLog;

public class CoordzDatabase
{
	private static String dbURL = "jdbc:derby:CoordzDB;create=true;"
					+ "user=ut;password=ut";
	
	// jdbc Connection
	private static Connection conn = null;
	private static Statement stmt = null;

	public static void createConnection()
	{
		try
		{
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			// Get a connection
			conn = DriverManager.getConnection(dbURL);
		}
		catch(Exception e)
		{
			CoordzLog.error("Error while establish DB connection", e);
			e.printStackTrace();
		}
	}

	public static void shutdown()
	{
		try
		{
			if(stmt != null)
			{
				stmt.close();
			}
			if(conn != null)
			{
//				DriverManager.getConnection(dbURL + ";shutdown=true");
				conn.close();
			}
		}
		catch(SQLException e)
		{
			CoordzLog.error("Error While closing DB connection", e);
		}
	}
}