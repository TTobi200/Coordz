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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.*;

import de.coordz.data.*;
import de.util.log.CooLog;

public class CooXMLDBUtil
{
	/** {@link String} for the xml DB folder path */
	public static final String XML_DB_ROOT = "./CoordzXML/";

	/** {@link String} for the xml root node */
	public static final String XML_COORDZ_DATA_ROOT = "CoordzData";
	/** {@link String} for the xml customer node */
	public static final String XML_CUSTOMER_ROOT = "Customer";
	/** {@link String} for the xml project node */
	public static final String XML_PROJECT_ROOT = "Project";

	/** {@link String} for the xml DB file extension */
	public static final String DATA_FILE_EXT = ".coordz";
	/** {@link String} for the xml DB customer file (always the same) */
	public static final String CUSTOMER_FILE = "customer" + DATA_FILE_EXT;

	/**
	 * Method to save {@link CooProject} from given {@link CooCustomer}.
	 * @param customer = the {@link CooCustomer} of this {@link CooProject}
	 * @param project = the {@link CooProject} to save
	 */
	public static void saveProject(CooCustomer customer, CooProject project)
	{
		File prjFile = new File(XML_DB_ROOT + customer.nameProperty().get() +
								"/" + project.nameProperty().get()
								+ DATA_FILE_EXT);
		File customerFold = prjFile.getParentFile();

		// If custom folder not exist -
		// create it with customer data
		if(!customerFold.exists())
		{
			customerFold.mkdirs();
			saveCustomer(customer);
		}

		saveData(project, prjFile);
		CooLog.debug("Save Project: " +
						prjFile.getAbsolutePath());
	}

	/**
	 * Method to save {@link CooCustomer}.
	 * @param customer = the {@link CooCustomer} to save
	 */
	public static void saveCustomer(CooCustomer customer)
	{
		File customerFile = new File(XML_DB_ROOT
										+ customer.nameProperty().get() +
										"/" + CUSTOMER_FILE);
		customerFile.getParentFile().mkdirs();

		saveData(customer, customerFile);
		CooLog.debug("Save Customer: " + customerFile.getAbsolutePath());
	}

	/**
	 * Method to save {@link CooData} to given {@link File}.
	 * @param data = the {@link CooData} to save
	 * @param file = the {@link File} to store data in
	 */
	public static void saveData(CooData data, File file)
	{
		Document doc;
		try
		{
			doc = getDocumentBuilder().newDocument();
			Element root = addElement(doc, doc, XML_COORDZ_DATA_ROOT);
			data.toXML(doc, root);

			CooXmlDomUtil.saveFile(doc, file);
		}
		catch(ParserConfigurationException | TransformerException e)
		{
			CooLog.error("Could not save Data", e);
		}
	}

	/**
	 * Method to parse all {@link CooCustomer}.
	 * @return {@link List} with all {@link CooCustomer}
	 */
	public static List<CooCustomer> getAllCustomers()
	{
		return getAllCustomers((new File(XML_DB_ROOT)));
	}

	/**
	 * Method to parse all {@link CooCustomer} from given xml-Databse.
	 * @param xmlDB = the xml-Database folder
	 * @return {@link List} with all {@link CooCustomer}
	 */
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
					Element customerRot = getDocumentBuilder().parse(
						customerXml)
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

							customer.addProject(project);
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
	
	public static void deleteCustomer(CooCustomer customer)
	{
		File customerFolder = new File(XML_DB_ROOT
			+ customer.nameProperty().get() + "/");
		
		Arrays.asList(customerFolder.listFiles()).forEach(
			f -> {f.delete();});
		customerFolder.delete();
		
		CooLog.debug("Delete Customer: " + customerFolder.getAbsolutePath());
	}

	public static void deleteProject(CooCustomer customer, CooProject project)
	{
		customer.getProjects().remove(project);
		File prjFile = new File(XML_DB_ROOT + customer.nameProperty().get() +
								"/" + project.nameProperty().get()
								+ DATA_FILE_EXT);
		
		prjFile.delete();
		
		CooLog.debug("Delete Project: " + prjFile.getAbsolutePath());
	}
}