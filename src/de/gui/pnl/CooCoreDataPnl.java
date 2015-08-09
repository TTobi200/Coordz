/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.pnl;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import de.coordz.data.CooCustomer;
import de.coordz.data.base.*;
import de.gui.*;
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

	@FXML
	protected CooImageView imgViewLogo;

	public CooCoreDataPnl()
	{
		try
		{
			CooFileUtil.loadFXML(this, CooFileUtil.FXML_COMP +
										CooFileUtil.IN_JAR_SEPERATOR
										+ "CooCoreDataPnl.fxml", this);
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
		imgViewLogo.bindBidirectional(
			customer.logoProprty());

		tblContacts.setItems(customer.getContacts());
		tblPalets.setItems(customer.getPalets());
	}

	@FXML
	public void changeLogo()
	{
		File logoFile = CooDialogs.showOpenImageDialog(getScene().getWindow(),
			"Logo auswählen");

		if(Objects.nonNull(logoFile))
		{
			try
			{
				imgViewLogo.imageProperty().set(new Image(logoFile.toURI()
					.toURL()
					.toString()));
			}
			catch(MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}