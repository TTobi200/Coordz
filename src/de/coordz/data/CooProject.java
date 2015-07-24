/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data;

import static de.util.CooXmlDomUtil.*;

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
		Element stations = addElement(doc, project, "Stations");
		this.stations.forEach(s -> s.toXML(doc, stations));
	}

	@Override
	public void fromXML(Element root)
	{
		Element project = getSingleElement(root, "Project");
		if(Objects.nonNull(project))
		{
			name = project.getAttribute("Name");
			general.fromXML(project);
			lapSoftware.fromXML(project);

			// Load all stations
			Element stations = getSingleElement(project,
							"Stations");
			addToList("Station", stations,
				CooStation.class, this.stations);
		}
	}
}