/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db;

/**
 * Interface for sql functions.
 * @author tobias.ohm
 */
public interface CooDBSQL
{
	/**
	 * Method to format Class into sql statement.
	 * @return the sql statement
	 */
	public String toSQL();
}