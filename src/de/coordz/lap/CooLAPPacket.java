/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap;

import java.io.IOException;
import java.util.*;

import javafx.collections.FXCollections;

public class CooLAPPacket
{
	/** Default packet header length */
	private static final int HEADER_LENGTH = 4;
	
	/** {@link Map} with all packet keys to values */
	private Map<String, Object> values;
	
	public CooLAPPacket()
	{
		values = FXCollections.observableHashMap();
	}
	
	public void fromStream(CooLittleEndianInputStream in)
			throws IOException
	{
		if(Objects.nonNull(in))
		{
			// Read the packet header
			readHeader(in);
			// Get the read message id
			short msgID = (short)values.get("Message_ID");
			
			switch(msgID)
			{
				case CooLAPClient.AUTOMATIC_CALIBRATION:
				case CooLAPClient.RESULT_OF_AUTOMATIC_CALIBRATION:
					readCalibResult(in);
					values.put("Name", "AUTOMATIC_CALIBRATION");
					break;
					
				case CooLAPClient.SWITCH_CALIBRATION:
				case CooLAPClient.RESULT_OF_SWITCH_CALIBRATION:
					readSwitchCalibResult(in);
					values.put("Name", "SWITCH_CALIBRATION");
					break;
					
				case CooLAPClient.SWITCH_CALIBRATION_ACKNOWLEDGE:
				case CooLAPClient.RESULT_OF_SWITCH_CALIBRATION_ACKNOWLEDGE:
					readSwitchCalibAckResult(in);
					values.put("Name", "SWITCH_CALIBRATION_ACKNOWLEDGE");
					break;
					
				case CooLAPClient.START_PROJECTION:
				case CooLAPClient.RESULT_OF_START_PROJECTION:
					readStartProjResult(in);
					values.put("Name", "START_PROJECTION");
					break;
					
				case CooLAPClient.START_AND_ADJUST_PROJECTION:
				case CooLAPClient.RESULT_OF_START_AND_ADJUST_PROJECTION:
					readStartAndAdjustProjResult(in);
					values.put("Name", "START_AND_ADJUST_PROJECTION");
					break;
				
				case CooLAPClient.SHOW_NEXT_CONTOUR:
				case CooLAPClient.RESULT_OF_SHOW_NEXT_CONTOUR:
					readShowNextContourResult(in);
					values.put("Name", "SHOW_NEXT_CONTOUR");
					break;
					
				case CooLAPClient.SHOW_PREVIOUS_CONTOUR:
				case CooLAPClient.RESULT_OF_SHOW_PREVIOUS_CONTOUR:
					readShowPrevContourResult(in);
					values.put("Name", "SHOW_PREVIOUS_CONTOUR");
					break;
					
				case CooLAPClient.STOP_PROJECTION:
				case CooLAPClient.RESULT_OF_STOP_PROJECTION:
					readStopProjResult(in);
					values.put("Name", "STOP_PROJECTION");
					break;
					
				case CooLAPClient.GET_SHIFT_ROTATION_INFO:
				case CooLAPClient.RESULT_OF_GET_SHIFT_ROTATION_INFO:
					readGetShiftRotationInfoResult(in);
					values.put("Name", "GET_SHIFT_ROTATION_INFO");
					break;
			}
		}
	}
	
	private short readHeader(CooLittleEndianInputStream in)
			throws IOException
	{
		// Message length, whole length of the message incl. header
		short msgLength = in.readShort();
		// ID of sender
		short source = in.readShort();
		// ID of receiver
		short destination = in.readShort();
		// ID of message
		short msgID = in.readShort();
		
		values.put("Message_Length", msgLength);
		values.put("Source", source);
		values.put("Destination", destination);
		values.put("Message_ID", msgID);
		
		return msgLength;
	}

	private void readCalibResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Status of the whole calibration 
		// 0: successful
		// 1: faulty
		// 2: file not found
		// 3: file not readable
		// 4: manual calibration required
		short result = in.readShort();
		// Number of calibrated projectors
		short projCount = in.readShort();
		
