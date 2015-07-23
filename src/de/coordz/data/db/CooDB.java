/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.db;

public abstract class CooDB implements CooDBStmt
{
	public abstract void connect(String dbName, String usrName,
					String password, boolean create);
	
	public abstract void shutdown();
}