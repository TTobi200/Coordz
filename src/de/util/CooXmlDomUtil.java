/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util;

import java.io.File;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import de.coordz.db.xml.CooDBXML;

public class CooXmlDomUtil
{
	public static Attr addAttribute(Document doc, Element parent,
					String attribute, Object value)
	{
		if(value != null)
		{
			Attr attr = doc.createAttribute(attribute);
			attr.setValue(String.valueOf(value));
			parent.setAttributeNode(attr);
			return attr;
		}
		return null;
	}

	public static Element addElement(Document doc, Node parent, String element)
	{
		return addElement(doc, parent, element, null);
	}

	public static Element addElement(Document doc, Node parent, String element,
					Object textValue)
	{
		Element elem = doc.createElement(element);
		if(Objects.nonNull(textValue))
		{
			elem.setTextContent(String.valueOf(textValue));
		}
		parent.appendChild(elem);

		return elem;
	}

	public static List<Element> getElements(Element root, String element)
	{
		NodeList list = root.getElementsByTagName(element);
		List<Element> ret = new ArrayList<>(list.getLength());

		for(int i = 0; i < list.getLength(); i++)
		{
			ret.add((Element)list.item(i));
		}

		return ret;
	}

	public static Element getSingleElement(Element root, String element)
	{
		NodeList list = root.getElementsByTagName(element);

		return isListEmpty(list) ? null : (Element)list.item(0);
	}

	public static boolean isListEmpty(NodeList list)
	{
		return list.getLength() == 0;
	}

	public static DocumentBuilder getDocumentBuilder()
		throws ParserConfigurationException
	{
		return DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}

	public static double getDoubleAttributeValue(Element element,
					String attribute)
	{
		double ret;

		Attr attr = element.getAttributeNode(attribute);

		try
		{
			ret = Double.valueOf(attr.getValue());
		}
		catch(NumberFormatException e)
		{
			ret = 0d;
		}
		return ret;
	}

	public static int getIntAttributeValue(Element element, String attribute)
	{
		int ret;

		Attr attr = element.getAttributeNode(attribute);

		try
		{
			ret = Integer.valueOf(attr.getValue());
		}
		catch(NumberFormatException e)
		{
			ret = 0;
		}
		return ret;
	}

	public static String getStringAttributeValue(Element element,
					String attribute)
	{
		Attr a = element.getAttributeNode(attribute);
		return a != null ? a.getValue() : null;
	}

	public static void saveFile(Document doc, File file)
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addToList(String tagName, Element parent, 
			Class clazz, List listToAdd)
	{
		try
		{
			NodeList xmlNode = parent.getElementsByTagName(
				tagName);

			for(int i = 0; i < xmlNode.getLength(); i++)
			{
				CooDBXML data = (CooDBXML)clazz.newInstance();
				data.fromXML((Element)xmlNode.item(i));
				listToAdd.add(data);
			}
		}
		catch(InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
}