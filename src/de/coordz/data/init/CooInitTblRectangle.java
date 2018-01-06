/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblRectangle;

public class CooInitTblRectangle extends InitTblRectangle
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default rectangle entry
		put("Rechteck 1", 0, 0, 0, 
			0, 0, 0, 0, 0);
	}
}