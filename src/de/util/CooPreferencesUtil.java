/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2014 T.Ohm . All Rights Reserved.
 */
package de.util;

import java.util.prefs.Preferences;

import de.gui.CooController;

public class CooPreferencesUtil
{
	public static final Preferences COORDZ_PREFERENCES = Preferences
			.userNodeForPackage(CooController.class);

	public static final String PREFERENCES_NODE_PREFIX = "Coordz";
	
	public static final String PREFERENCE_NODE_RUNNING = PREFERENCES_NODE_PREFIX + ".Running";

	public static boolean isCoordzAlreadyRunning()
	{
		return COORDZ_PREFERENCES.getBoolean(PREFERENCE_NODE_RUNNING, false);
	}

	public static void setCoordzRunning(boolean running)
	{
		COORDZ_PREFERENCES.putBoolean(PREFERENCE_NODE_RUNNING, running);
	}
}
