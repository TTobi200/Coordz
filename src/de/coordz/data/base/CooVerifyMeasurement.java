/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.*;

import java.util.*;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooVerifyMeasurement extends CooData
{
	protected List<CooRectangle> spefication;
	protected List<CooRectangle> result;

	public CooVerifyMeasurement()
	{
		spefication = new ArrayList<CooRectangle>();
		result = new ArrayList<CooRectangle>();
	}

	@Override
	public void toXML(Document doc, Element root)
	{
		Element verifyMeasurement = addElement(doc, root,
			"VerifyMeasurement");

		// Add all rectangles from specification
		Element specification = addElement(doc, verifyMeasurement,
			"Specification");
		this.spefication.forEach(r -> r.toXML(doc, specification));

		// Add all rectangles from result
		Element result = addElement(doc, verifyMeasurement,
			"Result");
		this.result.forEach(r -> r.toXML(doc, result));
	}

	@Override
	public void fromXML(Element root)
	{
		Element verifyMeasurement = getSingleElement(root,
			"VerifyMeasurement");
		if(Objects.nonNull(verifyMeasurement))
		{
			// Load all lasers
			addToList("Specification", verifyMeasurement,
				CooRectangle.class, spefication);

			// Load all lasers
			addToList("Result", verifyMeasurement,
				CooRectangle.class, result);
		}
	}
}