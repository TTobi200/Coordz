/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import java.io.IOException;
import java.util.Objects;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import de.coordz.data.CooCustomer;
import de.coordz.data.base.*;
import de.gui.*;
import de.util.CooFileUtil;
import de.util.log.CooLog;

public class CooCoreDataPnl extends BorderPane implements CooDataChanged
{
	@FXML
	protected TextField txtCustomer;
	@FXML
	protected TextField txtStreet;
	@FXML
	protected TextField txtPLZ;
	@FXML
	protected TextField txtLocation;

	@FXML
	protected TableView<CooContact> tblContacts;
	@FXML
	protected TableView<CooPalet> tblPalets;

	// vvvv See the comment below vvvv
	// Only as a workaround
	protected CooCustomer last;

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

		tblContacts.setRowFactory(new Callback<TableView<CooContact>, TableRow<CooContact>>()
		{
			@Override
			public TableRow<CooContact> call(TableView<CooContact> tableView)
			{
				final TableRow<CooContact> row = new TableRow<>();
				final ContextMenu contextMenu = new ContextMenu();
				final MenuItem addMenuItem = new MenuItem("Hinzufügen");
				final MenuItem removeMenuItem = new MenuItem("Entfernen");

				addMenuItem.setOnAction(new EventHandler<ActionEvent>()
				{
					@Override
					public void handle(ActionEvent event)
					{
						CooDialogs.showEditTable(getScene().getWindow(), tblContacts,
							"Kontakt hinzufügen");
					}
				});

				removeMenuItem.setOnAction(new EventHandler<ActionEvent>()
				{
					@Override
					public void handle(ActionEvent event)
					{
						tblContacts.getItems().remove(row.getItem());
					}
				});
				contextMenu.getItems().addAll(addMenuItem,
					removeMenuItem);
				row.setContextMenu(contextMenu);
				return row;
			}
		});
	}

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
			txtLocation.textProperty().unbindBidirectional(
				last.locationProperty());
		}
		last = customer;

		txtCustomer.textProperty().bindBidirectional(customer.nameProperty());
		txtStreet.textProperty().bindBidirectional(customer.streetProperty());
		txtPLZ.textProperty().bindBidirectional(customer.plzProperty());
		txtLocation.textProperty().bindBidirectional(
			customer.locationProperty());

		tblContacts.setItems(customer.getContacts());
		tblPalets.setItems(customer.getPalets());
	}
}