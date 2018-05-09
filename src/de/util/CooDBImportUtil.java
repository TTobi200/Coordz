/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.util;

import java.io.File;
import java.util.*;

import de.coordz.CooSystem;
import de.coordz.data.CooCustomer;
import de.coordz.db.xml.CooDBModel;
import de.util.log.CooLog;

public class CooDBImportUtil
{
	/** {@link String} for the DB image folder */
	public static final String IMAGE_FOLDER = "images";
	
	public static boolean requestUserProceed()
	{
		boolean proceed = Boolean.FALSE;
		CooDBModel model = CooSystem.getModel();
		String dbHost = model.dbHostProperty().get();
		String dbName = model.dbNameProperty().get();
		String dbUser = model.dbUserProperty().get();
		
		// Ask the user if really want to import xml data
		CooLog.debug("Do you really want to import all data to " + 
			dbUser + "@" + dbName + " on " + dbHost + "? (Y - Yes)");
		
		try(Scanner scan = new Scanner(System.in))
		{
			proceed = scan.next().toUpperCase().equals("Y");
		}
		
		return proceed;
	}
	
	/**
	 * Get the image folder from committed {@link CooCustomer}.
	 * @param dbFolder = the database folder
	 * @param customer = the {@link CooCustomer} to get the image folder form
	 * @return the image folder of committed {@link CooCustomer}
	 */
	public static File getImagesFolder(File dbFolder, CooCustomer customer)
	{
		File imageFolder = null;
		
		// Check if we have a customer
		if(Objects.isNull(customer))
		{
			return imageFolder;
		}
		// And if the customer has a name
		else if(Objects.nonNull(customer.nameProperty().get()))
		{
			// Construct the image folder
			imageFolder = new File(dbFolder.getAbsolutePath()
			+ File.separator
			+ customer.nameProperty().get()
			+ File.separator
			+ IMAGE_FOLDER);
		}
		
		return imageFolder;
	}
}