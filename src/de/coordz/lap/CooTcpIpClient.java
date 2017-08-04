/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap;

import java.io.IOException;
import java.net.*;

import de.util.log.CooLog;
import javafx.beans.property.*;

public class CooTcpIpClient
{
	/** The connected {@link Socket} */
	protected Socket socket;
	/** The server ip address or host name */
	protected String srvIp;
	/** The server port */
	protected int srvPort;
	/** Boolean Flag if client is connected */
	protected BooleanProperty connected;

	public CooTcpIpClient(String srvIp, int srvPort) throws UnknownHostException,
		IOException
	{
		// Try to establish connection to server peer
		connected = new SimpleBooleanProperty(Boolean.FALSE);
		socket = new Socket(srvIp, srvPort);
		
		// We are connected successfully
		this.srvIp = srvIp;
		this.srvPort = srvPort;
		connected.setValue(Boolean.TRUE);
	}
	
	/**
	 * Method to disconnect from peer.
	 * @return flag if disconnected.
	 */
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
				return Boolean.FALSE;
			}
			connected.setValue(Boolean.FALSE);
		}
		return Boolean.TRUE;
	}
	
	/**
	 * Method to check if client is {@link #connected} to peer.
	 * @return flag if client is {@link #connected} to peer
	 */
	public boolean isConnected()
	{
		return connected.get();
	}
	
	/**
	 * Get the server ip address or host name.
	 * @return the server ip address or host name
	 */
	public String getSrvIp()
	{
		return srvIp;
	}
	
	/**
	 * Get the connection port.
	 * @return the connection port
	 */
	public int getSrvPort()
	{
		return srvPort;
	}
}