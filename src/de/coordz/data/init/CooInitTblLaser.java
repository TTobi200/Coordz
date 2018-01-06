/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblLaser;

public class CooInitTblLaser extends InitTblLaser
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default laser entry
		put("Laser 1", "AT123456", "SN123456",
			0, 0, 0, 0d, 0, 0);
	}
}