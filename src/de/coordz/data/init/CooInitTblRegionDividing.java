/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblRegionDividing;

public class CooInitTblRegionDividing extends InitTblRegionDividing
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default region dividing entry
		put(1, 1);
	}
}