/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.*;

import java.time.LocalDate;
import java.util.Objects;

import org.w3c.dom.*;

import de.coordz.db.xml.CooDBXML;
import de.util.CooTimeUtil;
import javafx.beans.property.*;
import javafx.collections.*;

public class CooMeasurement implements CooDBXML
{
	/** {@link StringProperty} for the measurement name */
	protected StringProperty name;
	/** {@link StringProperty} for the measurement {@link LocalDate} */
	protected ObjectProperty<LocalDate> date;
	/** {@link StringProperty} for the measurement time from */
	protected StringProperty from;
	/** {@link StringProperty} for the measurement time to */
	protected StringProperty to;
	/** {@link StringProperty} for the measurement weather outside */
	protected StringProperty weather;
	/** {@link StringProperty} for the measurement notes */
	protected StringProperty notes;

	/** {@link ObjectProperty} for the station {@link CooTotalstation} */
	protected ObjectProperty<CooTotalstation> totalStation;
	/** {@link ObservableList} with all measurement {@link CooReticle} */
	protected ObservableList<CooReticle> reticles;
	/** {@link ObservableList} with all measurement {@link CooTarget} */
	protected ObservableList<CooTarget> targets;
	
	public CooMeasurement()
	{
		name = new SimpleStringProperty();
		date = new SimpleObjectProperty<>();
		from = new SimpleStringProperty();
		to = new SimpleStringProperty();
		weather = new SimpleStringProperty();
		notes = new SimpleStringProperty();
		totalStation = new SimpleObjectProperty<>(
						new CooTotalstation());
		reticles = FXCollections.observableArrayList();
		targets = FXCollections.observableArrayList();
	}

	@Override
	public void toXML(Document doc, Element root)
	{
		Element measurement = addElement(doc, root,
			"Measurement");
		measurement.setAttribute("Name", name.get());
		measurement.setAttribute("Date", String.valueOf(date.get()));
		measurement.setAttribute("From", from.get());
		measurement.setAttribute("To", to.get());
		measurement.setAttribute("Weather", weather.get());
		measurement.setAttribute("Notes", notes.get());

		totalStation.get().toXML(doc, measurement);
		Element reticles = addElement(doc, measurement,
			"Reticles");
		this.reticles.forEach(r -> r.toXML(doc, reticles));

		Element targets = addElement(doc, measurement,
			"Targets");
		this.targets.forEach(t -> t.toXML(doc, targets));
	}

	@Override
	public void fromXML(Element measurement)
	{
		if(Objects.nonNull(measurement))
		{
			name.set(measurement.getAttribute("Name"));
			date.set(LocalDate.parse(measurement.getAttribute("Date"),
				CooTimeUtil.SIMPLE_DATE_FORMATTER));
			
			from.set(measurement.getAttribute("From"));
			to.set(measurement.getAttribute("To"));
			weather.set(measurement.getAttribute("Weather"));
			notes.set(measurement.getAttribute("Notes"));

			totalStation.get().fromXML(getSingleElement(measurement,
							"Totalstation"));
			
			// Load all reticles
			Element reticles = getSingleElement(measurement,
							"Reticles");
			addToList("Reticle", reticles,
				CooReticle.class, this.reticles);

			// Load all targets
			Element targets = getSingleElement(measurement,
				"Targets");
			addToList("Target", targets,
				CooTarget.class, this.targets);
		}
	}
	
	/**
	 * Method to access {@link CooReticle}
	 * @return {@link #reticles}
	 */
	public ObservableList<CooReticle> getReticles()
	{
		return reticles;
	}
	
	/**
	 * Method to access {@link CooTarget}
	 * @return {@link #targets}
	 */
	public ObservableList<CooTarget> getTargets()
	{
		return targets;
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
	 * @return {@link #from}
	 */
	public StringProperty fromProperty()
	{
		return from;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #to}
	 */
	public StringProperty toProperty()
	{
		return to;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #weather}
	 */
	public StringProperty weatherProperty()
	{
		return weather;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #notes}
	 */
	public StringProperty notesProperty()
	{
		return notes;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #totalStation}
	 */
	public ObjectProperty<CooTotalstation> totalStationProperty()
	{
		return totalStation;
	}
}