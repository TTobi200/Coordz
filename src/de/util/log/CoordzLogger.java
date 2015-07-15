/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util.log;

public interface CoordzLogger
{
	/**
	 * Log the given message in the given level.
	 *
	 * @param level The level to log at
	 * @param message the message to log
	 */
	public default void log(CoordzLogLevel level, String message)
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
	public void log(CoordzLogLevel level, String message, Throwable cause);

	/**
	 * Log the given message in the {@link CoordzLogLevel#INFO}-level.
	 *
	 * @param message the message to log
	 */
	public default void info(String message)
	{
		log(CoordzLogLevel.INFO, message);
	}

	/**
	 * Log the given message in the {@link CoordzLogLevel#INFO}-level.
	 *
	 * @param message the message to log
	 * @param cause a throwable to log, will be ignored if null.
	 */
	public default void info(String message, Throwable cause)
	{
		log(CoordzLogLevel.INFO, message, cause);
	}

	/**
	 * Log the given message in the {@link CoordzLogLevel#DEBUG}-level.
	 *
	 * @param message the message to log
	 */
	public default void debug(String message)
	{
		log(CoordzLogLevel.DEBUG, message);
	}

	/**
	 * Log the given message in the {@link CoordzLogLevel#DEBUG}-level.
	 *
	 * @param message the message to log
	 * @param cause a throwable to log, will be ignored if null.
	 */
	public default void debug(String message, Throwable cause)
	{
		log(CoordzLogLevel.DEBUG, message, cause);
	}

	/**
	 * Log the given message in the {@link CoordzLogLevel#WARN}-level.
	 *
	 * @param message the message to log
	 */
	public default void warn(String message)
	{
		log(CoordzLogLevel.WARN, message);
	}

	/**
	 * Log the given message in the {@link CoordzLogLevel#WARN}-level.
	 *
	 * @param message the message to log
	 * @param cause a throwable to log, will be ignored if null.
	 */
	public default void warn(String message, Throwable cause)
	{
		log(CoordzLogLevel.WARN, message, cause);
	}

	/**
	 * Log the given message in the {@link CoordzLogLevel#ERROR}-level.
	 *
	 * @param message the message to log
	 */
	public default void error(String message)
	{
		log(CoordzLogLevel.ERROR, message);
	}

	/**
	 * Log the given message in the {@link CoordzLogLevel#ERROR}-level.
	 *
	 * @param message the message to log
	 * @param cause a throwable to log, will be ignored if null.
	 */
	public default void error(String message, Throwable cause)
	{
		log(CoordzLogLevel.ERROR, message, cause);
	}

	/**
	 * Log the given message in the {@link CoordzLogLevel#FATAL}-level.
	 *
	 * @param message the message to log
	 */
	public default void fatal(String message)
	{
		log(CoordzLogLevel.FATAL, message);
	}

	/**
	 * Log the given message in the {@link CoordzLogLevel#FATAL}-level.
	 *
	 * @param message the message to log
	 * @param cause a throwable to log, will be ignored if null.
	 */
	public default void fatal(String message, Throwable cause)
	{
		log(CoordzLogLevel.FATAL, message, cause);
	}
}
