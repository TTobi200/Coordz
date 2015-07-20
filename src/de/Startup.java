/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de;

import javafx.application.Application;
import de.gui.CoordzMainFrame;

public class Startup
{
	public static void main(String[] args)
	{
		// XXX Add this after debug
//		if(CoordzPreferencesUtil.isCoordzAlreadyRunning())
//		{
//			System.err.println("ERROR: Coordz already running");
//			CoordzSystem.exit(CoordzSystem.FATAL);
//		}
//
//		CoordzPreferencesUtil.setCoordzRunning(true);

		Application.launch(CoordzMainFrame.class, args);
	}
}