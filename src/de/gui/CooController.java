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
import de.coordz.CooSystem;
import de.coordz.data.CooProject;
import de.coordz.data.db.CooDB;
import de.util.CooLoggerUtil;
import de.util.log.CooLog;

public class CooController implements Initializable
{
	public static final boolean REORG_LOGS = true;
	public static final int DAYS_TO_SAVE_LOGS = 7;
	public static final String LOGGING_FOLDER = "./logging";

	protected static CooController instance;
	protected Stage primaryStage;

	@FXML
	protected TreeView<CooProject> prjTreeView;
	@FXML
	protected Label lblPrj;

	public static Object getInstance(Stage primaryStage)
	{
		if(instance == null)
		{
			instance = new CooController(primaryStage);
		}
		return instance;
	}

	public CooController(Stage primaryStage)
	{
		try
		{
			this.primaryStage = primaryStage;
			CooLoggerUtil.initLogging(LOGGING_FOLDER, DAYS_TO_SAVE_LOGS,
				REORG_LOGS);

			// Exit System on close requested
			primaryStage.setOnCloseRequest(
				new EventHandler<WindowEvent>()
				{
					@Override
					public void handle(final WindowEvent event)
					{
						event.consume();
						CooSystem.exit();
					}
				});
		}
		catch(IOException e)
		{
			CooLog.error("Error while initializing controller.",
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
		CooDB db = CooSystem.getSystemDatabase();
		db.connect("CoordzDB", "ut", "ut", true);

		// FORTEST Add Projects to tree
		TreeItem<CooProject> root = new TreeItem<CooProject>(
			new CooProject("Kunde"));

		TreeItem<CooProject> itmMischek = new TreeItem<CooProject>(new CooProject("Mischek"));
		TreeItem<CooProject> itmSBE = new TreeItem<CooProject>(new CooProject("SBE"));
		
		itmMischek.getChildren().add(new TreeItem<CooProject>(new CooProject(
			"A2015.10051 Laser Einrichtung")));
		itmMischek.getChildren().add(new TreeItem<CooProject>(new CooProject(
			"A2015.10084 Laser Update")));
		
		root.getChildren().addAll(itmMischek, itmSBE);

		prjTreeView.setRoot(root);
//		prjTreeView.getSelectionModel()
//			.selectedItemProperty()
//			.addListener((old, curr, newV) ->
//			{
//				lblPrj.setText(newV.getValue().getName());
//			});
	}
}