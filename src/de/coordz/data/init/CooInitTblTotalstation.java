/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblTotalstation;

public class CooInitTblTotalstation extends InitTblTotalstation
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default total station entry
		put(1, 0, 0, 0, 0d, 0d);
	}
}