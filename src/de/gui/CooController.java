/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.io.*;
import java.net.URL;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;
import de.coordz.CooSystem;
import de.coordz.data.CooCustomer;
import de.coordz.doc.CooDocument.Content;
import de.coordz.doc.*;
import de.gui.comp.CooCustomerTreeItem;
import de.gui.pnl.*;
import de.util.*;
import de.util.log.CooLog;
import de.util.pref.CooSystemPreferences;

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
	
	@FXML
	protected TabPane tabPane;

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
		treeViewPnl.addDataChangedListener(coreDataPnl);
		treeViewPnl.addDataChangedListener(projectDataPnl);
		treeViewPnl.addDataChangedListener(measurementsPnl);
		
		CooTabPaneDetacherUtil.create().makeTabsDetachable(tabPane);
		
		File lastOpened = new File(CooSystemPreferences.getSystemPreferences().getString(
			CooSystemPreferences.GENERAL_LAST_OPENED));
		if(Objects.nonNull(lastOpened) && lastOpened.exists())
		{
			openXMLDB(lastOpened);
		}
	}
	
	@FXML
	protected void newXMLDB()
	{
		File newDBFolder = CooDialogs.showOpenFolderDialog(primaryStage,
			"Neue Coordz DB");
		
		if(Objects.nonNull(newDBFolder))
		{
			openXMLDB(newDBFolder);
		}
	}

	@FXML
	protected void openXMLDB()
	{
		openXMLDB(null);
	}
	
	@FXML
	protected void exit()
	{
		CooSystem.exit();
	}
	
	protected void openXMLDB(File xmlDBFolder)
	{
		if(Objects.isNull(xmlDBFolder))
		{
			xmlDBFolder = CooDialogs.showOpenFolderDialog(primaryStage, 
				"Datenbank öffnen");
		}
		
		if(Objects.nonNull(xmlDBFolder))
		{
			// Create Tree Root
			CooCustomerTreeItem root = new CooCustomerTreeItem(
				new SimpleStringProperty("Kunden"), new CooCustomer());
			// Load all customers from xml DB
			List<CooCustomer> customers = CooXMLDBUtil.getAllCustomers(
				xmlDBFolder);
			
//			// FORTEST Save customer as pdf
			CooPdfDocument pdf = new CooPdfDocument(customers.get(1));
			pdf.addContent(
				Content.CUSTOMER,
				Content.CONTACTS);
			
			pdf.save(new File("./" + customers.get(1).nameProperty().get() + ".pdf"));
			
			// Add Customers to Tree Root
			customers.forEach(c ->
			{
				CooCustomerTreeItem customer = new CooCustomerTreeItem(
					c.nameProperty(), c);
				root.getChildren().add(customer);
			});
			
			treeViewPnl.getPrjTreeView().setRoot(root);
			root.setExpanded(true);
			CooSystemPreferences.getSystemPreferences()
				.getStringProperty(CooSystemPreferences.GENERAL_LAST_OPENED)
				.setValue(xmlDBFolder.getAbsolutePath());
		}
	}
}