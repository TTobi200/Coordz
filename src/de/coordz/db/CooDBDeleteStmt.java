/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2018 T.Ohm . All Rights Reserved.
 */
package de.coordz.db;

/**
 * Class to create a database delete statement that 
 * provides useful methods to construct and 
 * finally {@link #build()} the statement.
 */
public class CooDBDeleteStmt extends CooDBSelectStmt
{
	/**
	 * Method to build this statement as {@link String}.
	 * @return the build statement as {@link String}
	 */
	@Override
	public String build()
	{
		StringBuilder stmt = new StringBuilder("DELETE");
		
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
		
		return stmt.toString();
	}
}