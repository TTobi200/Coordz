/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db;

import java.util.*;

import de.util.CooSQLUtil;

/**
 * Class to create a database select statement that 
 * provides useful methods to construct and 
 * finally {@link #build()} the statement.
 */
public class CooDBSelectStmt
{
	/** Constant for ASC order by sorting */
	public static final String ASC = "ASC";
	/** Constant for DESC order by sorting */
	public static final String DESC = "DESC";
	
	/** {@link List} with columns to select */
	protected List<String> column;
	/** {@link List} with tables to select from */
	protected List<String> from;
	/** {@link List} with where arguments */
	protected List<String> where;
	/** {@link List} with order by arguments */
	protected List<String> orderBy;
	
	public CooDBSelectStmt()
	{
		column = new ArrayList<>();
		from = new ArrayList<>();
		where = new ArrayList<>();
		orderBy = new ArrayList<>();
	}
	
	/**
	 * Method to add a column to this statement.
	 * @param column = the column to select
	 * @return this {@link CooDBSelectStmt}
	 */
	public CooDBSelectStmt addColumn(String column)
	{
		this.column.add(column);
		return this;
	}
	
	/**
	 * Method to add a table to this statement.
	 * @param from = the table to select
	 * @return this {@link CooDBSelectStmt}
	 */
	public CooDBSelectStmt addFrom(String from)
	{
		this.from.add(from);
		return this;
	}
	
	/**
	 * Method to add a where argument to this statement.
	 * @param where = the column to select
	 * @param param = the where parameter
	 * @return this {@link CooDBSelectStmt}
	 */
	public CooDBSelectStmt addWhere(String where, Object param)
	{
		this.where.add(where.replace("?", CooSQLUtil.escapeString(param)));
		return this;
	}
	
	/**
	 * Method to add a order by ASC argument to this statement.
	 * @param colname = the column name to order by
	 * @return this {@link CooDBSelectStmt}
	 */
	public CooDBSelectStmt addOrderBy(String colname)
	{
		return addOrderBy(colname, ASC);
	}
	
	/**
	 * Method to add a order by argument to this statement.
	 * @param colname = the column name to order by
	 * @param type = the order by type ASC or DESC
	 * @return this {@link CooDBSelectStmt}
	 */
	public CooDBSelectStmt addOrderBy(String colname, String type)
	{
		this.orderBy.add(colname + " " + type);
		return this;
	}
	
	/**
	 * Method to build this statement as {@link String}.
	 * @return the build statement as {@link String}
	 */
	public String build()
	{
		StringBuilder stmt = new StringBuilder("SELECT ");
		
		// Add the column arguments
		for(int i = 0; i < column.size(); i++)
		{
			String c = CooSQLUtil.escapeColumn(column.get(i));
			stmt.append(c).append(i < column.size() -1 ? ", " : "");
		}

		// Add the from arguments
		if(from.size() > 0)
		{
			stmt.append(" FROM ");
			for(int i = 0; i < from.size(); i++)
			{
				String f = from.get(i);
				stmt.append(f).append(i < from.size() -1 ? ", " : "");
			}
		}
		
		// Add the where arguments
		if(where.size() > 0)
		{
			stmt.append(" WHERE ");
			for(int i = 0; i < where.size(); i++)
			{
				String w = where.get(i);
				stmt.append(w).append(i < where.size() -1 ? " AND " : "");
			}
		}
		
		// Add the order by arguments
		if(orderBy.size() > 0)
		{
			stmt.append(" ORDER BY ");
			for(int i = 0; i < orderBy.size(); i++)
			{
				String o = orderBy.get(i);
				stmt.append(o).append(i < orderBy.size() -1 ? " AND " : "");
			}
		}
		
		return stmt.toString();
	}
	
	@Override
	public String toString()
	{
		return build();
	}
}