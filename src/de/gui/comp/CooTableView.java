/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import de.coordz.data.CooData;
import de.gui.CooDialogs;
import de.util.CooFileUtil;
import de.util.log.CooLog;

public class CooTableView<T extends CooData> extends TableView<T>
{
	protected Class<T> clazz;
	protected IntegerProperty maxRowsProperty;

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
		maxRowsProperty = new SimpleIntegerProperty(Integer.MAX_VALUE);
		
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
		
		// Fill the complete width
		setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		getItems().addListener(new ListChangeListener<T>()
		{
			@Override
			public void onChanged(
							javafx.collections.ListChangeListener.Change<? extends T> c)
			{
				addMenuItm.setDisable(!(getItems().size() < maxRowsProperty.get()));
			}
		});
	}

	public void setClazz(Class<T> clazz)
	{
		this.clazz = clazz;
		
		// Colums autosize
//		getColumns().forEach(
//			c -> c.prefWidthProperty().bind(widthProperty().multiply(0.25)));
	}
	
	public IntegerProperty maxRowsProperty()
	{
		return maxRowsProperty;
	}
}
