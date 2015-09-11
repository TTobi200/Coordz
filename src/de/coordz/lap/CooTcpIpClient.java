/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap;

import java.io.IOException;
import java.net.*;

import javafx.beans.property.*;
import de.util.log.CooLog;

public class CooTcpIpClient
{
	protected Socket socket;
	protected BooleanProperty connected;
	protected String srvIp;
	protected int srvPort;

	public CooTcpIpClient(String srvIp, int srvPort) throws UnknownHostException,
		IOException
	{
		connected = new SimpleBooleanProperty(false);
		socket = new Socket(srvIp, srvPort);
		
		this.srvIp = srvIp;
		this.srvPort = srvPort;
		connected.setValue(true);
	}
	
	public boolean disconnect()
	{
		if(isConnected())
		{
			try
			{
				socket.close();
			}
			catch(IOException e)
			{
				CooLog.error("Could not close socket", e);
				return false;
			}
			connected.setValue(false);
		}
		return true;
	}
	
	public boolean isConnected()
	{
		return connected.get();
	}
	
	public String getSrvIp()
	{
		return srvIp;
	}
	
	public int getSrvPort()
	{
		return srvPort;
	}
}