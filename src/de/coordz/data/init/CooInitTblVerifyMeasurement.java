/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblVerifyMeasurement;

public class CooInitTblVerifyMeasurement extends InitTblVerifyMeasurement
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default verify measurement entry
		put(1, 1, 1);
	}
}