/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.util;

import java.sql.*;

import de.coordz.CooSystem;
import de.coordz.db.*;
import de.coordz.db.impl.*;
import de.util.log.CooLog;
import javafx.beans.property.*;
import javafx.collections.*;

public class CooSQLUtil
{
	/**
	 * Utility method to escape String in SQL statement.
	 * @param p = the property from value
	 * @param stmt = the statement as {@link StringBuilder}
	 */
	public static void escapeString(Property<?> p, StringBuilder stmt)
	{
		stmt.append(escapeString(p));
	}

	/**
	 * Utility method to escape String in SQL statement.
	 * @param p = the property from value
	 * @return the escaped {@link Object}
	 */
	public static Object escapeString(Property<?> p)
	{
		if(p instanceof IntegerProperty)
		{
			return p.getValue();
		}
		if(p instanceof DoubleProperty)
		{
			return p.getValue();
		}
		if(p instanceof BooleanProperty && 
			CooSystem.getDatabase() instanceof CooDBMySQL)
		{
			// Convert boolean to a digit for mySQL
			return "'" + ((boolean)p.getValue() ? 1 : 0) + "'";
		}
		if(p instanceof BooleanProperty && 
			CooSystem.getDatabase() instanceof CooDBMariaDB)
		{
			// Convert boolean to a digit for mariadb
			return "'" + ((boolean)p.getValue() ? 1 : 0) + "'";
		}
		if(p instanceof BooleanProperty && 
			CooSystem.getDatabase() instanceof CooDBOracle)
		{
			// Convert boolean to a digit for oracle
			return "'" + ((boolean)p.getValue() ? 1 : 0) + "'";
		}
		else
		{
			return "'" + p.getValue() + "'";
		}		
	}
	
	/**
	 * Utility method to escape String in SQL statement.
	 * @param o = the {@link Object} to escape
	 * @return the escaped {@link Object} as {@link String}
	 */
	public static String escapeString(Object o)
	{
		if(o instanceof Integer)
		{
			return String.valueOf(o);
		}
		if(o instanceof Double)
		{
			return String.valueOf(o);
		}
		if(o instanceof Boolean && 
			CooSystem.getDatabase() instanceof CooDBMySQL)
		{
			// Convert boolean to a digit for mySQL
			return "'" + ((boolean)o ? 1 : 0) + "'";
		}
		if(o instanceof Boolean && 
			CooSystem.getDatabase() instanceof CooDBMariaDB)
		{
			// Convert boolean to a digit for mariadb
			return "'" + ((boolean)o ? 1 : 0) + "'";
		}
		if(o instanceof Boolean && 
			CooSystem.getDatabase() instanceof CooDBOracle)
		{
			// Convert boolean to a digit for oracle
			return "'" + ((boolean)o ? 1 : 0) + "'";
		}
		else
		{
			return "'" + o + "'";
		}		
	}
	
	public static <T extends CooDBDao> T  loadDao(CooDB database, String tableName, 
		String columnID, Class<T> clazz, int i) throws SQLException
	{
		T dao = null;
		try
		{
			dao = clazz.newInstance();
			CooDBSelectStmt stmt = new CooDBSelectStmt();
			stmt.addFrom(tableName);
			stmt.addColumn("*");
			stmt.addWhere(columnID + " = ?", i);
			
			ResultSet res = database.execQuery(stmt);
			if(res.next())
			{
				dao.cre(res);
			}
		}
		catch(InstantiationException | IllegalAccessException e)
		{
			CooLog.error("Error while loading " + 
				tableName + " db dao", e);
		}
		
		return dao;
	}
	
	public static <T extends CooDBDao> ObservableList<T>  loadList(CooDB database,
		String tableName, String columnID, Class<T> clazz, int i) throws SQLException
	{
		ObservableList<T> list = FXCollections.observableArrayList();
		try
		{
			CooDBSelectStmt stmt = new CooDBSelectStmt();
			stmt.addFrom(tableName);
			stmt.addColumn("*");
			stmt.addWhere(columnID + " = ?", i);
			
			ResultSet res = database.execQuery(stmt);
			while(res.next())
			{
				T dao = clazz.newInstance();
				dao.cre(res);
				list.add(dao);
			}
		}
		catch(InstantiationException | IllegalAccessException e)
		{
			CooLog.error("Error while loading " + 
				tableName + " db list", e);
		}
		
		return list;
	}
}