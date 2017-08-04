/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap;

import java.io.*;
import java.util.*;

import javafx.collections.FXCollections;

public class CooLAPPacket
{
	/** {@link Map} with all packet keys to values */
	private Map<String, Object> values;
	
	/** {@link CooLittleEndianOutputStream} to collect data */
	private CooLittleEndianOutputStream leo;
	/** {@link ByteArrayOutputStream} to transform endian to byte array */
	private ByteArrayOutputStream out;
	/** Flag if packet header has been initialized */
	private Boolean headerInitialized;
	
	public CooLAPPacket()
	{
		values = FXCollections.observableHashMap();
	}
	
	/**
	 * Method to fill this {@link CooLAPPacket} from the committed
	 * {@link CooLittleEndianInputStream}. Data stored in {@link #values}.
	 * @param in = the {@link CooLittleEndianInputStream} to read from
	 * @throws IOException when filling this {@link CooLAPPacket} went wrong
	 */
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
	
	/**
	 * Method to read header bytes from this {@link CooLAPPacket}.
	 * @param in = the {@link CooLittleEndianInputStream} to read from 
	 * @return the header length as short
	 * @throws IOException when header reading went wrong
	 */
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
		
		headerInitialized = Boolean.TRUE;
		return msgLength;
	}

	/**
	 * Method to read calibration results from this {@link CooLAPPacket}.
	 * @param in = the {@link CooLittleEndianInputStream} to read from 
	 * @throws IOException when reading of calibration results went wrong
	 */
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
			String projName = in.readString(32);
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
	
	/**
	 * Method to read switch calibration results from this {@link CooLAPPacket}.
	 * @param in = the {@link CooLittleEndianInputStream} to read from 
	 * @throws IOException when reading of switch calibration results went wrong
	 */
	private void readSwitchCalibResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Use the same packet as automatic calibration result
		readCalibResult(in);
	}
	
	/**
	 * Method to read switch calibration acknowledge results from this {@link CooLAPPacket}.
	 * @param in = the {@link CooLittleEndianInputStream} to read from 
	 * @throws IOException when reading of switch calibration acknowledge results went wrong
	 */
	private void readSwitchCalibAckResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Packet only includes result information
		// 0: successful
		// 1: faulty
		readResultOnly(in);
	}
	
	/**
	 * Method to read start projection results from this {@link CooLAPPacket}.
	 * @param in = the {@link CooLittleEndianInputStream} to read from 
	 * @throws IOException when reading of start projection went wrong
	 */
	private void readStartProjResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Packet only includes result information
		readResultOnly(in);
	}
	
	/**
	 * Method to read start and adjust projection results from this {@link CooLAPPacket}.
	 * @param in = the {@link CooLittleEndianInputStream} to read from 
	 * @throws IOException when reading of start and adjust projection went wrong
	 */
	private void readStartAndAdjustProjResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Packet only includes result information
		readResultOnly(in);
	}
	
	/**
	 * Method to read show next contour results from this {@link CooLAPPacket}.
	 * @param in = the {@link CooLittleEndianInputStream} to read from 
	 * @throws IOException when reading of show next contour went wrong
	 */
	private void readShowNextContourResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Packet only includes result information
		readResultOnly(in);
	}
	
	/**
	 * Method to read show previous contour results from this {@link CooLAPPacket}.
	 * @param in = the {@link CooLittleEndianInputStream} to read from 
	 * @throws IOException when reading of show previous contour went wrong
	 */
	private void readShowPrevContourResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Packet only includes result information
		readResultOnly(in);
	}
	
	/**
	 * Method to read stop projection results from this {@link CooLAPPacket}.
	 * @param in = the {@link CooLittleEndianInputStream} to read from 
	 * @throws IOException when reading of stop projection went wrong
	 */
	private void readStopProjResult(CooLittleEndianInputStream in)
		throws IOException
	{
		// Packet only includes result information
		readResultOnly(in);
	}
	
	/**
	 * Method to read get shift rotation info results from this {@link CooLAPPacket}.
	 * @param in = the {@link CooLittleEndianInputStream} to read from 
	 * @throws IOException when reading of get shift rotation info went wrong
	 */
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
	
	/**
	 * Method to read only results from this {@link CooLAPPacket}.
	 * @param in = the {@link CooLittleEndianInputStream} to read from 
	 * @throws IOException when reading of results went wrong
	 */
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
	
	/**
	 * Method to initialize the {@link CooLAPPacket} header.
	 * @param source = the source id 
	 * @param destination = the destination id
	 * @param messageId = the message id
	 * @throws IOException when initializing went wrong
	 */
	public void initHeader(short source, short destination,
		short messageId) throws IOException
	{
		// We have a new header - open up empty stream
		leo = new CooLittleEndianOutputStream(out = new ByteArrayOutputStream());
		
		// 2. Source ID of sender UINT2
		leo.writeShort(source);
		// 3. Destination ID of receiver UINT2
		leo.writeShort(destination);
		// 4. Message_ID ID of message UINT2
		leo.writeShort(messageId);		
		
		values.put("Source", source);
		values.put("Destination", destination);
		values.put("Message_ID", messageId);
		
		headerInitialized = Boolean.TRUE;
	}

	/**
	 * Method to write an {@link String} to this {@link CooLAPPacket}.
	 * @param string = the {@link String} to write 
	 * @throws IOException when writing went wrong
	 */
	public void writeString(String string) throws IOException
	{
		writeString(null, string);
	}
	
	/**
	 * Method to write an {@link String} to this {@link CooLAPPacket}.
	 * @param key = the key for this {@link CooLAPPacket} value
	 * @param string = the {@link String} to write 
	 * @throws IOException when writing went wrong
	 */
	public void writeString(String key, String string) throws IOException
	{
		// Add the key an value to packet data
		putValue(key, string);
		
		for(char c : string.toCharArray())
		{
			leo.writeChar(c);
		}
	}
	
	/**
	 * Method to write an {@link Integer} to this {@link CooLAPPacket}.
	 * @param integer = the {@link Integer} to write 
	 * @throws IOException when writing went wrong
	 */
	public void writeInt(Integer integer) throws IOException
	{
		writeInt(null, integer);
	}
	
	/**
	 * Method to write an {@link Integer} to this {@link CooLAPPacket}.
	 * @param key = the key for this {@link CooLAPPacket} value 
	 * @param integer = the {@link Integer} to write 
	 * @throws IOException when writing went wrong
	 */
	public void writeInt(String key, Integer integer) throws IOException
	{
		// Add the key an value to packet data
		putValue(key, integer);
		
		leo.writeInt(integer);
	}

	/**
	 * Method to write an {@link Short} to this {@link CooLAPPacket}.
	 * @param shortValue = the {@link Short} to write 
	 * @throws IOException when writing went wrong
	 */
	public void writeShort(Short shortValue) throws IOException
	{
		writeShort(null, shortValue);
	}
	
	/**
	 * Method to write an {@link Short} to this {@link CooLAPPacket}.
	 * @param key = the key for this {@link CooLAPPacket} value 
	 * @param shortValue = the {@link Short} to write 
	 * @throws IOException when writing went wrong
	 */
	public void writeShort(String key, Short shortValue) throws IOException
	{
		// Add the key an value to packet data
		putValue(key, shortValue);
		
		leo.writeShort(shortValue);
	}
	
	/**
	 * Method to put {@link Object} to this {@link CooLAPPacket}.
	 * @param key = the key for the {@link Object}
	 * @param value = the {@link Object} to put
	 */
	private void putValue(String key, Object value)
	{
		if(Objects.nonNull(key))
		{
			values.put(key, value);
		}
	}

	/**
	 * Method to get the {@link Object} from this {@link CooLAPPacket}.
	 * @param key = the key to get {@link Object} from
	 * @return the {@link Object} or null
	 */
	public Object getValue(String key)
	{
		return values.get(key);
	}
	
	/**
	 * Check if this {@link CooLAPPacket} contains committed key.
	 * @param key = the key to search for
	 * @return flag if this {@link CooLAPPacket} contains the key
	 */
	public boolean containsValue(String key)
	{
		return values.containsKey(key);
	}
	
	/**
	 * Method to check if this {@link CooLAPPacket} has his
	 * header data initialized before.
	 * @return flag if {@link #headerInitialized}
	 */
	public boolean hasHeader()
	{
		return headerInitialized;
	}
	
	/**
	 * Method to convert this {@link CooLAPPacket} to an
	 * simple byte array with packet data.
	 * @return this {@link CooLAPPacket} as byte array
	 * @throws IOException when converting went wrong
	 */
	public byte[] toByteArray() throws IOException
	{
		// Flush the title endian stream 
		leo.flush();
		leo.close();
		
		// And return the byte stream
		return out.toByteArray();
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