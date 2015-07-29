/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.pnl;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import de.coordz.data.CooCustomer;
import de.coordz.data.base.*;
import de.gui.CooDataChanged;
import de.gui.comp.*;
import de.util.CooFileUtil;
import de.util.log.CooLog;

public class CooCoreDataPnl extends BorderPane implements CooDataChanged
{
	@FXML
	protected CooTextField txtCustomer;
	@FXML
	protected CooTextField txtStreet;
	@FXML
	protected CooTextField txtPLZ;
	@FXML
	protected CooTextField txtLocation;

	@FXML
	protected CooTableView<CooContact> tblContacts;
	@FXML
	protected CooTableView<CooPalet> tblPalets;

	public CooCoreDataPnl()
	{
		try
		{
			CooFileUtil.loadFXML(this, CooFileUtil.FXML_COMP +
				CooFileUtil.IN_JAR_SEPERATOR + "CooCoreDataPnl.fxml", this);
		}
		catch(IOException e)
		{
			CooLog.debug("Could not load FXML", e);
		}

		tblContacts.setClazz(CooContact.class);
		tblPalets.setClazz(CooPalet.class);
	}

	@Override
	public void customerChanged(CooCustomer customer)
	{
		txtCustomer.bindBidirectional(customer.nameProperty());
		txtStreet.bindBidirectional(customer.streetProperty());
		txtPLZ.bindBidirectional(customer.plzProperty());
		txtLocation.bindBidirectional(
			customer.locationProperty());

		tblContacts.setItems(customer.getContacts());
		tblPalets.setItems(customer.getPalets());
	}

}