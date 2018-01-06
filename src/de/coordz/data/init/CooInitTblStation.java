/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblStation;

public class CooInitTblStation extends InitTblStation
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default station entry
		put(1, "U01", "Station U01", 
			"station.txt", 0, 0, 0);
	}
}