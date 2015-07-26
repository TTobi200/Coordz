/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import java.util.Objects;

import javafx.beans.property.*;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import de.coordz.data.*;
import de.util.CooFileUtil;

public class CooCustomerTreeItem extends TreeItem<String>
{
	protected ObjectProperty<CooCustomer> customer;

	public CooCustomerTreeItem(StringProperty name, CooCustomer customer)
	{
		this.valueProperty().bind(name);
		this.customer = new SimpleObjectProperty<CooCustomer>(customer);
		
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
	
	public ObjectProperty<CooCustomer> customerProperty()
	{
		return customer;
	}
	
	public class CooProjectTreeItem extends TreeItem<String>
	{
		private CooProject project;

		public CooProjectTreeItem(String name, CooProject project)
		{
			super(name, new ImageView(
				CooFileUtil.getResourceIcon("laser_icon.png")));
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
