/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.*;

import java.time.LocalDate;
import java.util.*;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooMeasurement extends CooData
{
	protected String name;
	protected LocalDate date;
	protected String from;
	protected String to;
	protected String weather;

	protected List<CooReticle> reticles;
	protected List<CooTarget> targets;
	
	public CooMeasurement()
	{
		reticles = new ArrayList<CooReticle>();
		targets = new ArrayList<CooTarget>();
	}

	@Override
	public void toXML(Document doc, Element root)
	{
		Element measurement = addElement(doc, root, "Measurement");
		measurement.setAttribute("Name", name);
		measurement.setAttribute("Date", String.valueOf(date));
		measurement.setAttribute("From", from);
		measurement.setAttribute("To", to);
		measurement.setAttribute("Weather", weather);

		Element reticles = addElement(doc, measurement,
			"Reticles");
		this.reticles.forEach(r -> r.toXML(doc, reticles));

		Element targets = addElement(doc, measurement,
			"Targets");
		this.targets.forEach(t -> t.toXML(doc, targets));
	}

	@Override
	public void fromXML(Element measurement)
	{
		// Not needed we get the single target here
//		Element measurement = getSingleElement(root, "Measurement");
		if(Objects.nonNull(measurement))
		{
			name = measurement.getAttribute("Name");
			// TODO add LocalDate format
			// date = laser.getAttribute("Date");
			from = measurement.getAttribute("From");
			to = measurement.getAttribute("To");
			weather = measurement.getAttribute("Weather");

			// Load all reticles
			Element reticles = getSingleElement(measurement,
							"Reticles");
			addToList("Reticle", reticles,
				CooReticle.class, this.reticles);

			// Load all targets
			Element targets = getSingleElement(measurement,
				"Targets");
			addToList("Target", targets,
				CooTarget.class, this.targets);
		}
	}
}