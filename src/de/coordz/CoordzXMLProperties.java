/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz;

import static de.util.CoordzXmlDomUtil.*;

import java.io.File;
import java.util.Objects;

import javafx.beans.property.Property;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class CoordzXMLProperties extends CoordzProperties
{
	public static final String PROP_FILE = "./Coordz.save";
	private static CoordzXMLProperties systemProperties = new CoordzXMLProperties();

	public static CoordzXMLProperties getSystemProperties()
	{
		return systemProperties;
	}

	public static void setSystemProperties(
					CoordzXMLProperties systemProperties)
	{
		if(Objects.nonNull(systemProperties))
		{
			CoordzXMLProperties.systemProperties = systemProperties;
		}
	}

	@Override
	public void load(File file)
	{
		try
		{
			if(!file.exists())
			{
				System.out.println("Can't find properties-file ");
				return;
			}

//			Element root = getDocumentBuilder().parse(file)
//				.getDocumentElement();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void storeProp(Element parent, String attribute)
	{
		storeProp(parent, attribute, "");
	}

	protected void storeProp(Element parent, String attribute, String def)
	{
		String value = getStringAttributeValue(parent, attribute);

		if(value != null)
		{
			putString(attribute, value);
		}
	}

	protected void storePropBoolean(Element parent, String attribute)
	{
		String value = getStringAttributeValue(parent, attribute);

		if(value != null)
		{
			putBoolean(attribute, Boolean.valueOf(value));
		}
	}

	protected void storePropInteger(Element parent, String attribute)
	{
		String value = getStringAttributeValue(parent, attribute);

		if(value != null)
		{
			putInt(attribute, Integer.valueOf(value));
		}
	}

	protected void storePropDouble(Element parent, String attribute)
	{
		String value = getStringAttributeValue(parent, attribute);

		if(value != null)
		{
			putDouble(attribute, Double.valueOf(value));
		}
	}

	@Override
	public boolean save(File file)
	{
		try
		{
			File parentFolder = file.getParentFile();
			if(parentFolder != null && !parentFolder.exists())
			{
				parentFolder.mkdirs();
			}

			Document doc = getDocumentBuilder().newDocument();

//			Element root = addElement(doc, doc, XML_ROOT_ELEMENT);
			saveFile(doc, file);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	protected void saveFile(Document doc, File file)
		throws TransformerException
	{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
			"{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(file);

		transformer.transform(source, result);
	}

	protected Object getValue(String prop)
	{
		Property<?> p = get(prop);
		if(p != null && p.getValue() != null)
		{
			return p.getValue();
		}

		return null;
	}

	public static void saveSystemProperties()
	{
		getSystemProperties().save(new File(PROP_FILE));
	}
}
