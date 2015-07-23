/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util.log;

import java.util.Objects;

import de.util.CooLoggerUtil;

public class CooLog
{
	/** A logger using {@link System#out} and {@link System#err} for logging */
	public static final CooLogger systemLogger = (level, message, cause) -> {
		switch(level)
		{
			case INFO:
			case DEBUG:
			{
				System.out.println(CooLoggerUtil.creMessageString(level, message));
				if(Objects.nonNull(cause))
				{
					cause.printStackTrace(System.out);
				}
				break;
			}
			case ERROR:
			case FATAL:
			case WARN:
			{
				System.err.println(CooLoggerUtil.creMessageString(level, message));
				if(Objects.nonNull(cause))
				{
					cause.printStackTrace(System.err);
				}
				break;
			}
		}
	};

	/** The default logger for this application */
	private static CooLogger defaultLogger = systemLogger;

	/**
	 * @return The current default-logger for this application
	 */
	public static CooLogger getDefaultLogger()
	{
		return defaultLogger;
	}

	/**
	 * @param defaultLogger The new default-logger for this application
	 */
	public static void setDefaultLogger(CooLogger defaultLogger)
	{
		CooLog.defaultLogger = defaultLogger;
	}

	public static void log(CooLogLevel level, String message)
	{
		log(level, message, null);
	}
	public static void log(CooLogLevel level, String message, Throwable cause)
	{
		getDefaultLogger().log(level, message, cause);
	}

	public static void info(String message)
	{
		log(CooLogLevel.INFO, message);
	}
	public static void info(String message, Throwable cause)
	{
		log(CooLogLevel.INFO, message, cause);
	}

	public static void debug(String message)
	{
		log(CooLogLevel.DEBUG, message);
	}
	public static void debug(String message, Throwable cause)
	{
		log(CooLogLevel.DEBUG, message, cause);
	}

	public static void warn(String message)
	{
		log(CooLogLevel.WARN, message);
	}
	public static void warn(String message, Throwable cause)
	{
		log(CooLogLevel.WARN, message, cause);
	}

	public static void error(String message)
	{
		log(CooLogLevel.ERROR, message);
	}
	public static void error(String message, Throwable cause)
	{
		log(CooLogLevel.ERROR, message, cause);
	}

	public static void fatal(String message)
	{
		log(CooLogLevel.FATAL, message);
	}
	public static void fatal(String message, Throwable cause)
	{
		log(CooLogLevel.FATAL, message, cause);
	}
}
