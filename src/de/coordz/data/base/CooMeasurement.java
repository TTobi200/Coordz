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

import de.coordz.db.gen.dao.DaoMeasurement;
import de.coordz.db.xml.CooDBXML;
import javafx.beans.property.*;
import javafx.collections.*;

public class CooMeasurement extends DaoMeasurement implements CooDBXML
{
	// FIXME $TO: Moved total station to separate table
	/** {@link ObjectProperty} for the station {@link CooTotalstation} */
	protected ObjectProperty<CooTotalstation> totalStation;
	/** {@link ObservableList} with all measurement {@link CooReticle} */
	protected ObservableList<CooReticle> reticles;
	/** {@link ObservableList} with all measurement {@link CooTarget} */
	protected ObservableList<CooTarget> targets;
	
	public CooMeasurement()
	{
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
		measurement.setAttribute("Name", nameProperty().get());
		measurement.setAttribute("Date", String.valueOf(dateProperty().get()));
		measurement.setAttribute("From", fromProperty().get());
		measurement.setAttribute("To", toProperty().get());
		measurement.setAttribute("Weather", weatherProperty().get());
		measurement.setAttribute("Notes", notesProperty().get());

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
			nameProperty().set(measurement.getAttribute("Name"));
			// FIXME $TO: Convert date to timestamp
//			measurementDateProperty().set(LocalDate.parse(measurement.getAttribute("Date"),
//				CooTimeUtil.SIMPLE_DATE_FORMATTER));
			
			fromProperty().set(measurement.getAttribute("From"));
			toProperty().set(measurement.getAttribute("To"));
			weatherProperty().set(measurement.getAttribute("Weather"));
			notesProperty().set(measurement.getAttribute("Notes"));

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
	 * @return {@link #totalStation}
	 */
	public ObjectProperty<CooTotalstation> totalStationProperty()
	{
		return totalStation;
	}
}