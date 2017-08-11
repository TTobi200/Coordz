/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.lap;

public class TestLAPClient
{
	public static void main(String[] args)
	{
		try(CooLAPClient client = new CooLAPClient("10.221.46.207"))
		{
			// FORTEST $TO: Start an automatic calibration
//			CooLAPPacketImpl packet = client.startAutoCalibration(
//				new File("C:\\Users\\User\\Desktop\\Lasertest"
//				+ "\\01_Einmessungen\\01_Kalibrierdateien\\CheckPlate.cal"));
			
			// FORTEST $TO: Switch an calibration mode
//			CooLAPPacketImpl packet = client.switchCalibrationMode(CooLAPClient.AUTOMATIC_CALIBRATION_MODE,
//				new File("C:\\Users\\User\\Desktop\\Lasertest"
//				+ "\\01_Einmessungen\\01_Kalibrierdateien\\CheckPlate.cal"));
			
			// FORTEST $TO: Start an projection
//			CooLAPPacketImpl packet = client.startProjection(
//				new File("C:\\Users\\User\\Desktop\\Lasertest"
//				+ "\\01_Einmessungen\\02_Messmatrix\\LaserData.ply"));
			
			// FORTEST $TO: Start an adjusted projection
//			CooLAPPacketImpl packet = client.startAndAdjustProjection(
//				new File("C:\\Users\\User\\Desktop\\Lasertest"
//				+ "\\01_Einmessungen\\02_Messmatrix\\LaserData.ply"));
			
			// FORTEST $TO: Stop an projection
//			CooLAPPacketImpl packet = client.stopProjection();
			
			// FORTEST $TO: Show previous contour
//			CooLAPPacketImpl packet = client.previousContour();
			
//			System.out.println(packet.getResult());
			
//			CooLAPPacketImpl packet = client.getShiftRotationInfo();
//			System.out.println(packet.getShiftRotationInfo());
			
//			for(CooLAPProjector p : packet.getProjectors())
//			{
//				System.out.println(p);
//				
//				for(CooLAPTarget t : p.getTargets())
//				{
//					System.out.println(t.toString());
//				}
//			}
			
			// FORTEST $TO Stress test on LAP server
//			for(int i = 0; i < 200; i++)
//			{
//				// FORTEST $TO: Show previous contour
//				CooLAPPacketImpl packet = client.previousContour();
//			}
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
	}
}