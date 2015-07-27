/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import de.coordz.data.CooProject;
import de.gui.CooDataChanged;
import de.util.CooFileUtil;
import de.util.log.CooLog;

public class CooProjectDataPnl extends BorderPane implements CooDataChanged
{

	// vvvv See the comment below vvvv
	// Only as a workaround
	protected CooProject lastProject;
	
	@FXML
	protected TextField txtPrjName;
	@FXML
	protected TextField txtPrjDate;

	@FXML
	protected TextField txtSoftName;
	@FXML
	protected TextField txtSoftVersion;
	
	public CooProjectDataPnl()
	{
		try
		{
			CooFileUtil.loadFXML(this, CooFileUtil.FXML_COMP +
										CooFileUtil.IN_JAR_SEPERATOR
										+ "CooProjectDataPnl.fxml", this);
		}
		catch(IOException e)
		{
			CooLog.debug("Could not load FXML", e);
		}

	}

	@Override
	public void projectChanged(CooProject project)
	{
		// TODO Try to find a solution
		// Bidirectional bindings have to be unbind bidirectional
		// Only .unbind of all customer propertys not working
		if(Objects.nonNull(lastProject))
		{
			txtPrjName.textProperty().unbindBidirectional(
				lastProject.nameProperty());
			txtPrjDate.textProperty().unbindBidirectional(
				lastProject.dateProperty());
			
			txtSoftName.textProperty().unbindBidirectional(
				lastProject.lapSoftwareProperty().get().nameProperty());
			txtSoftVersion.textProperty().unbindBidirectional(
				lastProject.lapSoftwareProperty().get().nameProperty());
		}
		lastProject = project;

		txtPrjName.textProperty().bindBidirectional(project.nameProperty());
		// TODO Bind ObjectProperty to an textfield?
//		txtPrjDate.textProperty().bindBidirectional(project.dateProperty());
		
		txtSoftName.textProperty().bindBidirectional(project.lapSoftwareProperty().get().nameProperty());
		txtSoftVersion.textProperty().bindBidirectional(project.lapSoftwareProperty().get().versionProperty());
	}

}