/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import de.coordz.data.CooData;
import de.gui.CooDialogs;
import de.util.CooFileUtil;
import de.util.log.CooLog;

public class CooTableView<T extends CooData> extends TableView<T>
{
	protected Class<T> clazz;
	
	final TableRow<T> row = new TableRow<>();
	final ContextMenu contextMenu = new ContextMenu();
	final MenuItem editMenuItm = new MenuItem("Bearbeiten", new ImageView(
		CooFileUtil.getResourceIcon("edit.png")));
	final MenuItem addMenuItm = new MenuItem("Hinzufügen", new ImageView(
		CooFileUtil.getResourceIcon("add.png")));
	final MenuItem remMenuItm = new MenuItem("Entfernen", new ImageView(
		CooFileUtil.getResourceIcon("delete.png")));

	public CooTableView()
	{
		editMenuItm.setOnAction(event ->
		{
			CooDialogs.showEditTable(getScene().getWindow(),
				this, "Bearbeiten von Eintrag");
		});

		addMenuItm.setOnAction(event ->
		{
			try
			{
				getItems().add(clazz.newInstance());
				getSelectionModel().selectLast();
				CooDialogs.showEditTable(getScene().getWindow(),
					this, "Hinzufügen von Eintrag");
			}
			catch(IllegalAccessException | InstantiationException e)
			{
				CooLog.error("Could not create instance of class", e);
			}
		});
		
		remMenuItm.setOnAction(event ->
		{
			getItems().remove(
				getSelectionModel()
					.getSelectedItem());
		});
		
		contextMenu.getItems().addAll(addMenuItm,
			editMenuItm,
			new SeparatorMenuItem(),
			remMenuItm);
		setContextMenu(contextMenu);
		editMenuItm.disableProperty().bind(
			getSelectionModel()
			.selectedItemProperty().isNull());
		remMenuItm.disableProperty().bind(
			getSelectionModel()
			.selectedItemProperty().isNull());
	}
	
	public void setClazz(Class<T> clazz)
	{
		this.clazz = clazz;
	}
}
