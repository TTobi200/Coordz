/*
 * $Header$
 * 
 * $Log$
 * Copyright � 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.pnl;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import de.coordz.data.CooProject;
import de.coordz.data.base.CooStation;
import de.gui.CooDataChanged;
import de.gui.comp.CooTableView;
import de.util.CooFileUtil;
import de.util.log.CooLog;

public class CooMeasurementsPnl extends BorderPane implements CooDataChanged
{
	@FXML
	protected CooTableView<CooStation> tblStations;
	
	public CooMeasurementsPnl()
	{
		try
		{
			CooFileUtil.loadFXML(this, CooFileUtil.FXML_COMP +
				CooFileUtil.IN_JAR_SEPERATOR + "CooMeasurementsPnl.fxml", this);
		}
		catch(IOException e)
		{
			CooLog.debug("Could not load FXML", e);
		}
		
		tblStations.setClazz(CooStation.class);
	}
	
	@Override
	public void projectChanged(CooProject project)
	{
		tblStations.setItems(project.getStations());
	}
}
