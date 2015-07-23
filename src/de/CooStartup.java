/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de;

import static de.util.CooXmlDomUtil.*;

import java.io.File;

import javafx.application.Application;

import org.w3c.dom.*;

import de.coordz.data.*;
import de.gui.CooMainFrame;
import de.util.CooXmlDomUtil;

public class CooStartup
{
	public static void main(String[] args)
	{
		// XXX Add this after debug
//		if(CoordzPreferencesUtil.isCoordzAlreadyRunning())
//		{
//			System.err.println("ERROR: Coordz already running");
//			CoordzSystem.exit(CoordzSystem.FATAL);
//		}
//
//		CoordzPreferencesUtil.setCoordzRunning(true);
		
		// FORTEST Test the new xml db
		textXMLBasedDB();

		Application.launch(CooMainFrame.class, args);
	}

	private static void textXMLBasedDB()
	{
		try
		{
			// Create Costomer and Project
			CooCustomer customerTest = new CooCustomer();
			CooProject projectTest = new CooProject("Test");

//			// Load Customer
//			Element root = getDocumentBuilder().parse(
//				new File("./CoordzXML/customer.xml"))
//				 .getDocumentElement();
//			customerTest.fromXML(root);
//			
//			// Save Customer
//			Document doc = getDocumentBuilder().newDocument();
//			Element root2 = addElement(doc, doc, "CoordzData");
//			customerTest.toXML(doc, root2);
//			CooXmlDomUtil.saveFile(doc, new File(
//				"./CoordzXML/customer.xml"));
			
			// Load Project
			Element root = getDocumentBuilder().parse(
				new File("./CoordzXML/project.xml"))
				 .getDocumentElement();
			projectTest.fromXML(root);
			
			// Save Project
			Document doc = getDocumentBuilder().newDocument();
			Element root2 = addElement(doc, doc, "CoordzData");
			projectTest.toXML(doc, root2);
			CooXmlDomUtil.saveFile(doc, new File(
				"./CoordzXML/project.xml"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
}