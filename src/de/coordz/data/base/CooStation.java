/*
 * $Header$
 * 
 * $Log$
 * Copyright � 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.*;

import java.io.File;
import java.util.Objects;

import javafx.beans.property.*;
import javafx.collections.*;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooStation extends CooData
{
	/** {@link StringProperty} for the station name */
	protected StringProperty name;
	/** {@link ObjectProperty} for the station file */
	protected ObjectProperty<File> file;
	/** {@link IntegerProperty} for the station x offset */
	protected IntegerProperty xOffset;
	/** {@link IntegerProperty} for the station y offset */
	protected IntegerProperty yOffset;
	/** {@link IntegerProperty} for the station z offset */
	protected IntegerProperty zOffset;

	/** {@link ObjectProperty} for the station {@link CooTotalstation} */
	protected ObjectProperty<CooTotalstation> totalStation;
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
		file = new SimpleObjectProperty<File>();
		xOffset = new SimpleIntegerProperty();
		yOffset = new SimpleIntegerProperty();
		zOffset = new SimpleIntegerProperty();
		totalStation = new SimpleObjectProperty<CooTotalstation>(
						new CooTotalstation());
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
		station.setAttribute("File", file.get().getAbsolutePath());
		station.setAttribute("XOffset", String.valueOf(xOffset.get()));
		station.setAttribute("YOffset", String.valueOf(yOffset.get()));
		station.setAttribute("ZOffset", String.valueOf(zOffset.get()));

		totalStation.get().toXML(doc, station);
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
			// TODO parse to file, or only text?
//			file.set(Integer.valueOf(station.getAttribute("X"));
			xOffset.set(Integer.valueOf(station.getAttribute("XOffset")));
			yOffset.set(Integer.valueOf(station.getAttribute("YOffset")));
			zOffset.set(Integer.valueOf(station.getAttribute("ZOffset")));
			
			totalStation.get().fromXML(getSingleElement(station,
				"Totalstation"));
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
	 * @return {@link #file}
	 */
	public ObjectProperty<File> fileProperty()
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
	 * @return {@link #totalStation}
	 */
	public ObjectProperty<CooTotalstation> totalStationProperty()
	{
		return totalStation;
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
}