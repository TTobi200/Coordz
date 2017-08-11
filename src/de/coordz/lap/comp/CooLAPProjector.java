/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap.comp;

import de.coordz.lap.CooLAPPacket;
import javafx.collections.*;

/**
 * Class that implements an {@link CooLAPComponent} for
 * {@link CooLAPPacket} projector data.
 * 
 * @author tobias.ohm
 * @version 1.0
 */
public class CooLAPProjector implements CooLAPComponent
{
	/**
	 * Enum that describes projector results.
	 */
	public enum ProjectorResult 
	{
		SUCCESSFUL,
		CALIBRATION_RESULT_EXCEEDS_LIMIT,
		AT_LEAST_ONE_TARGET_NOT_FOUND,
		UNKNOWN;
		
		/**
		 * Method to get {@link ProjectorResult} for committed enum ordinal.
		 * @param ordinal = the ordinal to get {@link ProjectorResult} for
		 * @return the {@link ProjectorResult} for committed ordinal
		 */
		public static ProjectorResult valueOf(int ordinal)
		{
			return ordinal == SUCCESSFUL.ordinal() ? SUCCESSFUL : 
				ordinal == CALIBRATION_RESULT_EXCEEDS_LIMIT.ordinal() ? CALIBRATION_RESULT_EXCEEDS_LIMIT : 
				ordinal == AT_LEAST_ONE_TARGET_NOT_FOUND.ordinal() ? AT_LEAST_ONE_TARGET_NOT_FOUND : 
					UNKNOWN;
		}
	}
	
	/** The number of the corresponding projector */
	private int proj;
	
	/** The name of the projector */
	private String name;
	/** The address of the projector */
	private short address;
	/** The result of the projector */
	private ProjectorResult result;
	/** The actual RMS of the projector */
	private float rms;
	
	/** {@link ObservableList} with all {@link CooLAPTarget} of this projector */
	private ObservableList<CooLAPTarget> targets;
	
	/**
	 * Constructor to create an {@link CooLAPProjector}.
	 * @param proj = the projector id
	 */
	public CooLAPProjector(int proj)
	{
		this.proj = proj;
	}
	
	@Override
	public void fromPacket(CooLAPPacket packet)
	{
		// Create list with all targets
		targets = FXCollections.observableArrayList();
		
		// First check if we have specified projector set
		if(packet.containsValue("ProjName_" + proj))
		{
			name = String.valueOf(packet.getValue("ProjName_" + proj));
			address = (short)packet.getValue("ProjAddr_" + proj);
			result = ProjectorResult.valueOf((short)packet.getValue("ProjRes_" + proj));
			rms = (float)packet.getValue("ProjRMS_" + proj);
			
			// Number of targets for first projector
			short tgtCount = (short)packet.getValue("TgtCount_" + proj);
			
			// Loop through all targets of projector
			for(int tgt = 1; tgt <= tgtCount; tgt++)
			{
				CooLAPTarget target = new CooLAPTarget(proj, tgt);
				target.fromPacket(packet);
				targets.add(target);
			}
		}
	}
	
	@Override
	public String toString()
	{
		// Build a string describing this projector
		StringBuilder projector = new StringBuilder("LAP Projector");
		projector.append(" [")
		.append("Name=" + getName()).append("; ")
		.append("Address=" + getAddress()).append("; ")
		.append("Result=" + getResult()).append("; ")
		.append("RMS=" + getRms()).append("]");
		
		return projector.toString();
	}
	
	/**
	 * Method to get {@link #name}.
	 * @return {@link #name}.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Method to get {@link #address}.
	 * @return {@link #address}.
	 */
	public short getAddress()
	{
		return address;
	}
	
	/**
	 * Method to get {@link #result}.
	 * @return {@link #result}.
	 */
	public ProjectorResult getResult()
	{
		return result;
	}
	
	/**
	 * Method to get {@link #rms}.
	 * @return {@link #rms}.
	 */
	public float getRms()
	{
		return rms;
	}
	
	/**
	 * Method to get {@link #targets}.
	 * @return {@link #targets}.
	 */
	public ObservableList<CooLAPTarget> getTargets()
	{
		return targets;
	}
}