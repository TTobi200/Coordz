/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz;

import javafx.application.Platform;
import de.util.CoordzPreferencesUtil;

public class CoordzSystem
{
	/** int symbolizing a a normal ending */
	public static final int NORMAL = 0x0;
	/** int symbolizing a a unnormal ending */
	public static final int FATAL = 0x1;
	public static final int SERVER_ERROR = 0x10;
	
	private static CoordzXMLProperties systemProperties = CoordzXMLProperties.getSystemProperties();

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
		CoordzPreferencesUtil.setCoordzRunning(false);
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

	public static CoordzXMLProperties getSystemProperties()
	{
		return systemProperties;
	}
}