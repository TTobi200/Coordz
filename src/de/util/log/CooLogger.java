/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util.log;

public interface CooLogger
{
	/**
	 * Log the given message in the given level.
	 *
	 * @param level The level to log at
	 * @param message the message to log
	 */
	public default void log(CooLogLevel level, String message)
	{
		log(level, message, null);
	}

	/**
	 * Log the given message in the given level.
	 *
	 * @param level The level to log at
	 * @param message the message to log
	 * @param cause a throwable to log, will be ignored if null.
	 */
	public void log(CooLogLevel level, String message, Throwable cause);

	/**
	 * Log the given message in the {@link CooLogLevel#INFO}-level.
	 *
	 * @param message the message to log
	 */
	public default void info(String message)
	{
		log(CooLogLevel.INFO, message);
	}

	/**
	 * Log the given message in the {@link CooLogLevel#INFO}-level.
	 *
	 * @param message the message to log
	 * @param cause a throwable to log, will be ignored if null.
	 */
	public default void info(String message, Throwable cause)
	{
		log(CooLogLevel.INFO, message, cause);
	}

	/**
	 * Log the given message in the {@link CooLogLevel#DEBUG}-level.
	 *
	 * @param message the message to log
	 */
	public default void debug(String message)
	{
		log(CooLogLevel.DEBUG, message);
	}

	/**
	 * Log the given message in the {@link CooLogLevel#DEBUG}-level.
	 *
	 * @param message the message to log
	 * @param cause a throwable to log, will be ignored if null.
	 */
	public default void debug(String message, Throwable cause)
	{
		log(CooLogLevel.DEBUG, message, cause);
	}

	/**
	 * Log the given message in the {@link CooLogLevel#WARN}-level.
	 *
	 * @param message the message to log
	 */
	public default void warn(String message)
	{
		log(CooLogLevel.WARN, message);
	}

	/**
	 * Log the given message in the {@link CooLogLevel#WARN}-level.
	 *
	 * @param message the message to log
	 * @param cause a throwable to log, will be ignored if null.
	 */
	public default void warn(String message, Throwable cause)
	{
		log(CooLogLevel.WARN, message, cause);
	}

	/**
	 * Log the given message in the {@link CooLogLevel#ERROR}-level.
	 *
	 * @param message the message to log
	 */
	public default void error(String message)
	{
		log(CooLogLevel.ERROR, message);
	}

	/**
	 * Log the given message in the {@link CooLogLevel#ERROR}-level.
	 *
	 * @param message the message to log
	 * @param cause a throwable to log, will be ignored if null.
	 */
	public default void error(String message, Throwable cause)
	{
		log(CooLogLevel.ERROR, message, cause);
	}

	/**
	 * Log the given message in the {@link CooLogLevel#FATAL}-level.
	 *
	 * @param message the message to log
	 */
	public default void fatal(String message)
	{
		log(CooLogLevel.FATAL, message);
	}

	/**
	 * Log the given message in the {@link CooLogLevel#FATAL}-level.
	 *
	 * @param message the message to log
	 * @param cause a throwable to log, will be ignored if null.
	 */
	public default void fatal(String message, Throwable cause)
	{
		log(CooLogLevel.FATAL, message, cause);
	}
}
