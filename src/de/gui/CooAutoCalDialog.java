/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2014 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.io.*;
import java.util.Objects;

import de.coordz.algo.*;
import de.coordz.data.base.CooTarget;
import de.coordz.lap.CooLAPClient;
import de.gui.comp.CooTableView;
import de.util.*;
import de.util.log.CooLog;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.*;

public class CooAutoCalDialog extends Stage
{
	public static final String FXML = "CooAutoCalDialog.fxml";

	public static final String TAB = "\t";
	public static final int DEF_TARGET_COUNT = 4;
	public static final double DEF_HEIGTH = 400;
	public static final double DEF_WIDTH = 600;
	
	@FXML
	private CooTableView<CooTarget> tblTargets;
	@FXML
	private Label progress;
	@FXML
	private CheckBox cbOnlySelected;
	@FXML
	private Spinner<Integer> spinRange;
	@FXML
	private CheckBox cbAdjustX;
	@FXML
	private CheckBox cbAdjustY;
	@FXML
	private CheckBox cbAdjustZ;
	@FXML
	private TextFlow txtLog;
	@FXML
	private ScrollPane scrollPane;

	private boolean running;

	private CooCalAlgorithm algo;

	public CooAutoCalDialog(Window parent, CooLAPClient client)
	{
		this(parent, client, FXCollections.observableArrayList());
	}
	
	public CooAutoCalDialog(Window parent, CooLAPClient client,
			ObservableList<CooTarget> targets)
	{
		init(parent, client, targets);
	}

	private void init(Window parent, CooLAPClient client,
			ObservableList<CooTarget> targets)
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
			
			tblTargets.setClazz(CooTarget.class);
			tblTargets.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
			
			// Create the algorithm
			File tempFile = new File("./CoordzAutoCal.cal");
			algo = new CooCalAlgorithmBase(
				client, this, tempFile);
			
			// Check if no targets committed
			if(Objects.isNull(targets) | targets.isEmpty())
			{
				// Generate some default targets
				targets = algo.createDefaultTargets(DEF_TARGET_COUNT);
			}
			// Add a copy of committed target
			targets.forEach(t -> tblTargets.getItems().add(algo.copyTarget(t)));
			algo.initialize(targets);
			
			log("Automated calibration opened...");
			log("Connected to LAP Pro Soft " + client.getSrvIp() +
				" on Port " + client.getSrvPort());
		}
		catch(IOException e)
		{
			CooLog.error("Error while initializing " +
				getClass().getName(), e);
		}
	}
	
	@FXML
	private void start() throws IOException
	{
		progress.setVisible(Boolean.TRUE);
		txtLog.getChildren().clear();
		progress("Process started...");
		running = Boolean.TRUE;
		
		ObservableList<CooTarget> targets = tblTargets.getItems();
		
		// Create own worker thread
		Thread worker = new Thread(() -> 
		{
			try
			{
				log("Automated calibration started...");
				log("Using range " + spinRange.getValue() + "...");
				
				algo.setRunning(Boolean.TRUE);
				algo.start(targets, spinRange.getValue(), cbAdjustX.isSelected(),
					cbAdjustY.isSelected(), cbAdjustZ.isSelected());
				
				log("Automated calibration finished!");
				progress("Process finished");
				running = Boolean.FALSE;
				
//				 Delete the calibration file
//				tempFile.delete();
			}
			catch (Exception e) 
			{
				error("Exception occured - stopping process!");
				CooLog.error("Error while automated calibration", e);
				running = Boolean.FALSE;
			}
		});
		worker.setDaemon(Boolean.TRUE);
		worker.start();
	}
	
	@FXML
	private void cancel()
	{
		if(!running)
		{
			// Process not running so close dialog
			hide();
		}

		error("Automated calibration canceled by user...");
		error("Please wait until last target scan finished...");
		running = Boolean.FALSE;
		algo.setRunning(Boolean.FALSE);
		progress.setVisible(Boolean.FALSE);
	}
	
	public void error(String message)
	{
		log(message, Color.RED);
	}
	
	public void success(String message)
	{
		log(message, Color.BLUE);
	}
	
	public void log(String message)
	{
		log(message, Color.BLACK);
	}
	
	protected void logSeperator()
	{
		// Create a separator 
		Separator separator = new Separator(Orientation.HORIZONTAL);
		separator.prefWidthProperty().bind(txtLog.widthProperty());
		log(separator);
	}
	
	protected void log(String message, Color color)
	{
		Text text = new Text(message);
		text.setFill(color);
		log(text, new Text(System.lineSeparator()));
	}
	
	private void log(Node... n)
	{
		Platform.runLater(() -> 
		{
			txtLog.getChildren().addAll(n);
			scrollPane.getParent().layout();
			scrollPane.setVvalue(1.0);
		});
	}
	
	public void progress(String message)
	{
		Platform.runLater(() -> 
			progress.setText(message));
	}
}