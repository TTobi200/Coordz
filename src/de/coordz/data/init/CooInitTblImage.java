/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.io.File;
import java.sql.SQLException;

import de.coordz.data.base.CooImage;
import de.coordz.db.gen.init.InitTblImage;

public class CooInitTblImage extends InitTblImage
{
	public static final String IMAGE_LOGO = "Logo";
	
	@Override
	public void init() throws SQLException
	{
		// Add initial default image entry
		put(1, IMAGE_LOGO, "./CoordzXML/Musterfirma/Logo.png");
	}
	
	protected void put(int customerId, String name, String file)
		throws SQLException
	{
		CooImage image = new CooImage();
		image.cre();
		image.customerIdProperty().set(customerId);
		image.nameProperty().set(name);
		image.store(new File(file));
	}
}