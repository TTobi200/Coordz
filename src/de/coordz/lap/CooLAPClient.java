/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap;

import java.io.*;

public class CooLAPClient extends CooTcpIpClient
{
	/** Default ip from LAP Software (localhost) */
	public static final String DEF_LAP_SOFTWARE_IP = "127.0.0.1";
	/** Default port where LAP Software listen */
	public static final int LAP_SOFTWARE_PORT = 8000;

	/** ID of the LAP Software */
	public static final int LAP = 0x0001;
	/** ID of the client Software */
	public static final int CLIENT = 0x0002;

	/** 0x0010 Automatic calibration client->LAP */
	public static final int AUTOMATIC_CALIBRATION = 0x0010;
	/** 0x0011 Switch Calibration client->LAP */
	public static final int SWITCH_CALIBRATION = 0x0011;
	/** 0x0012 Switch Calibration Acknowledge client->LAP */
	public static final int SWITCH_CALIBRATION_ACKNOWLEDGE = 0x0012;
	/** 0x0020 Start projection client->LAP */
	public static final int START_PROJECTION = 0x0020;
	/** 0x0021 Start and adjust projection client->LAP */
	public static final int START_AND_ADJUST_PROJECTION = 0x0021;
	/** 0x0022 Show next contour client->LAP */
	public static final int SHOW_NEXT_CONTOUR = 0x0022;
	/** 0x0023 Show previous contour client->LAP */
	public static final int SHOW_PREVIOUS_CONTOUR = 0x0023;
	/** 0x0030 Stop projection client->LAP */
	public static final int STOP_PROJECTION = 0x0030;
	/** 0x0040 Get Shift- / Rotation Info client->LAP */
	public static final int GET_SHIFT_ROTATION_INFO = 0x0040;

	/** 0x0110 Result of “Automatic calibration” LAP->client */
	public static final int RESULT_OF_AUTOMATIC_CALIBRATION = 0x0110;
	/** 0x0111 Result of “ Switch Calibration“ LAP->client */
	public static final int RESULT_OF_SWITCH_CALIBRATION = 0x0111;
	/** 0x0112 Result of “ Switch Calibration Acknowledge“ LAP->client */
	public static final int RESULT_OF_SWITCH_CALIBRATION_ACKNOWLEDGE = 0x0112;
	/** 0x0120 Result of “Start projection” LAP->client */
	public static final int RESULT_OF_START_PROJECTION = 0x0120;
	/** 0x0121 Result of “ Start and adjust projection” LAP->client */
	public static final int RESULT_OF_START_AND_ADJUST_PROJECTION = 0x0121;
	/** 0x0122 Result of “Show next contour“ LAP->client */
	public static final int RESULT_OF_SHOW_NEXT_CONTOUR = 0x0122;
	/** 0x0123 Result of “Show previous contour“ LAP->client */
	public static final int RESULT_OF_SHOW_PREVIOUS_CONTOUR = 0x0123;
	/** 0x0130 Result of “Stop projection” LAP->client */
	public static final int RESULT_OF_STOP_PROJECTION = 0x0130;
	/** 0x0140 Result of “ Get Shift- / Rotation Info“ LAP->client */
	public static final int RESULT_OF_GET_SHIFT_ROTATION_INFO = 0x0140;

	public CooLAPClient()
	{
		this(DEF_LAP_SOFTWARE_IP);
	}

	public CooLAPClient(String srvIp)
	{
		super(srvIp, LAP_SOFTWARE_PORT);
	}

	public void startAutoCalibration(File file) throws IOException
	{
		if(connected.get())
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			try (CooLittleEndianOutputStream leo = new CooLittleEndianOutputStream(
				out))
			{
				// 2. Source ID of sender UINT2
				leo.writeShort(CLIENT);
				// 3. Destination ID of receiver UINT2
				leo.writeShort(LAP);
				// 4. Message_ID ID of message UINT2
				leo.writeShort(AUTOMATIC_CALIBRATION);
				// 2 CalibPath Path and name of calibration file Char[n]
				for(char c : file.getAbsolutePath().toCharArray())
				{
					leo.writeChar(c);
				}

				leo.flush();
				leo.close();

				byte[] b = out.toByteArray();
				try (OutputStream oout = socket.getOutputStream())
				{
					// Message length - Swap to little endian
					oout.write(CooByteSwapper.swap(((short)b.length + 2)));
					// Write message
					oout.write(b);
					oout.flush();
					oout.close();
				}
			}
		}
	}
}