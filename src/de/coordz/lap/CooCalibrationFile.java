/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap;

import java.io.*;
import java.util.*;

import de.coordz.data.base.CooTarget;
import de.util.log.CooLog;
import javafx.collections.FXCollections;

/**
 * Utility class to {@link #load(File)} and {@link #save(File, List)}
 * {@link CooTarget} to/from an {@link File}. 
 */
public class CooCalibrationFile
{
	/** Constant that defines a target declaration */
	public static final String TARGET_DECLARATION = "T ";
	/** Constant for data separator */
	public static final String SEPARATOR = "		";
	/** Constant for the value format */
	private static final String VALUE_FORMAT = "%-4d";
	
	/**
	 * Method to save {@link CooTarget} to calibration {@link File}.
	 * @param calFile = the calibration {@link File} to save
	 * @param targets = the {@link CooTarget} to save
	 */
	public static void save(File calFile, List<CooTarget> targets)
	{
		// Open PrintWriter output stream
		try(PrintWriter out = new PrintWriter(calFile))
		{
			// Print the amount of targets
			out.println(targets.size());
			
			// Add the targets to file
			for(int i = 0; i < targets.size(); i++)
			{
				// Get the target for specified index
				CooTarget target = targets.get(i);
				
				// Print out the single target data
				out.print(TARGET_DECLARATION + (i + 1));
				out.print(SEPARATOR);
				out.print(String.format(VALUE_FORMAT,
					target.xProperty().get()));
				out.print(SEPARATOR);
				out.print(String.format(VALUE_FORMAT,
					target.yProperty().get()));
				out.print(SEPARATOR);
				out.print(String.format(VALUE_FORMAT,
					target.zProperty().get()));
				out.println();
			}
		}
		catch(IOException e)
		{
			CooLog.error("Error while saving calibration file", e);
		}
	}
	
	/**
	 * Method to load a {@link CooCalibrationFile}
	 * from committed {@link File}.
	 * @param calFile = the {@link File} to load
	 * @return the loaded {@link CooTarget}'s
	 */
	public static List<CooTarget> load(File calFile)
	{
		// Create a list with targets
		List<CooTarget> targets = FXCollections
			.observableArrayList();
		
		// Read in the calibration file
		try(BufferedReader in = new BufferedReader(
			new FileReader(calFile)))
		{
			// Skip the first line with targets amount
			in.readLine();
				
			String line = null;
			while(Objects.nonNull(line = in.readLine()))
			{
				// Split the line on spaces 2-4
				String[] split = line.split("[\\s+]{2,4}");

				// Check if we have all data 
				if(split.length == 4)
				{
					// Create a new target and fill the data
					CooTarget target = new CooTarget();
					target.nameProperty().set(split[0]);
					target.xProperty().set(Integer.valueOf(split[1]));
					target.yProperty().set(Integer.valueOf(split[2]));
					target.zProperty().set(Integer.valueOf(split[3]));
					
					// Add the target to target list
					targets.add(target);
				}
			}
		}
		catch(IOException e)
		{
			CooLog.error("Error while loading calibration file", e);
		}
		
		// Create a new calibration file with targets
		return targets;
	}
}