/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblContact;

public class CooInitTblContact extends InitTblContact
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default contact entry
		put(1, "Max", "Mustermann", "01244 - 123", "max@mustermann.com");
	}
}