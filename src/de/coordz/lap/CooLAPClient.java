/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap;

import java.io.*;
import java.net.UnknownHostException;
import java.util.*;

import de.util.log.CooLog;

public class CooLAPClient extends CooTcpIpClient
{
	/** Default ip from LAP Software (localhost) */
//	public static final String DEF_LAP_SOFTWARE_IP = "127.0.0.1";
	// FORTEST Add the laser test ip address
	public static final String DEF_LAP_SOFTWARE_IP = "10.221.46.207";
	/** Default port where LAP Software listen */
	public static final int LAP_SOFTWARE_PORT = 8000;

	/** ID of the LAP Software */
	public static final short LAP = 0x0001;
	/** ID of the client Software */
	public static final short CLIENT = 0x0002;

	/** 0x0010 Automatic calibration client->LAP */
	public static final short AUTOMATIC_CALIBRATION = 0x0010;
	/** 0x0011 Switch Calibration client->LAP */
	public static final short SWITCH_CALIBRATION = 0x0011;
	/** 0x0012 Switch Calibration Acknowledge client->LAP */
	public static final short SWITCH_CALIBRATION_ACKNOWLEDGE = 0x0012;
	/** 0x0020 Start projection client->LAP */
	public static final short START_PROJECTION = 0x0020;
	/** 0x0021 Start and adjust projection client->LAP */
	public static final short START_AND_ADJUST_PROJECTION = 0x0021;
	/** 0x0022 Show next contour client->LAP */
	public static final short SHOW_NEXT_CONTOUR = 0x0022;
	/** 0x0023 Show previous contour client->LAP */
	public static final short SHOW_PREVIOUS_CONTOUR = 0x0023;
	/** 0x0030 Stop projection client->LAP */
	public static final short STOP_PROJECTION = 0x0030;
	/** 0x0040 Get Shift- / Rotation Info client->LAP */
	public static final short GET_SHIFT_ROTATION_INFO = 0x0040;

	/** 0x0110 Result of “Automatic calibration” LAP->client */
	public static final short RESULT_OF_AUTOMATIC_CALIBRATION = 0x0110;
	/** 0x0111 Result of “ Switch Calibration“ LAP->client */
	public static final short RESULT_OF_SWITCH_CALIBRATION = 0x0111;
	/** 0x0112 Result of “ Switch Calibration Acknowledge“ LAP->client */
	public static final short RESULT_OF_SWITCH_CALIBRATION_ACKNOWLEDGE = 0x0112;
	/** 0x0120 Result of “Start projection” LAP->client */
	public static final int RESULT_OF_START_PROJECTION = 0x0120;
	/** 0x0121 Result of “ Start and adjust projection” LAP->client */
	public static final short RESULT_OF_START_AND_ADJUST_PROJECTION = 0x0121;
	/** 0x0122 Result of “Show next contour“ LAP->client */
	public static final short RESULT_OF_SHOW_NEXT_CONTOUR = 0x0122;
	/** 0x0123 Result of “Show previous contour“ LAP->client */
	public static final short RESULT_OF_SHOW_PREVIOUS_CONTOUR = 0x0123;
	/** 0x0130 Result of “Stop projection” LAP->client */
	public static final short RESULT_OF_STOP_PROJECTION = 0x0130;
	/** 0x0140 Result of “ Get Shift- / Rotation Info“ LAP->client */
	public static final short RESULT_OF_GET_SHIFT_ROTATION_INFO = 0x0140;
	
	/** 1 Switch Calibration Automatic calibration */
	public static final int AUTOMATIC_CALIBRATION_MODE = 1;
	/** 2 Switch Calibration No calibration */
	public static final int NO_CALIBRATION_MODE = 2;
	/** 3 Switch Calibration Position check of target film */
	public static final int POSITION_CHECK_OF_TARGET_FILM_MODE = 3;
	/** 4 Switch Calibration Position check of target hole */
	public static final int POSITION_CHECK_OF_TARGET_HOLE_MODE = 4;

	public CooLAPClient() throws UnknownHostException, IOException
	{
		this(DEF_LAP_SOFTWARE_IP);
	}

	public CooLAPClient(String srvIp) throws UnknownHostException, IOException
	{
		this(srvIp, LAP_SOFTWARE_PORT);
	}

	public CooLAPClient(String srvIp, int srvPort) throws UnknownHostException,
		IOException
	{
		super(srvIp, srvPort);
	}

	public CooLAPPacket startAutoCalibration(File calibrationFile)
			throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try (CooLittleEndianOutputStream leo = 
			new CooLittleEndianOutputStream(out))
		{
			// 2. Source ID of sender UINT2
			leo.writeShort(CLIENT);
			// 3. Destination ID of receiver UINT2
			leo.writeShort(LAP);
			// 4. Message_ID ID of message UINT2
			leo.writeShort(AUTOMATIC_CALIBRATION);
			// 2 CalibPath Path and name of calibration file Char[n]
			for(char c : calibrationFile.getAbsolutePath().toCharArray())
			{
				leo.writeChar(c);
			}
			
			// Send command to lap software
			sendToServer("Start Auto Calibration", out, leo);
			// And receive the answer packet
			packet = receiveFromServer();
		}
		
