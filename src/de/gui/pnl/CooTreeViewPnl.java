/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.pnl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import de.coordz.data.*;
import de.coordz.doc.*;
import de.gui.*;
import de.gui.comp.CooCustomerTreeItem;
import de.gui.comp.CooCustomerTreeItem.CooProjectTreeItem;
import de.util.*;
import de.util.log.CooLog;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

public class CooTreeViewPnl extends BorderPane
{
	protected ObservableList<CooDataChanged> components;

	@FXML
	protected TextField txtSearch;
	@FXML
	protected Button btnAdd;
	@FXML
	protected Button btnDelete;
	@FXML
	protected Button btnExport;

	@FXML
	protected TreeView<String> prjTreeView;

	public CooTreeViewPnl()
	{
		try
		{
			CooFileUtil.loadFXML(this, CooFileUtil.FXML_COMP +
				CooFileUtil.IN_JAR_SEPERATOR
					+ "CooTreeViewPnl.fxml", this);
		}
		catch(IOException e)
		{
			CooLog.debug("Could not load FXML", e);
		}

		components = FXCollections.observableArrayList();
		prjTreeView.getSelectionModel().selectedItemProperty()
			.addListener(new ChangeListener<TreeItem<String>>()
			{
				protected CooCustomer lastSelCustomer;

				@Override
				public void changed(
								ObservableValue<? extends TreeItem<String>> obs,
								TreeItem<String> old,
								TreeItem<String> newV)
				{
					TreeItem<String> selectedItem = newV;

					if(selectedItem instanceof CooProjectTreeItem)
					{
						CooCustomer customer = ((CooCustomerTreeItem)selectedItem.getParent())
							.customerProperty().get();

						CooProject project = ((CooProjectTreeItem)selectedItem)
							.projectProperty().get();

						if(customer != lastSelCustomer)
						{
							lastSelCustomer = customer;
							components.forEach(c -> c.customerChanged(
								customer));
						}

						components.forEach(c -> c.projectChanged(
							project));
					}
					else if(selectedItem instanceof CooCustomerTreeItem)
					{
						CooCustomer customer = ((CooCustomerTreeItem)selectedItem)
							.customerProperty().get();
						
						if(customer != lastSelCustomer)
						{
							lastSelCustomer = customer;
							components.forEach(c -> c.customerChanged(
								customer));
						}

						CooProject empty = new CooProject();
						components.forEach(c -> c.projectChanged(empty));
					}

					btnExport.setDisable(selectedItem == prjTreeView.getRoot()
								|| !(selectedItem instanceof CooCustomerTreeItem));
					btnDelete.setDisable(selectedItem == prjTreeView.getRoot());
				}
			});
		txtSearch.setOnKeyReleased(e->
		{
			prjTreeView.getRoot().getChildren().forEach(treeItm -> 
			{
				CooGuiUtil.selectMatchingNode(prjTreeView, 
					treeItm, txtSearch.getText());
			});
		});
	}

	@FXML
	protected void add() throws SQLException
	{
		TreeItem<String> selItem = prjTreeView.getSelectionModel()
			.getSelectedItem();

		// If root is selected - add new customer
		if(Objects.nonNull(selItem) && selItem == prjTreeView.getRoot())
		{
			CooCustomer newCustomer = new CooCustomer();
			newCustomer.cre();
			newCustomer.nameProperty().set("Neuer Kunde");
			newCustomer.insert();
			
			selItem.getChildren().add(
				new CooCustomerTreeItem(newCustomer.nameProperty(),
					newCustomer));
		} // Else add new project to customer
		else if(selItem instanceof CooCustomerTreeItem)
		{
			CooProject newPrj = new CooProject();
			CooCustomer customer = ((CooCustomerTreeItem)selItem)
				.customerProperty().get();
			newPrj.cre();
			newPrj.nameProperty().set("Neues Projekt");
			customer.addProject(newPrj);
			newPrj.insert(customer.customerIdProperty().get());

			selItem.getChildren().add(new CooProjectTreeItem(
				newPrj.nameProperty(), newPrj));
		}
	}

	@FXML
	protected void save()
	{
		TreeItem<String> selItem = prjTreeView.getSelectionModel()
			.getSelectedItem();

		// Save all projects and the customer
		if(Objects.nonNull(selItem) && selItem == prjTreeView.getRoot())
		{
			selItem.getChildren().filtered(treeItm -> treeItm instanceof
				CooCustomerTreeItem).forEach(treeItm ->
			{
				CooCustomer customer = ((CooCustomerTreeItem)treeItm)
					.customerProperty().get();

				customer.getProjects().forEach(prj ->
					CooXMLDBUtil.saveProject(customer, prj));
				CooXMLDBUtil.saveCustomer(customer);
			});
		}
		else if(selItem instanceof CooCustomerTreeItem)
		{
			CooCustomer customer = ((CooCustomerTreeItem)selItem)
				.customerProperty().get();

			customer.getProjects().forEach(prj ->
			CooXMLDBUtil.saveProject(customer, prj));
			CooXMLDBUtil.saveCustomer(customer);

		} // Only save selected project
		else if(selItem instanceof CooProjectTreeItem)
		{
			CooCustomer customer = ((CooCustomerTreeItem)selItem.getParent())
				.customerProperty().get();

			CooXMLDBUtil.saveProject(customer,
				((CooProjectTreeItem)selItem).projectProperty().get());
		}
	}

	@FXML
	protected void delete()
	{
		TreeItem<String> selItem = prjTreeView.getSelectionModel()
			.getSelectedItem();

		if(Objects.nonNull(selItem) && selItem != prjTreeView.getRoot())
		{
			String itmType = selItem instanceof CooProjectTreeItem
							? "Projekt " : "Kunde ";
			itmType += "\"" + selItem.getValue() + "\"";

			if(CooDialogs.showConfirmDialog(getScene().getWindow(),
				itmType + " löschen", "Wollen Sie " + itmType
										+ " wirklich löschen?"))
			{
				// If project selected - delete it from customer project list
				if(selItem instanceof CooProjectTreeItem)
				{
					CooCustomer customer = ((CooCustomerTreeItem)selItem.getParent())
						.customerProperty()
						.get();
					CooProject project = ((CooProjectTreeItem)selItem)
						.projectProperty().get();

					CooXMLDBUtil.deleteProject(customer, project);
				}
				else if(selItem instanceof CooCustomerTreeItem)
				{
					CooCustomer customer = ((CooCustomerTreeItem)selItem)
						.customerProperty().get();

					CooXMLDBUtil.deleteCustomer(customer);
				}

				// Remove the selected item
				selItem.getParent().getChildren().remove(
					selItem);
			}
		}
	}

	@FXML
	public void export()
	{
		TreeItem<String> selItem = prjTreeView.getSelectionModel()
			.getSelectedItem();

		if(selItem != prjTreeView.getRoot()
			&& selItem instanceof CooCustomerTreeItem)
		{
			CooCustomer customer = ((CooCustomerTreeItem)selItem)
				.customerProperty().get();

			CooDialogs.showToDocDialog(getScene().getWindow(), customer,
				new CooPdfDocument(), new CooXlsDocument());
		}
	}

	public void addDataChangedListener(CooDataChanged component)
	{
		components.add(component);
	}

	public TreeView<String> getPrjTreeView()
	{
		return prjTreeView;
	}
}