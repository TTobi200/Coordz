/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2014 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.io.File;
import java.util.*;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.converter.NumberStringConverter;

import org.controlsfx.control.*;

import de.coordz.data.*;
import de.coordz.doc.*;
import de.coordz.doc.CooDocument.Content;
import de.util.*;

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

			Property p = (Property)((TableColumn<?, ?>)tblView.getColumns()
				.get(i))
				.getCellObservableValue(tblView.getSelectionModel()
					.getSelectedIndex());

			if(p instanceof IntegerProperty || p instanceof DoubleProperty
				|| p instanceof FloatProperty)
			{
				txt.textProperty().bindBidirectional(
					p, new NumberStringConverter());
			}
			else if(p instanceof StringProperty)
			{
				txt.textProperty().bindBidirectional(p);
			}

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

	public static File showOpenFolderDialog(Stage parent, String title)
	{
		DirectoryChooser dlg = new DirectoryChooser();
		dlg.setTitle(title);
		return dlg.showDialog(parent);
	}

	public static File showOpenImageDialog(Window parent, String title)
	{
		FileChooser dlg = new FileChooser();
		dlg.setTitle(title);
		dlg.getExtensionFilters().add(new FileChooser.ExtensionFilter(
			"Bilddateien", "*." + CooXMLDBUtil.CUSTOMER_LOGO_PIC_TYPE));

		return dlg.showOpenDialog(parent);
	}

	private static void showSaveFileDialog(Window parent, String title,
					StringProperty strProp, ExtensionFilter fileFilter)
	{
		FileChooser dlg = new FileChooser();
		dlg.setTitle(title);
		dlg.getExtensionFilters().add(fileFilter);

		File file = dlg.showSaveDialog(parent);

		strProp.set(Objects.nonNull(file) ? file
			.getAbsolutePath() : "");
	}

	public static void showSaveFileDialog(Window parent, String title,
					StringProperty strProp)
	{
		showSaveFileDialog(parent, title, strProp, null);
	}

	public static void showToDocDialog(Window owner, CooCustomer customer,
					CooDocument... document)
	{
		CheckBoxTreeItem<Content> root = new CheckBoxTreeItem<Content>(
			Content.CUSTOM.setName("Inhalt"));
		CheckTreeView<Content> checkTreeView = new CheckTreeView<>(root);
		Button btnBrowse = new Button("Browse");
		TextField txtOutFile = new TextField();
		BorderPane contentPane = new BorderPane();
		ComboBox<CooDocument> documents = new ComboBox<CooDocument>();
		CheckComboBox<CooProject> projects = new CheckComboBox<CooProject>();

		documents.getSelectionModel().selectedItemProperty().addListener((old, curr, newV) -> 
		{
			newV.getAvailableContent()
			.stream()
			.filter(c -> !c.equals(root.getValue()))
			.forEach(
				c -> root.getChildren().add(
					new CheckBoxTreeItem<CooDocument.Content>(c)));
		});

		btnBrowse.setOnAction(e ->
		{
			showSaveFileDialog(owner,
				"Dokument erstellen", txtOutFile.textProperty(), documents.getSelectionModel()
					.selectedItemProperty().get().getFileFilter());
		});

		btnBrowse.disableProperty().bind(
			documents.getSelectionModel()
			.selectedItemProperty().isNull());

		documents.setPromptText("Ausgabeformat wählen...");
		documents.getItems().addAll(document);
		documents.setMaxWidth(Double.POSITIVE_INFINITY);
		projects.getItems().addAll(customer.getProjects());
		
		HBox.setHgrow(txtOutFile, Priority.ALWAYS);
		txtOutFile.setDisable(true);

		contentPane.setCenter(checkTreeView);
		contentPane.setTop(documents);
		contentPane.setBottom(new VBox(projects, new HBox(
			txtOutFile, btnBrowse)));
		root.setExpanded(true);

		Alert dlg = new Alert(AlertType.INFORMATION);
		dlg.setTitle(CooMainFrame.TITLE);
		CooGuiUtil.grayOutParent(owner,
			dlg.showingProperty());
		dlg.initOwner(owner);
		dlg.setHeaderText("\"" + customer.nameProperty().get() 
			+"\" exportieren");
		dlg.getDialogPane().setContent(contentPane);
		checkTreeView.setMaxHeight(150);
		checkTreeView.setMaxWidth(450);
		dlg.showAndWait();

		CooDocument selItm = documents.getSelectionModel()
			.selectedItemProperty()
			.get();
		ObservableList<CooProject> selPrj = projects.getCheckModel()
			.getCheckedItems();

		if(!selPrj.isEmpty() && Objects.nonNull(selItm)
			&& !txtOutFile.getText().isEmpty())
		{
			CooPdfDocument pdf = new CooPdfDocument();
			checkTreeView.getCheckModel().getCheckedItems()
				.forEach(i -> pdf.addContent(i.getValue()));

			pdf.save(new File(txtOutFile.getText()), customer,
				selPrj.toArray(new CooProject[0]));
		}
	}
}