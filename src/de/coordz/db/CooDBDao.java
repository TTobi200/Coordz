/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import de.coordz.CooSystem;
import de.util.CooSQLUtil;
import de.util.log.CooLog;
import javafx.beans.property.*;

public abstract class CooDBDao
{
	/** {@link String} with the table name */
	protected String tableName;
	/** {@link String} with the table primary key */
	protected String tablePKey;
	/** {@link LinkedHashMap} with column to property */
	protected LinkedHashMap<String, Property<?>> columnToProperty;
	/** Boolean flag if dao is in database */
	private boolean isInDB;
	
	protected CooDBDao()
	{
		columnToProperty = new LinkedHashMap<>();
	}
	
	/**
	 * Method to create a DAO with next primary key.
	 * @throws SQLException - when creation from {@link ResultSet} failed
	 */
	public void cre() throws SQLException
	{
		cre(Boolean.TRUE);
	}
	
	/**
	 * Method to create a DAO with next primary key.
	 * @param init = flag if dao should be initialized
	 * @throws SQLException - when creation from {@link ResultSet} failed
	 */
	public void cre(boolean init) throws SQLException
	{
		// Build the select statement
		CooLog.debug("Searching for next available primary key");
		StringBuilder stmt = new StringBuilder("SELECT ");
		stmt.append("MAX(").append(tablePKey.toUpperCase()).append(") ")
			.append("AS maxEntry ")
			.append("FROM ")
			.append(tableName);
		
		// Select the max primary key from table
		ResultSet res = CooSystem.getDatabase()
			.execQuery(stmt.toString());
		
		// Set the max entry to unknown
		int maxEntry = -1;
		if(res.next())
		{
			maxEntry = res.getInt("maxEntry");
		}

		CooLog.debug("Max primary key entry <" + maxEntry + 
			"> found for table <" + tableName + ">");

		// Create DAO with next primary key
		cre(maxEntry + 1, init);
	}
	
	/**
	 * Method to create a DAO from committed primary key.
	 * Calls the {@link #doSelect(int)} for primary key.
	 * @param pkey = the primary key to create DAO from
	 * @throws SQLException - when creation from {@link ResultSet} failed
	 */
	public void cre(int pkey) throws SQLException
	{
		cre(pkey, Boolean.TRUE);
	}
	
	/**
	 * Method to create a DAO from committed primary key.
	 * Calls the {@link #doSelect(int)} for primary key.
	 * @param pkey = the primary key to create DAO from
	 * @param init = flag if dao should be initialized
	 * @throws SQLException - when creation from {@link ResultSet} failed
	 */
	public void cre(int pkey, boolean init) throws SQLException
	{
		isInDB = doSelect(pkey);
		
		if(init && !isInDB)
		{
			// If not in database - fill with defaults
			intDefaults();
		}
	}
	
	/**
	 * Method to initializes default values of this DAO.
	 */
	private void intDefaults()
	{
		if(!columnToProperty.isEmpty())
		{
			// Loop through all columns excluded the primary
			columnToProperty.keySet().stream()
				.filter(column -> !column.equals(tablePKey.toUpperCase()))
				.forEach(column ->
			{
				// Get the property for column
				Property<?> p = columnToProperty.get(column);
				
				if(p instanceof StringProperty)
				{
					setProperty(p, "");
				}
				else if(p instanceof IntegerProperty)
				{
					setProperty(p, 0);
				}
				else if(p instanceof ObjectProperty)
				{
					setProperty(p, Timestamp.valueOf(
						LocalDateTime.now()));
				}
			});
		}
	}

	/**
	 * Method to create a DAO from committed {@link ResultSet}.
	 * @param res = the {@link ResultSet} to create DAO from
	 * @throws SQLException - when creation from {@link ResultSet} failed
	 */
	public void cre(ResultSet res) throws SQLException
	{
		// FIXME $TO: Not working when specified columns selected
		
		if(!columnToProperty.isEmpty())
		{
			// Get the ResultSet data and fill dao property
			for(int i = 1; i <= res.getMetaData().getColumnCount(); i++)
			{
				Property<?> prop = columnToProperty.get(
					res.getMetaData().getColumnName(i).toUpperCase());
				setProperty(prop, res.getObject(i));
			}
			
			// This item is in database
			isInDB = Boolean.TRUE;
		}
	}
	
