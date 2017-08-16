/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap.comp;

import de.coordz.lap.*;

/**
 * Interface that describes a {@link CooLAPComponent} send
 * by an {@link CooLAPPacket} and read {@link #fromPacket(CooLAPPacketImpl)}.
 * 
 * @author tobias.ohm
 * @version 1.1
 */
public interface CooLAPComponent
{
	/**
	 * Method to construct this {@link CooLAPComponent} from
	 * committed {@link CooLAPPacketImpl}.
	 * @param packet = the {@link CooLAPPacketImpl} to get data from
	 */
	public void fromPacket(CooLAPPacketImpl packet);
}