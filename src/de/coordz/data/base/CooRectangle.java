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

public class CooRectangle extends CooData
{
	/** {@link StringProperty} for the rectangle name */
	protected StringProperty name;
	/** {@link IntegerProperty} for the rectangle width */
	protected IntegerProperty width;
	/** {@link IntegerProperty} for the rectangle height */
	protected IntegerProperty height;
	/** {@link IntegerProperty} for the rectangle length */
	protected IntegerProperty length;
	/** {@link IntegerProperty} for the rectangle x coordinate */
	protected IntegerProperty x;
	/** {@link IntegerProperty} for the rectangle y coordinate */
	protected IntegerProperty y;
	/** {@link IntegerProperty} for the rectangle z coordinate */
	protected IntegerProperty z;
	/** {@link IntegerProperty} for the rectangle diagonal 1 */
	protected IntegerProperty d1;
	/** {@link IntegerProperty} for the rectangle diagonal 2 */
	protected IntegerProperty d2;
	
	public CooRectangle()
	{
		name = new SimpleStringProperty();
		width = new SimpleIntegerProperty();
		height = new SimpleIntegerProperty();
		length = new SimpleIntegerProperty();
		x = new SimpleIntegerProperty();
		y = new SimpleIntegerProperty();
		z = new SimpleIntegerProperty();
		d1 = new SimpleIntegerProperty();
		d2 = new SimpleIntegerProperty();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element rectangle = addElement(doc, root, "Rectangle");
		rectangle.setAttribute("Name", name.get());
		rectangle.setAttribute("Width", String.valueOf(width.get()));
		rectangle.setAttribute("Height", String.valueOf(height.get()));
		rectangle.setAttribute("Length", String.valueOf(length.get()));
		rectangle.setAttribute("X", String.valueOf(x.get()));
		rectangle.setAttribute("Y", String.valueOf(y.get()));
		rectangle.setAttribute("Z", String.valueOf(z.get()));
		rectangle.setAttribute("D1", String.valueOf(d1.get()));
		rectangle.setAttribute("D2", String.valueOf(d2.get()));
	}
	
	@Override
	public void fromXML(Element rectangle)
	{
		if(Objects.nonNull(rectangle))
		{
			name.set(rectangle.getAttribute("Name"));
			width.set(Integer.valueOf(rectangle.getAttribute("Width")));
			height.set(Integer.valueOf(rectangle.getAttribute("Height")));
			length.set(Integer.valueOf(rectangle.getAttribute("Length")));
			x.set(Integer.valueOf(rectangle.getAttribute("X")));
			y.set(Integer.valueOf(rectangle.getAttribute("Y")));
			z.set(Integer.valueOf(rectangle.getAttribute("Z")));
			d1.set(Integer.valueOf(rectangle.getAttribute("D1")));
			d2.set(Integer.valueOf(rectangle.getAttribute("D2")));
		}
	}
	
	public StringProperty nameProperty()
	{
		return name;
	}
	
	public IntegerProperty widthProperty()
	{
		return width;
	}
	
	public IntegerProperty heightProperty()
	{
		return height;
	}
	
	public IntegerProperty lengthProperty()
	{
		return length;
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
	
	public IntegerProperty d1Property()
	{
		return d1;
	}
	
	public IntegerProperty d2Property()
	{
		return d2;
	}
}