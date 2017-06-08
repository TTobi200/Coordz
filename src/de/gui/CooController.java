/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileLock;
import java.util.*;

import de.coordz.CooSystem;
import de.coordz.data.CooCustomer;
import de.gui.comp.*;
import de.gui.pnl.*;
import de.gui.sett.CooSettingsDialog;
import de.gui.sett.CooSettingsDialog.SettingType;
import de.util.*;
import de.util.log.CooLog;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class CooController implements Initializable, CooDataChanged
{
	public static final boolean REORG_LOGS = true;
	public static final int DAYS_TO_SAVE_LOGS = 7;
	public static final String LOGGING_FOLDER = "./logging";

	public static final String DOCUMENT_FOLDER = "./doc";

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
	protected Menu menuDocs;

	@FXML
	protected TabPane tabPane;
	@FXML
	protected Tab tabGallery;
	
	@FXML
	protected CooImageGallery imageGallery;

	private Property<String> xmlDbPath;
	private RandomAccessFile randomAccessFile;
	private FileLock fileLock;
	private CooCustomer customer;

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
			CooLog.error("Error while initializing "
				+ "controller.", e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		treeViewPnl.addDataChangedListener(coreDataPnl);
		treeViewPnl.addDataChangedListener(projectDataPnl);
		treeViewPnl.addDataChangedListener(measurementsPnl);
		treeViewPnl.addDataChangedListener(this);

		// Make panes detachable
		CooTabPaneDetacherUtil.create().makeTabsDetachable(tabPane);

		// Add documents to GUI
		CooGuiUtil.addDocToMenu(menuDocs, new File(DOCUMENT_FOLDER));
		
		// FIXME: $TO: TabPanes loose selection when they where detached
		// Load the Images when tab selected
		tabGallery.setOnSelectionChanged(e -> imageGallery.loadImages(
			CooXMLDBUtil.getImagesFolder(customer), tabGallery.isSelected()));

		// FORTEST load the coordz xml database
//		primaryStage.setOnShowing(e -> openXMLDB(new File("D:/Unitechnik/repos/Coordz/CoordzXML")));
	}
	
	@Override
	public void customerChanged(CooCustomer customer)
	{
		// Store the last selected customer
		this.customer = customer;
		
		// Refresh the gallery when selected
		if(tabGallery.isSelected())
		{
			// Load the images for selected customer
			imageGallery.loadImages(CooXMLDBUtil.getImagesFolder(
				customer), Boolean.TRUE);
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
		openXMLDB(CooDialogs.showOpenFolderDialog(primaryStage,
			"Datenbank öffnen"));
	}

	@FXML
	protected void exit()
	{
		CooSystem.exit();
	}
	
	@FXML
	public void openLogging() throws Exception
	{
		new CooLogViewer(primaryStage, new File(
			LOGGING_FOLDER)).show();
	}
	
	@FXML
	protected void cut()
	{
		CooGuiUtil.cut(primaryStage.getScene());
	}
	
	@FXML
	protected void copy()
	{
		CooGuiUtil.copy(primaryStage.getScene());
	}
	
	@FXML
	protected void paste()
	{
		CooGuiUtil.paste(primaryStage.getScene());
	}

	@SuppressWarnings("unchecked")
	@FXML
	protected void openSettings()
	{
		// FORTEST try out the new settings dialog ^_^

		// Create the dialog
		CooSettingsDialog dialog = new CooSettingsDialog(primaryStage,
			"Coordz",
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

		dialog.addSetting("LAP Software", "Bereichsaufteilung mit Überlappung", SettingType.BOOLEAN);
		dialog.addSetting("LAP Software", "Bereichsaufteilung Überlappung", SettingType.TEXT);

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
			try
			{
				// Check if the xml database already opened by other program
				final File file = new File(xmlDBFolder, "CoordzXMLDatabase");
				randomAccessFile = new RandomAccessFile(file, "rw");
				
				// Check if we have already opened the database
//				if(!CooSystem.getComputerName()
//					.equals(randomAccessFile.readLine()))
//				{
					// Check if other process has opened the database
					fileLock = randomAccessFile.getChannel().tryLock();
					// Check if we have a file lock
					if(Objects.isNull(fileLock))
					{
						CooDialogs.showErrorDialog(primaryStage, 
							"Datenbank Zugriff felgeschlagen",
							"Die Datenbank ist bereits in einem "
							+ "anderen Programm geöffnet.");
						return;
					}
					
					// We have access to the database
					// Store our name in file an continue
//					randomAccessFile.writeUTF(CooSystem.getComputerName());
//				}
			}
			catch(IOException e)
			{
				CooDialogs.showExceptionDialog(primaryStage, 
					"Fehler beim öffnen des DB Locks", e);
			}
			
			CooDialogs.showProgressDialog(primaryStage,
				"Stammdaten aus Datenbank laden", new Task<Void>()
			{
				@Override
				protected Void call() throws Exception
				{
					// Create Tree Root
					CooCustomerTreeItem root = new CooCustomerTreeItem(
						new SimpleStringProperty("Kunden"),
						new CooCustomer());
					// Load all customers from xml DB
					List<CooCustomer> customers = null;
					try
					{
						customers = CooXMLDBUtil.getAllCustomers(
							xmlDBFolder);
					}
					catch(Exception e)
					{
						updateMessage("Fehler beim Laden der XML-DB");
						CooDialogs.showExceptionDialog(primaryStage, 
							"Fehler beim Laden der XML-DB", e);
						updateProgress(0, 0);
						return null;
					}
						if(Objects.nonNull(customers))
					{
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
					}
					return null;
				}
			});
		}
	}
}