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
//		// Check if coffez already running
//		if(CoordzPreferencesUtil.isCoordzAlreadyRunning())
//		{
//			System.err.println("ERROR: Coffez already running");
//			CoordzSystem.exit(CoordzSystem.FATAL);
//		}
//
//		// Set coffez running
//		CoordzPreferencesUtil.setCoordzRunning(true);

		Application.launch(CoordzMainFrame.class, args);
	}
}