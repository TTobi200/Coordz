/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.util;

import java.sql.*;

import javax.sql.rowset.serial.SerialBlob;

import de.coordz.CooSystem;
import de.coordz.db.*;
import de.coordz.db.impl.*;
import de.gui.comp.CooTableView;
import de.util.log.CooLog;
import javafx.beans.property.*;
import javafx.collections.*;
import oracle.sql.BLOB;

@SuppressWarnings("deprecation")
public class CooSQLUtil
{
	/**
	 * Utility method to escape String in SQL statement.
	 * @param p = the property from value
	 * @param stmt = the statement as {@link StringBuilder}
	 * @throws SQLException when escaping went wrong
	 */
	public static void escapeString(Property<?> p, StringBuilder stmt) throws SQLException
	{
		stmt.append(escapeString(p));
	}

	/**
	 * Utility method to escape String in SQL statement.
	 * @param p = the property from value
	 * @return the escaped {@link Object}
	 * @throws SQLException when escaping went wrong
	 */
	public static Object escapeString(Property<?> p) throws SQLException
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
		if(p instanceof ObjectProperty && p.getValue() instanceof BLOB &&
			CooSystem.getDatabase() instanceof CooDBOracle)
		{
			// Always insert empty blob
			return "hextoraw('0')";
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
	
	public static void updateDao(CooDBDao dao, 
		ReadOnlyBooleanProperty focused)
	{
		focused.addListener((obs, old, newv) -> 
		{
			// Update dao when focused lost only
			if(!newv)
			{
				try
				{
					dao.update();
				}
				catch(SQLException e)
				{
					CooLog.error("Error while updating dao", e);
				}
			}
		});
	}
	
	public static <T extends CooDBDao> void updateDaos(
		CooTableView<T> tblDaos, int fKey)
	{
		tblDaos.setTableDataEventListener(e -> 
		{
			try
			{
				// Get the dao that changed
				T dao = e.daoProperty().get();
				// Perform the action on dao
				switch(e.actionProperty().get())
				{
					case ADD:
						dao.cre(Boolean.FALSE);
						dao.insert(fKey);
						break;
					case DELETE:
						dao.delete();
						break;
					case EDIT:
						dao.update();
						break;
				}
			}
			catch(SQLException ex)
			{
				CooLog.error("Error while updating "
					+ "table dao", ex);
			}
		});
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
	
	@SuppressWarnings("unchecked")
	public static void setTimestampProperty(Property<?> prop, Object value)
	{
		try
		{
			if(value instanceof java.sql.Timestamp)
			{
				((ObjectProperty<java.sql.Timestamp>)prop).setValue(
					(java.sql.Timestamp)value);
			}
			else if(value instanceof oracle.sql.TIMESTAMP)
			{
				// Convert to oracle Timestamp
				((ObjectProperty<java.sql.Timestamp>)prop).setValue(
					oracle.sql.TIMESTAMP.toTimestamp(
						((oracle.sql.TIMESTAMP)value).getBytes()));
			}
		}
		catch(SQLException e)
		{
			CooLog.error("Error while setting "
				+ "timestamp property", e);
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	public static void setBlobProperty(Property<?> prop, Object value)
	{
		try
		{
			if(value instanceof java.sql.Blob)
			{
				((ObjectProperty<java.sql.Blob>)prop).setValue(
					(java.sql.Blob)value);
			}
			else if(value instanceof oracle.sql.BLOB)
			{
				((ObjectProperty<java.sql.Blob>)prop).setValue(
					(Blob)((oracle.sql.BLOB)value).toJdbc());
			}
			else if(value instanceof byte[]) // SQL-Server
			{
				((ObjectProperty<java.sql.Blob>)prop).setValue(
					new SerialBlob((byte[])value));
			}
		}
		catch(SQLException e)
		{
			CooLog.error("Error while setting "
				+ "Blob property", e);
		}
	}
}