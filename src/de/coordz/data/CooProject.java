/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data;

import static de.util.CooXmlDomUtil.*;

import java.time.LocalDate;
import java.util.*;

import javafx.beans.property.*;

import org.w3c.dom.*;

import de.coordz.data.base.*;

public class CooProject extends CooData
{
	/** {@link StringProperty} for the project name */
	protected StringProperty name;
	/** {@link ObjectProperty} for the project {@link LocalDate} */
	protected ObjectProperty<LocalDate> date;
	/** {@link ObjectProperty} for the project {@link CooLAPSoftware} */
	protected ObjectProperty<CooLAPSoftware> lapSoftware;
	
	/** {@link List} with all project {@link CooStation} */
	protected List<CooStation> stations;

	public CooProject()
	{
		name = new SimpleStringProperty();
		date = new SimpleObjectProperty<LocalDate>();
		lapSoftware = new SimpleObjectProperty<CooLAPSoftware>(
						new CooLAPSoftware());
		stations = new ArrayList<CooStation>();
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
			// TODO parse the date
//			date.set(LocalDate.parse(project.getAttribute("Date")));
			lapSoftware.get().fromXML(getSingleElement(project,
				"LAPSoftware"));

			// Load all stations
			Element stations = getSingleElement(project,
							"Stations");
			addToList("Station", stations,
				CooStation.class, this.stations);
		}
	}
	
	public List<CooStation> getStations()
	{
		return stations;
	}
	
	public StringProperty nameProperty()
	{
		return name;
	}
	
	public ObjectProperty<LocalDate> dateProperty()
	{
		return date;
	}
	
	public ObjectProperty<CooLAPSoftware> lapSoftwareProperty()
	{
		return lapSoftware;
	}
}