/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2018 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.io.File;
import java.util.prefs.*;

import de.gui.sett.CooSettingsDialog;
import de.util.*;
import de.util.log.CooLog;
import javafx.stage.Stage;

public class UTPreferences extends CooSettingsDialog
{
	public static final String GROUP_GENERAL = "Allgemein";
	public static final String GENERAL_DATABASE_FOLDER = "Datenbank Ordner";
	
	public UTPreferences(Stage parent)
	{
		super(parent, CooMainFrame.TITLE, 
			CooFileUtil.getResourceIcon("Logo.png"));
		init(CooMainFrame.TITLE);
	}

	private void init(String name)
	{
		// Add the general settings
		addSetting(GROUP_GENERAL, GENERAL_DATABASE_FOLDER, SettingType.TEXT, 
			".\\CoordzXML");

		// Add the update settings
		addSetting("Update", "Automatisch überprüfen",
			SettingType.BOOLEAN);

		// User decides what to do on cancel
		setOnCancel(l ->
		{
			if(CooDialogs.showConfirmDialog(this, "Änderungen verwerfen",
				"Möchten Sie wirklich abbrechen?"))
			{
				close();
			}
		});

		// User decides what to do after save
		setOnSave(l ->
		{
			close();
		});
		
		try
		{
			load(Preferences.userRoot().node(name));
		}
		catch(BackingStoreException e)
		{
			CooLog.error("Error while loading preferences", e);
		}
	}
	
	public void showDialog(Stage parent)
	{
		CooGuiUtil.grayOutParent(parent,
			showingProperty());
		CooGuiUtil.relativeToOwner(this, parent);
		showAndWait();	
	}
	
	public File getDBFolder()
	{
		return new File(groups.get(GROUP_GENERAL).getProps()
			.get(GENERAL_DATABASE_FOLDER).getValue().toString());
	}
}