/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2018 T.Ohm . All Rights Reserved.
 */
package de.coordz.algo;

import java.io.*;

import de.coordz.data.base.CooTarget;
import de.coordz.lap.*;
import de.gui.CooAutoCalDialog;
import de.util.log.CooLog;
import javafx.collections.*;

public abstract class CooCalAlgorithm
{
	protected File calFile;
	protected CooLAPClient client;
	protected CooAutoCalDialog dialog;
	private boolean running;

	public CooCalAlgorithm(CooLAPClient client, CooAutoCalDialog dialog, File calFile)
	{
		this.client = client;
		this.calFile = calFile;
		this.dialog = dialog;
	}

	public abstract void initialize(ObservableList<CooTarget> targets);
	
	public abstract void start(ObservableList<CooTarget> targets, int range,
		boolean adjustX, boolean adjustY, boolean adjustZ);
	
	protected CooLAPPacketImpl calibrate(ObservableList<CooTarget> targets)
	{
		CooLAPPacketImpl result = null;
		try
		{
			// Write the new values into calibration file
			CooCalibrationFile.save(calFile, targets);
			// Start auto calibration and check result
			result = client.startAutoCalibration(calFile);
		}
		catch(IOException e)
		{
			CooLog.error("Error while calibrating system", e);
			error("Error while calibrating system - " 
				+ e.getLocalizedMessage());
		}
		return result;
	}
	
	public CooTarget copyTarget(CooTarget target)
	{
		CooTarget copy = new CooTarget();
		copy.nameProperty().set(target.nameProperty().get());
		copy.xProperty().set(target.xProperty().get());
		copy.yProperty().set(target.yProperty().get());
		copy.zProperty().set(target.zProperty().get());
		return copy;
	}
	
	public ObservableList<CooTarget> createDefaultTargets(int count)
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
	
	protected void log(String message)
	{
		dialog.log(message);
	}
	
	protected void error(String message)
	{
		dialog.error(message);
	}
	
	protected void success(String message)
	{
		dialog.success(message);
	}
	
	protected void progress(String message)
	{
		dialog.progress(message);
	}

	public boolean setRunning(Boolean running)
	{
		this.running = running;
		return running;
	}
	
	public boolean isRunning()
	{
		return running;
	}
}