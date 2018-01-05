/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblUsers;

public class CooInitTblUsers extends InitTblUsers
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default user
		put("Coo", "Gast", 
			"02261 123", "coordz@unitechnik.de", 
			"user_m.png", 
			0, 0, 0, 
			Boolean.FALSE);
	}
}