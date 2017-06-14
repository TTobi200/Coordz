/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.pnl;

import java.io.*;
import java.util.Objects;

import de.coordz.data.*;
import de.coordz.data.base.*;
import de.coordz.lap.*;
import de.gui.*;
import de.gui.comp.*;
import de.gui.view2D.CooView2D;
import de.gui.view3D.CooView3D;
import de.util.*;
import de.util.log.CooLog;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser.ExtensionFilter;

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
	protected TitledPane tpTargtes;
	@FXML
	protected HBox hBoxTargets;
	@FXML
	protected Button btnExport;
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
	protected CooView2D view2D;
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
		
		tpTargtes.setContentDisplay(ContentDisplay.RIGHT);
		CooMainFrame.doOnShowing(()-> 
		{
			CooGuiUtil.moveButtonsOnTitlepane(tpTargtes, hBoxTargets);
		});
	}
	
	@FXML
	protected void connect()
	{
		client = CooDialogs.showConnectToLAPSoft(
			getScene().getWindow());
		
		if(Objects.nonNull(client))
		{
			btnConn.setDisable(Boolean.TRUE);
			connectionStringProperty.setValue("Verbunden (IP: " + client.getSrvIp() 
				+ " Port: " + client.getSrvPort() + ")");
			CooLog.debug("Connected to LAP Software (IP: " + client.getSrvIp() 
				+ " Port: " + client.getSrvPort() + ")");
		}
	}
	
	@FXML
	protected void disconnect()
	{
		if(Objects.nonNull(client) && client.isConnected())
		{
			btnConn.setDisable(!client.disconnect());
			connectionStringProperty.setValue("Nicht verbunden");
			CooLog.debug("Disconnected from LAP Software");
		}
	}
	
	@FXML
	protected void startManualCalibration() throws IOException
	{
		if(Objects.nonNull(client) && client.isConnected())
		{
			// TODO commit the file
			client.startManualCalibration(new File(""));
		}
	}
	
	@FXML
	protected void startAutoCalibration() throws IOException
	{
		if(Objects.nonNull(client) && client.isConnected())
		{
			// Receive the result packet 
			// TODO Generate a temp file and commit it here
			CooLAPPacket packet = client.startAutoCalibration(
				new File("doc/LAP Software/Calibration.cal"));
			
			short result = packet.containsValue("Result")
				? (short)packet.getValue("Result") : 1;
			String message = "";
			
			switch(result)
			{
				case 0:
					// 0: successful
					message = "Die Automatische Kalibrierung"
						+ " wurde erfolgreich durchgeführt";
					break;
				default:
				case 1:
					// 1: faulty
					message = "Die Automatische Kalibrierung konnte"
						+ " nich durchgeführt werden";
				case 2:
					// 2: file not found
					message = "Die Kalibrierungsdatei konnte nicht "
						+ "gefunden werden";
					break;
				case 3:
					// 3: file not readable
					message = "Die Kalibrierungsdatei konnte nicht "
						+ "gelesen werden";
					break;
				case 4:
					// 4: manual calibration required
					message = "Eine Grundkalibrierung ist erforderlich";
					break;
			}
			
			// Show user inromation
			showInfoDialog("Automatische Kalibrierung", message);
		}
	}
	
	@FXML
	protected void startProjection() throws IOException
	{
		if(Objects.nonNull(client) && client.isConnected())
		{
			client.startProjection(new File(""));
		}
	}
	
	@FXML
	protected void stopProjection() throws IOException
	{
		if(Objects.nonNull(client) && client.isConnected())
		{
			client.stopProjection();
		}
	}
	
	@FXML
	protected void previousContour() throws IOException
	{
		if(Objects.nonNull(client) && client.isConnected())
		{
			client.previousContour();
		}
	}
	
	@FXML
	protected void nextContour() throws IOException
	{
		if(Objects.nonNull(client) && client.isConnected())
		{
			client.nextContour();
		}
	}
	
	private void showInfoDialog(String header, String message)
	{
		CooDialogs.showInfoDialog(getScene().getWindow(),
			header, message);		
	}
	
	@FXML
	protected void importCalFile()
	{
		// Show open calibration file dialog
		File calFile = CooDialogs.showOpenFileDialog(getScene().getWindow(),
			"CAL Datei importieren", new ExtensionFilter(
				"Kalibrierdatei *.cal", "*.cal"));
		
		// Check if we have an file specified
		if(Objects.nonNull(calFile))
		{
			// Load the targets and display them in table
			tblTargets.getItems().setAll(
				CooCalibrationFile.load(calFile));
		}
	}
	
	@FXML
	protected void exportCalFile()
	{
		// Show save calibration file dialog
		File calFile = CooDialogs.showSaveFileDialog(getScene().getWindow(),
			"CAL Datei exportieren", new ExtensionFilter(
				"Kalibrierdatei *.cal", "*.cal"));
		
		// Check if we have an file specified
		if(Objects.nonNull(calFile))
		{
			// Save the file with targets from table
			CooCalibrationFile.save(calFile,
				tblTargets.getItems());
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
		// Inform the 2D View
		view2D.measurementChanged(measurement);
	}
	
	@Override
	public void projectChanged(CooProject project)
	{
		cbStations.setItems(project.getStations());
		
		// Inform the 3D View
		view3D.projectChanged(project);
		// Inform the 2D View
		view2D.projectChanged(project);
	}
	
	@Override
	public void customerChanged(CooCustomer customer)
	{
		// Inform the 3D View
		view3D.customerChanged(customer);
		// Inform the 2D View
		view2D.customerChanged(customer);
	}
}