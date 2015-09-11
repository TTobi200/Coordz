/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view3D;

import de.coordz.data.base.CooMeasurement;

public interface CooMeasurementChanged
{
	public default void measurementChanged(CooMeasurement measurement)
	{
		// Do nothing if not overwritten
	}
}