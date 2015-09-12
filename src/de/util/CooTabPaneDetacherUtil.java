/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util;

import java.util.*;

import javafx.beans.property.*;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import com.sun.glass.ui.*;

public class CooTabPaneDetacherUtil
{
	private TabPane tabPane;
	private Tab currentTab;
	private final List<Tab> originalTabs;
	private final Map<Integer, Tab> tapTransferMap;
	private String[] stylesheets;
	private final BooleanProperty alwaysOnTop;

	private CooTabPaneDetacherUtil()
	{
		originalTabs = new ArrayList<>();
		stylesheets = new String[] {};
		tapTransferMap = new HashMap<>();
		alwaysOnTop = new SimpleBooleanProperty();
	}

	/**
	 * Creates a new instance of the TabPaneDetacher
	 *
	 * @return The new instance of the TabPaneDetacher.
	 */
	public static CooTabPaneDetacherUtil create()
	{
		return new CooTabPaneDetacherUtil();
	}

	public BooleanProperty alwaysOnTopProperty()
	{
		return alwaysOnTop;
	}

	public Boolean isAlwaysOnTop()
	{
		return alwaysOnTop.get();
	}

	/**
	 * 
	 * Sets whether detached Tabs should be always on top.
	 * 
	 * @param alwaysOnTop The state to be set.
	 * @return The current TabPaneDetacher instance.
	 */
	public CooTabPaneDetacherUtil alwaysOnTop(boolean alwaysOnTop)
	{
		alwaysOnTopProperty().set(alwaysOnTop);
		return this;
	}

	/**
	 * Make all added {@link Tab}s of the given {@link TabPane} detachable.
	 *
	 * @param tabPane The {@link TabPane} to take over.
	 * @return The current TabPaneDetacher instance.
	 */
	public CooTabPaneDetacherUtil makeTabsDetachable(TabPane tabPane)
	{
		this.tabPane = tabPane;
		originalTabs.addAll(tabPane.getTabs());
		for(int i = 0; i < tabPane.getTabs().size(); i++)
		{
			tapTransferMap.put(i, tabPane.getTabs().get(i));
		}
		tabPane.getTabs().stream().forEach(t ->
		{
			t.setClosable(false);
		});
		tabPane.setOnDragDetected(
			(MouseEvent event) ->
			{
				if(event.getSource() instanceof TabPane)
				{
					Pane rootPane = (Pane)tabPane.getScene().getRoot();
					rootPane.setOnDragOver((DragEvent event1) ->
					{
						event1.acceptTransferModes(TransferMode.ANY);
						event1.consume();
					});
					currentTab = tabPane.getSelectionModel().getSelectedItem();
					SnapshotParameters snapshotParams = new SnapshotParameters();
					snapshotParams.setTransform(Transform.scale(0.4, 0.4));
					WritableImage snapshot = currentTab.getContent().snapshot(
						snapshotParams, null);
					Dragboard db = tabPane.startDragAndDrop(TransferMode.MOVE);
					ClipboardContent clipboardContent = new ClipboardContent();
					clipboardContent.put(DataFormat.PLAIN_TEXT, "");
					db.setDragView(snapshot, 40, 40);
					db.setContent(clipboardContent);
				}
				event.consume();
			});
		tabPane.setOnDragDone(
			(DragEvent event) ->
			{
				openTabInStage(currentTab);
				tabPane.setCursor(Cursor.DEFAULT);
				event.consume();
			});
		return this;
	}

	/**
	 * Opens the content of the given {@link Tab} in a separate Stage. While the
	 * content is removed from the {@link Tab} it is
	 * added to the root of a new {@link Stage}. The Window title is set to the
	 * name of the {@link Tab};
	 *
	 * @param tab The {@link Tab} to get the content from.
	 */
	public void openTabInStage(final Tab tab)
	{
		if(tab == null)
		{
			return;
		}
		int originalTab = originalTabs.indexOf(tab);
		tapTransferMap.remove(originalTab);
		Pane content = (Pane)tab.getContent();
		if(content == null)
		{
			throw new IllegalArgumentException("Can not detach Tab '"
												+ tab.getText()
												+ "': content is empty (null).");
		}
		tab.setContent(null);
		final Scene scene = new Scene(content, content.getPrefWidth(),
			content.getPrefHeight());
		scene.getStylesheets().addAll(stylesheets);
		Stage stage = new Stage();
		stage.setScene(scene);
		scene.getStylesheets().addAll(
			tab.getTabPane().getScene().getStylesheets());
		stage.setTitle(tab.getText());
		stage.setAlwaysOnTop(isAlwaysOnTop());
		stage.getIcons().add(CooFileUtil.
			getResourceIcon("Logo.png"));
		Robot r = Application.GetApplication().createRobot();
		stage.setX(r.getMouseX());
		stage.setY(r.getMouseY());
		stage.setOnCloseRequest(t ->
		{
			stage.close();
			tab.setContent(content);
			int originalTabIndex = originalTabs.indexOf(tab);
			tapTransferMap.put(originalTabIndex, tab);
			int index = 0;
			SortedSet<Integer> keys = new TreeSet<>(tapTransferMap.keySet());
			for(Integer key : keys)
			{
				Tab value = tapTransferMap.get(key);
				if(!tabPane.getTabs().contains(value))
				{
					tabPane.getTabs().add(index, value);
				}
				index++;
			}
			tabPane.getSelectionModel().select(tab);
		});
		stage.setOnShown(t ->
		{
			tab.getTabPane().getTabs().remove(tab);
		});
		stage.show();
	}

}