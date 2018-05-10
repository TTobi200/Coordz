/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db;

public enum CooDBValTypes
{
	VARCHAR("String", "StringProperty"), 
	INTEGER("int", "IntegerProperty"),
	DOUBLE("double", "DoubleProperty"),
	BOOLEAN("boolean", "BooleanProperty"), 
	TIMESTAMP("Timestamp", "ObjectProperty<Timestamp>"),
	BLOB("Blob", "ObjectProperty<Blob>");
	
	private String primitive;
	private String property;

	private CooDBValTypes(String primitive, String property)
	{
		this.primitive = primitive;
		this.property = property;
	}
	
	public String getPrimitive()
	{
		return primitive;
	}
	
	public String getProperty()
	{
		return property;
	}
}