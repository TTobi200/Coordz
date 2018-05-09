/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz;

import static de.util.CooXmlDomUtil.getDocumentBuilder;

import java.io.*;
import java.nio.channels.FileLock;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import de.coordz.db.CooDB;
import de.coordz.db.xml.CooDBModel;
import de.util.*;
import de.util.log.CooLog;
import javafx.application.Platform;

public class CooSystem
{
	/** int symbolizing a a normal ending */
	public static final int NORMAL = 0x0;
	/** int symbolizing a a unnormal ending */
	public static final int FATAL = 0x1;
	public static final int SERVER_ERROR = 0x10;
	
	public static final boolean REORG_LOGS = Boolean.TRUE;
	public static final int DAYS_TO_SAVE_LOGS = 7;
	public static final String LOGGING_FOLDER = "./logging";

	// Decoded with default key
	public static final String ADMIN_PASSWORD = "HtkkjeFirns";

	private static RandomAccessFile randomAccessFile;
	private static FileLock fileLock;
	
	/** Flag if database should be used - otherwise xml */
	public static final boolean USE_DB = Boolean.TRUE;
	/** The {@link CooDBModel} used in {@link #database} */
	private static CooDBModel model;
	/** The actual used {@link CooDB} */
	private static CooDB database;

	/**
	 * Normal ending of this application
	 */
	public static void exit()
	{
		exit(NORMAL);
	}

	/**
	 * Ending of this application with the given status
	 *
	 * @param status the return value
	 */
	public static void exit(int status)
	{
		if(Objects.nonNull(fileLock) && Objects.nonNull(randomAccessFile))
		{
			try
			{
				fileLock.release();
				randomAccessFile.close();
				CooLog.debug("Releasing the file lock");
			}
			catch(Exception e)
			{
				CooLog.error("Error while releasing file lock", e);
			}
		}
		
		CooLog.debug("Shutdown Coordz State:" + status);
		Platform.exit();

		if(status != NORMAL)
		{
			System.exit(status);
		}
	}

	public static boolean isRunningInSceneBuilder()
	{
		return System.getProperty("app.preferences.id", "").contains(
			"scenebuilder");
	}

	public static void startup() throws SAXException, ParserConfigurationException,
		InstantiationException, IllegalAccessException, IOException
	{
		try
		{
			// Initialize logging
			 CooLoggerUtil.initLogging(LOGGING_FOLDER, 
				 DAYS_TO_SAVE_LOGS, REORG_LOGS);

			// Lock a random file to secure single instance
			final File file = new File("Coo_Running_Flag");
			randomAccessFile = new RandomAccessFile(file, "rw");
			fileLock = randomAccessFile.getChannel().tryLock();

			if(fileLock == null)
			{
				CooLog.error("Coo already running - fileLocked - shutting down");
				exit(FATAL);
			}
		}
		catch(IOException e)
		{
			CooLog.error("Error while starting up coordz system", e);
		}
	}
	
	public static void startupDatabase() throws SAXException, ParserConfigurationException,
		InstantiationException, IllegalAccessException, IOException
	{
		// There should only be once database running
		if(USE_DB && Objects.isNull(database))
		{
			// Load the current database model
			model = new CooDBModel();
			Element root = getDocumentBuilder()
				.parse(CooFileUtil.getResourceStream(CooFileUtil.DB_FOLDER + 
					CooFileUtil.IN_JAR_SEPERATOR + "DBModel.xml"))
				.getDocumentElement();
			model.fromXML(root);
			
			// Create the database and establish connection
			database = model.dbTypeProperty().get().getInstance().newInstance();
			database.connect(model.dbHostProperty().get(), model.dbPortProperty().get(),
				model.dbNameProperty().get(), model.dbUserProperty().get(), 
				model.dbPasswordProperty().get(), model.dbCreateProperty().get());
		}
	}

	public static void isDBPresent()
	{
		if(!CooSystem.USE_DB)
		{
			CooLog.error("CooSystem.USE_DB is switched "
				+ "to FALSE - no DB available");
			System.exit(0);
		}		
	}

	public static CooDB getDatabase()
	{
		return database;
	}
	
	public static CooDBModel getModel()
	{
		return model;
	}
}