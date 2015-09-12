/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.pnl;

import java.io.*;
import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import de.coordz.data.*;
import de.coordz.data.base.*;
import de.coordz.lap.CooLAPClient;
import de.gui.*;
import de.gui.comp.*;
import de.gui.view3D.*;
import de.util.*;
import de.util.log.CooLog;

public class CooMeasurementsPnl extends BorderPane implements CooDataChanged, CooMeasurementChanged
{
	protected ObservableList<CooMeasurementChanged> components;
	
	@FXML
	protected CooTableView<CooMeasurement> tblMeasurement;
	@FXML
	protected TabPane tabMeasurements;
	@FXML
	protected CooTableView<CooReticle> tblReticles;
	@FXML
	protected CooTableView<CooTarget> tblTargets;
	@FXML
	protected CooTableView<CooTotalstation> tblTotalStation;
	@FXML
	protected Label lblTargetFile;
	@FXML
	protected Button btnConn;
	@FXML
	protected ComboBox<CooStation> cbStations;
	@FXML
	protected CooView3D view3D;
	@FXML
	protected CooTextArea txtNotes;
	
	protected CooLAPClient client;
	protected StringProperty connectionStringProperty;
	protected StringProperty fileStringProperty;
	
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
		
		tblMeasurement.setClazz(CooMeasurement.class);
		tblReticles.setClazz(CooReticle.class);
		tblTargets.setClazz(CooTarget.class);
		tblTotalStation.setClazz(CooTotalstation.class);
		
		// There can only be one total station
		tblTotalStation.maxRowsProperty().setValue(1);
		
		CooTabPaneDetacherUtil.create().makeTabsDetachable(
			tabMeasurements);
		
		connectionStringProperty = new SimpleStringProperty("Nicht verbunden");
		fileStringProperty = new SimpleStringProperty("-");
		
		lblTargetFile.textProperty().bind(Bindings.concat("Status: ", connectionStringProperty,
			" | ", "Targetdatei: ", fileStringProperty));
		
		// Station selection changed
		cbStations.getSelectionModel().selectedItemProperty().addListener(
			(old, curr, newV) -> tblMeasurement.setItems(Objects.nonNull(newV) ? newV.getMeasurements() : null));
		
		// Measurement selection changed
		components = FXCollections.observableArrayList(this, view3D);
		tblMeasurement.getSelectionModel()
			.selectedItemProperty()
			.addListener(
				(obs, old, newV) -> 
				{
					txtNotes.bindBidirectional(Objects.nonNull(newV) ? 
						newV.notesProperty() : new SimpleStringProperty());
					components.forEach(c -> c.measurementChanged(newV));
				});
	}
	
	@FXML
	protected void connect()
	{
		client = CooDialogs.showConnectToLAPSoft(
			getScene().getWindow());
		
		if(Objects.nonNull(client))
		{
			btnConn.setDisable(true);
			connectionStringProperty.setValue("Verbunden (IP: " + client.getSrvIp() 
				+ " Port: " + client.getSrvPort() + ")");
		}
	}
	
	@FXML
	protected void disconnect()
	{
		if(Objects.nonNull(client))
		{
			btnConn.setDisable(!client.disconnect());
			connectionStringProperty.setValue("Nicht verbunden");
		}
	}
	
	@FXML
	protected void startManualCalibration() throws IOException
	{
		if(Objects.nonNull(client))
		{
			// TODO commit the file
			client.startManualCalibration(new File(""));
		}
	}
	
	@FXML
	protected void startAutoCalibration() throws IOException
	{
		if(Objects.nonNull(client))
		{
			// TODO commit the file
			client.startAutoCalibration(new File(""));
		}
	}
	
	@FXML
	protected void startProjection() throws IOException
	{
		if(Objects.nonNull(client))
		{
			client.startProjection(new File(""));
		}
	}
	
	@FXML
	protected void stopProjection() throws IOException
	{
		if(Objects.nonNull(client))
		{
			client.stopProjection();
		}
	}
	
	@FXML
	protected void previousContour() throws IOException
	{
		if(Objects.nonNull(client))
		{
			client.previousContour();
		}
	}
	
	@FXML
	protected void nextContour() throws IOException
	{
		if(Objects.nonNull(client))
		{
			client.nextContour();
		}
	}
	
	@Override
	public void measurementChanged(CooMeasurement measurement)
	{
		tblReticles.setItems(Objects.nonNull(measurement) ?
						measurement.getReticles() : null);
		tblTargets.setItems(Objects.nonNull(measurement) ?
						measurement.getTargets() : null);
		tblTotalStation.getItems().setAll(Objects.nonNull(measurement) ?
						measurement.totalStationProperty().get() : null);
		
		// Inform the 3D View
		view3D.measurementChanged(measurement);
	}
	
	@Override
	public void projectChanged(CooProject project)
	{
		cbStations.setItems(project.getStations());
		
		// Inform the 3D View
		view3D.projectChanged(project);
	}
	
	@Override
	public void customerChanged(CooCustomer customer)
	{
		// Inform the 3D View
		view3D.customerChanged(customer);
	}
}