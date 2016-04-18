/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.util.*;

import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;
import de.util.CooFileUtil;

public class CooMainFrame extends Application
{
	public static final String TITLE = "Coordz";
	private static final String FXML = "CoordzGui.fxml";
	
	protected static List<Runnable> onShowing;
	protected static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		CooMainFrame.primaryStage = primaryStage;
		onShowing = new LinkedList<>();
		addListener(primaryStage);
		
		Parent root = CooFileUtil.loadFXML(
			CooController.getInstance(primaryStage),
			CooFileUtil.FXML_FOLDER + CooFileUtil.IN_JAR_SEPERATOR + FXML);
		Scene primaryScene = new Scene(root);

		primaryScene.getStylesheets().add("/include/fxml/stylesheet.css");
		primaryStage.setScene(primaryScene);
		
		primaryStage.getIcons().add(CooFileUtil.getResourceIcon(
			"Logo.png"));
		primaryStage.setTitle(TITLE);
		primaryStage.setWidth(1000);
		primaryStage.setHeight(600);
		primaryStage.show();
	}
	
	private void addListener(Stage stage)
	{
		stage.showingProperty().addListener((p, o, n) ->
		{
			List<Runnable> copy = new ArrayList<>(onShowing);
			onShowing.clear();
			copy.forEach(Runnable::run);
		});
	}

	public static void doOnShowing(Runnable task)
	{
		if(primaryStage != null && primaryStage.isShowing())
		{
			task.run();
		}
		else if(onShowing != null && !onShowing.contains(task))
		{
			onShowing.add(task);
		}
	}
}