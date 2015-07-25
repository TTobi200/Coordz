/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.scene.control.*;
import de.coordz.data.*;
import de.gui.CooDataChanged;
import de.gui.comp.CooCustomerTreeItem.CooProjectTreeItem;

public class CooCustomerTreeView extends TreeView<String>
{
	protected ObservableList<CooDataChanged> components;
	
	public CooCustomerTreeView()
	{
		components = FXCollections.observableArrayList();
		getSelectionModel().selectedItemProperty()
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
					
					CooProject project = customer.getProjects().get(selectedItem.getValue());
					
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
	
	public void addDataChangedListener(CooDataChanged component)
	{
		components.add(component);
	}
}