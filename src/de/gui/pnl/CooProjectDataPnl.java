/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.pnl;

import java.io.IOException;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import de.coordz.data.CooProject;
import de.coordz.data.base.*;
import de.gui.CooDataChanged;
import de.gui.comp.*;
import de.util.CooFileUtil;
import de.util.log.CooLog;

public class CooProjectDataPnl extends BorderPane implements CooDataChanged
{
	@FXML
	protected CooTextField txtPrjName;
	@FXML
	protected CooTextField txtPrjDate;

	@FXML
	protected CooTextField txtSoftName;
	@FXML
	protected CooTextField txtSoftVersion;

	@FXML
	protected CooTableView<CooStation> tblStations;

	@FXML
	protected ComboBox<CooStation> cbStation;
	@FXML
	protected CooTextField txtGateIp;
	@FXML
	protected CooTextField txtGateMAC;
	@FXML
	protected CooTableView<CooLaser> tblLaser;

	public CooProjectDataPnl()
	{
		try
		{
			CooFileUtil.loadFXML(this, CooFileUtil.FXML_COMP +
					CooFileUtil.IN_JAR_SEPERATOR + "CooProjectDataPnl.fxml", this);
		}
		catch(IOException e)
		{
			CooLog.debug("Could not load FXML", e);
		}

		tblStations.setClazz(CooStation.class);
		tblLaser.setClazz(CooLaser.class);
		
		cbStation.getSelectionModel().selectedItemProperty().addListener(
			(old, curr, newV) ->
			{
				if(Objects.nonNull(newV))
				{
					CooGateway gateway = newV.gatewayProperty().get();
					tblLaser.setItems(gateway.getLaser());
					txtGateIp.bindBidirectional(
						gateway.ipProperty());
					txtGateMAC.bindBidirectional(
						gateway.macProperty());
				}
				else
				{
					txtGateIp.unbindBidirectional();
					txtGateMAC.unbindBidirectional();
					tblLaser.setItems(FXCollections.observableArrayList());
				}
			});
	}

	@Override
	public void projectChanged(CooProject project)
	{
		txtPrjName.bindBidirectional(project.nameProperty());
		// TODO Bind ObjectProperty to an textfield?
		// txtPrjDate.textProperty().bindBidirectional(project.dateProperty());

		// LAP Software fields
		txtSoftName.bindBidirectional(project.lapSoftwareProperty()
			.get()
			.nameProperty());
		txtSoftVersion.bindBidirectional(project.lapSoftwareProperty()
			.get()
			.versionProperty());

		// Station fields
		tblStations.setItems(project.getStations());

		// Gateway fields
		cbStation.setItems(project.getStations());
	}
}