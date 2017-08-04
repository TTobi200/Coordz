/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap;

import java.io.*;
import java.net.UnknownHostException;
import java.util.Objects;

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

	/**
	 * Method to send {@link #AUTOMATIC_CALIBRATION} to LAP ProSoft Server.
	 * @param calibrationFile = the calibration file
	 * @return the received {@link CooLAPPacket} from server
	 * @throws IOException when something went wrong
	 */
	public CooLAPPacket startAutoCalibration(File calibrationFile)
			throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();

		// Initializes the header values
		packet.initHeader(CLIENT, LAP, AUTOMATIC_CALIBRATION);
			
		// 2 CalibPath Path and name of calibration file Char[n]
		packet.writeString("CalibPath", calibrationFile.getAbsolutePath());
			
		// Send command to lap software
		sendToServer("Start Auto Calibration", packet);
		// And receive the answer packet
		return receiveFromServer();
	}
	
	/**
	 * Method to send {@link #SWITCH_CALIBRATION} to LAP ProSoft Server.
	 * @param mode = the calibration mode
	 * @param calibrationFile = the calibration file
	 * @return the received {@link CooLAPPacket} from server
	 * @throws IOException when something went wrong
	 */
	public CooLAPPacket switchCalibrationMode(Integer mode, File calibrationFile)
		throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();
		
		// Initializes the header values
		packet.initHeader(CLIENT, LAP, SWITCH_CALIBRATION);
			
		// 2 Calibration Mode INT2
		// Automatic calibration = 1
		// No calibration Position = 2
		// check of target film = 3
		// Position check of target hole = 4
		packet.writeInt("Mode", mode);
		// 3 CalibPath Path and name of calibration file Char[n]
		packet.writeString("CalibPath", calibrationFile.getAbsolutePath());
			
		// Send command to lap software
		sendToServer("Switch Calibration", packet);
		// And receive the answer packet
		return receiveFromServer();
	}
	
	/**
	 * Method to send {@link #SWITCH_CALIBRATION_ACKNOWLEDGE} to LAP ProSoft Server.
	 * @param state = the calibration state
	 * @return the received {@link CooLAPPacket} from server
	 * @throws IOException when something went wrong
	 */
	public CooLAPPacket switchCalibrationAcknowledge(short state)
		throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();
		
		// Initializes the header values
		packet.initHeader(CLIENT, LAP, SWITCH_CALIBRATION_ACKNOWLEDGE);
			
		// 2 Status of position check
		// 0: check Ok
		// 1: check refused
		packet.writeShort("Status", state);
					
		// Send command to lap software
		sendToServer("Switch Calibration Acknowledge", packet);
		// And receive the answer packet
		return receiveFromServer();
	}

	/**
	 * Manual Calibration not supported from LAP Server.
	 * @param file = the calibration file
	 * @return the received {@link CooLAPPacket} from server
	 * @throws IOException when something went wrong
	 */
	public CooLAPPacket startManualCalibration(File file) 
			throws IOException
	{
		throw new UnsupportedOperationException("The manual calibration is "
			+ "not implemented in TCP-IP Interface of LAP");
	}

	/**
	 * Method to send {@link #START_PROJECTION} to LAP ProSoft Server.
	 * @param projectionFile = the projection file to display
	 * @return the received {@link CooLAPPacket} from server
	 * @throws IOException when something went wrong
	 */
	public CooLAPPacket startProjection(File projectionFile) 
			throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();

		// Initializes the header values
		packet.initHeader(CLIENT, LAP, START_PROJECTION);
			
		// 2 ProjPath Path and name of a projection file Char[n]
		packet.writeString("ProjPath", projectionFile.getAbsolutePath());

		// Send command to lap software
		sendToServer("Start Projection", packet);
		// And receive the answer packet
		return receiveFromServer();
	}
	
	/**
	 * Method to send {@link #START_AND_ADJUST_PROJECTION} to LAP ProSoft Server.
	 * @param projectionFile = the projection file to display
	 * @return the received {@link CooLAPPacket} from server
	 * @throws IOException when something went wrong
	 */
	public CooLAPPacket startAndAdjustProjection(File projectionFile) 
			throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();

		// Initializes the header values
		packet.initHeader(CLIENT, LAP, START_AND_ADJUST_PROJECTION);
			
		// TODO $TO: Commit an vector rotation object with necessary parameter
		// FORTEST $TO: Rotate the element 90 degrees
		// 2 Height Height of object / shift in z-Coo. INT4 [1/100 mm]
		packet.writeInt("Height", 0);
		// 3 Shift_x Shift vector x-Coo. INT4 [1/100 mm]
		packet.writeInt("Shift_x", -50000);
		// 4 Shift_y Shift vector y-Coo. INT4 [1/100 mm]
		packet.writeInt("Shift_y", 0);
		// 5 RotAngle Rotation angle (clockwise) INT4 [1/100 deg]
		packet.writeInt("RotAngle", 9000);
		// 6 RotCentre_x Rotation centre x-Coo. INT4 [1/100 mm]
		packet.writeInt("RotCentre_x", 0);
		// 7 RotCentre_y Rotation centre y-Coo. INT4 [1/100 mm]
		packet.writeInt("RotCentre_y", 0);
			
		// 8 ProjPath Path and name of a projection file Char[n]
		packet.writeString("ProjPath", projectionFile.getAbsolutePath());

		// Send command to lap software
		sendToServer("Start and adjust projection", packet);
		// And receive the answer packet
		return receiveFromServer();
	}

	/**
	 * Method to send {@link #STOP_PROJECTION} to LAP ProSoft Server.
	 * @return the received {@link CooLAPPacket} from server
	 * @throws IOException when something went wrong
	 */
	public CooLAPPacket stopProjection() throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();

		// Initializes the header values
		packet.initHeader(CLIENT, LAP, STOP_PROJECTION);
			
		// Send command to lap software
		sendToServer("Stop Projection", packet);
		// And receive the answer packet
		return receiveFromServer();
	}

	/**
	 * Method to send {@link #SHOW_PREVIOUS_CONTOUR} to LAP ProSoft Server.
	 * @return the received {@link CooLAPPacket} from server
	 * @throws IOException when something went wrong
	 */
	public CooLAPPacket previousContour() throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();

		// Initializes the header values
		packet.initHeader(CLIENT, LAP, SHOW_PREVIOUS_CONTOUR);
			
		// Send command to lap software
		sendToServer("Previous Contour", packet);
		// And receive the answer packet
		return receiveFromServer();
	}

	/**
	 * Method to send {@link #SHOW_NEXT_CONTOUR} to LAP ProSoft Server.
	 * @return the received {@link CooLAPPacket} from server
	 * @throws IOException when something went wrong
	 */
	public CooLAPPacket nextContour() throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();

		// Initializes the header values
		packet.initHeader(CLIENT, LAP, SHOW_NEXT_CONTOUR);
			
		// Send command to lap software
		sendToServer("Next Contour", packet);
		// And receive the answer packet
		return receiveFromServer();
	}
	
	/**
	 * Method to send {@link #GET_SHIFT_ROTATION_INFO} to LAP ProSoft Server.
	 * @return the received {@link CooLAPPacket} from server
	 * @throws IOException when something went wrong
	 */
	public CooLAPPacket getShiftRotationInfo() throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();

		// Initializes the header values
		packet.initHeader(CLIENT, LAP, GET_SHIFT_ROTATION_INFO);
			
		// Send command to lap software
		sendToServer("Get Shift- / Rotation Info", packet);
		// And receive the answer packet
		return receiveFromServer();
	}
	
	/**
	 * Method to start receiving an {@link CooLAPPacket} from the LAP ProSoft Server.
	 * @return the received {@link CooLAPPacket} from server
	 * @throws IOException when receiving went wrong
	 */
	private CooLAPPacket receiveFromServer() throws IOException
	{
		// Define a packet for receiving
		CooLAPPacket packet = new CooLAPPacket();

		// Read data from little endian input stream
		CooLittleEndianInputStream in = new 
			CooLittleEndianInputStream(socket.getInputStream());
		
		// Wait until we have all data
		while(in.available() == 0){}
		
		// We have a comple packet received
		packet.fromStream(in);
			
		// Debug the send byte array
		CooLog.debug("Server -> Client: " + 
			packet.toString());
		
		return packet;
	}
	
	/**
	 * Method to start sending an {@link CooLAPPacket} to LAP ProSoft Server.
	 * @param command = the command name to send
	 * @param packet = the {@link CooLAPPacket} to send
	 * @throws IOException when sending went wrong
	 */
	protected void sendToServer(String command, CooLAPPacket packet) throws IOException
	{
		if(Objects.nonNull(packet) && packet.hasHeader())
		{
			byte[] b = packet.toByteArray();
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
			CooLog.debug("Client -> Server: " + 
				packet.toString());
		}
	}
}