	/**
	 * Method to create a DAO from committed {@link Map}.
	 * @param values = the {@link Map} to create DAO from
	 * @throws SQLException - when creation from {@link Map} failed
	 */
	public void cre(Map<String, Object> values) throws SQLException
	{
		if(Objects.nonNull(values) && !values.isEmpty()
			&& values.containsKey(tablePKey.toUpperCase()))
		{
			// Try to create the dao from committed primary key
			cre(Integer.valueOf(String.valueOf(values.get(
				tablePKey.toUpperCase()))));
			
			if(!columnToProperty.isEmpty())
			{
				// Get the Map data and fill dao property
				values.keySet().forEach(column -> 
				{
					Property<?> prop = columnToProperty.get(column);
					setProperty(prop, values.get(column));
				});
			}
		}
	}
	
	/**
	 * Method to set property value from dao. 
	 * @param prop = the {@link Property} to set
	 * @param value = the value to set
	 */
	@SuppressWarnings("unchecked")
	private void setProperty(Property<?> prop, Object value)
	{
		if(prop instanceof StringProperty)
		{
			((StringProperty)prop).setValue(String.valueOf(value));
		}
		else if(prop instanceof IntegerProperty)
		{
			((IntegerProperty)prop).setValue(Integer.valueOf(String.valueOf(value)));
		}
		else if(prop instanceof DoubleProperty)
		{
			((DoubleProperty)prop).setValue(Double.valueOf(String.valueOf(value)));
		}
		else if(prop instanceof BooleanProperty)
		{
			String bool = String.valueOf(value);
			((BooleanProperty)prop).setValue(Boolean.valueOf(
				bool.equals("1") ? "true" : bool));
		}
		else if(prop instanceof ObjectProperty)
		{
			((ObjectProperty<Timestamp>)prop).setValue(
				(Timestamp)value);
		}
	}

	/**
	 * Method to update this DAO in the database.
	 * @return if this dao {@link #isInDB}
	 * @throws SQLException - when update went wrong
	 */
	public boolean update() throws SQLException
	{
		StringBuilder stmt = new StringBuilder("UPDATE ");
		stmt.append(tableName)
			.append(" SET ");
		
		int i = 0;
		for(String column : columnToProperty.keySet())
		{
			Property<?> p = columnToProperty.get(column);
			
			stmt.append(column + " = " );
			
			// On integer property don't escape value with ''
			CooSQLUtil.escapeString(p, stmt);
			
			// Append , if not the last
			if(i++ < columnToProperty.size() -1)
			{
				stmt.append(", ");
			}
		}
		
		stmt.append(" WHERE ")
			.append(tablePKey.toUpperCase())
			.append(" = ")
			.append(CooSQLUtil.escapeString(
				columnToProperty.get(tablePKey.toUpperCase())));
		
		// Execute the SQL update statement
		return isInDB = CooSystem.getDatabase().
			execUpdate(stmt.toString()) > 0;
	}
	
	/**
	 * Method to insert this DAO to the database.
	 * @return if this dao {@link #isInDB}
	 * @throws SQLException - when insert went wrong
	 */
	public boolean insert() throws SQLException
	{
		StringBuilder stmt = new StringBuilder("INSERT INTO ");
		stmt.append(tableName)
			.append(" VALUES (");
		
		int i = 0;
		for(String column : columnToProperty.keySet())
		{
			// Add the value of the property
			Property<?> p = columnToProperty.get(column);
			
			// On integer property don't escape value with ''
			CooSQLUtil.escapeString(p, stmt);
			
			// Append , if not the last otherwise close with )
			stmt.append(i++ < columnToProperty.size() -1 ? ", " : ")");
		}
		
		// Execute the SQL statement
		CooSystem.getDatabase().exec(stmt.toString());
		
		// This item is in database
		return isInDB = Boolean.TRUE;
	}
	
