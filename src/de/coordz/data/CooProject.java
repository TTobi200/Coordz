/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data;

import static de.util.CooXmlDomUtil.*;

import java.time.LocalDate;
import java.util.Objects;

import javafx.beans.property.*;
import javafx.collections.*;

import org.w3c.dom.*;

import de.coordz.data.base.*;
import de.util.CooTimeUtil;

public class CooProject extends CooData
{
	/** {@link StringProperty} for the project name */
	protected StringProperty name;
	/** {@link ObjectProperty} for the project {@link LocalDate} */
	protected ObjectProperty<LocalDate> date;
	/** {@link ObjectProperty} for the project {@link CooLAPSoftware} */
	protected ObjectProperty<CooLAPSoftware> lapSoftware;
	
	/** {@link ObservableList} with all project {@link CooStation} */
	protected ObservableList<CooStation> stations;

	public CooProject()
	{
		name = new SimpleStringProperty();
		date = new SimpleObjectProperty<LocalDate>();
		lapSoftware = new SimpleObjectProperty<CooLAPSoftware>(
						new CooLAPSoftware());
		stations = FXCollections.observableArrayList();
	}

	@Override
	public void toXML(Document doc, Element root)
	{
		Element project = addElement(doc, root, "Project");
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
			name.set(project.getAttribute("Name"));
			date.set(LocalDate.parse(project.getAttribute("Date"),
				CooTimeUtil.SIMPLE_DATE_FORMATTER));
			lapSoftware.get().fromXML(getSingleElement(project,
				"LAPSoftware"));

			// Load all stations
			Element stations = getSingleElement(project,
							"Stations");
			addToList("Station", stations,
				CooStation.class, this.stations);
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
	 * @return {@link #name}
	 */
	public StringProperty nameProperty()
	{
		return name;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #date}
	 */
	public ObjectProperty<LocalDate> dateProperty()
	{
		return date;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #lapSoftware}
	 */
	public ObjectProperty<CooLAPSoftware> lapSoftwareProperty()
	{
		return lapSoftware;
	}
}