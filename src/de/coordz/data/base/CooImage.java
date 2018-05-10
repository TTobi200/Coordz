/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import java.io.*;
import java.sql.*;

import de.coordz.CooSystem;
import de.coordz.db.gen.dao.DaoImage;
import de.coordz.db.gen.inf.InfImage;
import de.util.log.CooLog;
import javafx.scene.image.Image;

public class CooImage extends DaoImage
{
	public Image load() throws SQLException
	{
		// Get the image data from blob
		byte barr[] = dataProperty().get().getBytes(1, 
			(int)dataProperty().get().length());
		// Create and return the image
		return new Image(new ByteArrayInputStream(barr));
	}
	
	public void store(File imageFile) 
	{
		try
		{
			// Add initial default image entry
			PreparedStatement stmt = CooSystem.getDatabase().prepareStatement(
				"INSERT INTO " + InfImage.TABLE_NAME + " VALUES (?, ?, ?, ?)");
				
			// Set the primary key
			stmt.setInt(imageIdProperty().get(), 1);
			// Set the foreign key customer id
			stmt.setInt(2, customerIdProperty().get());
			// Set the image name
			stmt.setString(3, nameProperty().get());
					
			FileInputStream fin = new FileInputStream("./CoordzXML/Musterfirma/Logo.png");
			stmt.setBinaryStream(4, fin, fin.available());  
			stmt.executeUpdate();
		}
		catch (IOException | SQLException e)
		{
			CooLog.error("Error while storing image", e);
		}
	}
}