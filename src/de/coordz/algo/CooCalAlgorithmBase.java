/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2018 T.Ohm . All Rights Reserved.
 */
package de.coordz.algo;

import java.io.File;
import java.util.*;

import de.coordz.data.base.CooTarget;
import de.coordz.lap.*;
import de.coordz.lap.CooLAPPacketImpl.Result;
import de.coordz.lap.comp.CooLAPTarget;
import de.gui.CooAutoCalDialog;
import javafx.beans.property.IntegerProperty;
import javafx.collections.*;

public class CooCalAlgorithmBase extends CooCalAlgorithm
{
	/** {@link Map} with all target results */
	private Map<Short, Float> targetResults;
	/** {@link Map} with the best targets */
	private Map<Short, CooTarget> bestTargets;
	
	/** The maximum amount of progress */
	private int maxProgress;
	/** The current progress */
	private int run;
	
	public CooCalAlgorithmBase(CooLAPClient client, 
		CooAutoCalDialog dialog, File calFile)
	{
		super(client, dialog, calFile);
	}

	@Override
	public void initialize(ObservableList<CooTarget> targets)
	{
		int targetCount = targets.size();
		targetResults = new HashMap<>(targetCount);
		bestTargets = new HashMap<>(targetCount);
	}

	@Override
	public void start(ObservableList<CooTarget> targets, int range, 
		boolean adjustX, boolean adjustY, boolean adjustZ)
	{
		int targetCount = targets.size();
		int progressPerTarget = targetCount * (range * 2);
		maxProgress = adjustX ? progressPerTarget : 0;
		maxProgress += adjustY ? progressPerTarget : 0;
		maxProgress += adjustZ ? progressPerTarget : 0;
		run = 1;
		
		// Test the committed targets
		if(setRunning(initialTest(targets)))
		{
			for(short targetNo = 0; targetNo < targets.size(); targetNo++)
			{
				// Get the target
				CooTarget t = targets.get(targetNo);
				// Get the target name
				String targetName = t.nameProperty().get();
				
				if(adjustX)
				{
					// Start adjusting targets x coordinates
					log("Adjust X coord of target " + targetName);
					adjustXCoord(targets, targetNo, range);
				}
				
				if(adjustY)
				{
					// Start adjusting targets y coordinates
					log("Adjust Y coord of target " + targetName);
					adjustYCoord(targets, targetNo, range);
				}
				
				if(adjustZ)
				{
					// Start adjusting targets z coordinates
					log("Adjust Z coord of target " + targetName);
					adjustZCoord(targets, targetNo, range);
				}
				
				// Check if user canceled
				if(!isRunning())
				{
					break;
				}
			}
			
			// Print out the best target combination
			log("Best target combination was:");
			bestTargets.values().forEach(t -> log(
				CooAutoCalDialog.TAB + t.toString()));
		}
	}
	
	private boolean initialTest(ObservableList<CooTarget> targets)
	{
		boolean successfull = Boolean.FALSE;
		log("Initial calibration started...");
		
		// Start auto calibration and check result
		CooLAPPacketImpl result = calibrate(targets);
		
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
				
				log(CooAutoCalDialog.TAB + "Initial results: " 
					+ targetResults.toString());
				successfull = Boolean.TRUE;
				break;
		}
		
		return successfull;
	}
	
	private void adjustXCoord(ObservableList<CooTarget> targets, 
		short targetNo, int range)
	{
		// Get the target to adjust
		CooTarget t = targets.get(targetNo);
		// Adjust the x coordinate
		adjustCoord(targets, calFile, targetNo, t.xProperty(), range);
	}
	
	private void adjustYCoord(ObservableList<CooTarget> targets, 
		short targetNo, int range)
	{
		// Get the target to adjust
		CooTarget t = targets.get(targetNo);
		// Adjust the y coordinate
		adjustCoord(targets, calFile, targetNo, t.yProperty(), range);
	}
	
	private void adjustZCoord(ObservableList<CooTarget> targets,
		short targetNo, int range)
	{
		// Get the target to adjust
		CooTarget t = targets.get(targetNo);
		// Adjust the z coordinate
		adjustCoord(targets, calFile, targetNo, t.zProperty(), range);
	}
	
	private void adjustCoord(ObservableList<CooTarget> targets, File calFile,
		short targetNo, IntegerProperty coord, int range)
	{
		// Store the coordinate value to rest
		int startValue = coord.get();
		
		// Test the negative range
		for(int i = 0; i < range; i++)
		{
			coord.set(coord.get() - 1);
			adjustCoord(targets, calFile, targetNo);
			
			// Check if user canceled
			if(!isRunning())
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
			if(!isRunning())
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
		File calFile, short targetNo)
	{
		// Get the target by number
		CooTarget t = targets.get(targetNo);
		
		// Update the progress indicator
		log(CooAutoCalDialog.TAB + "Testing " + t.toString() + "...");
		progress("Running test " + run++ + " of " + maxProgress);
		
		// Start auto calibration and check result
		CooLAPPacketImpl result = calibrate(targets);
		
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
					success(CooAutoCalDialog.TAB + CooAutoCalDialog.TAB +
						"Deviation changed from " + deviation +
						" to " + target.get().getDeviation());
					
					// Copy the target and add it to best targets
					CooTarget targetCopy = copyTarget(t);
					bestTargets.put(targetNo, targetCopy);
					targetResults.put(lapTargetNo, deviation);
				}
			}
		}
	}
}