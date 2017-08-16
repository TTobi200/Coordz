/*
 * Copyright © 2017 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.coordz.lap.comp;

import de.coordz.lap.CooLAPPacketImpl;

/**
 * Class that implements an {@link CooLAPComponent} for
 * {@link CooLAPPacketImpl} target data.
 * 
 * @author tobias.ohm
 * @version 1.1
 */
public class CooLAPTarget implements CooLAPComponent
{
	/**
	 * Enum that describes target results.
	 */
	public enum TargetResult
	{
		TARGET_FOUND,
		TARGET_NOT_FOUND,
		UNKNOWN;
		
		/**
		 * Method to get {@link TargetResult} for committed enum ordinal.
		 * @param ordinal = the ordinal to get {@link TargetResult} for
		 * @return the {@link TargetResult} for committed ordinal
		 */
		public static TargetResult valueOf(int ordinal)
		{
			return ordinal == TARGET_FOUND.ordinal() ? TARGET_FOUND : 
				ordinal == TARGET_NOT_FOUND.ordinal() ? TARGET_NOT_FOUND : 
					UNKNOWN;
		}
	}
	
	/** The number of the corresponding projector */
	private int proj;
	/** The taget number */
	private int tgt;
	
	/** The number of this target */
	private short number;
	/** The result of this target */
	private TargetResult result;
	/** The actual deviation of this target */
	private float deviation;
	
	/**
	 * Constructor to create an {@link CooLAPTarget}.
	 * @param proj = the projector id
	 * @param tgt = the traget id
	 */
	public CooLAPTarget(int proj, int tgt)
	{
		this.proj = proj;
		this.tgt = tgt;
	}
	
	@Override
	public void fromPacket(CooLAPPacketImpl packet)
	{
		number = packet.getShort("TgtNumber_" + proj + "_" + tgt);
		result = TargetResult.valueOf(packet.getShort("TgtRes_" + proj + "_" + tgt));
		deviation = packet.getFloat("TgtDev_" + proj + "_" + tgt);
	}
	
	@Override
	public String toString()
	{
		// Build a string describing this target
		StringBuilder target = new StringBuilder("LAP Target");
		target.append(" [")
		.append("Number=" + getNumber()).append("; ")
		.append("Result=" + getResult()).append("; ")
		.append("Deviation=" + getDeviation()).append("]");
		
		return target.toString();
	}
	
	/**
	 * Method to get {@link #number}.
	 * @return {@link #number}.
	 */
	public short getNumber()
	{
		return number;
	}
	
	/**
	 * Method to get {@link #result}.
	 * @return {@link #result}.
	 */
	public TargetResult getResult()
	{
		return result;
	}
	
	/**
	 * Method to get {@link #deviation}.
	 * @return {@link #deviation}.
	 */
	public float getDeviation()
	{
		return deviation;
	}
}