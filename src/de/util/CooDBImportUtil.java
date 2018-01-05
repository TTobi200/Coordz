/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.util;

import java.util.Scanner;

import de.coordz.CooSystem;
import de.coordz.db.xml.CooDBModel;
import de.util.log.CooLog;

public class CooDBImportUtil
{
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
}