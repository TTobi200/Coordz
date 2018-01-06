/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblPalet;

public class CooInitTblPalet extends InitTblPalet
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default palet entry
		put(1, "Palette 1", "0", 0, 0);
	}
}