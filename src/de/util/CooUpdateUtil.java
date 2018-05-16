/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2014 T.Ohm . All Rights Reserved.
 */
package de.util;

import java.io.*;

import de.coordz.CooSystem;
import de.gui.*;
import de.util.log.CooLog;
import javafx.beans.property.*;
import javafx.stage.Window;

public class CooUpdateUtil
{
	public static enum UpdateState
	{
		AVAILABLE("Eine neue Version von Coordz wurde gefunden.\r\nJetzt beenden und Update beginnen?"),
		NEEDED("Das Update von Coordz ist zwingend erforderlich!"),
		UP_TO_DATE("Coordz ist auf dem aktuellsten Stand."),
		NOT_VAIALBLE("Update von Coordz momentan nicht möglich!");

		protected StringProperty msg;

		private UpdateState(String msgKey)
		{
			this.msg = new SimpleStringProperty(msgKey);
		}

		public StringProperty msgProperty()
		{
			// TODO as it is done here, someone could get the property and modify it
			// maybe it is better to use a ReadOnlyStringWrapper instead
			// and return msg.getReadOnly() in this method
			return msg;
		}

		public String getMsg()
		{
			return msgProperty().get();
		}
	}

	public static final String DEF_UPDATE_FOLDER = "\\\\utnas01\\Daten\\Produkt_Aut\\Backup\\UniCAM Tools\\Tools\\Coordz\\Update";
	public static final String DEF_VERSION_FILE = "version.update";
	public static final String DEF_FILE_NAME = "Coordz.exe";
	public static final boolean DEF_UPDATE_CHECK_ON_STARTUP = Boolean.FALSE;

	private static final String IMPORTANT_UPDATE_CHAR = "!";

	public static void update(Window owner, boolean showInfoIfNot)
	{
		UpdateState state = searchForUpdate();

		switch(state)
		{
			case AVAILABLE:
			{
				if(CooDialogs.showConfirmDialog(owner,
					CooMainFrame.TITLE,	state.getMsg()))
				{
					doUpdate(owner);
				}
				return;
			}
			case NEEDED:
			{
				CooDialogs.showWarnDialog(owner,
					CooMainFrame.TITLE, state.getMsg());
				doUpdate(owner);
				return;
			}
			case NOT_VAIALBLE:
			case UP_TO_DATE:
			default:
			{
				if(showInfoIfNot)
				{
					CooDialogs.showConfirmDialog(owner,
						CooMainFrame.TITLE, state.getMsg());
				}
				break;
			}
		}
	}

	protected static UpdateState searchForUpdate()
	{
		// TODO $TO Add this to update settings
		File updateDir = new File(DEF_UPDATE_FOLDER);
		File versionFile = new File(updateDir, DEF_VERSION_FILE);
		File exe = new File(updateDir, DEF_FILE_NAME);

		if(updateDir.exists() && versionFile.exists() && exe.exists())
		{
			try (BufferedReader r = new BufferedReader(new FileReader(
				versionFile)))
			{
				String line = r.readLine();
				if(line != null)
				{
					boolean isNecessary = false;
					if(line.startsWith(IMPORTANT_UPDATE_CHAR))
					{
						isNecessary = true;
						line = line.replace(IMPORTANT_UPDATE_CHAR, "");
					}
					
					Double version = Double.valueOf(line);
					return version > CooSystem.VERSION ? (isNecessary ?
						UpdateState.NEEDED : UpdateState.AVAILABLE) :
							UpdateState.UP_TO_DATE;
				}
			}
			catch(FileNotFoundException e)
			{
				CooLog.error("Version File not found", e);
			}
			catch(IOException e)
			{
				CooLog.error("Error while parsing version file", e);
			}
		}

		return UpdateState.NOT_VAIALBLE;
	}

	protected static void doUpdate(Window owner)
	{
		// TODO $TO Add this to update settings
		doUpdate(owner, DEF_FILE_NAME);
	}

	protected static void doUpdate(Window owner, String fileName)
	{
		try
		{
			owner.hide();

			Runtime r = Runtime.getRuntime();
			File outFold = new File(".");
			File srcFold = new File(DEF_UPDATE_FOLDER);

			StringBuilder roboCmd = new StringBuilder(
				"cmd /c start /wait robocopy ");
			roboCmd.append(wrap(srcFold.getAbsolutePath())).append(" ").
				append(wrap(outFold.getAbsolutePath())).append(" ").
				append(wrap(fileName));

			CooLog.debug("Running robocopy: " + roboCmd);
			Process robocopy = r.exec(roboCmd.toString());

			if(robocopy.waitFor() == 0)
			{
				StringBuilder startCmd = new StringBuilder("cmd /c javaw -jar ");
				startCmd.append(wrap(new File(outFold, fileName).getAbsolutePath()));

				CooLog.debug("Running start jar: " + startCmd);
				r.exec(startCmd.toString());
			}
		}
		catch(IOException e)
		{
			CooLog.error("Error while doing update", e);
		}
		catch(InterruptedException e)
		{
			CooLog.error("Error while performing robocopy", e);
		}
	}

	private static String wrap(String text)
	{
		return "\"" + text + "\"";
	}
}