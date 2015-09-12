/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view3D.comp;

import de.coordz.data.CooData;
import de.util.CooXformUtil;

public abstract class CooData3D <T extends CooData> extends CooXformUtil
{
	// Abstract interface for all Coordz 3D Data
	
	public abstract void dataChanged(T data);
}
