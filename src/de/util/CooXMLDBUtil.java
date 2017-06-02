/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util;

import static de.util.CooXmlDomUtil.*;

import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import de.coordz.data.*;
import de.util.log.CooLog;
import javafx.beans.property.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class CooXMLDBUtil
{
	protected static ObjectProperty<File> xmlDBFolder = new SimpleObjectProperty<>();;

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

	/** {@link String} for the ustomer logo file (always the same) */
	public static final String CUSTOMER_LOGO = "Logo.png";
	/** {@link String} for the  customer logo picture type (always png)*/
	public static final String CUSTOMER_LOGO_PIC_TYPE = "png";

	/**
	 * Method to save {@link CooProject} from given {@link CooCustomer}.
	 * @param customer = the {@link CooCustomer} of this {@link CooProject}
	 * @param project = the {@link CooProject} to save
	 */
	public static void saveProject(CooCustomer customer, CooProject project)
	{
		File prjFile = new File(xmlDBFolder.get().getAbsolutePath()
								+ File.separator
								+ customer.nameProperty().get()
								+ File.separator
								+ project.nameProperty().get()
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
		File customerFile = new File(xmlDBFolder.get().getAbsolutePath()
										+ File.separator
										+ customer.nameProperty().get()
										+ File.separator
										+ CUSTOMER_FILE);
		customerFile.getParentFile().mkdirs();

		File customerLogo = new File(xmlDBFolder.get().getAbsolutePath()
										+ File.separator
										+ customer.nameProperty().get()
										+ File.separator
										+ CUSTOMER_LOGO);
		
		saveData(customer, customerFile);
		saveLogo(customer, customerLogo);
		CooLog.debug("Save Customer: " + customerFile.getAbsolutePath());
	}

	/**
	 * Method to save the {@link CooCustomer} logo
	 * @param customer = the {@link CooCustomer} to save the logo from
	 * @param customerLogo = the logo
	 */
	public static void saveLogo(CooCustomer customer, File customerLogo)
	{
		try
		{
			Image logo = customer.logoProprty().get();
			if(Objects.nonNull(logo))
			{
				ImageIO.write(
					SwingFXUtils.fromFXImage(logo, null),
					CUSTOMER_LOGO_PIC_TYPE, customerLogo);
				CooLog.debug("Save Customer Logo: " + customerLogo.getAbsolutePath());
			}
		}
		catch(IOException e)
		{
			CooLog.error("Error while saving customer logo", e);
		}
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
	 * Method to parse all {@link CooCustomer} from given xml-Databse.
	 * @param xmlDB = the xml-Database folder
	 * @return {@link List} with all {@link CooCustomer}
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static List<CooCustomer> getAllCustomers(File xmlDB) throws SAXException, 
			IOException, ParserConfigurationException
	{
		List<CooCustomer> customers = new ArrayList<>();

		if(Objects.nonNull(xmlDB) && xmlDB.exists() && xmlDB.isDirectory())
		{
			xmlDBFolder.set(xmlDB);

			for(File f : Arrays.asList(xmlDB.listFiles()))			
			{
				// Found customer directory
				if(f.isDirectory())
				{
					File customerXml = new File(f, CUSTOMER_FILE);
					File customerLogo = new File(f, CUSTOMER_LOGO);
					CooCustomer customer = new CooCustomer();
					customers.add(customer);

					if(customerLogo.exists())
					{
						customer.logoProprty().set(
							new Image(String.valueOf(
								customerLogo.toURI().toURL())));
					}

					Element customerRot = getDocumentBuilder().parse(
						customerXml)
						.getDocumentElement();
					customer.fromXML(getSingleElement(customerRot,
						XML_CUSTOMER_ROOT));

					for(File p : Arrays.asList(f.listFiles()))
					{
						String pFileName = p.getName();

						if(!pFileName.equals(CUSTOMER_FILE) &&
							pFileName.endsWith(DATA_FILE_EXT))
						{
							CooProject project = new CooProject();

							Element root = getDocumentBuilder().parse(p)
								.getDocumentElement();
							project.fromXML(getSingleElement(root,
								XML_PROJECT_ROOT));

							customer.addProject(project);
						}
					}
				}
			}
		}

		return customers;
	}

	public static void deleteCustomer(CooCustomer customer)
	{
		File customerFolder = new File(xmlDBFolder.get().getAbsolutePath()
										+ File.separator
										+ customer.nameProperty().get()
										+ File.separator);
		if(customerFolder.exists())
		{
			Arrays.asList(customerFolder.listFiles()).forEach(
				f -> {
					f.delete();
				});
			customerFolder.delete();

			CooLog.debug("Delete Customer: " + customerFolder.getAbsolutePath());
		}
	}

	public static void deleteProject(CooCustomer customer, CooProject project)
	{
		customer.getProjects().remove(project);
		File prjFile = new File(xmlDBFolder.get().getAbsolutePath()
								+ File.separator
								+ customer.nameProperty().get()
								+ File.separator
								+ project.nameProperty().get()
								+ DATA_FILE_EXT);

		prjFile.delete();

		CooLog.debug("Delete Project: " + prjFile.getAbsolutePath());
	}
}