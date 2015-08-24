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
			client.startAutoCalibration(new File(
				"doc/LAP Software/Calibration.cal"));
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}