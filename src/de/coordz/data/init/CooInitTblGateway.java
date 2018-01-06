/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblGateway;

public class CooInitTblGateway extends InitTblGateway
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default gateway entry
		put(1, "10.10.10.10", "80-41-ae-fd-7e");
	}
}