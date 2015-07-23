/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data;

import static de.util.CooXmlDomUtil.addElement;

import java.util.*;

import org.w3c.dom.*;

import de.coordz.data.base.*;

public class CooProject extends CooData
{
	protected String name;
	protected CooGeneral general;
	protected CooLAPSoftware lapSoftware;
	protected List<CooStation> stations;
	
	public CooProject(String name)
	{
		this.name = name;
		general = new CooGeneral();
		lapSoftware = new CooLAPSoftware();
		stations = new ArrayList<CooStation>();
	}
	
	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public void toXML(Document doc, Element root)
	{
		Element project = addElement(doc, root, "Project");
		general.toXML(doc, project);
		lapSoftware.toXML(doc, project);
		
		// Add all stations
		stations.forEach(s -> s.toXML(doc, project));
	}
}