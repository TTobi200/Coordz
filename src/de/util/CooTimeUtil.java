/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util;

import java.text.*;
import java.time.format.DateTimeFormatter;

public class CooTimeUtil
{
	public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	/** The formatter for times, that should be used in file-names */
	public static final DateTimeFormatter FILE_NAME_TIME_FORMATTER = DateTimeFormatter
			.ofPattern("YYYY_MM_dd_HHmmss");
	/** Pattern for time date/display */
	public static final DateFormat displayTimeFormat = new SimpleDateFormat(
		"dd-MM-yyyy HH:mm:ss");
}
