/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui;

import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;
import de.util.CoordzFileUtil;

public class CoordzMainFrame extends Application
{
	public static final String TITLE = "Coffez";
	private static final String FXML = "CoffezGui.fxml";

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Parent root = CoordzFileUtil.loadFXML(
			CoordzController.getInstance(primaryStage),
			CoordzFileUtil.FXML_FOLDER + CoordzFileUtil.IN_JAR_SEPERATOR + FXML);
		Scene primaryScene = new Scene(root);

		primaryScene.getStylesheets().add("/include/fxml/stylesheet.css");
		primaryStage.setScene(primaryScene);
		
		// First maximize, otherwise display bug on startup with windows
		primaryStage.setMaximized(true);
		primaryStage.setFullScreen(true);
		primaryStage.getIcons().add(CoordzFileUtil.getResourceIcon(
			"U-Logo.png"));
		primaryStage.setTitle(TITLE);
		primaryStage.show();
	}
}