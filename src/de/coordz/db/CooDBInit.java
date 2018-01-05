/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db;

import java.sql.SQLException;

/**
 * Abstract class to initializes database table.
 */
public abstract class CooDBInit
{
	/**
	 * Method to initializes database table.
	 * @throws SQLException when initializing when wrong
	 */
	public abstract void init() throws SQLException;
}