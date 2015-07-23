/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.db;

import java.sql.*;

public interface CooDBStmt
{ 
	public default void createTable(String name) throws SQLException
	{
		exec("CREATE " + name);
	}
	
	public default void deleteTable(String name) throws SQLException
	{
		exec("DROP " + name);
	}
	
	public ResultSet exec(String stmt) throws SQLException;
}