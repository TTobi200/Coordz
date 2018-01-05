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
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class CooAutoCalDialog extends Stage
{
	public static final String FXML = "CooAutoCalDialog.fxml";

	public static final String TAB = "\t";
	public static final double DEF_HEIGTH = 400;
	public static final double DEF_WIDTH = 600;
	
	@FXML
	private CooTableView<CooTarget> tblTargets;
	@FXML
	private Label progress;
	@FXML
	private Spinner<Integer> spinRange;
	@FXML
	private CheckBox cbAdjustX;
	@FXML
	private CheckBox cbAdjustY;
	@FXML
	private CheckBox cbAdjustZ;
	@FXML
	private TextArea txtLog;

	/** {@link Map} with all target results */
	private Map<Integer, Double> targetResults;
	/** {@link Map} with the best targets */
	private Map<Integer, CooTarget> bestTargets;
	
	private CooLAPClient client;
	private boolean running;

	private int maxProgress;
	private int run;

	public CooAutoCalDialog(Window parent, CooLAPClient client)
	{
		init(parent, client);
	}

	private void init(Window parent, CooLAPClient client)
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
			log("Automated calibration opened...");
			log("Connected to LAP Pro Soft " + client.getSrvIp() +
				" on Port " + client.getSrvPort());
			
			// FORTEST Add some test targets
			for(int i = 1; i < 5; i++)
			{
				CooTarget t = new CooTarget();
				t.nameProperty().set("T " + i);
				tblTargets.getItems().add(t);
			}
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
		progress.setText("Process started...");
		run = 1;
		
		ObservableList<CooTarget> targets = tblTargets.getItems();
		File tempFile = File.createTempFile("tempCalibFile", ".cal");
		tempFile.deleteOnExit();
		running = Boolean.TRUE;
			
		// Create own worker thread
		Thread worker = new Thread(() -> 
		{
			try
			{
				log("Automated calibration started...");
				log("Using range " + spinRange.getValue() + "...");
				
				for(CooTarget t : targets)
				{
					// Get the target name
					String targetName = t.nameProperty().get();
					
					if(cbAdjustX.isSelected())
					{
						// Start adjusting targets x coordinates
						log("Adjust X coord of target " + targetName);
						adjustXCoord(targets, tempFile, t, spinRange.getValue());
					}
					
					// Check if user canceled
					if(!running)
					{
						break;
					}
				}
				
				// Inform user that process finished
				log("Automated calibration finished!");
				progress("Process finished");
				running = Boolean.FALSE;
				// Delete the calibration file
				tempFile.delete();
			}
			catch (Exception e) 
			{
				log("Exception occured  - " + e.getMessage());
				CooLog.error("Error while automated calibration", e);
			}
		});
		worker.setDaemon(Boolean.TRUE);
		worker.start();
	}
	
	private void adjustXCoord(ObservableList<CooTarget> targets, 
		File calFile, CooTarget t, int range) throws IOException
	{
		// Store the x coordinate to rest
		int x = t.xProperty().get();
		
		// Test the negative range
		for(int i = 0; i < range; i++)
		{
			t.xProperty().set(t.xProperty().get() - 1);
			adjustCoord(targets, calFile, t);
			
			// Check if user canceled
			if(!running)
			{
				// Reset the x coordinate
				t.xProperty().set(x);
				return;
			}
		}
		// Reset the x coordinate
		t.xProperty().set(x);
		
		// Test the positive range
		for(int i = 0; i < range; i++)
		{
			t.xProperty().set(t.xProperty().get() + 1);
			adjustCoord(targets, calFile, t);
			
			// Check if user canceled
			if(!running)
			{
				// Reset the x coordinate
				t.xProperty().set(x);
				return;
			}
		}
		// Reset the x coordinate
		t.xProperty().set(x);
	}
	
	private void adjustCoord(ObservableList<CooTarget> targets, 
		File calFile, CooTarget t) throws IOException
	{
		// Update the progress indicator
		log(TAB + "Testing " + t.toString() + "...");
		progress("Running test " + run++ + " of " + maxProgress);
		
		// Write the new values into calibration file
		CooCalibrationFile.save(calFile, targets);
		
		// Start auto calibration and check result
		CooLAPPacketImpl result = client.startAutoCalibration(calFile);
		
		if(result.getResult() == Result.SUCCESSFUL)
		{
			// Get all available targets
			ObservableList<CooLAPTarget> lapTargets = FXCollections.observableArrayList();
			result.getProjectors().forEach(p -> lapTargets.addAll(p.getTargets()));
			
			int number = targets.indexOf(t) + 1;
			Optional<CooLAPTarget> target = lapTargets.filtered(lapTarget ->
				lapTarget.getNumber() == number).stream().findFirst();
			
			if(target.isPresent())
			{
				// Get the last target deviation
				double deviation = targetResults.get(number);
				deviation = Objects.isNull(deviation) ? Double.MAX_VALUE : deviation;
				// And compare it with actual
				if(target.get().getDeviation() < deviation)
				{
					log(TAB + "Target deviation: " + t.toString());
					log(TAB + "Deviation changed from " + deviation +
						" to " + target.get().getDeviation());
					
					// Copy the target and add it to best targets
					CooTarget targetCopy = copyTarget(t);
					bestTargets.put(number, targetCopy);
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

	@FXML
	private void cancel()
	{
		if(!running)
		{
			// Process not running so close dialog
			hide();
		}

		log("Automated calibration canceled by user...");
		log("Please wait until last target scan finished...");
		running = Boolean.FALSE;
		progress.setVisible(Boolean.FALSE);
	}
	
	private void log(String message)
	{
		Platform.runLater(() -> 
		{
			txtLog.appendText(message);
			txtLog.appendText("\r\n");
		});
	}
	
	private void progress(String message)
	{
		Platform.runLater(() -> 
			progress.setText(message));
	}
}