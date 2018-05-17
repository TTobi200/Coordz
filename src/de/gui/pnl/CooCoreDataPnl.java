/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.pnl;

import static de.util.CooSQLUtil.*;

import java.io.*;
import java.net.MalformedURLException;
import java.sql.*;
import java.util.Objects;

import de.coordz.CooSystem;
import de.coordz.data.CooCustomer;
import de.coordz.data.base.*;
import de.coordz.data.init.CooInitTblImage;
import de.coordz.db.CooDBSelectStmt;
import de.coordz.db.gen.inf.InfImage;
import de.gui.*;
import de.gui.comp.*;
import de.util.CooFileUtil;
import de.util.log.CooLog;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

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
	
	private CooCustomer customer;

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
		// FORTEST $TO: Load the customer from database
		// TODO: $TO: Move this to loading method
		if(CooSystem.USE_DB)
		{
			try
			{
				customer.fromDB(CooSystem.getDatabase());
				updateDao(customer, txtCustomer);
				updateDao(customer, txtStreet);
				updateDao(customer, txtPLZ);
				updateDao(customer, txtLocation);
				
				Integer customerID = customer.customerIdProperty().get();
				updateDaos(tblContacts, customerID);
				updateDaos(tblPalets, customerID);
			}
			catch(SQLException e)
			{
				CooLog.error("Error while loading "
					+ "customer from db", e);
			}
		}
		
		this.customer = customer;
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
		try
		{
			File logoFile = CooDialogs.showOpenImageDialog(getScene().getWindow(),
				"Logo auswählen");

			if(Objects.nonNull(logoFile))
			{
				imgViewLogo.imageProperty().set(new Image(
					logoFile.toURI().toURL().toString()));
				
				// FORTEST Store the image in database
				if(CooSystem.USE_DB)
				{
					// Select existing customer logo
					CooDBSelectStmt stmt = new CooDBSelectStmt();
					stmt.addFrom(InfImage.TABLE_NAME);
					stmt.addColumn("*");
					stmt.addWhere(InfImage.CUSTOMERID + " = ?", 
						customer.customerIdProperty().get());
					stmt.addWhere(InfImage.NAME + " = ?",
						CooInitTblImage.IMAGE_LOGO);
					
					CooImage daoImage = new CooImage();
					ResultSet res = CooSystem.getDatabase().execQuery(stmt);
					
					if(res.next())
					{
						// Image already present
						daoImage.cre(res);
					}
					else
					{
						// Insert a new omage entry
						daoImage.cre();
						daoImage.nameProperty().set(CooInitTblImage.IMAGE_LOGO);
						daoImage.insert(customer.customerIdProperty().get());
					}
					
					// Store the image data in blob
					daoImage.store(logoFile);
				}
			}
		}
		catch(MalformedURLException | SQLException e)
		{
			CooLog.error("Error while changing customer logo", e);
		}
	}
}