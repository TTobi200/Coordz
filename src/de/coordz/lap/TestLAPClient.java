/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap;

import java.io.*;

public class TestLAPClient
{
	public static void main(String[] args)
	{
		try
		{
			CooLAPClient client = new CooLAPClient(
				"127.0.0.1");
			
			// FORTEST $TO: Start an automatic calibration
			client.startAutoCalibration(new File(
				"doc/LAP Software/Calibration.cal"));
			
			// FORTEST $TO: Start an projection
//			client.startProjection(new File(
//				"doc/LAP Software/LaserData.ply"));
			
			// FORTEST $TO: Show next contour
//			client.nextContour();
			
			// FORTEST $TO: Show previous contour
//			client.previousContour();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}