/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de;

import javafx.application.Application;
import de.gui.CooMainFrame;

public class CooStartup
{
	public static void main(String[] args)
	{
		// TODO add After debug in eclipse
		// if(CooPreferencesUtil.isCoordzAlreadyRunning())
		// {
		// System.err.println("ERROR: Coordz already running");
		// CooSystem.exit(CooSystem.FATAL);
		// }
		// CooPreferencesUtil.setCoordzRunning(true);
		Application.launch(CooMainFrame.class, args);
	}
}