/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.init;

import java.io.*;
import java.sql.*;

import de.coordz.CooSystem;
import de.coordz.db.gen.dao.DaoImage;
import de.coordz.db.gen.inf.InfImage;
import de.coordz.db.gen.init.InitTblImage;
import de.util.log.CooLog;

public class CooInitTblImage extends InitTblImage
{
	public static final String IMAGE_LOGO = "Logo";
	
	@Override
	public void init() throws SQLException
	{
		try
		{
			// Add initial default image entry
			PreparedStatement stmt = CooSystem.getDatabase().prepareStatement(
				"INSERT INTO " + InfImage.TABLE_NAME + " VALUES (?, ?, ?, ?)");
	
			// Create a new image dao
			DaoImage dao = new DaoImage();
			dao.cre();
			
			// Set the primary key
			stmt.setInt(dao.imageIdProperty().get(), 1);
			// Set the foreign key project id
			stmt.setInt(2, 1);
			// Set the image name
			stmt.setString(3, IMAGE_LOGO);
		
			FileInputStream fin = new FileInputStream("./CoordzXML/Musterfirma/Logo.png");
			stmt.setBinaryStream(4, fin, fin.available());  
			stmt.executeUpdate();  
		}
		catch(IOException e)
		{
			CooLog.error("Error while initalizing table " +
					InfImage.TABLE_NAME);
		}  
	}
}