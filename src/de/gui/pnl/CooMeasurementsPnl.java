/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.pnl;

import java.io.*;
import java.util.*;

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
	protected CooTableView<CooRectangle> tblRefSpec;
	@FXML
	protected CooTableView<CooRectangle> tblResult;
	
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
		tblRefSpec.setClazz(CooRectangle.class);
		tblResult.setClazz(CooRectangle.class);
		
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
			(old, curr, newV) -> stationChanged(newV));
		
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
		
		// Reference measurement selection changed
		tblRefSpec.getSelectionModel().selectedItemProperty()
			.addListener((obs, old, newV) -> 
				selectEntryByName(tblResult, newV));
		tblResult.getSelectionModel().selectedItemProperty()
		.addListener((obs, old, newV) -> 
			selectEntryByName(tblRefSpec, newV));
		
		tpTargtes.setContentDisplay(ContentDisplay.RIGHT);
		CooMainFrame.doOnShowing(()-> 
		{
			CooGuiUtil.moveButtonsOnTitlepane(tpTargtes, hBoxTargets);
		});
	}
	
	private void  selectEntryByName(CooTableView<CooRectangle> child,
			CooRectangle value)
	{
		if(Objects.nonNull(value))
		{
			// Try to select entry in child table
			child.getItems().filtered(
				// Check if we have a name from rectangle
				itm -> Objects.nonNull(itm.nameProperty().get()))
				// Check if it equals to parent selected value
				.filtered(itm -> itm.nameProperty()
					.get().equals(value.nameProperty().get())).forEach(itm ->
				// Then select this entry
				child.getSelectionModel().select(itm));
		}
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
//			CooLAPPacket packet = client.startAutoCalibration(
//				new File("doc/LAP Software/Calibration.cal"));
			// FORTEST Use the file on laser test pc
			CooLAPPacket packet = client.startAutoCalibration(
				new File("C:\\Users\\User\\Desktop\\Lasertest"
				+ "\\01_Einmessungen\\01_Kalibrierdateien\\CheckPlate.cal"));
			
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
	protected void switchCalibrationMode() throws IOException
	{
		// Get all choices for calibration mode
		HashMap<String, Integer> calibModes = new HashMap<>();
		calibModes.put("Kalibrierung erforderlich", 
			CooLAPClient.AUTOMATIC_CALIBRATION_MODE);
		calibModes.put("Kalibrierung nicht erforderlich", 
			CooLAPClient.NO_CALIBRATION_MODE);
		calibModes.put("Positionsüberprüfung der targets",
			CooLAPClient.POSITION_CHECK_OF_TARGET_FILM_MODE);
		calibModes.put("Positionsüberprüfung auf Bohrlochhöhe",
			CooLAPClient.POSITION_CHECK_OF_TARGET_HOLE_MODE);
		
		// Ask user to choose a calibration mode
		Integer result = CooDialogs.showChooseDialog(getScene().getWindow(),
			"Kalibriermodus ändern", "Kalibriermodus auswählen:", calibModes);
		
		if(Objects.nonNull(result) && Objects.nonNull(
			client) && client.isConnected())
		{
			// Receive the result packet 
			// TODO Generate a temp file and commit it here
//			CooLAPPacket packet = client.switchCalibrationMode(
//				result, new File("doc/LAP Software/Calibration.cal"));
			// FORTEST $TO: Use the calibration file on test pc
			CooLAPPacket packet = client.switchCalibrationMode(result,
				new File("C:\\Users\\User\\Desktop\\Lasertest"
				+ "\\01_Einmessungen\\01_Kalibrierdateien\\CheckPlate.cal"));
		}
	}
	
	@FXML
	protected void startProjection() throws IOException
	{
		if(Objects.nonNull(client) && client.isConnected())
		{
//			client.startProjection(new File(
//				"doc/LAP Software/LaserData.ply"));
			// FORTEST $TO: Use the projection file on test pc
			CooLAPPacket packet = client.startProjection(
				new File("C:\\Users\\User\\Desktop\\Lasertest"
				+ "\\01_Einmessungen\\02_Messmatrix\\LaserData.ply"));
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
	
	private void stationChanged(CooStation station)
	{
		// Display all measurements for this station
		tblMeasurement.setItems(Objects.nonNull(station)
			? station.getMeasurements() : null);
		
		// Display the reference measurement
		tblRefSpec.setItems(Objects.nonNull(station)
			? station.verifyMeasurementProperty()
				.get().getSpecification() : null);
		tblResult.setItems(Objects.nonNull(station)
			? station.verifyMeasurementProperty()
				.get().getResult() : null);
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