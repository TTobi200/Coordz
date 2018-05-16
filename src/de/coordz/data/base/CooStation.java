/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooSQLUtil.*;
import static de.util.CooXmlDomUtil.*;

import java.sql.SQLException;
import java.util.Objects;

import org.w3c.dom.*;

import de.coordz.db.CooDB;
import de.coordz.db.gen.dao.DaoStation;
import de.coordz.db.gen.inf.*;
import de.coordz.db.xml.*;
import javafx.beans.property.*;
import javafx.collections.*;

public class CooStation extends DaoStation implements CooDBXML, CooDBLoad
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
	
	
	@Override
	public void fromDB(CooDB database) throws SQLException
	{
		// FORTEST select the region dividing
		regionDeviding.set(loadDao(database, InfRegionDividing.TABLE_NAME,
			InfRegionDividing.PROJECTID, CooRegionDividing.class,
			projectIdProperty().get()));
		regionDeviding.get().fromDB(database);
		
		// FORTEST Select the measurements
		measurements.setAll(loadList(database, InfMeasurement.TABLE_NAME, 
			InfMeasurement.STATIONID, CooMeasurement.class,
			stationIdProperty().get()));
		
		// FORTEST select the verify measurement
		verifyMeasurement.set(loadDao(database, InfVerifyMeasurement.TABLE_NAME,
			InfVerifyMeasurement.STATIONID, CooVerifyMeasurement.class,
			stationIdProperty().get()));
		verifyMeasurement.get().fromDB(database);
		
		// FORTEST select the gateway
		gateway.set(loadDao(database, InfGateway.TABLE_NAME,
			InfGateway.STATIONID, CooGateway.class,
			stationIdProperty().get()));
		gateway.get().fromDB(database);
		
		// FORTEST load the measurement data
		for(CooMeasurement measurement : measurements)
		{
			measurement.fromDB(database);
		}
	}
	
	@Override
	public void delete(CooDB database) throws SQLException
	{
		// Delete all referred data
		deleteAll(database, regionDeviding.get());
		deleteAll(database, measurements);
		deleteAll(database, verifyMeasurement.get());
		deleteAll(database, gateway.get());

		// Delete the DAO
		super.delete(database);
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