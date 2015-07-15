/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util.log;

import java.util.Objects;

import de.util.CoordzLoggerUtil;

public class CoordzLog
{
	/** A logger using {@link System#out} and {@link System#err} for logging */
	public static final CoordzLogger systemLogger = (level, message, cause) -> {
		switch(level)
		{
			case INFO:
			case DEBUG:
			{
				System.out.println(CoordzLoggerUtil.creMessageString(level, message));
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
				System.err.println(CoordzLoggerUtil.creMessageString(level, message));
				if(Objects.nonNull(cause))
				{
					cause.printStackTrace(System.err);
				}
				break;
			}
		}
	};

	/** The default logger for this application */
	private static CoordzLogger defaultLogger = systemLogger;

	/**
	 * @return The current default-logger for this application
	 */
	public static CoordzLogger getDefaultLogger()
	{
		return defaultLogger;
	}

	/**
	 * @param defaultLogger The new default-logger for this application
	 */
	public static void setDefaultLogger(CoordzLogger defaultLogger)
	{
		CoordzLog.defaultLogger = defaultLogger;
	}

	public static void log(CoordzLogLevel level, String message)
	{
		log(level, message, null);
	}
	public static void log(CoordzLogLevel level, String message, Throwable cause)
	{
		getDefaultLogger().log(level, message, cause);
	}

	public static void info(String message)
	{
		log(CoordzLogLevel.INFO, message);
	}
	public static void info(String message, Throwable cause)
	{
		log(CoordzLogLevel.INFO, message, cause);
	}

	public static void debug(String message)
	{
		log(CoordzLogLevel.DEBUG, message);
	}
	public static void debug(String message, Throwable cause)
	{
		log(CoordzLogLevel.DEBUG, message, cause);
	}

	public static void warn(String message)
	{
		log(CoordzLogLevel.WARN, message);
	}
	public static void warn(String message, Throwable cause)
	{
		log(CoordzLogLevel.WARN, message, cause);
	}

	public static void error(String message)
	{
		log(CoordzLogLevel.ERROR, message);
	}
	public static void error(String message, Throwable cause)
	{
		log(CoordzLogLevel.ERROR, message, cause);
	}

	public static void fatal(String message)
	{
		log(CoordzLogLevel.FATAL, message);
	}
	public static void fatal(String message, Throwable cause)
	{
		log(CoordzLogLevel.FATAL, message, cause);
	}
}
