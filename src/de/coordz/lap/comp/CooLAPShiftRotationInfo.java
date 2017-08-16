/*
 * Copyright © 2017 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.coordz.lap.comp;

import de.coordz.lap.CooLAPPacketImpl;

/**
 * Class that implements an {@link CooLAPComponent} for
 * {@link CooLAPPacketImpl} shift and rotation info.
 * 
 * @author tobias.ohm
 * @version 1.1
 */
public class CooLAPShiftRotationInfo implements CooLAPComponent
{
	/** Shift vector x-Coo. [1/100 mm] */
	private float shiftX;
	/** Shift vector y-Coo. [1/100 mm] */
	private float shiftY;
	/** Rotation angle (clockwise) [1/100 deg] */
	private float rotAngle;
	/** Rotation centre x-Coo. [1/100 mm] */
	private float rotCenterX;
	/** Rotation centre y-Coo. [1/100 mm] */
	private float rotCenterY;
	
	@Override
	public void fromPacket(CooLAPPacketImpl packet)
	{
		shiftX = packet.getFloat("Shift_x");
		shiftY = packet.getFloat("Shift_y");
		rotAngle = packet.getFloat("RotAngle");
		rotCenterX = packet.getFloat("RotCentre_x");
		rotCenterY = packet.getFloat("RotCentre_y");
	}
	
	@Override
	public String toString()
	{
		// Build a string describing this shift rotation info
		StringBuilder laser = new StringBuilder("LAP Shift Rotation Info");
		laser.append(" [")
		.append("Shift X=" + getShiftX()).append("; ")
		.append("Shift Y=" + getShiftY()).append("; ")
		.append("Rotation Angle=" + getRotAngle()).append("; ")
		.append("Rotation Center X=" + getRotCenterX()).append("; ")
		.append("Rotation Center Y=" + getRotCenterY()).append("]");
		
		return laser.toString();
	}
	
	/**
	 * Method to get {@link #shiftX}.
	 * @return {@link #shiftX}.
	 */
	public float getShiftX()
	{
		return shiftX;
	}
	
	/**
	 * Method to get {@link #shiftY}.
	 * @return {@link #shiftY}.
	 */
	public float getShiftY()
	{
		return shiftY;
	}
	
	/**
	 * Method to get {@link #rotAngle}.
	 * @return {@link #rotAngle}.
	 */
	public float getRotAngle()
	{
		return rotAngle;
	}
	
	/**
	 * Method to get {@link #rotCenterX}.
	 * @return {@link #rotCenterX}.
	 */
	public float getRotCenterX()
	{
		return rotCenterX;
	}
	
	/**
	 * Method to get {@link #rotCenterY}.
	 * @return {@link #rotCenterY}.
	 */
	public float getRotCenterY()
	{
		return rotCenterY;
	}
}