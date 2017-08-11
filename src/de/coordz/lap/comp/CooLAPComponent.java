/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap.comp;

import de.coordz.lap.CooLAPPacket;

/**
 * Interface that describes a {@link CooLAPComponent} send
 * by an {@link CooLAPPacket} and read {@link #fromPacket(CooLAPPacket)}.
 * 
 * @author tobias.ohm
 * @version 1.0
 */
public interface CooLAPComponent
{
	/**
	 * Method to construct this {@link CooLAPComponent} from
	 * committed {@link CooLAPPacket}.
	 * @param packet = the {@link CooLAPPacket} to get data from
	 */
	public void fromPacket(CooLAPPacket packet);
}