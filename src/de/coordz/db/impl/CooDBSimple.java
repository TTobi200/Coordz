/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db.impl;

import java.sql.*;
import java.util.Objects;

import de.coordz.db.*;
import de.util.log.CooLog;

/**
 * Abstract class that fills {@link CooDB} methods
 * and provides variables for a simple database connection.
 */
public abstract class CooDBSimple extends CooDB
{
	/** {@link StringBuilder} with the connection URL */
	protected StringBuilder dbURL;
	/** {@link Connection} holding the actual connection */
	protected Connection conn = null;
	/** {@link Statement} holding the actual statement */
	protected Statement stmt = null;

	@Override
	public void shutdown() throws SQLException
	{
		CooLog.debug("Shutdown the database instance");
		
		// Close statement
		if(Objects.nonNull(stmt))
		{
			stmt.close();
		} // Shutdown DB driver
		if(Objects.nonNull(dbURL))
		{
			DriverManager.getConnection(
				dbURL.append(";shutdown=true").toString());
		} // Close connection
		if(Objects.nonNull(conn))
		{
			conn.close();
		}
	}
	
	@Override
	public boolean exec(String sql) throws SQLException
	{
		CooLog.debug("Execute SQL: \"" + sql + "\"");
		return stmt.execute(sql);
	}

	@Override
	public ResultSet execQuery(String sql) throws SQLException
	{
		CooLog.debug("Execute SQL Query: \"" + sql + "\"");
		return stmt.executeQuery(sql);
	}
	
	@Override
	public ResultSet execQuery(CooDBSelectStmt stmt) throws SQLException
	{
		return execQuery(stmt.build());
	}

	@Override
	public int execUpdate(String sql) throws SQLException
	{
		CooLog.debug("Execute SQL Update: \"" + sql + "\"");
		return stmt.executeUpdate(sql);
	}
}