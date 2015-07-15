/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util.log;

import java.util.*;

public class CoordzLoggerCollection implements CoordzLogger
{
	/** the loggers to log to */
	protected Collection<CoordzLogger> logger;

	public CoordzLoggerCollection(CoordzLogger... logger)
	{
		this.logger = new ArrayList<>(Arrays.asList(logger));
	}

	@Override
	public void log(CoordzLogLevel level, String message, Throwable cause)
	{
		logger.forEach(logger -> logger.log(level, message, cause));
	}

	/**
	 * @param logger additional loggers for this collection
	 */
	public void addLogger(CoordzLogger... logger)
	{
		this.logger.addAll(Arrays.asList(logger));
	}

	/**
	 * @param logger loggers to be removed from this collection
	 */
	public void removeLogger(CoordzLogger... logger)
	{
		this.logger.removeAll(Arrays.asList(logger));
	}

	/**
	 * Remove all loggers from this collection
	 */
	public void removeAllLogger()
	{
		logger.clear();
	}
}
