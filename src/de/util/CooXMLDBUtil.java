/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util;

import static de.util.CooXmlDomUtil.*;

import java.io.File;
import java.util.*;

import org.w3c.dom.Element;

import de.coordz.data.*;
import de.util.log.CooLog;


public class CooXMLDBUtil
{
	public static final String XML_CUSTOMER_ROOT = "Customer";
	public static final String XML_PROJECT_ROOT = "Project";
	
	public static final String COORDZ_XML_DATABASE = "customer";
	public static final String DATA_FILE_EXT = ".coordz";
	public static final String CUSTOMER_FILE = "customer" 
					+ DATA_FILE_EXT;
	
	public static List<CooCustomer> getAllCustomers(File xmlDB)
	{
		List<CooCustomer> customers = new ArrayList<CooCustomer>();

		Arrays.asList(xmlDB.listFiles()).forEach(f ->
		{
			// Found customer directory
			if(f.isDirectory())
			{
				File customerXml = new File(f, CUSTOMER_FILE);
				CooCustomer customer = new CooCustomer();
				customers.add(customer);
				
				try
				{
					Element customerRot = getDocumentBuilder().parse(customerXml)
					 .getDocumentElement();
					customer.fromXML(getSingleElement(customerRot,
						XML_CUSTOMER_ROOT));
				}
				catch(Exception e)
				{
					CooLog.error("Could not load customer", e);
				}

				Arrays.asList(f.listFiles()).forEach(p ->
				{
					if(!p.getName().equals(customerXml.getName()))
					{
						CooProject project = new CooProject();

						try
						{
							Element root = getDocumentBuilder().parse(p)
								.getDocumentElement();
							project.fromXML(getSingleElement(root,
								XML_PROJECT_ROOT));
							
							customer.addProject(project.nameProperty().get(),
								project);
						}
						catch(Exception e)
						{
							CooLog.error("Could not load project", e);
						}
					}
		         });
			}
		});
		
		return customers;
	}
}