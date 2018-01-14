/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2018 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import de.coordz.db.CooDBDao;

public interface CooTableDataEventListener <T extends CooDBDao>
{
	/**
	 * Method triggered when table data changed.
	 * @param e = the {@link CooTableDataEvent}
	 */
	public void tableDataChanged(CooTableDataEvent<T> e);
}