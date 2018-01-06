/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.*;

import java.util.Objects;

import org.w3c.dom.*;

import de.coordz.db.gen.dao.DaoStation;
import de.coordz.db.xml.CooDBXML;
import javafx.beans.property.*;
import javafx.collections.*;

public class CooStation extends DaoStation implements CooDBXML
{
	// FIXME $TO: Link this with database fields
	/** {@link ObjectProperty} for the station {@link CooRegionDividing} */
	protected ObjectProperty<CooRegionDividing> regionDeviding;
	/** {@link ObservableList} with all station {@link CooMeasurement} */
	protected ObservableList<CooMeasurement> measurements;
	/** {@link ObjectProperty} for the station {@link CooVerifyMeasurement} */
	protected ObjectProperty<CooVerifyMeasurement> verifyMeasurement;
	/** {@link ObjectProperty} for the station {@link CooGateway} */
	protected ObjectProperty<CooGateway> gateway;
	
	public CooStation()
	{
		regionDeviding = new SimpleObjectProperty<>(
						new CooRegionDividing());
		measurements = FXCollections.observableArrayList();
		verifyMeasurement = new SimpleObjectProperty<>(
						new CooVerifyMeasurement());
		gateway = new SimpleObjectProperty<>(
						new CooGateway());
	}

	@Override
	public void toXML(Document doc, Element root)
	{
		Element station = addElement(doc, root, "Station");
		station.setAttribute("Name", nameProperty().get());
		station.setAttribute("Description", descriptionProperty().get());
		station.setAttribute("File", fileProperty().get());
		station.setAttribute("XOffset", String.valueOf(xOffsetProperty().get()));
		station.setAttribute("YOffset", String.valueOf(yOffsetProperty().get()));
		station.setAttribute("ZOffset", String.valueOf(zOffsetProperty().get()));

		regionDeviding.get().toXML(doc, station);

		// Add all measurements
		Element measurements = addElement(doc, station,
			"Measurements");
		this.measurements.forEach(measurement ->
			measurement.toXML(doc, measurements));

		verifyMeasurement.get().toXML(doc, station);
		gateway.get().toXML(doc, station);
	}
	
	@Override
	public void fromXML(Element station)
	{
		if(Objects.nonNull(station))
		{
			nameProperty().set(station.getAttribute("Name"));
			descriptionProperty().set(station.getAttribute("Description"));
			fileProperty().set(station.getAttribute("File"));
			xOffsetProperty().set(Integer.valueOf(station.getAttribute("XOffset")));
			yOffsetProperty().set(Integer.valueOf(station.getAttribute("YOffset")));
			zOffsetProperty().set(Integer.valueOf(station.getAttribute("ZOffset")));
			
			regionDeviding.get().fromXML(getSingleElement(station,
				"RegionDividing"));
			
			// Load all measurements
			Element measurements = getSingleElement(station,
							"Measurements");
			addToList("Measurement", measurements,
				CooMeasurement.class, this.measurements);
			
			verifyMeasurement.get().fromXML(getSingleElement(station,
				"VerifyMeasurement"));
			gateway.get().fromXML(getSingleElement(station,
				"Gateway"));
		}
	}
	
	/**
	 * Method to access {@link CooMeasurement}
	 * @return {@link #measurements}
	 */
	public ObservableList<CooMeasurement> getMeasurements()
	{
		return measurements;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #regionDeviding}
	 */
	public ObjectProperty<CooRegionDividing> regionDevidingProperty()
	{
		return regionDeviding;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #verifyMeasurement}
	 */
	public ObjectProperty<CooVerifyMeasurement> verifyMeasurementProperty()
	{
		return verifyMeasurement;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #gateway}
	 */
	public ObjectProperty<CooGateway> gatewayProperty()
	{
		return gateway;
	}
	
	@Override
	public String toString()
	{
		return nameProperty().get();
	}
}