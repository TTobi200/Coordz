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
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import de.coordz.data.*;
import de.gui.CooDataChanged;
import de.util.CooFileUtil;
import de.util.log.CooLog;

public class CooCoreDataPanel extends BorderPane implements CooDataChanged
{
	@FXML
	protected Label lblPrjName;

	@FXML
	protected TextField txtCustomer;
	@FXML
	protected TextField txtStreet;
	@FXML
	protected TextField txtPLZ;
	@FXML
	protected TextField txtLocation;

	public CooCoreDataPanel()
	{
		try
		{
			CooFileUtil.loadFXML(this, CooFileUtil.FXML_COMP +
										CooFileUtil.IN_JAR_SEPERATOR
										+ "CooCoreDataPanel.fxml", this);
		}
		catch(IOException e)
		{
			CooLog.debug("Could not load FXML", e);
		}
	}

	// vvvv See the comment below vvvv
	// Only as a workaround
	protected CooCustomer last;
	
	@Override
	public void customerChanged(CooCustomer customer)
	{
		// TODO Try to find a solution
		// Bidirectional bindings have to be unbind bidirectional
		// Only .unbind of all customer propertys not working
		if(Objects.nonNull(last))
		{
			txtCustomer.textProperty().unbindBidirectional(last.nameProperty());
			txtStreet.textProperty().unbindBidirectional(last.streetProperty());
			txtPLZ.textProperty().unbindBidirectional(last.plzProperty());
			txtLocation.textProperty().unbindBidirectional(last.locationProperty());
		}
		last = customer;
		
		txtCustomer.textProperty().bindBidirectional(customer.nameProperty());
		txtStreet.textProperty().bindBidirectional(customer.streetProperty());
		txtPLZ.textProperty().bindBidirectional(customer.plzProperty());
		txtLocation.textProperty().bindBidirectional(customer.locationProperty());
	}

	@Override
	public void projectChanged(CooProject project)
	{
		lblPrjName.textProperty().bind(
			project.nameProperty());
	}
}