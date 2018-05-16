/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db.impl;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

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
			case BLOB:
				retType = "BLOB";
				break;
			default:
			case VARCHAR:
				retType = "VARCHAR";
				break;
		}
		return retType;
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
	
	@Override
	public int execUpdate(CooDBDeleteStmt stmt) throws SQLException
	{
		return execUpdate(stmt.build());
	}
	
	@Override
	public int execPrepareStatement(String stmt, List<Object> args)
		throws SQLException
	{
		// Create a prepared statement on database 
		PreparedStatement prepStmt = prepareStatement(stmt);
		
		// Fill the statement with data
		int index = 1;
		for(Object o : args)
		{
			if (o instanceof Integer) 
			{
				prepStmt.setInt(index, (Integer)o);
			}
			else if (o instanceof Double) 
			{
				prepStmt.setDouble(index, (Double)o);
			}
			else if (o instanceof Boolean) 
			{
				prepStmt.setBoolean(index, (Boolean)o);
			}
			else if (o instanceof Timestamp) 
			{
				prepStmt.setTimestamp(index, (Timestamp)o);
			}
			else if (o instanceof LocalDate) 
			{
				prepStmt.setDate(index, Date.valueOf(
					(LocalDate)o));
			}
			else if (o instanceof Blob) 
			{
				prepStmt.setBinaryStream(index, 
					((Blob)o).getBinaryStream());
			}
			else
			{
				prepStmt.setString(index, (String)o);
			}
			
			index++;
		}
		
		// Build the log statement string
		for(Object a : args)
		{
			stmt = stmt.replaceFirst("\\?", 
				"\"" + String.valueOf(a) + "\"");
		}

		CooLog.debug("Executing prepared SQL Update "
			+ "statement: \"" + stmt + "\" ");
		return prepStmt.executeUpdate();
	}
	
	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException
	{
		return conn.prepareStatement(sql);
	}
	
	@Override
	public Blob createBlob() throws SQLException
	{
		return conn.createBlob();
	}
	
	@Override
	public String escapeColumn(String column)
	{
		return !column.equals("*") ? "\"" + column + "\"" : column;
	}
}