/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.*;
import java.time.LocalDateTime;

import de.coordz.db.gen.init.InitTblMeasurement;

public class CooInitTblMeasurement extends InitTblMeasurement
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default measurement entry
		put(1, "Messung 01", Timestamp.valueOf(LocalDateTime.now()),
			"00:00", "24:00", "Sonning", "-");
	}
}