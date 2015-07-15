/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;
import de.coordz.*;
import de.coordz.database.*;
import de.util.CoordzLoggerUtil;
import de.util.log.CoordzLog;

public class CoordzController implements Initializable
{
	public static final boolean REORG_LOGS = true;
	public static final int DAYS_TO_SAVE_LOGS = 7;
	public static final String LOGGING_FOLDER = "./logging";

	protected static CoordzController instance;
	protected Stage primaryStage;

	@FXML
	protected TreeView<CoordzProject> prjTreeView;
	@FXML
	protected Label lblPrj;

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

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		loadTestProjects();		
	}
	
	@SuppressWarnings("unchecked")
	private void loadTestProjects()
	{
		// FORTEST Create databse connection
		CoordzDatabase.createConnection();
		
		// FORTEST Add Projects to tree
		TreeItem<CoordzProject> root = new TreeItem<CoordzProject>(
			new CoordzProject("Projekte"));
		
		root.getChildren().addAll(
			new TreeItem<CoordzProject>(new CoordzProject("A2015.10051 Mischek")),
			new TreeItem<CoordzProject>(new CoordzProject("A2014.10438 SBE")),
			new TreeItem<CoordzProject>(new CoordzProject("A2014.10331 Geelen")));
		
		prjTreeView.setRoot(root);
		prjTreeView.getSelectionModel().selectedItemProperty().addListener((old, curr, newV) -> 
		{
			lblPrj.setText(newV.getValue().getName());
		});
	}
}