/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.database;

public abstract class CoordzDB implements CoordzDBStmt
{
	public abstract void connect(String dbName, String usrName,
					String password, boolean create);
	
	public abstract void shutdown();
}