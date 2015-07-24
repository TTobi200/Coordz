/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.addElement;

import java.util.Objects;

import javafx.beans.property.*;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooLaser extends CooData
{
	/** {@link StringProperty} for the laser name */
	protected StringProperty name;
	/** {@link StringProperty} for the laser mac */
	protected StringProperty mac;
	/** {@link StringProperty} for the laser serial number */
	protected StringProperty serialNr;

	/** {@link IntegerProperty} for the laser x value */
	protected IntegerProperty x;
	/** {@link IntegerProperty} for the laser y value */
	protected IntegerProperty y;
	/** {@link IntegerProperty} for the laser z value */
	protected IntegerProperty z;
	/** {@link DoubleProperty} for the laser total deviation */
	protected DoubleProperty totalDeviation;

	public CooLaser()
	{
		name = new SimpleStringProperty();
		mac = new SimpleStringProperty();
		serialNr = new SimpleStringProperty();
		x = new SimpleIntegerProperty();
		y = new SimpleIntegerProperty();
		z = new SimpleIntegerProperty();
		totalDeviation = new SimpleDoubleProperty();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element laser = addElement(doc, root, "Laser");
		laser.setAttribute("Name", name.get());
		laser.setAttribute("MAC", mac.get());
		laser.setAttribute("SerialNr", serialNr.get());
		laser.setAttribute("X", String.valueOf(x.get()));
		laser.setAttribute("Y", String.valueOf(y.get()));
		laser.setAttribute("Z", String.valueOf(z.get()));
		laser.setAttribute("TotalDeviation", 
			String.valueOf(totalDeviation.get()));
	}
	
	@Override
	public void fromXML(Element laser)
	{
		if(Objects.nonNull(laser))
		{
			name.set(laser.getAttribute("Name"));
			mac.set(laser.getAttribute("MAC"));
			serialNr.set(laser.getAttribute("SerialNr"));
			x.set(Integer.valueOf(laser.getAttribute("X")));
			y.set(Integer.valueOf(laser.getAttribute("Y")));
			z.set(Integer.valueOf(laser.getAttribute("Z")));
			totalDeviation.set(Double.valueOf(laser.getAttribute(
				"TotalDeviation")));
		}
	}
	
	public StringProperty nameProperty()
	{
		return name;
	}
	
	public StringProperty macProperty()
	{
		return mac;
	}
	
	public StringProperty serialNrProperty()
	{
		return serialNr;
	}
	
	public IntegerProperty xProperty()
	{
		return x;
	}
	
	public IntegerProperty yProperty()
	{
		return y;
	}
	
	public IntegerProperty zProperty()
	{
		return z;
	}
	
	public DoubleProperty totalDeviationProperty()
	{
		return totalDeviation;
	}
}