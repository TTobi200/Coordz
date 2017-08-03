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
			short msgLength = readHeader(in);
			// Get the read message id
			short msgID = (short)values.get("Message_ID");
			
			switch(msgID)
			{
				case CooLAPClient.AUTOMATIC_CALIBRATION:
				case CooLAPClient.RESULT_OF_AUTOMATIC_CALIBRATION:
					readCalibResult(in, msgLength - HEADER_LENGTH);
					values.put("Name", "AUTOMATIC_CALIBRATION");
					break;
					
				case CooLAPClient.SWITCH_CALIBRATION:
				case CooLAPClient.RESULT_OF_SWITCH_CALIBRATION:
					readSwitchCalibResult(in, msgLength - HEADER_LENGTH);
					values.put("Name", "SWITCH_CALIBRATION");
					break;
					
				case CooLAPClient.START_PROJECTION:
				case CooLAPClient.RESULT_OF_START_PROJECTION:
					readStartProjResult(in);
					values.put("Name", "START_PROJECTION");
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

	private void readCalibResult(CooLittleEndianInputStream in,
		int dataLength)	throws IOException
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
			
		values.put("Result", result);
		values.put("ProjCount", projCount);
	}
	
	private void readSwitchCalibResult(CooLittleEndianInputStream in,
		int dataLength) throws IOException
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

		values.put("Result", result);
		values.put("ProjCount", projCount);
	}
	
	private void readStartProjResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Result
		// 0: successful
		// 1: file not found
		// 2: file not readable
		// 3: system not calibrated
		// 4: projection out of range
		short result = in.readShort();

		values.put("Result", result);
	}
	
	private void readShowNextContourResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Result
		// 0: success
		// 1: end of list
		// 2: no open file
		// 3: no valid calibration
		// 4: projection out of range
		short result = in.readShort();

		values.put("Result", result);
	}
	
	private void readShowPrevContourResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Result
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