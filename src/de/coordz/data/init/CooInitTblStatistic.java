/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblStatistic;

public class CooInitTblStatistic extends InitTblStatistic
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default statistic entry
		put(1, "JANUARY", "2017", 10);
	}
}