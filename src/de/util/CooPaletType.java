/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util;

import javafx.beans.property.*;

public enum CooPaletType
{
	U("Universal"), R("Randschalung");
	
	protected StringProperty name;

	private CooPaletType(String name)
	{
		this.name = new SimpleStringProperty(name);
	}
	
	public StringProperty nameProperty()
	{
		return name;
	}
	
	public static CooPaletType parse(String type)
	{
		CooPaletType result = U;
		switch(type)
		{
			case "R":
			case "r":
			case "Randschalung":
				result = R;
				break;
				
			default:
			case "U":
			case "u":
			case "Universal":
				result = U;
				break;
		}
		return result;
	}
}
