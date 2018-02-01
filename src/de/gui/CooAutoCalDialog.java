/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2014 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.io.*;
import java.util.*;

import de.coordz.data.base.CooTarget;
import de.coordz.lap.*;
import de.coordz.lap.CooLAPPacketImpl.Result;
import de.coordz.lap.comp.CooLAPTarget;
import de.gui.comp.CooTableView;
import de.util.*;
import de.util.log.CooLog;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
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

	/** {@link Map} with all target results */
	private Map<Short, Float> targetResults;
	/** {@link Map} with the best targets */
	private Map<Short, CooTarget> bestTargets;
	
	private CooLAPClient client;
	private boolean running;

	private int maxProgress;
	private int run;

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
			
			this.client = client;
			tblTargets.setClazz(CooTarget.class);
			tblTargets.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
			
			// Check if no targets committed
			if(Objects.isNull(targets) | targets.isEmpty())
			{
				// Generate some default targets
				targets = createDefaultTargets(DEF_TARGET_COUNT);
			}
			// Add a copy of committed target
			targets.forEach(t -> tblTargets.getItems().add(copyTarget(t)));
			
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
		int targetCount = tblTargets.getItems().size();
		int progressPerTarget = targetCount * (spinRange.getValue() * 2);
		maxProgress = cbAdjustX.isSelected() ? progressPerTarget : 0;
		maxProgress += cbAdjustY.isSelected() ? progressPerTarget : 0;
		maxProgress += cbAdjustZ.isSelected() ? progressPerTarget : 0;

		targetResults = new HashMap<>(targetCount);
		bestTargets = new HashMap<>(targetCount);
		progress.setVisible(Boolean.TRUE);
		txtLog.getChildren().clear();
		progress("Process started...");
		running = Boolean.TRUE;
		run = 1;
		
		ObservableList<CooTarget> targetsToAdjust = cbOnlySelected.isSelected()
			? tblTargets.getSelectionModel().getSelectedItems() : tblTargets.getItems();
		ObservableList<CooTarget> targets = tblTargets.getItems();
		File tempFile = new File("./CoordzAutoCal.cal");
			
		// Create own worker thread
		Thread worker = new Thread(() -> 
		{
			try
			{
				log("Automated calibration started...");
				log("Using range " + spinRange.getValue() + "...");
				
				// Test the committed targets
				if(running = initialTest(targets, tempFile))
				{
					for(short targetNo = 0; targetNo < targets.size(); targetNo++)
					{
						// Get the target
						CooTarget t = targets.get(targetNo);
						// Get the target name
						String targetName = t.nameProperty().get();
						
						// Check if target should be adjusted
						if(targetsToAdjust.contains(t))
						{
							if(cbAdjustX.isSelected())
							{
								// Start adjusting targets x coordinates
								log("Adjust X coord of target " + targetName);
								adjustXCoord(targets, tempFile, targetNo, spinRange.getValue());
							}
							
							if(cbAdjustY.isSelected())
							{
								// Start adjusting targets y coordinates
								log("Adjust Y coord of target " + targetName);
								adjustYCoord(targets, tempFile, targetNo, spinRange.getValue());
							}
							
							if(cbAdjustZ.isSelected())
							{
								// Start adjusting targets z coordinates
								log("Adjust Z coord of target " + targetName);
								adjustZCoord(targets, tempFile, targetNo, spinRange.getValue());
							}
						}
						
						// Check if user canceled
						if(!running)
						{
							break;
						}
					}
					
					// Print out the best target combination
					log("Best target combination was:");
					bestTargets.values().forEach(t -> log(TAB + t.toString()));
				}
				
				// Inform user that process finished
				log("Automated calibration finished!");
				progress("Process finished");
				running = Boolean.FALSE;
//				logSeperator();
				
				// FORTEST $TO: Dont delete the calibration file
				// Delete the calibration file
//				tempFile.delete();
			}
			catch (Exception e) 
			{
				error("Exception occured  - " + e.getMessage());
				CooLog.error("Error while automated calibration", e);
			}
		});
		worker.setDaemon(Boolean.TRUE);
		worker.start();
	}
	
	private boolean initialTest(ObservableList<CooTarget> targets,
		File calFile) throws IOException
	{
		boolean successfull = Boolean.FALSE;
		log("Initial calibration started...");
		
		// Write the new values into calibration file
		CooCalibrationFile.save(calFile, targets);
		
		// Start auto calibration and check result
		CooLAPPacketImpl result = client.startAutoCalibration(calFile);	
		
		switch(result.getResult())
		{
			case FAULTY:
			case UNKNOWN:
			case NO_OPEN_FILE:
			case FILE_NOT_FOUND:
			case FILE_NOT_READABLE:
			case NO_VALID_CALIBRATION:
				error("Error <" + result.getResult().name() + 
					"> while initial calibration!");
				successfull = Boolean.FALSE;
				break;
			
			case PROJECTION_OUT_OF_RANGE:
			case MANUAL_CALIBRATION_REQUIRED:
			case SYSTEM_NOT_CALIBRATED:
				error("Please perform a manual calibration with "
					+ "file <" + calFile.getAbsolutePath() + "> first...");
				successfull = Boolean.FALSE;
				break;
				
			case SUCCESSFUL:
				// Add the lap target results
				ObservableList<CooLAPTarget> lapTargets = FXCollections.observableArrayList();
					result.getProjectors().forEach(p -> lapTargets.addAll(p.getTargets()));
				lapTargets.forEach(lapT -> targetResults.put(lapT.getNumber(), lapT.getDeviation()));
				// Add the initial targets
				for(short i = 0; i < targets.size(); i++)
				{
					bestTargets.put(i, copyTarget(targets.get(i)));
				}
				
				log(TAB + "Initial results: " + targetResults.toString());
				successfull = Boolean.TRUE;
				break;
		}
		
		return successfull;
	}

	private void adjustXCoord(ObservableList<CooTarget> targets, 
		File calFile, short targetNo, int range) throws IOException
	{
		// Get the target to adjust
		CooTarget t = targets.get(targetNo);
		// Adjust the x coordinate
		adjustCoord(targets, calFile, targetNo, t.xProperty(), range);
	}
	
	private void adjustYCoord(ObservableList<CooTarget> targets, 
		File calFile, short targetNo, int range) throws IOException
	{
		// Get the target to adjust
		CooTarget t = targets.get(targetNo);
		// Adjust the y coordinate
		adjustCoord(targets, calFile, targetNo, t.yProperty(), range);
	}
	
	private void adjustZCoord(ObservableList<CooTarget> targets, 
		File calFile, short targetNo, int range) throws IOException
	{
		// Get the target to adjust
		CooTarget t = targets.get(targetNo);
		// Adjust the z coordinate
		adjustCoord(targets, calFile, targetNo, t.zProperty(), range);
	}
	
	private void adjustCoord(ObservableList<CooTarget> targets, File calFile,
		short targetNo, IntegerProperty coord, int range) throws IOException
	{
		// Store the coordinate value to rest
		int startValue = coord.get();
		
		// Test the negative range
		for(int i = 0; i < range; i++)
		{
			coord.set(coord.get() - 1);
			adjustCoord(targets, calFile, targetNo);
			
			// Check if user canceled
			if(!running)
			{
				// Reset the coordinate value
				coord.set(startValue);
				return;
			}
		}
		// Reset the coordinate value
		coord.set(startValue);
		
		// Test the positive range
		for(int i = 0; i < range; i++)
		{
			coord.set(coord.get() + 1);
			adjustCoord(targets, calFile, targetNo);
			
			// Check if user canceled
			if(!running)
			{
				// Reset the coordinate value
				coord.set(startValue);
				return;
			}
		}
		// Reset the coordinate value
		coord.set(startValue);
	}
	
	private void adjustCoord(ObservableList<CooTarget> targets, 
		File calFile, short targetNo) throws IOException
	{
		// Get the target by number
		CooTarget t = targets.get(targetNo);
		
		// Update the progress indicator
		log(TAB + "Testing " + t.toString() + "...");
		progress("Running test " + run++ + " of " + maxProgress);
		
		// Write the new values into calibration file
		CooCalibrationFile.save(calFile, targets);
		
		// Start auto calibration and check result
		CooLAPPacketImpl result = client.startAutoCalibration(calFile);
		
		if(result.getResult() == Result.SUCCESSFUL)
		{
			// Define the lapTargetNo
			short lapTargetNo = (short)(targetNo + 1);
			// Get all available targets
			ObservableList<CooLAPTarget> lapTargets = FXCollections.observableArrayList();
			result.getProjectors().forEach(p -> lapTargets.addAll(p.getTargets()));
			// Find the current target in lap return package
			Optional<CooLAPTarget> target = lapTargets.filtered(lapTarget ->
				lapTarget.getNumber() == lapTargetNo).stream().findFirst();
			
			if(target.isPresent())
			{
				// Get the last target deviation
				Float deviation = targetResults.get(lapTargetNo);
				deviation = Objects.isNull(deviation) ? Float.MAX_VALUE : deviation;
				// And compare it with actual
				if(target.get().getDeviation() < deviation)
				{
					success(TAB + TAB + "Deviation changed from " + deviation +
						" to " + target.get().getDeviation());
					
					// Copy the target and add it to best targets
					CooTarget targetCopy = copyTarget(t);
					bestTargets.put(targetNo, targetCopy);
					targetResults.put(lapTargetNo, deviation);
				}
			}
		}
	}
	
	protected CooTarget copyTarget(CooTarget target)
	{
		CooTarget copy = new CooTarget();
		copy.nameProperty().set(target.nameProperty().get());
		copy.xProperty().set(target.xProperty().get());
		copy.yProperty().set(target.yProperty().get());
		copy.zProperty().set(target.zProperty().get());
		return copy;
	}
	
	private static ObservableList<CooTarget> createDefaultTargets(int count)
	{
		ObservableList<CooTarget> targets = FXCollections.observableArrayList();
		
		for(int i = 1; i <= count; i++)
		{
			CooTarget t = new CooTarget();
			t.nameProperty().set("T " + i);
			targets.add(t);
		}
		return targets;
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
		progress.setVisible(Boolean.FALSE);
	}
	
	protected void error(String message)
	{
		log(message, Color.RED);
	}
	
	protected void success(String message)
	{
		log(message, Color.BLUE);
	}
	
	protected void log(String message)
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
	
	private void progress(String message)
	{
		Platform.runLater(() -> 
			progress.setText(message));
	}
}