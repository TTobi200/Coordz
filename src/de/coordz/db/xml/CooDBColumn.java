/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db.xml;

import java.util.Objects;

import org.w3c.dom.*;

import de.coordz.db.CooDBValTypes;
import javafx.beans.property.*;

public class CooDBColumn implements CooDBXML
{
	protected StringProperty name;
	protected ObjectProperty<CooDBValTypes> type;
	protected IntegerProperty length;
	protected BooleanProperty nullable;
	protected StringProperty description;
	
	public CooDBColumn()
	{
		name = new SimpleStringProperty();
		type = new SimpleObjectProperty<>();
		length = new SimpleIntegerProperty();
		nullable = new SimpleBooleanProperty();
		description = new SimpleStringProperty();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		
	}

	@Override
	public void fromXML(Element column)
	{
		if(Objects.nonNull(column))
		{
			name.set(column.getAttribute("NAME"));
			type.set(CooDBValTypes.valueOf(
				column.getAttribute("TYPE")));
			length.set(Integer.valueOf(
				column.getAttribute("LENGTH")));
			nullable.set(Boolean.valueOf(
				column.getAttribute("NULLABLE")));
			description.set(column.getAttribute("DESC"));
		}
	}
	
	public StringProperty nameProperty()
	{
		return name;
	}
	
	public ObjectProperty<CooDBValTypes> typeProperty()
	{
		return type;
	}
	
	public IntegerProperty lengthProperty()
	{
		return length;
	}
	
	public BooleanProperty nullableProperty()
	{
		return nullable;
	}
	
	public StringProperty descriptionProperty()
	{
		return description;
	}
}