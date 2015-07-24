/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.*;

import java.io.File;
import java.util.*;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooStation extends CooData
{
	protected String name;
	protected File file;
	protected int XOffset;
	protected int YOffset;
	protected int ZOffset;

	protected CooTotalstation totalStation;
	protected CooRegionDividing regionDeviding;
	protected List<CooMeasurement> measurements;
	protected CooVerifyMeasurement verifyMeasurement;
	protected CooGateway gateway;
	
	public CooStation()
	{
		file = new File("");
		totalStation = new CooTotalstation();
		regionDeviding = new CooRegionDividing();
		measurements = new ArrayList<CooMeasurement>();
		verifyMeasurement = new CooVerifyMeasurement();
		gateway = new CooGateway();
	}

	@Override
	public void toXML(Document doc, Element root)
	{
		Element station = addElement(doc, root, "Station");
		station.setAttribute("Name", name);
		station.setAttribute("File", file.getAbsolutePath());
		station.setAttribute("XOffset", String.valueOf(XOffset));
		station.setAttribute("YOffset", String.valueOf(YOffset));
		station.setAttribute("ZOffset", String.valueOf(ZOffset));

		totalStation.toXML(doc, station);
		regionDeviding.toXML(doc, station);

		// Add all measurements
		Element measurements = addElement(doc, station,
			"Measurements");
		this.measurements.forEach(measurement ->
			measurement.toXML(doc, measurements));

		verifyMeasurement.toXML(doc, station);
		gateway.toXML(doc, station);
	}
	
	@Override
	public void fromXML(Element station)
	{
		// Not needed we get the single station here
//		Element station = getSingleElement(root, "Station");
		if(Objects.nonNull(station))
		{
			name = station.getAttribute("Name");
			// TODO parse to file, or only text?
//			file = Integer.valueOf(station.getAttribute("X"));
			XOffset = Integer.valueOf(station.getAttribute("XOffset"));
			YOffset = Integer.valueOf(station.getAttribute("YOffset"));
			ZOffset = Integer.valueOf(station.getAttribute("ZOffset"));
			
			totalStation.fromXML(station);
			regionDeviding.fromXML(station);
			
			// Load all measurements
			Element measurements = getSingleElement(station,
							"Measurements");
			addToList("Measurement", measurements,
				CooMeasurement.class, this.measurements);
			
			verifyMeasurement.fromXML(station);
			gateway.fromXML(station);
		}
	}
}