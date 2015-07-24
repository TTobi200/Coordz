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
	protected List<CooRectangle> specification;
	protected List<CooRectangle> result;

	public CooVerifyMeasurement()
	{
		specification = new ArrayList<CooRectangle>();
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
		this.specification.forEach(r -> r.toXML(doc, specification));

		// Add all rectangles from result
		Element result = addElement(doc, verifyMeasurement,
			"Result");
		this.result.forEach(r -> r.toXML(doc, result));
	}

	@Override
	public void fromXML(Element verifyMeasurement)
	{
		if(Objects.nonNull(verifyMeasurement))
		{
			// Load all specification rectangles
			Element specification = getSingleElement(verifyMeasurement,
							"Specification");
			addToList("Rectangle", specification,
				CooRectangle.class, this.specification);

			// Load all result rectangles
			Element result = getSingleElement(verifyMeasurement,
							"Result");
			addToList("Rectangle", result,
				CooRectangle.class, this.result);
		}
	}
}