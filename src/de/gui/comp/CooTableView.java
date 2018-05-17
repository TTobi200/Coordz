/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import java.util.Objects;

import de.coordz.db.CooDBDao;
import de.gui.CooDialogs;
import de.gui.comp.CooTableDataEvent.Action;
import de.util.CooFileUtil;
import de.util.log.CooLog;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

public class CooTableView<T extends CooDBDao> extends TableView<T>
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
	
	private CooTableDataEventListener<T> tableDataEventListener;

	public CooTableView()
	{
		maxRowsProperty = new SimpleIntegerProperty(Integer.MAX_VALUE);
		
		editMenuItm.setOnAction(event ->
		{
			CooDialogs.showEditTable(getScene().getWindow(),
				this, "Bearbeiten von Eintrag");
			
			// Inform the table listener
			informListener(getSelectionModel()
				.getSelectedItem(), Action.EDIT);
		});

		addMenuItm.setOnAction(event ->
		{
			try
			{
				T dao = clazz.newInstance();
				getItems().add(dao);
				getSelectionModel().selectLast();
				CooDialogs.showEditTable(getScene().getWindow(),
					this, "Hinzufügen von Eintrag");
				
				// Inform the table listener
				informListener(dao, Action.ADD);
			}
			catch(IllegalAccessException | InstantiationException e)
			{
				CooLog.error("Could not create instance of class", e);
			}
		});

		remMenuItm.setOnAction(event ->
		{
			if(CooDialogs.showConfirmDialog(getScene().getWindow(),
				"Eintrag löschen", "Wollen Sie diesen Eintrag wirklich löschen?"))
			{
				T dao = getSelectionModel().getSelectedItem();
				getItems().remove(dao);
				// Inform the table listener
				informListener(dao, Action.DELETE);
			}
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

	private void informListener(T dao, Action action)
	{
		if(Objects.nonNull(tableDataEventListener))
		{
			tableDataEventListener.tableDataChanged(
				new CooTableDataEvent<>(dao, action));
		}		
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
	
	public void setTableDataEventListener(CooTableDataEventListener<T> listener)
	{
		this.tableDataEventListener = listener;
	}
}