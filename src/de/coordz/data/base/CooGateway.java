/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.*;

import java.util.*;

import javafx.beans.property.*;
import javafx.collections.*;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooGateway extends CooData
{
	/** {@link StringProperty} for the gateway ip */
	protected StringProperty ip;
	/** {@link StringProperty} for the gateway mac */
	protected StringProperty mac;
	
	/** {@link List} with all gateway {@link CooLaser} */
	protected ObservableList<CooLaser> laser;
	
	public CooGateway()
	{
		ip = new SimpleStringProperty();
		mac = new SimpleStringProperty();
		laser = FXCollections.observableArrayList();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element gateway = addElement(doc, root,
			"Gateway");
		gateway.setAttribute("IP", ip.get());
		gateway.setAttribute("MAC", mac.get());
		
		laser.forEach(l -> l.toXML(doc, gateway));
	}
	
	@Override
	public void fromXML(Element gateway)
	{
		if(Objects.nonNull(gateway))
		{
			ip.set(gateway.getAttribute("IP"));
			mac.set(gateway.getAttribute("MAC"));
			
			// Load all laser
			addToList("Laser", gateway,
				CooLaser.class, this.laser);
		}
	}
	
	public ObservableList<CooLaser> getLaser()
	{
		return laser;
	}
	
	public StringProperty ipProperty()
	{
		return ip;
	}
	
	public StringProperty macProperty()
	{
		return mac;
	}
}