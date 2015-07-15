/*
 * $Header$
 * 
 * $Log$
 * Copyright � 2015 T.Ohm . All Rights Reserved.
 */
package de.util.log;

import java.io.*;
import java.util.Objects;

import de.util.CoordzLoggerUtil;

public class CoordzStreamLogger implements CoordzLogger
{
	/** the stream used for outputting the logs. */
	protected OutputStream out;
	/** whether to add a lineseparator after each log-call */
	protected boolean addLineSeparator;

	/**
	 * @param stream the underlying stream to use
	 */
	public CoordzStreamLogger(OutputStream stream)
	{
		this(stream, false);
	}

	/**
	 * @param stream the underlying stream to use
	 * @param addLineSeparator whether to add a lineseparator after each log-call
	 */
	public CoordzStreamLogger(OutputStream stream, boolean addLineSeparator)
	{
		out = Objects.requireNonNull(stream);
		this.addLineSeparator = addLineSeparator;
	}

	@Override
	public void log(CoordzLogLevel level, String message, Throwable cause)
	{
		try
		{
			message = CoordzLoggerUtil.creMessageString(level, message);
			out.write(message.getBytes());

			if(addLineSeparator && !message.endsWith(System.lineSeparator()))
			{
				out.write(System.lineSeparator().getBytes());
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return the stream used for logging by this logger
	 */
	public OutputStream getOutputStream()
	{
		return out;
	}
}
