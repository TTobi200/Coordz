/*
 * $Header$
 * 
 * $Log$
 * Copyright � 2015 T.Ohm . All Rights Reserved.
 */
package de.gui;

import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;
import de.util.CooFileUtil;

public class CooMainFrame extends Application
{
	public static final String TITLE = "Coordz";
	private static final String FXML = "CoordzGui.fxml";

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Parent root = CooFileUtil.loadFXML(
			CooController.getInstance(primaryStage),
			CooFileUtil.FXML_FOLDER + CooFileUtil.IN_JAR_SEPERATOR + FXML);
		Scene primaryScene = new Scene(root);

		primaryScene.getStylesheets().add("/include/fxml/stylesheet.css");
		primaryStage.setScene(primaryScene);
		
		primaryStage.getIcons().add(CooFileUtil.getResourceIcon(
			"Logo.png"));
		primaryStage.setTitle(TITLE);
		primaryStage.show();
	}
}