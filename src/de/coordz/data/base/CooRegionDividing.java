/*
 * $Header$
 * 
 * $Log$
 * Copyright � 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.*;

import java.util.Objects;

import org.w3c.dom.*;

import de.coordz.data.CooData;
import javafx.collections.*;

public class CooRegionDividing extends CooData
{
	/** {@link ObservableList} with all region dividing {@link CooLaser} */
	protected ObservableList<CooLaser> laser;

	public CooRegionDividing()
	{
		laser = FXCollections.observableArrayList();
	}

	@Override
	public void toXML(Document doc, Element root)
	{
		Element regionDividing = addElement(doc, root,
			"RegionDividing");
		laser.forEach(l -> l.toXML(doc, regionDividing));
	}

	@Override
	public void fromXML(Element regionDividing)
	{
		if(Objects.nonNull(regionDividing))
		{
			// Load all lasers
			addToList("Laser", regionDividing,
				CooLaser.class, this.laser);
		}
	}
	
	public ObservableList<CooLaser> fromLaser(ObservableList<CooPalet> palets, 
		ObservableList<CooLaser> laser)
	{
		// Store the laser from gateway
		this.laser = laser;
		
		if(!palets.isEmpty() && !laser.isEmpty())
		{
			// Get the first palet length and number of laser
			int length = palets.get(0).lengthProperty().get();
			int numLaser = laser.size();
			
			// Define the region per laser 
			// and the start position
			int region = length / numLaser;
			int position = 0;
			
			for(int i = 0; i < laser.size(); i++)
			{
				CooLaser l = laser.get(i);
				l.fromProperty().set(position);
				l.toProperty().set(i + 1 < laser.size() ?
					position + region : length);
				
				// Move to next position
				position += region;
				// Overlap each laser region
				position -= i + 1 < laser.size() ? 100 : 0;
			}
		}
		
		return laser;
	}
	
	/**
	 * Method to access {@link CooLaser}
	 * @return {@link #laser}
	 */
	public ObservableList<CooLaser> getLaser()
	{
		return laser;
	}
}