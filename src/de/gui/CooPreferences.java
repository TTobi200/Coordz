/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2018 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.util.prefs.*;

import de.gui.sett.CooSettingsDialog;
import de.util.*;
import de.util.log.CooLog;
import javafx.stage.Stage;

public class CooPreferences extends CooSettingsDialog
{
	public static final String GROUP_UPDATE = "Update";
	public static final String AUTO_UPDATE = "Automatisch überprüfen";
	
	public CooPreferences(Stage parent)
	{
		super(parent, CooMainFrame.TITLE, 
			CooFileUtil.getResourceIcon("Logo.png"));
		init(CooMainFrame.TITLE);
	}

	private void init(String name)
	{
		// Add the update settings
		addSetting(GROUP_UPDATE, AUTO_UPDATE,
			SettingType.BOOLEAN, Boolean.TRUE.toString());

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
	
	public Boolean getAutoUpdate()
	{
		return Boolean.valueOf(groups.get(GROUP_UPDATE).getProps()
			.get(AUTO_UPDATE).getValue().toString());
	}
}