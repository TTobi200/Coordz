/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap;

import java.io.IOException;
import java.net.Socket;

import javafx.beans.property.*;
import de.util.log.CooLog;

public class CooTcpIpClient
{
	protected Socket socket;
	protected BooleanProperty connected;

	public CooTcpIpClient(String srvIp, int srvPort)
	{
		try
		{
			connected = new SimpleBooleanProperty(false);
			socket = new Socket(srvIp, srvPort);
		}
		catch(IOException e)
		{
			CooLog.error("Could not open socket for IP " +
							srvIp, e);
			return;
		}

		connected.setValue(true);
	}
}