/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.*;
import java.time.LocalDateTime;

import de.coordz.db.gen.init.InitTblProject;

public class CooInitTblProject extends InitTblProject
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default project entry
		put(1, Timestamp.valueOf(LocalDateTime.now()));
	}
}