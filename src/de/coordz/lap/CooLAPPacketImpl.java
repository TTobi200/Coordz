/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap;

import de.coordz.lap.comp.*;
import javafx.collections.*;

/**
 * Class that implements {@link CooLAPPacket} to access
 * values from server packet easily with following methods:
 * <li> {@link #getResult()}
 * <li> {@link #getProjectors()}
 * <li> {@link #getShiftRotationInfo()}
 * 
 * @author tobias.ohm
 * @version 1.0
 */
public class CooLAPPacketImpl extends CooLAPPacket
{
	/**
	 * Enum that describes packet results.
	 */
	public enum Result 
	{
		SUCCESSFUL, FAULTY,
		FILE_NOT_FOUND, FILE_NOT_READABLE, NO_OPEN_FILE,
		MANUAL_CALIBRATION_REQUIRED, SYSTEM_NOT_CALIBRATED,
		PROJECTION_OU_OF_RANGE, NO_VALID_CALIBRATION,
		UNKNOWN
	}
	
	/**
	 * Method to get the {@link Result} of this packet.
	 * @return the {@link Result} of this packet
	 */
	public Result getResult()
	{
		Result ret = Result.UNKNOWN;
		
		// Check if packet contains a result
		if(containsValue("Result"))
		{
			// Get the read message id
			short msgID = (short)getValue("Message_ID");
			// Get the read result value
			short result = (short)getValue("Result");
	
			switch(msgID)
			{
				case CooLAPClient.AUTOMATIC_CALIBRATION:
				case CooLAPClient.RESULT_OF_AUTOMATIC_CALIBRATION:
				case CooLAPClient.SWITCH_CALIBRATION:
				case CooLAPClient.RESULT_OF_SWITCH_CALIBRATION:
				case CooLAPClient.SWITCH_CALIBRATION_ACKNOWLEDGE:
				case CooLAPClient.RESULT_OF_SWITCH_CALIBRATION_ACKNOWLEDGE:
				case CooLAPClient.STOP_PROJECTION:
				case CooLAPClient.RESULT_OF_STOP_PROJECTION:
					// Results of calibration packets
					ret = result == 0 ? Result.SUCCESSFUL : 
						result == 1 ? Result.FAULTY : 
						result == 2 ? Result.FILE_NOT_FOUND : 
						result == 3 ? Result.FILE_NOT_READABLE : 
						result == 4 ? Result.MANUAL_CALIBRATION_REQUIRED : 
						Result.UNKNOWN;
					break;
	
				case CooLAPClient.START_PROJECTION:
				case CooLAPClient.RESULT_OF_START_PROJECTION:
				case CooLAPClient.START_AND_ADJUST_PROJECTION:
				case CooLAPClient.RESULT_OF_START_AND_ADJUST_PROJECTION:
					// Results of projection packets
					ret = result == 0 ? Result.SUCCESSFUL : 
						result == 1 ? Result.FILE_NOT_FOUND : 
						result == 2 ? Result.FILE_NOT_READABLE : 
						result == 3 ? Result.SYSTEM_NOT_CALIBRATED : 
						result == 4 ? Result.PROJECTION_OU_OF_RANGE : 
						Result.UNKNOWN;
					break;
	
				case CooLAPClient.SHOW_NEXT_CONTOUR:
				case CooLAPClient.RESULT_OF_SHOW_NEXT_CONTOUR:
				case CooLAPClient.SHOW_PREVIOUS_CONTOUR:
				case CooLAPClient.RESULT_OF_SHOW_PREVIOUS_CONTOUR:
					// Result of contour packets
					ret = result == 0 ? Result.SUCCESSFUL : 
						result == 1 ? Result.FILE_NOT_FOUND : 
						result == 2 ? Result.NO_OPEN_FILE : 
						result == 3 ? Result.NO_VALID_CALIBRATION : 
						result == 4 ? Result.PROJECTION_OU_OF_RANGE : 
						Result.UNKNOWN;
					break;
	
				case CooLAPClient.GET_SHIFT_ROTATION_INFO:
				case CooLAPClient.RESULT_OF_GET_SHIFT_ROTATION_INFO:
					// No result send
					break;
			}
		}
		
		return ret;
	}

	/**
	 * Method to get all {@link CooLAPProjector} from this packet.
	 * @return a {@link ObservableList} with {@link CooLAPProjector}
	 */
	public ObservableList<CooLAPProjector> getProjectors()
	{
		// Create list with all projectors
		ObservableList<CooLAPProjector> projectors = FXCollections.observableArrayList();

		// First check if we have projector set
		if(containsValue("ProjCount"))
		{
			// Number of calibrated projectors
			short projCount = (short)getValue("ProjCount");
			
			// Loop through all projectors
			for(int proj = 1; proj <= projCount; proj++)
			{
				CooLAPProjector projector = new CooLAPProjector(proj);
				projector.fromPacket(this);
				projectors.add(projector);
			}
		}
		
		return projectors;
	}
	
	/**
	 * Method to get the {@link CooLAPShiftRotationInfo} from this packet.
	 * @return the {@link CooShiftRotationInfo} of this packet.
	 */
	public CooLAPShiftRotationInfo getShiftRotationInfo()
	{
		CooLAPShiftRotationInfo info = new CooLAPShiftRotationInfo();
		info.fromPacket(this);
		return info;
	}
}