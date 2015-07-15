/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import javafx.collections.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.*;

public class CoordzFileUtil
{
	public static final String IN_JAR_SEPERATOR = "/";

	public static final String INCLUDE_FOLDER = "include";
	public static final String FXML_FOLDER = INCLUDE_FOLDER + IN_JAR_SEPERATOR
												+ "fxml";
	public static final String ICON_FOLDER = INCLUDE_FOLDER + IN_JAR_SEPERATOR
												+ "icons";
	public static final String ICON_OUT_FOLDER = "." + File.separator +
													"icons";

	public static final String FXML_COMP = FXML_FOLDER + IN_JAR_SEPERATOR
											+ "comp";

	public static boolean fileCanBeLoad(String filePath)
	{
		return fileCanBeLoad(new File(filePath));
	}

	public static boolean fileCanBeLoad(File file)
	{
		return fileExists(file) && file.canRead();
	}

	public static boolean fileCanBeSaved(String filePath)
	{
		return fileCanBeSaved(new File(filePath));
	}

	public static boolean fileCanBeSaved(File file)
	{
		return !fileExists(file) && file.canWrite();
	}

	public static boolean fileExists(File file)
	{
		if(file != null)
		{
			if(file.exists() && file.isFile())
			{
				return true;
			}
		}
		return false;
	}

	public static String getResourceFile(String path)
	{
		return CoordzFileUtil.class.getClassLoader()
			.getResource(path)
			.getFile();
	}

	public static URL getResourceURL(String path)
	{
		return CoordzFileUtil.class.getClassLoader().getResource(path);
	}

	public static InputStream getResourceStream(String icon)
	{
		return CoordzFileUtil.class.getClassLoader()
			.getResourceAsStream(icon);
	}

	public static File creOpenDialog(Window parent, String initPath)
	{
		FileChooser f = new FileChooser();
		f.setInitialDirectory(initPath != null ? new File(initPath) : null);
		return f.showOpenDialog(parent);
	}

	public static File creSaveDialog(Window parent, String initPath)
	{
		return creSaveDialog(parent, initPath, "");
	}

	public static File creSaveToFoldDialog(Window parent)
	{
		return creSaveToFoldDialog(parent, null);
	}

	public static File creSaveToFoldDialog(Window parent,
					String initFileName)
	{
		DirectoryChooser dir = new DirectoryChooser();
		return dir.showDialog(parent);
	}

	public static File creSaveDialog(Window parent, String initPath,
					String initFileName)
	{
		FileChooser f = new FileChooser();

		f.setInitialDirectory(initPath != null ? new File(initPath) : null);
		f.setInitialFileName(initFileName != null ? initFileName : "");
		return f.showSaveDialog(parent);
	}

	public static Image getResourceIcon(String icon)
	{
		return new Image(getResourceStream(
			ICON_FOLDER + IN_JAR_SEPERATOR + icon));
	}

	public static Image getResourceOutIcon(String icon)
		throws MalformedURLException
	{
		File iconFile = new File(ICON_OUT_FOLDER + File.separator + icon);
		if(!iconFile.exists())
		{
			return getResourceIcon("user_m.png");
		}
		
		return new Image(iconFile.toURI().toURL().toString());
	}

	public static ObservableList<String> getResourceOutIcons()
	{
		// TODO do this better!
		ObservableList<String> names = FXCollections.observableArrayList();

		Arrays.asList(new File(ICON_OUT_FOLDER).listFiles()).forEach(f ->
		{
			names.add(f.getName());
		});

		return names;
	}

	public static Parent loadFXML(Object ctrl, String fxml) throws IOException
	{
		return loadFXML(ctrl, fxml, null);
	}

	public static Parent loadFXML(Object ctrl, String fxml, Node root)
		throws IOException
	{
		FXMLLoader loader = new FXMLLoader(
			CoordzFileUtil.getResourceURL(fxml));

		if(root != null)
		{
			loader.setRoot(root);
		}

		loader.setController(ctrl);
		return loader.load();
	}
}