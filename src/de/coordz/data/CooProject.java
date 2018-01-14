/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data;

import static de.util.CooSQLUtil.*;
import static de.util.CooXmlDomUtil.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

import org.w3c.dom.*;

import de.coordz.data.base.*;
import de.coordz.db.CooDB;
import de.coordz.db.gen.dao.DaoProject;
import de.coordz.db.gen.inf.*;
import de.coordz.db.xml.*;
import javafx.beans.property.*;
import javafx.collections.*;

public class CooProject extends DaoProject implements CooDBXML, CooDBLoad
{
	/** {@link ObjectProperty} for the project {@link CooLAPSoftware} */
	protected ObjectProperty<CooLAPSoftware> lapSoftware;
	
	/** {@link ObservableList} with all project {@link CooStation} */
	protected ObservableList<CooStation> stations;

	public CooProject()
	{
		lapSoftware = new SimpleObjectProperty<>(
						new CooLAPSoftware());
		stations = FXCollections.observableArrayList();
	}

	@Override
	public void toXML(Document doc, Element root)
	{
		Element project = addElement(doc, root, "Project");
		project.setAttribute("Name", nameProperty().get());
		project.setAttribute("Date", String.valueOf(dateProperty().get() 
			!= null ? dateProperty().get() : LocalDate.now()));
		
		lapSoftware.get().toXML(doc, project);

		// Add all stations
		Element stations = addElement(doc, project, "Stations");
		this.stations.forEach(s -> s.toXML(doc, stations));
	}

	@Override
	public void fromXML(Element project)
	{
		if(Objects.nonNull(project))
		{
			nameProperty().set(project.getAttribute("Name"));
			// FIXME: $TO: Add the date as time stamp
//			dateProperty().set(LocalDate.parse(project.getAttribute("Date"),
//				CooTimeUtil.SIMPLE_DATE_FORMATTER));
			lapSoftware.get().fromXML(getSingleElement(project,
				"LAPSoftware"));

			// Load all stations
			Element stations = getSingleElement(project,
							"Stations");
			addToList("Station", stations,
				CooStation.class, this.stations);
		}
	}
	
	@Override
	public void fromDB(CooDB database) throws SQLException
	{
		// FORTEST Select the stations
		stations.setAll(loadList(database, InfStation.TABLE_NAME, 
			InfStation.PROJECTID, CooStation.class, 
			projectIdProperty().get()));
		
		// FORTEST Select the lap software
		lapSoftware.set(loadDao(database, InfLAPSoftware.TABLE_NAME,
			InfLAPSoftware.PROJECTID, CooLAPSoftware.class,
			projectIdProperty().get()));
		
		// FORTEST load the station data
		for(CooStation station : stations)
		{
			station.fromDB(database);
		}
	}
	
	/**
	 * Method to access {@link CooStation}
	 * @return {@link #stations}
	 */
	public ObservableList<CooStation> getStations()
	{
		return stations;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #lapSoftware}
	 */
	public ObjectProperty<CooLAPSoftware> lapSoftwareProperty()
	{
		return lapSoftware;
	}
	
	@Override
	public String toString()
	{
		return nameProperty().get();
	}
}