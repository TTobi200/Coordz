/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2014 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.util.*;

import javafx.beans.property.Property;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import de.util.CooGuiUtil;

public class CooDialogs
{
	protected CooDialogs()
	{
		// Not visible
	}

	public static boolean showConfirmDialog(Window owner, String head,
					String msg)
	{
		return showDialog(owner, AlertType.CONFIRMATION, head, msg).get()
			.equals(ButtonType.OK);
	}

	public static Optional<ButtonType> showInfoDialog(Window owner,
					String head, String msg)
	{
		return showDialog(owner, AlertType.INFORMATION, head, msg);
	}

	public static Optional<ButtonType> showWarnDialog(Window owner,
					String head, String msg)
	{
		return showDialog(owner, AlertType.WARNING, head, msg);
	}

	public static Optional<ButtonType> showErrorDialog(Window owner,
					String head, String msg)
	{
		return showDialog(owner, AlertType.ERROR, head, msg);
	}

	public static Optional<ButtonType> showDialog(Window owner, AlertType type,
					String head, String msg)
	{
		Alert dlg = new Alert(type, msg);
		dlg.setTitle(CooMainFrame.TITLE);
		CooGuiUtil.grayOutParent(owner,
			dlg.showingProperty());
		dlg.initOwner(owner);
		dlg.setHeaderText(head);

		return dlg.showAndWait();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void showEditTable(Window owner,
					TableView tblView, String head)
	{
		GridPane pane = new GridPane();
		pane.setHgap(5d);
		pane.setVgap(5d);
		ObservableList<TableColumn<?, ?>> columns =
						tblView.getColumns();
		for(int i = 0; i < columns.size(); i++)
		{
			Label lbl = new Label(columns.get(i).getText());
			TextField txt = new TextField();
			txt.setMinWidth(280);

			txt.textProperty().bindBidirectional(
				(Property<String>)
				((TableColumn<?, ?>)tblView.getColumns().get(i))
					.getCellObservableValue(tblView.getSelectionModel()
						.getSelectedIndex()));

			GridPane.setRowIndex(lbl, i);
			GridPane.setColumnIndex(lbl, 1);
			GridPane.setRowIndex(txt, i);
			GridPane.setColumnIndex(txt, 2);
			pane.getChildren().addAll(lbl, txt);
		}

		Alert dlg = new Alert(AlertType.INFORMATION);
		dlg.setTitle(CooMainFrame.TITLE);
		CooGuiUtil.grayOutParent(owner,
			dlg.showingProperty());
		dlg.initOwner(owner);
		dlg.setHeaderText(head);
		dlg.getDialogPane().setContent(pane);
		dlg.showAndWait();
	}

	public static <T> T showChooseDialog(Window owner, String header,
					String choiceText,
					HashMap<String, T> choices)
	{
		ChoiceDialog<String> dlg = new ChoiceDialog<String>(null,
			FXCollections.observableArrayList(choices.keySet()));

		dlg.getDialogPane().setContentText(choiceText);
		CooGuiUtil.grayOutParent(owner,
			dlg.showingProperty());
		dlg.initOwner(owner);
		dlg.setHeaderText(header);

		dlg.showAndWait();

		return choices.get(dlg.getResult());
	}
}