/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2014 T.Ohm . All Rights Reserved.
 */
package de.util.pref;

import java.util.Objects;
import java.util.prefs.Preferences;

import javafx.beans.property.Property;
import de.gui.CooMainFrame;
import de.util.log.CooLog;

public class CooSystemPreferences extends CooPreferences
{
	public static final String NETTEST_KONFIGURATOR_NODE = CooMainFrame.TITLE;
	
	public static final String ROOT_GENERAL = "General";
	public static final String GENERAL_LAST_OPENED = "LastOpened";
	
	
	private static CooSystemPreferences systemProperties = new CooSystemPreferences();
	
	public static CooSystemPreferences getSystemPreferences()
	{
		return systemProperties;
	}

	public static void setSystemProperties(
					CooSystemPreferences systemProperties)
	{
		if(Objects.nonNull(systemProperties))
		{
			CooSystemPreferences.systemProperties = systemProperties;
		}
	}

	protected CooSystemPreferences()
	{
		load();
	}

	@Override
	public void load()
	{
		try
		{
			Preferences root = Preferences.userRoot().node(
				NETTEST_KONFIGURATOR_NODE);

			loadGeneralSettings(root);
		}
		catch(Exception e)
		{
			CooLog.error("Could not load preferences", e);
		}
	}

	protected void loadGeneralSettings(Preferences root)
	{
		Preferences general = root.node(ROOT_GENERAL);

		if(Objects.nonNull(general))
		{
			storeProp(general, GENERAL_LAST_OPENED);
		}
	}

	@Override
	public void save()
	{
		try
		{
			Preferences root = Preferences.userRoot().node(
				NETTEST_KONFIGURATOR_NODE);

			saveGeneralSettings(root);
			root.flush();
		}
		catch(Exception e)
		{
			CooLog.error("Could not save preferences", e);
		}
	}

	protected void saveGeneralSettings(Preferences root)
	{
		Preferences general = root.node(ROOT_GENERAL);

		general.put(GENERAL_LAST_OPENED, 
			getValue(GENERAL_LAST_OPENED));
	}

	protected void storeProp(Preferences root, String attribute)
	{
		storeProp(root, attribute, "");
	}

	protected void storeProp(Preferences root, String attribute, String def)
	{
		String value = root.get(attribute, def);

		if(value != null)
		{
			putString(attribute, value);
		}
	}

	protected void storePropBoolean(Preferences parent, String attribute)
	{
		storePropBoolean(parent, attribute, false);
	}

	protected void storePropBoolean(Preferences parent, String attribute,
					boolean def)
	{
		String value = parent.get(attribute, String.valueOf(def));

		if(value != null)
		{
			putBoolean(attribute, Boolean.valueOf(value));
		}
	}

	protected void storePropInteger(Preferences parent, String attribute)
	{
		String value = parent.get(attribute, null);

		if(value != null)
		{
			putInt(attribute, Integer.valueOf(value));
		}
	}
	
	protected void storePropInteger(Preferences parent, String attribute, 
					Integer def)
	{
		String value = parent.get(attribute, String.valueOf(def));

		if(value != null)
		{
			putInt(attribute, Integer.valueOf(value));
		}
	}

	protected String getValue(String prop)
	{
		Property<?> p = get(prop);
		if(p != null && p.getValue() != null)
		{
			return String.valueOf(p.getValue());
		}

		return "";
	}

	public static void saveSystemProperties()
	{
		getSystemPreferences().save();
	}
}
