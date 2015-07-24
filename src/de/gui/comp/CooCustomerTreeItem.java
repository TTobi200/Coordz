/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import java.util.Objects;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import de.coordz.data.*;

public class CooCustomerTreeItem extends TreeItem<String>
{
	protected CooCustomer customer;

	public CooCustomerTreeItem(StringProperty name, CooCustomer customer)
	{
		this.valueProperty().bind(name);
		this.customer = customer;
		
		if(Objects.nonNull(customer))
		{
			customer.getProjects().forEach((ident, prj) -> 
			{
				CooProjectTreeItem project = new CooProjectTreeItem(
					ident, prj);
				getChildren().add(project);
			});
		}
	}
	
	public class CooProjectTreeItem extends TreeItem<String>
	{
		private CooProject project;

		public CooProjectTreeItem(String name, CooProject project)
		{
			super(name);
			this.project = project;
		}

		public CooProject getProject()
		{
			return project;
		}

		public void setProject(CooProject project)
		{
			this.project = project;
		}
	}
}
