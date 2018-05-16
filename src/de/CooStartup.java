/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.coordz.CooSystem;
import de.coordz.data.CooDataInit;
import de.coordz.db.*;
import de.coordz.db.xml.CooDBModel;
import de.gui.CooMainFrame;
import de.util.CooDBImportUtil;
import de.util.log.CooLog;
import javafx.application.Application;

public class CooStartup
{
	public static void main(String[] args)
	{
		// Store committed arguments in list
		List<String> argList = Arrays.asList(args);
		
		try
		{
			
			// Check if generating daos should be executed
			if(argList.contains("GENALL"))
			{
				CooSystem.startupDatabase();
				doGenAll(argList);
			} 
			// Check if db should be initialized
			else if(argList.contains("INITDB"))
			{
				CooSystem.startupDatabase();
				doInitDB(argList);
			}
			// Check if db should be initialized from xml
			else if(argList.contains("INITDBXML"))
			{
				CooSystem.startupDatabase();
				doInitDBXML(argList);
			}
			// When we have no parameter - start GUI
			else 
			{
				CooSystem.startup();
				// Launch the application with given arguments
				Application.launch(CooMainFrame.class, args);
			}
		}
		catch(Exception e)
		{
			CooLog.error("Error while starting up system", e);
		}		
	}

	private static void doGenAll(List<String> argList) throws IOException, 
		SAXException, ParserConfigurationException, SQLException, 
		InstantiationException, IllegalAccessException
	{
		// Check if database present
		CooSystem.isDBPresent();
			
		// Load the database model configuration
		CooDBModel model = CooSystem.getModel();

		// Create the database information objects
		CooDBCreInfs infs = new CooDBCreInfs();
		infs.create(model);
		
		// Create the database access objects
		CooDBCreDaos daos = new CooDBCreDaos();
		daos.create(model);
			
		// Create the initializes table objects
		CooDBCreInits inits = new CooDBCreInits();
		inits.create(model);
	}
	
	private static void doInitDB(List<String> argList) 	throws IOException, 
		SQLException, SAXException, ParserConfigurationException, 
		InstantiationException, IllegalAccessException
	{
		// Check if database present
		CooSystem.isDBPresent();
		
		// If not ANT task ask user to proceed init db
		if(argList.contains("ANT") || CooDBImportUtil
			.requestUserProceed())
		{
			// Cleanup the database and drop tables
			CooSystem.getDatabase().cleanUp(Boolean.TRUE);
		}
			
		// Start initializing database
		new CooDataInit().init();
	}
	
	private static void doInitDBXML(List<String> argList) 	throws IOException, 
		SQLException, SAXException, ParserConfigurationException, 
		InstantiationException, IllegalAccessException
	{
		// Check if database present
		CooSystem.isDBPresent();
		
		// If not ANT task ask user to proceed init db
		if(argList.contains("ANT") || CooDBImportUtil
			.requestUserProceed())
		{
			// Start initializing database from xml
//			CooDBImportUtil.importToDatabase();
		}
	}
}