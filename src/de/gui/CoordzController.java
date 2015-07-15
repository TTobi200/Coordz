/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.stage.*;
import de.coordz.CoordzSystem;
import de.util.CoordzLoggerUtil;
import de.util.log.CoordzLog;

public class CoordzController
{
	public static final boolean REORG_LOGS = true;
	public static final int DAYS_TO_SAVE_LOGS = 7;
	public static final String LOGGING_FOLDER = "./logging";

	protected static CoordzController instance;
	protected Stage primaryStage;

	public static Object getInstance(Stage primaryStage)
	{
		if(instance == null)
		{
			instance = new CoordzController(primaryStage);
		}
		return instance;
	}

	public CoordzController(Stage primaryStage)
	{
		try
		{
			this.primaryStage = primaryStage;
			CoordzLoggerUtil.initLogging(LOGGING_FOLDER, DAYS_TO_SAVE_LOGS,
				REORG_LOGS);

			// Exit System on close requested
			primaryStage.setOnCloseRequest(
				new EventHandler<WindowEvent>()
				{
					@Override
					public void handle(final WindowEvent event)
					{
						event.consume();
						CoordzSystem.exit();
					}
				});
		}
		catch(IOException e)
		{
			CoordzLog.error("Error while initializing controller.",
				e);
		}
	}
}