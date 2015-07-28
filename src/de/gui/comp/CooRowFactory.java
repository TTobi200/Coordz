/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2014 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import de.coordz.data.base.CooContact;
import de.gui.CooDialogs;
import de.util.CooFileUtil;

public class CooRowFactory implements
				Callback<TableView<CooContact>, TableRow<CooContact>>
{
	@Override
	public TableRow<CooContact> call(TableView<CooContact> tableView)
	{
		final TableRow<CooContact> row = new TableRow<>();
		final ContextMenu contextMenu = new ContextMenu();
		final MenuItem editMenuItm = new MenuItem("Bearbeiten", new ImageView(
			CooFileUtil.getResourceIcon("edit.png")));
		final MenuItem addMenuItm = new MenuItem("Hinzufügen", new ImageView(
			CooFileUtil.getResourceIcon("add.png")));
		final MenuItem remMenuItm = new MenuItem("Entfernen", new ImageView(
			CooFileUtil.getResourceIcon("delete.png")));

		editMenuItm.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				CooDialogs.showEditTable(tableView.getScene().getWindow(),
					tableView,
					"Bearbeiten");
			}
		});

		addMenuItm.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				tableView.getItems().add(new CooContact());
				tableView.getSelectionModel().selectLast();
				CooDialogs.showEditTable(tableView.getScene().getWindow(),
					tableView,
					"Hinzufügen");
			}
		});

		remMenuItm.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				tableView.getItems().remove(
					tableView.getSelectionModel()
						.getSelectedItem());
			}
		});
		contextMenu.getItems().addAll(addMenuItm,
			editMenuItm,
			new SeparatorMenuItem(),
			remMenuItm);
		tableView.setContextMenu(contextMenu);
		editMenuItm.disableProperty().bind(
			tableView.getSelectionModel()
			.selectedItemProperty().isNull());
		remMenuItm.disableProperty().bind(
			tableView.getSelectionModel()
			.selectedItemProperty().isNull());
		return row;
	}

}