/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui;

import de.coordz.data.*;

public interface CooDataChanged
{
	public default void customerChanged(CooCustomer customer)
	{
		// Do nothing if not overwritten
	}
	
	public default void projectChanged(CooProject project)
	{
		// Do nothing if not overwritten
	}
}