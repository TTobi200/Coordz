/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util.log;

import java.util.*;

public class CooLoggerCollection implements CooLogger
{
	/** the loggers to log to */
	protected Collection<CooLogger> logger;

	public CooLoggerCollection(CooLogger... logger)
	{
		this.logger = new ArrayList<>(Arrays.asList(logger));
	}

	@Override
	public void log(CooLogLevel level, String message, Throwable cause)
	{
		logger.forEach(logger -> logger.log(level, message, cause));
	}

	/**
	 * @param logger additional loggers for this collection
	 */
	public void addLogger(CooLogger... logger)
	{
		this.logger.addAll(Arrays.asList(logger));
	}

	/**
	 * @param logger loggers to be removed from this collection
	 */
	public void removeLogger(CooLogger... logger)
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
