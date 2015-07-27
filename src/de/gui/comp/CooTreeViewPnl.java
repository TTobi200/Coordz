/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import java.io.IOException;
import java.util.Objects;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import de.coordz.data.*;
import de.gui.*;
import de.gui.comp.CooCustomerTreeItem.CooProjectTreeItem;
import de.util.CooFileUtil;
import de.util.log.CooLog;

public class CooTreeViewPnl extends BorderPane
{
	protected ObservableList<CooDataChanged> components;
	
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
								ObservableValue<? extends TreeItem<String>> observable,
								TreeItem<String> old_val,
								TreeItem<String> new_val)
				{
					TreeItem<String> selectedItem = new_val;

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
						lastSelCustomer = customer;

						components.forEach(c -> c.customerChanged(
							customer));
						components.forEach(c -> c.projectChanged(
							new CooProject()));
					}
				}
			});
	}
	
	@FXML
	protected void add()
	{
		TreeItem<String> selItem = prjTreeView.getSelectionModel()
						.getSelectedItem();
		
		// If root is selected - add new customer
		if(selItem == prjTreeView.getRoot())
		{
			CooCustomer newCustomer = new CooCustomer();
			newCustomer.nameProperty().set("Neuer Kunde");
			
			selItem.getChildren().add(
				new CooCustomerTreeItem(newCustomer.nameProperty(),
					newCustomer));
		} // Else add new project to customer
		else if(selItem instanceof CooCustomerTreeItem)
		{
			CooProject newPrj = new CooProject();
			newPrj.nameProperty().set("Neues Projekt");
			((CooCustomerTreeItem)selItem).customerProperty()
				.get().addProject(newPrj);
			
			selItem.getChildren().add(new CooProjectTreeItem(
				newPrj.nameProperty(), newPrj));
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
							? "Projekt "  : "Kunde ";
			itmType += "\"" + selItem.getValue() + "\"";
			
			if(CooDialogs.showConfirmDialog(getScene().getWindow(),
				itmType + " löschen", "Wollen Sie " + itmType + " wirklich löschen?"))
			{
				selItem.getParent().getChildren().remove(
					selItem);
			}
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