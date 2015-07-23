/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.addElement;

import java.util.*;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooGateway extends CooData
{
	protected String ip;
	protected String mac;
	
	protected List<CooLaser> laser;
	
	public CooGateway()
	{
		laser = new ArrayList<CooLaser>();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element gateway = addElement(doc, root, "Gateway");
		gateway.setAttribute("IP", ip);
		gateway.setAttribute("MAC", mac);
		
		laser.forEach(l -> l.toXML(doc, gateway));
	}
}