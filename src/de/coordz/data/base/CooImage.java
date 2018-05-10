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
			// Create prepared statement to update image into blob
			PreparedStatement stmt = CooSystem.getDatabase().prepareStatement("UPDATE " + 
				InfImage.TABLE_NAME + " SET " + InfImage.DATA + " = ? WHERE " + InfImage.IMAGEID + " = ?");
			
			// Add the image as binary stream blob
			FileInputStream fin = new FileInputStream(imageFile);
			stmt.setBinaryStream(1, fin, fin.available());  
			// Add the where image id
			stmt.setInt(2, imageIdProperty().get());
			
			// Execute the update on database
			stmt.executeUpdate();
		}
		catch (IOException | SQLException e)
		{
			CooLog.error("Error while storing image", e);
		}
	}
}