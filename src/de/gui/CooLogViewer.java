/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2014 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import de.coordz.CooSystem;
import de.util.*;
import de.util.log.CooLog;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.ImageView;
import javafx.stage.*;

public class CooLogViewer extends Stage
{
	public static final String FXML = "CooLogViewer.fxml";

	public static final double DEF_HEIGTH = 350;
	public static final double DEF_WIDTH = 600;

	@FXML
	protected TreeView<String> treeLog;
	@FXML
	protected TabPane tabPaneLog;

	public CooLogViewer(Window parent, File logFold)
	{
		init(parent, logFold);
	}

	private void init(Window parent, File logFold)
	{
		try
		{
			Parent root = CooFileUtil.loadFXML(this,
				CooFileUtil.FXML_FOLDER + CooFileUtil.IN_JAR_SEPERATOR + FXML);

			setScene(new Scene(root));
			setHeight(DEF_HEIGTH);
			setWidth(DEF_WIDTH);
			setTitle(CooMainFrame.TITLE);
			getIcons().add(CooFileUtil.getResourceIcon(
				"Logo.png"));
			CooGuiUtil.setModality(this, parent);
			CooGuiUtil.grayOutParent(parent,
				showingProperty());

			initTree(logFold);
		}
		catch(IOException e)
		{
			CooLog.error("Error while initializing " +
				getClass().getName(), e);
		}
	}

	private void initTree(File logFold)
	{
		TreeItem<String> rootItem = new TreeItem<>(
			CooSystem.LOGGING_FOLDER,
			new ImageView(CooFileUtil.getResourceIcon("logging.png")));
		Map<String, List<File>> dateToFiles = CooLoggerUtil.mapLogs(logFold);

		dateToFiles.keySet().forEach(date ->
		{
			TreeItem<String> itmLogDate = new TreeItem<>(date);
			dateToFiles.get(date).forEach(f ->
				itmLogDate.getChildren().add(new LogFoldTreeItem(f)));

			rootItem.getChildren().add(itmLogDate);
		});

		rootItem.setExpanded(true);
		tabPaneLog.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		treeLog.setRoot(rootItem);
		treeLog.getSelectionModel()
			.selectedItemProperty()
			.addListener(
				(ChangeListener<TreeItem<String>>)(observable, oldValue,
								selectedItem) ->
				{
					if(selectedItem instanceof LogFoldTreeItem)
					{
						showLogFile((LogFoldTreeItem)(selectedItem));
					}
				});
	}

	private void showLogFile(LogFoldTreeItem itm)
	{
		try
		{
			String fileName = itm.getValue();

			// Search if already opened the file in one tab
			ObservableList<Tab> openedTabs = tabPaneLog.getTabs();
			for(Tab tab : openedTabs)
			{
				if(tab.getText().equals(fileName))
				{
					// Switch to already opened tab
					tabPaneLog.getSelectionModel().select(tab);
					return;
				}
			}

			// Otherwise create a new tab with log file content
			Tab tabLog = new Tab(fileName);
			tabLog.setGraphic(genIcon(fileName));
			TextArea txtLog = new TextArea(new String(
				Files.readAllBytes(itm.getLogFile().toPath())));
			tabLog.setContent(txtLog);

			tabPaneLog.getTabs().add(tabLog);
			tabPaneLog.getSelectionModel().select(tabLog);
		}

		catch(IOException e)
		{
			CooLog.error("Error while opening logfile", e);
		}
	}

	public Node genIcon(String name)
	{
		String icon = null;
		
		if(name.endsWith(CooLoggerUtil.DEBUG_FILE_EXT))
		{
			icon = "debug.png";
		}
		else if(name.endsWith(CooLoggerUtil.ERR_FILE_EXT))
		{
			icon = "stop.png";
		}
		
		return new ImageView(CooFileUtil.getResourceIcon(icon));
	}

	@FXML
	private void doDelete()
	{
		treeLog.getSelectionModel()
			.getSelectedItems()
			.forEach(
				itm ->
				{
					treeLog.getRoot().getChildren().remove(itm);
					itm.getChildren()
						.filtered(child -> child instanceof LogFoldTreeItem)
						.forEach(
							child -> ((LogFoldTreeItem)child).getLogFile()
								.delete());
				});
	}

	public class LogFoldTreeItem extends TreeItem<String>
	{
		private File logFile;

		public LogFoldTreeItem(File logFile)
		{
			super(logFile.getName());
			setGraphic(genIcon(logFile.getName()));

			this.logFile = logFile;
		}

		public File getLogFile()
		{
			return logFile;
		}
	}
}