	/**
	 * Method to delete this DAO from database.
	 * @throws SQLException - when delete went wrong
	 */
	public void delete() throws SQLException
	{
		// Only if this DAO is in database
		if(isInDB)
		{
			StringBuilder stmt = new StringBuilder("DELETE FROM ");
			stmt.append(tableName)
			.append(" WHERE ")
			.append(tablePKey.toUpperCase())
			.append(" = ")
			.append(CooSQLUtil.escapeString(
				columnToProperty.get(tablePKey.toUpperCase())));
			
			// Execute the SQL statement
			CooSystem.getDatabase().exec(stmt.toString());
			
			// This item is in database
			isInDB = Boolean.FALSE;
		}
	}
	
	/**
	 * Method to select DAO for committed primary key.
	 * @param pkey = the primary key of DAO
	 * @return true if DAO is in database
	 * @throws SQLException - when failed to select DAO
	 */
	@SuppressWarnings("unchecked")
	private boolean doSelect(Object pkey) throws SQLException
	{
		// Try to find dao with same primary key
		CooDBSelectStmt stmt = new CooDBSelectStmt();
		stmt.addColumn("*")
			.addFrom(tableName)
			.addWhere(tablePKey.toUpperCase() + " = ?", pkey);
		
		// Define the committed primary key
		Property<?> pKeyProp = columnToProperty.get(tablePKey.toUpperCase());
		if(pKeyProp instanceof StringProperty)
		{
			((StringProperty)pKeyProp).setValue((String)pkey);
		}
		else if(pKeyProp instanceof IntegerProperty)
		{
			((IntegerProperty)pKeyProp).setValue((Number)pkey);
		}
		else if(pKeyProp instanceof ObjectProperty)
		{
			((ObjectProperty<Timestamp>)pKeyProp)
				.setValue((Timestamp)pkey);
		}
		
		// Execute the SQL statement
		ResultSet result = CooSystem.getDatabase().execQuery(stmt.toString());
		
		// Only if data found
		if(result.next() && !columnToProperty.isEmpty())
		{
			int idx = 1;
			for(String column : columnToProperty.keySet())
			{
				Property<?> p = columnToProperty.get(column);
				
				if(p instanceof StringProperty)
				{
					((StringProperty)p).setValue(result.getString(idx));
				}
				else if(p instanceof IntegerProperty)
				{
					((IntegerProperty)p).setValue(result.getInt(idx));
				}
				else if(p instanceof ObjectProperty)
				{
					((ObjectProperty<Timestamp>)p).setValue(
						Timestamp.valueOf(LocalDateTime.now()));
				}
				idx++;
			}
			
			// This item is in database 
			return Boolean.TRUE;
		}
		
		// This item is not in database 
		return Boolean.FALSE;
	}
	
	/**
	 * Method to get this dao as simple {@link Map}.
	 * @return this dao as simple {@link Map}
	 */
	public Map<String, Object> toMap()
	{
		// Create a HashMap with dao data
		Map<String, Object> map = new HashMap<>();
		// Add all column property values
		columnToProperty.keySet().forEach(column -> map.put(column,
			columnToProperty.get(column).getValue()));
		return map;
	}
	
	/**
	 * Method to add column for this DAO.
	 * @param column = the name of the column
	 * @param property = the property of this column
	 */
	public void addColumn(String column, Property<?> property)
	{
		columnToProperty.put(column.toUpperCase(), property);
	}
	
	/**
	 * Method to check if this DAO is in database.
	 * @return true if this DAO is in database
	 */
	public boolean isInDB()
	{
		return isInDB;
	}
	
	/**
	 * Method to get the {@link #tableName} of this DAO.
	 * @return the {@link #tableName} of this DAO.
	 */
	public String getTableName()
	{
		return tableName;
	}
	
	public String getTablePKey()
	{
		return tablePKey;
	}
}