		// Loop through all projectors
		for(int proj = 1; proj <= projCount; proj++)
		{
			// Name of projector
			String projName = readString(in, 32);
			// Address of projector
			short  projAdress = in.readShort();
			// Status of the calibration of projector
			// 0: successful
			// 1: calibration result exceeds limit
			// 2: at least one target not found
			short projResult = in.readShort();
			// Root mean square of first projector [1/100mm]
			float projRMS = in.readFloat();
			// Number of targets for first projector
			short tgtCount = in.readShort();
			
			// Loop through all targets of projector
			for(int tgt = 1; tgt <= tgtCount; tgt++)
			{
				// Number of target of projector
				short tgtNumber = in.readShort();
				// Status of the target
				// 0: Target found
				// 1: Target not found
				short tgtResult = in.readShort();
				// Deviation of the first target
				float tgtDeviation = in.readFloat(); 
				
				values.put("TgtNumber_" + proj + "_" + tgt, tgtNumber);
				values.put("TgtRes_" + proj + "_" + tgt, tgtResult);
				values.put("TgtDev_" + proj + "_" + tgt, tgtDeviation);
			}

			values.put("ProjName_" + proj, projName);
			values.put("ProjAddr_" + proj, projAdress);
			values.put("ProjRes_" + proj, projResult);
			values.put("ProjRMS_" + proj, projRMS);
			values.put("TgtCount_" + proj, tgtCount);
		}
			
		values.put("Result", result);
		values.put("ProjCount", projCount);
	}
	
	private void readSwitchCalibResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Use the same packet as automatic calibration result
		readCalibResult(in);
	}
	
	private void readSwitchCalibAckResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Packet only includes result information
		// 0: successful
		// 1: faulty
		readResultOnly(in);
	}
	
	private void readStartProjResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Packet only includes result information
		readResultOnly(in);
	}
	
	private void readStartAndAdjustProjResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Packet only includes result information
		readResultOnly(in);
	}
	
	private void readShowNextContourResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Packet only includes result information
		readResultOnly(in);
	}
	
	private void readShowPrevContourResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Packet only includes result information
		readResultOnly(in);
	}
	
	private void readStopProjResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Packet only includes result information
		readResultOnly(in);
	}
	
	private void readGetShiftRotationInfoResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Shift vector x-Coo. [1/100 mm]
		float shiftX = in.readFloat();
		// Shift vector y-Coo. [1/100 mm]
		float shiftY = in.readFloat();
		// Rotation angle (clockwise) [1/100 deg]
		float rotAngle = in.readFloat();
		// Rotation centre x-Coo. [1/100 mm] 
		float rotCentreX = in.readFloat();
		// Rotation centre y-Coo. [1/100 mm]
		float rotCentreY = in.readFloat();
		
		values.put("Shift_x", shiftX);
		values.put("Shift_y", shiftY);
		values.put("RotAngle", rotAngle);
		values.put("RotCentre_x", rotCentreX);
		values.put("RotCentre_y", rotCentreY);
	}
	
	private void readResultOnly(CooLittleEndianInputStream in)
		throws IOException
	{
		// Result of the operation
		// 0: success
		// 1: end of list
		// 2: no open file
		// 3: no valid calibration
		// 4: projection out of range
		short result = in.readShort();

		values.put("Result", result);
	}
	
	public Object getValue(String key)
	{
		return values.get(key);
	}
	
	public boolean containsValue(String key)
	{
		return values.containsKey(key);
	}
	
	public static String readString(CooLittleEndianInputStream in,
		int data) throws IOException
	{
		// TODO $TO: Move this method to CooLittleEndianInputStream
		byte[] bytes = new byte[data];
		in.read(bytes);
		return new String(bytes);
	}

	@Override
	public String toString()
	{
		StringBuilder strPacket = new StringBuilder("Packet");
		
		strPacket.append(" [");
		int i = 1;
		for(String k : new TreeSet<>(values.keySet()))
		{
			strPacket
				.append(k)
				.append("=")
				.append(values.get(k));
			
			if(i++ < values.size())
			{
				strPacket.append("; ");
			}
		}
		strPacket.append("]");
		return strPacket.toString();
	}
}