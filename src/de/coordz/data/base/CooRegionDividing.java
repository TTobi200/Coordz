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

public class CooRegionDividing extends CooData
{
	protected List<CooLaser> laser;
	
	public CooRegionDividing()
	{
		laser = new ArrayList<CooLaser>();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element regionDividing = addElement(doc, root,
			"RegionDividing");
		laser.forEach(l -> l.toXML(doc, regionDividing));
	}
}