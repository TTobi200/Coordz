/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.sql.SQLException;

import de.coordz.db.gen.init.InitTblCustomer;

public class CooInitTblCustomer extends InitTblCustomer
{
	@Override
	public void init() throws SQLException
	{
		// Add initial default customer entry
		put("Musterfirma", "Musterstr. 7", 
			"12345", "Musterhausen", "logo.png");
	}
}