		return packet;
	}
	
	public CooLAPPacket switchCalibrationMode(Integer mode, File calibrationFile)
		throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try (CooLittleEndianOutputStream leo = 
			new CooLittleEndianOutputStream(out))
		{
			// 2. Source ID of sender UINT2
			leo.writeShort(CLIENT);
			// 3. Destination ID of receiver UINT2
			leo.writeShort(LAP);
			// 4. Message_ID ID of message UINT2
			leo.writeShort(SWITCH_CALIBRATION);
			
			// 2 Calibration Mode INT2
			// Automatic calibration = 1
			// No calibration Position = 2
			// check of target film = 3
			// Position check of target hole = 4
			leo.writeInt(mode);
			
			// 3 CalibPath Path and name of calibration file Char[n]
			for(char c : calibrationFile.getAbsolutePath().toCharArray())
			{
				leo.writeChar(c);
			}
					
			// Send command to lap software
			sendToServer("Switch Calibration", out, leo);
			// And receive the answer packet
			packet = receiveFromServer();
		}
				
		return packet;
	}

	public CooLAPPacket startManualCalibration(File file) 
			throws IOException
	{
		throw new UnsupportedOperationException("The manual calibration is "
			+ "not implemented in TCP-IP Interface of LAP");
	}

	public CooLAPPacket startProjection(File projectionFile) 
			throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try (CooLittleEndianOutputStream leo = 
			new CooLittleEndianOutputStream(out))
		{
			// 2. Source ID of sender UINT2
			leo.writeShort(CLIENT);
			// 3. Destination ID of receiver UINT2
			leo.writeShort(LAP);
			// 4. Message_ID ID of message UINT2
			leo.writeShort(START_PROJECTION);
			// 2 ProjPath Path and name of a projection file Char[n]
			for(char c : projectionFile.getAbsolutePath().toCharArray())
			{
				leo.writeChar(c);
			}

			// Send command to lap software
			sendToServer("Start Projection", out, leo);
			// And receive the answer packet
			packet = receiveFromServer();
		}
		
		return packet;
	}
	
	public CooLAPPacket startAndAdjustProjection(File projectionFile) 
			throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try (CooLittleEndianOutputStream leo = 
			new CooLittleEndianOutputStream(out))
		{
			// 2. Source ID of sender UINT2
			leo.writeShort(CLIENT);
			// 3. Destination ID of receiver UINT2
			leo.writeShort(LAP);
			// 4. Message_ID ID of message UINT2
			leo.writeShort(START_PROJECTION);

			// 2 Height Height of object / shift in z-Coo. INT4 [1/100 mm]
			
			
			
			// 8 ProjPath Path and name of a projection file Char[n]
			for(char c : projectionFile.getAbsolutePath().toCharArray())
			{
				leo.writeChar(c);
			}

			// Send command to lap software
			sendToServer("Start and adjust projection", out, leo);
			// And receive the answer packet
			packet = receiveFromServer();
		}
		
		return packet;
	}

	public CooLAPPacket stopProjection() throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try (CooLittleEndianOutputStream leo = 
				new CooLittleEndianOutputStream(out))
		{
			// 2. Source ID of sender UINT2
			leo.writeShort(CLIENT);
			// 3. Destination ID of receiver UINT2
			leo.writeShort(LAP);
			// 4. Message_ID ID of message UINT2
			leo.writeShort(STOP_PROJECTION);

			// Send command to lap software
			sendToServer("Stop Projection", out, leo);
			// And receive the answer packet
			packet = receiveFromServer();
		}
		
		return packet;
	}

	public CooLAPPacket previousContour() throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try (CooLittleEndianOutputStream leo = 
			new CooLittleEndianOutputStream(out))
		{
			// 2. Source ID of sender UINT2
			leo.writeShort(CLIENT);
			// 3. Destination ID of receiver UINT2
			leo.writeShort(LAP);
			// 4. Message_ID ID of message UINT2
			leo.writeShort(SHOW_PREVIOUS_CONTOUR);

			// Send command to lap software
			sendToServer("Previous Contour", out, leo);
			// And receive the answer packet
			packet = receiveFromServer();
		}
				
		return packet;
	}

	public CooLAPPacket nextContour() throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try (CooLittleEndianOutputStream leo = 
			new CooLittleEndianOutputStream(out))
		{
			// 2. Source ID of sender UINT2
			leo.writeShort(CLIENT);
			// 3. Destination ID of receiver UINT2
			leo.writeShort(LAP);
			// 4. Message_ID ID of message UINT2
			leo.writeShort(SHOW_NEXT_CONTOUR);

			// Send command to lap software
			sendToServer("Next Contour", out, leo);
			// And receive the answer packet
			packet = receiveFromServer();
		}
		
		return packet;
	}
	
	private CooLAPPacket receiveFromServer() throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();

		// Read data from little endian input stream
		CooLittleEndianInputStream in = new 
			CooLittleEndianInputStream(socket.getInputStream());
		
		CooLog.debug("Waiting on reply from LAP-Software...");
			
		// Wait until we have all data
		while(in.available() == 0){}
		
		// We have a comple packet received
		packet.fromStream(in);
			
		// Debug the send byte array
		CooLog.debug("Lap-Software -> Client: " + 
			packet.toString());
		
		return packet;
	}
	
	protected void sendToServer(String command, ByteArrayOutputStream out,
			CooLittleEndianOutputStream leo) throws IOException
	{
		if(Objects.nonNull(out) && Objects.nonNull(leo))
		{
			CooLog.debug("Sending <" + command + "> to LAP-Software");
			
			leo.flush();
			leo.close();
			
			byte[] b = out.toByteArray();
			OutputStream oout = socket.getOutputStream();
			
			ByteArrayOutputStream collector = new ByteArrayOutputStream();
			// Message length - Swap to little endian
			collector.write(CooByteSwapper.toLE((short)(b.length + 2)));
			// Write message
			collector.write(b);
			collector.flush();
			collector.close();
			
			oout.write(collector.toByteArray());
			oout.flush();
			
			// Debug the send byte array
			CooLog.debug("Client -> Lap-Software: " + 
				Arrays.toString(collector.toByteArray()));
		}
	}
}