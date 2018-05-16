/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db;

import de.coordz.db.impl.*;

public enum CooDBTypes
{
	DERBY("Derby", CooDBDerby.class), 				// Fully working
	ORACLE("Oracle", CooDBOracle.class),			// Fully working
	SQLSERVER("SQLServer", CooDBSQLServer.class),	// Fully working - productive
	MYSQL("MySQL", CooDBMySQL.class),				// Not tested yet
	MARIADB("MariaDB", CooDBMariaDB.class);			// Not tested yet
	
	private String name;
	private Class<CooDB> instance;

	@SuppressWarnings("unchecked")
	private <T extends CooDB> CooDBTypes(String name, Class<T> instance)
	{
		this.name = name;
		this.instance = (Class<CooDB>)instance;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Class<CooDB> getInstance()
	{
		return instance;
	}
}