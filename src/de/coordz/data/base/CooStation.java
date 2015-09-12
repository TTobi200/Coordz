/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.*;

import java.util.Objects;

import javafx.beans.property.*;
import javafx.collections.*;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooStation extends CooData
{
	/** {@link StringProperty} for the station name */
	protected StringProperty name;
	/** {@link StringProperty} for the station description */
	protected StringProperty description;
	/** {@link StringProperty} for the station file */
	protected StringProperty file;
	/** {@link IntegerProperty} for the station x offset */
	protected IntegerProperty xOffset;
	/** {@link IntegerProperty} for the station y offset */
	protected IntegerProperty yOffset;
	/** {@link IntegerProperty} for the station z offset */
	protected IntegerProperty zOffset;

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
		name = new SimpleStringProperty();
		description = new SimpleStringProperty();
		file = new SimpleStringProperty();
		xOffset = new SimpleIntegerProperty();
		yOffset = new SimpleIntegerProperty();
		zOffset = new SimpleIntegerProperty();
		regionDeviding = new SimpleObjectProperty<CooRegionDividing>(
						new CooRegionDividing());
		measurements = FXCollections.observableArrayList();
		verifyMeasurement = new SimpleObjectProperty<CooVerifyMeasurement>(
						new CooVerifyMeasurement());
		gateway = new SimpleObjectProperty<CooGateway>(
						new CooGateway());
	}

	@Override
	public void toXML(Document doc, Element root)
	{
		Element station = addElement(doc, root, "Station");
		station.setAttribute("Name", name.get());
		station.setAttribute("Description", description.get());
		station.setAttribute("File", file.get());
		station.setAttribute("XOffset", String.valueOf(xOffset.get()));
		station.setAttribute("YOffset", String.valueOf(yOffset.get()));
		station.setAttribute("ZOffset", String.valueOf(zOffset.get()));

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
			name.set(station.getAttribute("Name"));
			description.set(station.getAttribute("Description"));
			file.set(station.getAttribute("File"));
			xOffset.set(Integer.valueOf(station.getAttribute("XOffset")));
			yOffset.set(Integer.valueOf(station.getAttribute("YOffset")));
			zOffset.set(Integer.valueOf(station.getAttribute("ZOffset")));
			
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
	 * @return {@link #name}
	 */
	public StringProperty nameProperty()
	{
		return name;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #description}
	 */
	public StringProperty descriptionProperty()
	{
		return description;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #file}
	 */
	public StringProperty fileProperty()
	{
		return file;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #xOffset}
	 */
	public IntegerProperty xOffsetProperty()
	{
		return xOffset;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #yOffset}
	 */
	public IntegerProperty yOffsetProperty()
	{
		return yOffset;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #zOffset}
	 */
	public IntegerProperty zOffsetProperty()
	{
		return zOffset;
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