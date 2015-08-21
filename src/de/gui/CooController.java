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

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;
import de.coordz.CooSystem;
import de.coordz.data.CooCustomer;
import de.gui.comp.CooCustomerTreeItem;
import de.gui.pnl.*;
import de.gui.sett.*;
import de.gui.sett.CooSettingsDialog.SettingType;
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
	
	@FXML
	protected TabPane tabPane;
	
	private Property<String> xmlDbPath;

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
			xmlDbPath = new SimpleStringProperty();
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
		
//		File lastOpened = new File(CooSystemPreferences.getSystemPreferences().getString(
//			CooSystemPreferences.GENERAL_LAST_OPENED));
//		if(Objects.nonNull(lastOpened) && lastOpened.exists())
//		{
//			openXMLDB(lastOpened);
//		}
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
		openXMLDB(CooDialogs.showOpenFolderDialog(primaryStage, 
						"Datenbank öffnen"));
	}
	
	@FXML
	protected void exit()
	{
		CooSystem.exit();
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	protected void openSettings()
	{
		// FORTEST try out the new settings dialog ^_^
		
		// Create the dialog
		CooSettingsDialog dialog = new CooSettingsDialog(primaryStage, "Coordz",
			CooFileUtil.getResourceIcon("Logo.png"));

		// Add the settings - text, bool, combo
		dialog.addSetting("Allgemein", "Datei", SettingType.TEXT);
		dialog.addSetting("Allgemein", "Automatisch verbinden",
			SettingType.BOOLEAN);
		dialog.addSetting("Allgemein", "Sprache", SettingType.COMBO,
			"Deutsch", "Englisch");

		dialog.addSetting("Anzeige", "Name", SettingType.TEXT);
		dialog.addSetting("Anzeige", "Animiert", SettingType.BOOLEAN);
		dialog.addSetting("Anzeige", "Style", SettingType.COMBO,
			"Style 1", "Style 2");

		// LOOK HERE - connect user property to setting
		xmlDbPath.bind((ObservableValue<String>)dialog.addSetting(
			"XML-Datenbank", "Pfad", SettingType.TEXT, xmlDbPath.getValue()));
		dialog.addSetting("XML-Datenbank", "Automatisch laden",
			SettingType.BOOLEAN);
		
		dialog.addSetting("Datenbank", "Name", SettingType.TEXT);
		dialog.addSetting("Datenbank", "Treiber", SettingType.TEXT);
		dialog.addSetting("Datenbank", "Benutzer", SettingType.TEXT);
		dialog.addSetting("Datenbank", "Passwort", SettingType.TEXT);

		dialog.addSetting("Update", "Version", SettingType.TEXT);
		dialog.addSetting("Update", "Automatisch überprüfen",
			SettingType.BOOLEAN);

		// User decides what to do on cancel
		dialog.setOnCancel(l ->
		{
			if(CooDialogs.showConfirmDialog(dialog, "Änderungen verwerfen",
				"Möchten Sie wirklich abbrechen?"))
			{
				dialog.close();
			}
		});

		// User decides what to do on save
		dialog.setOnSave(l ->
		{
			dialog.close();
		});

		CooGuiUtil.grayOutParent(primaryStage, 
			dialog.showingProperty());
		dialog.showAndWait();
	}
	
	protected void openXMLDB(final File xmlDBFolder)
	{
		if(Objects.nonNull(xmlDBFolder))
		{
			CooDialogs.showProgressDialog(primaryStage, 
				"Stammdaten aus Datenbank laden", new Task<Void>()
				{
					@Override
					protected Void call() throws Exception
					{
						// Create Tree Root
						CooCustomerTreeItem root = new CooCustomerTreeItem(
							new SimpleStringProperty("Kunden"), new CooCustomer());
						// Load all customers from xml DB
						List<CooCustomer> customers = CooXMLDBUtil.getAllCustomers(
							xmlDBFolder);
						
						// Add Customers to Tree Root
						for(int i = 0; i < customers.size(); i++)
						{
							CooCustomer c = customers.get(i);
							updateMessage("Lädt " + c.nameProperty().get());
							CooCustomerTreeItem customer = new CooCustomerTreeItem(
								c.nameProperty(), c);
							root.getChildren().add(customer);
							updateProgress(i + 1, customers.size());
						}
						
						updateProgress(1, 1);
						updateMessage("Stammdaten erfolgreich geleaden!");
						treeViewPnl.getPrjTreeView().setRoot(root);
						root.setExpanded(true);
						xmlDbPath.setValue(xmlDBFolder.getAbsolutePath());
						return null;
					}
				});
		}
	}
}