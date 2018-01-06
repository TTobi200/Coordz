/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblReticle;

public class CooInitTblReticle extends InitTblReticle
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default reticle entry
		put(1, "Z1", 0, 0, 0);
	}
}