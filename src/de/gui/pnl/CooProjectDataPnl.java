/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.pnl;

import static de.util.CooSQLUtil.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import de.coordz.CooSystem;
import de.coordz.data.*;
import de.coordz.data.base.*;
import de.gui.*;
import de.gui.comp.*;
import de.util.*;
import de.util.log.CooLog;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

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
	protected CooTextField txtGateIp;
	@FXML
	protected CooTextField txtGateMAC;
	@FXML
	protected CooTableView<CooLaser> tblLaser;
	
	@FXML
	protected TitledPane tpRegDividing;
	@FXML
	protected HBox hBoxRegDividing;
	@FXML
	protected CooTableView<CooLaser> tblRegDevide;
	
	/** The last selected {@link CooCustomer} */
	private CooCustomer customer;
	
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
		tblRegDevide.setClazz(CooLaser.class);
		
		// Inform the panels when station changed
		tblStations.getSelectionModel().selectedItemProperty()
			.addListener((old, curr, newV) -> stationChanged(newV));
		// Recalculate the region dividing when laser added or deleted
//		tblLaser.addOnAddFinished(l -> calculateRegionDividing(tblStations.
//			getSelectionModel().selectedItemProperty().get()));
//		tblLaser.addOnDeleteFinished(l -> calculateRegionDividing(tblStations.
//			getSelectionModel().selectedItemProperty().get()));
		
		// Move the calculate buttons to right site
		tpRegDividing.setContentDisplay(ContentDisplay.RIGHT);
		CooMainFrame.doOnShowing(()-> CooGuiUtil.
			moveButtonsOnTitlepane(tpRegDividing, hBoxRegDividing));
	}

	@Override
	public void projectChanged(CooProject project)
	{
		// FORTEST $TO: Load the project from database
		// TODO: $TO: Move this to loading method
		if(CooSystem.USE_DB)
		{
			try
			{
				project.fromDB(CooSystem.getDatabase());
				updateDao(project, txtPrjName);
				updateDao(project.lapSoftwareProperty()
					.get(), txtSoftName);
				updateDao(project.lapSoftwareProperty()
					.get(), txtSoftVersion);
				
				updateDaos(tblStations, project.
					projectIdProperty().get());
			}
			catch(SQLException e)
			{
				CooLog.error("Error while loading "
					+ "project from db", e);
			}
		}
		
		txtPrjName.bindBidirectional(project.nameProperty());
		// TODO Bind ObjectProperty to an textfield?
		// txtPrjDate.textProperty().bindBidirectional(project.dateProperty());

		// LAP Software fields
		txtSoftName.bindBidirectional(project
			.lapSoftwareProperty().get().nameProperty());
		txtSoftVersion.bindBidirectional(project
			.lapSoftwareProperty().get().versionProperty());

		// Station fields
		tblStations.setItems(project.getStations());
		
		// Reset the station bind fields
		stationChanged(null);
	}
	
	public void stationChanged(CooStation station)
	{
		if(Objects.nonNull(station))
		{
			// FORTEST $TO: Load the station from database
			if(CooSystem.USE_DB)
			{
				try
				{
					station.fromDB(CooSystem.getDatabase());
					updateDao(station.gatewayProperty()
						.get(),txtGateIp);
					updateDao(station.gatewayProperty()
						.get(),txtGateMAC);
					
					updateDaos(tblLaser, station.
						gatewayProperty().get()
						.gatewayIdProperty().get());
				}
				catch(SQLException e)
				{
					CooLog.error("Error while loading "
						+ "station from db", e);
				}
			}
			
			// Get the gateway for this station 
			CooGateway gateway = station.gatewayProperty().get();
			// And set the fields
			tblLaser.setItems(gateway.getLaser());
			txtGateIp.bindBidirectional(
				gateway.ipProperty());
			txtGateMAC.bindBidirectional(
				gateway.macProperty());

			// Get the region dividing for this station
			tblRegDevide.setItems(station.regionDevidingProperty()
				.get().getLaser());
		}
		else
		{
			// Clear all fields
			txtGateIp.unbindBidirectional();
			txtGateMAC.unbindBidirectional();
			tblLaser.setItems(FXCollections.observableArrayList());
			tblRegDevide.setItems(FXCollections.observableArrayList());
		}
	}
	
	@Override
	public void customerChanged(CooCustomer customer)
	{
		// Store the last selected customer
		this.customer = customer;
	}

	@FXML
	private void calcRegDividing()
	{
		if(CooDialogs.showConfirmDialog(getScene().getWindow(),
			"Bereichsaufteilung berechnen", "Soll die "
			+ "Bereichsufteilung berechnet werden?"))
		{
			CooStation station = tblStations.
				getSelectionModel().getSelectedItem();
			
			if(Objects.nonNull(station) && Objects.nonNull(customer))
			{
				// Get the region dividing an station laser
				CooRegionDividing dividing = station.regionDevidingProperty().get();
				ObservableList<CooLaser> laser = station.gatewayProperty().get().getLaser();
				
				// Calculate the region diving and display it
				tblRegDevide.setItems(dividing.fromLaser(customer.getPalets(), laser));	
			}
		}
	}
}