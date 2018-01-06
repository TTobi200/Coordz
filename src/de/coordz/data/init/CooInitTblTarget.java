/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblTarget;

public class CooInitTblTarget extends InitTblTarget
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default target entry
		put(1, "T1", 0, 0, 0);
	}
}