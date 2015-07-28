/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.Label;
import javafx.stage.*;
import de.coordz.CooSystem;
import de.coordz.data.CooCustomer;
import de.gui.comp.*;
import de.gui.pnl.*;
import de.util.*;
import de.util.log.CooLog;

public class CooController implements Initializable
{
	public static final boolean REORG_LOGS = true;
	public static final int DAYS_TO_SAVE_LOGS = 7;
	public static final String LOGGING_FOLDER = "./logging";

	protected static CooController instance;
	protected Stage primaryStage;

	@FXML
	protected CooCoreDataPnl coreDataPnl;
	
	@FXML
	protected CooTreeViewPnl treeViewPnl;
	@FXML
	protected CooProjectDataPnl projectDataPnl;
	@FXML
	protected CooMeasurementsPnl measurementsPnl;
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
		treeViewPnl.addDataChangedListener(coreDataPnl);
		treeViewPnl.addDataChangedListener(projectDataPnl);
		treeViewPnl.addDataChangedListener(measurementsPnl);
	}

	private void loadTestProjects()
	{
		// Create Tree Root
		CooCustomerTreeItem root = new CooCustomerTreeItem(
			new SimpleStringProperty("Kunden"), new CooCustomer());
		// Load all customers from xml DB
		List<CooCustomer> customers = CooXMLDBUtil.getAllCustomers();

		// Add Customers to Tree Root
		customers.forEach(c ->
		{
			CooCustomerTreeItem customer = new CooCustomerTreeItem(
				c.nameProperty(), c);
			root.getChildren().add(customer);
		});

		treeViewPnl.getPrjTreeView().setRoot(root);
		root.setExpanded(true);
	}
}