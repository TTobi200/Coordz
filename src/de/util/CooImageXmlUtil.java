/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2018 T.Ohm . All Rights Reserved.
 */
package de.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import javax.imageio.ImageIO;

import org.w3c.dom.DOMException;

import de.util.log.CooLog;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class CooImageXmlUtil
{
	private final static ExecutorService exec = Executors.newCachedThreadPool(r ->
	{
		Thread thread = new Thread(r);
		thread.setDaemon(true);
		return thread;
	});

	public static String encodeImage(Image image)
	{
		String encodedImage = "";
		try
		{
			if(Objects.nonNull(image))
			{
				BufferedImage buffImage = SwingFXUtils.fromFXImage(image, null);
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				ImageIO.write(buffImage, "png", bytes);
				encodedImage = Base64.getEncoder().encodeToString(
					bytes.toByteArray());
			}
		}
		catch(DOMException | IOException  e)
		{
			CooLog.error("Could not encode image", e);
		}
		return encodedImage;
	}

	public static Image readImage(String image)
	{
		try
		{
			PipedInputStream pipedInput = new PipedInputStream();

			FutureTask<Image> imageTask = new FutureTask<>(
				new Callable<Image>()
				{
					@Override
					public Image call() throws Exception
					{
						try (InputStream imageStream = Base64.getDecoder()
							.wrap(
								pipedInput))
						{
							BufferedImage buffImage = ImageIO.read(imageStream);
							return SwingFXUtils.toFXImage(buffImage, null);
						}
					}
				});

			exec.submit(imageTask);

			try (PrintWriter output = new PrintWriter(new PipedOutputStream(
				pipedInput)))
			{
				output.write(image);
				output.close();
			}

			if(Objects.nonNull(image) && !image.isEmpty())
			{
				return imageTask.get();
			}
		}
		catch(IOException | InterruptedException | ExecutionException e)
		{
			CooLog.error("Could not read encoded image", e);
		}

		return null;
	}
}