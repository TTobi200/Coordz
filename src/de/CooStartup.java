/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de;

import javafx.application.Application;
import de.coordz.CooSystem;
import de.gui.CooMainFrame;
import de.util.CooPreferencesUtil;

public class CooStartup
{
	public static void main(String[] args)
	{
		if(CooPreferencesUtil.isCoordzAlreadyRunning())
		{
			System.err.println("ERROR: Coordz already running");
			CooSystem.exit(CooSystem.FATAL);
		}

		CooPreferencesUtil.setCoordzRunning(true);
		
		// Save Project
//		Document doc = getDocumentBuilder().newDocument();
//		Element root2 = addElement(doc, doc, "CoordzData");
//		projectTest.toXML(doc, root2);
//		CooXmlDomUtil.saveFile(doc, new File(
//			"./CoordzXML/project.xml"));
		
		Application.launch(CooMainFrame.class, args);
	}
}