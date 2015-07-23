/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz;

import javafx.application.Platform;
import de.coordz.data.*;
import de.coordz.data.db.*;
import de.util.CooPreferencesUtil;

public class CooSystem
{
	/** int symbolizing a a normal ending */
	public static final int NORMAL = 0x0;
	/** int symbolizing a a unnormal ending */
	public static final int FATAL = 0x1;
	public static final int SERVER_ERROR = 0x10;
	
	private static CooXMLProperties systemProperties = CooXMLProperties.getSystemProperties();
	private static CooDB systemDatabase = new CooDerbyDB(); 

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
		CooPreferencesUtil.setCoordzRunning(false);
		getSystemDatabase().shutdown();
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

	public static CooXMLProperties getSystemProperties()
	{
		return systemProperties;
	}
	
	public static CooDB getSystemDatabase()
	{
		return systemDatabase;
	}
}