/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2018 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

public class CooTimestampStringConverter extends StringConverter<Timestamp>
{
	@Override
	public String toString(Timestamp object)
	{
		return object.toLocalDateTime().toString();
	}

	@Override
	public Timestamp fromString(String string)
	{
		return Timestamp.valueOf(LocalDateTime.parse(string + " 00:00",
			DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
	}
}