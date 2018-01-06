/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblLAPSoftware;

public class CooInitTblLAPSoftware extends InitTblLAPSoftware
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default lap software entry
		put(1, "LAP Pro-Soft", "1.0");
	}
}