/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.addElement;

import java.util.Objects;

import org.w3c.dom.*;

import de.coordz.data.CooData;
import javafx.beans.property.*;

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
	
	/** {@link IntegerProperty} for the laser region dividing from value */
	protected IntegerProperty from;
	/** {@link IntegerProperty} for the laser region dividing to value */
	protected IntegerProperty to;

	public CooLaser()
	{
		name = new SimpleStringProperty();
		mac = new SimpleStringProperty();
		serialNr = new SimpleStringProperty();
		x = new SimpleIntegerProperty();
		y = new SimpleIntegerProperty();
		z = new SimpleIntegerProperty();
		totalDeviation = new SimpleDoubleProperty();
		from = new SimpleIntegerProperty();
		to = new SimpleIntegerProperty();
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
		laser.setAttribute("From", String.valueOf(from.get()));
		laser.setAttribute("To", String.valueOf(to.get()));
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
			from.set(Integer.valueOf(laser.getAttribute("From")));
			to.set(Integer.valueOf(laser.getAttribute("To")));
		}
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
	 * @return {@link #mac}
	 */
	public StringProperty macProperty()
	{
		return mac;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #serialNr}
	 */
	public StringProperty serialNrProperty()
	{
		return serialNr;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #x}
	 */
	public IntegerProperty xProperty()
	{
		return x;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #y}
	 */
	public IntegerProperty yProperty()
	{
		return y;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #z}
	 */
	public IntegerProperty zProperty()
	{
		return z;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #totalDeviation}
	 */
	public DoubleProperty totalDeviationProperty()
	{
		return totalDeviation;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #from}
	 */
	public IntegerProperty fromProperty()
	{
		return from;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #to}
	 */
	public IntegerProperty toProperty()
	{
		return to;
	}
}