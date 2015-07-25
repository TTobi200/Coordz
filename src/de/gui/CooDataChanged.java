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
	public void customerChanged(CooCustomer customer);
	
	public void projectChanged(CooProject project);
}