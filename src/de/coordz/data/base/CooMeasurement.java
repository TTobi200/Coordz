/*
 * $Header$
 * 
 * $Log$
 * Copyright � 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.*;

import java.time.LocalDate;
import java.util.Objects;

import javafx.beans.property.*;
import javafx.collections.*;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooMeasurement extends CooData
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

	/** {@link ObservableList} with all measurement {@link CooReticle} */
	protected ObservableList<CooReticle> reticles;
	/** {@link ObservableList} with all measurement {@link CooTarget} */
	protected ObservableList<CooTarget> targets;
	
	public CooMeasurement()
	{
		name = new SimpleStringProperty();
		date = new SimpleObjectProperty<LocalDate>();
		from = new SimpleStringProperty();
		to = new SimpleStringProperty();
		weather = new SimpleStringProperty();
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
			// TODO add LocalDate format
			// date = laser.getAttribute("Date");
			from.set(measurement.getAttribute("From"));
			to.set(measurement.getAttribute("To"));
			weather.set(measurement.getAttribute("Weather"));

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
	
	public ObservableList<CooReticle> getReticles()
	{
		return reticles;
	}
	
	public ObservableList<CooTarget> getTargets()
	{
		return targets;
	}
	
	public StringProperty nameProperty()
	{
		return name;
	}
	
	public ObjectProperty<LocalDate> dateProperty()
	{
		return date;
	}
	
	public StringProperty fromProperty()
	{
		return from;
	}
	
	public StringProperty toProperty()
	{
		return to;
	}
	
	public StringProperty weatherProperty()
	{
		return weather;
	}
}