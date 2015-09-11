/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2014 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.converter.*;

import org.controlsfx.control.*;

import de.coordz.data.*;
import de.coordz.doc.*;
import de.coordz.doc.CooDocument.Content;
import de.coordz.lap.CooLAPClient;
import de.util.*;
import de.util.log.CooLog;

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
	
	public static void showExceptionDialog(Window owner, String msg, Exception e) 
	{
		// First log the exception
		CooLog.error(msg, e);
		
		Alert dlg = new Alert(AlertType.ERROR);
		dlg.setTitle(CooMainFrame.TITLE);
		CooGuiUtil.grayOutParent(owner,
			dlg.showingProperty());
		dlg.initOwner(owner);
		dlg.setHeaderText("Exception aufgetreten");
		
		BorderPane pane = new BorderPane();
		BorderPane head = new BorderPane();
		Button btnShowStack = new Button("V");
		Label lblExMsg = new Label(msg);
		TextArea txtStack = new TextArea();
		txtStack.setEditable(false);
		txtStack.setVisible(false);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos, true);
		e.printStackTrace(ps);
		ps.close();
		
		txtStack.appendText(baos.toString());
		txtStack.selectPositionCaret(0);
		txtStack.deselect();
		
		btnShowStack.setOnAction(hdl -> 
		{
			boolean stackVisible = txtStack.isVisible();
			txtStack.setVisible(!stackVisible);
			btnShowStack.setText(stackVisible ? "V" : "^");
			dlg.setHeight(stackVisible ? 150 : 400);
		});
		
		head.setLeft(lblExMsg);
		head.setRight(btnShowStack);
		
		pane.setTop(head);
		pane.setCenter(txtStack);
		
		dlg.getDialogPane().setContent(pane);
		dlg.show();
		dlg.setHeight(150);
	}
	
	public static void showProgressDialog(Window owner, String head, Task<Void> task)
	{
		Alert dlg = new Alert(AlertType.INFORMATION);
		dlg.setTitle(CooMainFrame.TITLE);
		CooGuiUtil.grayOutParent(owner,
			dlg.showingProperty());
		dlg.initOwner(owner);
		dlg.setHeaderText(head);
		
		VBox root = new VBox();
		root.setAlignment(Pos.TOP_CENTER);
		final ProgressBar progress = new ProgressBar();
		progress.prefWidthProperty().bind(root.widthProperty());
		root.getChildren().add(progress);
		
		BorderPane pane = new BorderPane();
		Label lblMsg = new Label();
		lblMsg.textProperty().bind(task.messageProperty());
		progress.progressProperty().bind(task.progressProperty());
		pane.setTop(lblMsg);
		pane.setCenter(root);
		
		dlg.getDialogPane().setContent(pane);
		dlg.show();
		task.run();
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
			Node n = new TextField();

			Property p = (Property)((TableColumn<?, ?>)tblView.getColumns()
				.get(i))
				.getCellObservableValue(tblView.getSelectionModel()
					.getSelectedIndex());

			if(p instanceof IntegerProperty)
			{
				((TextField)n).textProperty().bindBidirectional(
					p, new NumberStringConverter());
			}
			else if(p instanceof DoubleProperty || p instanceof FloatProperty)
			{
				((TextField)n).textProperty().bindBidirectional(
					p, new DoubleStringConverter());
			}
			else if(p instanceof StringProperty)
			{
				((TextField)n).textProperty().bindBidirectional(p);
			}
			else if(p instanceof BooleanProperty)
			{
				n = new CheckBox();
				((CheckBox)n).selectedProperty().bindBidirectional(p);
			}
			else if(p instanceof ObjectProperty<?> 
				&& p.getValue() instanceof LocalDate)
			{
				n = new DatePicker((LocalDate)p.getValue());
				((DatePicker)n).setEditable(false);
				((DatePicker)n).getEditor().textProperty().bindBidirectional(p, 
					new LocalDateStringConverter());
			}
			
			if(n instanceof Region)
			{
				((Region)n).setMinWidth(280);
			}

			GridPane.setRowIndex(lbl, i);
			GridPane.setColumnIndex(lbl, 1);
			GridPane.setRowIndex(n, i);
			GridPane.setColumnIndex(n, 2);
			pane.getChildren().addAll(lbl, n);
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
	
	public static CooLAPClient showConnectToLAPSoft(Window owner)
	{
		GridPane pane = new GridPane();
		pane.setHgap(5d);
		pane.setVgap(5d);
		Label lblIP = new Label("Ip:");
		TextField txtIp = new TextField();
		Label lblPort = new Label("Port:");
		TextField txtPort = new TextField();
		txtIp.setMinWidth(280);
		txtIp.setPromptText(CooLAPClient.DEF_LAP_SOFTWARE_IP);
		txtPort.setMinWidth(280);
		txtPort.setPromptText(String.valueOf(CooLAPClient.LAP_SOFTWARE_PORT));

		GridPane.setRowIndex(lblIP, 1);
		GridPane.setColumnIndex(lblIP, 1);
		GridPane.setRowIndex(txtIp, 1);
		GridPane.setColumnIndex(txtIp, 2);
		
		GridPane.setRowIndex(lblPort, 2);
		GridPane.setColumnIndex(lblPort, 1);
		GridPane.setRowIndex(txtPort, 2);
		GridPane.setColumnIndex(txtPort, 2);
		pane.getChildren().addAll(lblIP, txtIp,lblPort, txtPort);

		Alert dlg = new Alert(AlertType.CONFIRMATION);
		dlg.setTitle(CooMainFrame.TITLE);
		CooGuiUtil.grayOutParent(owner,
			dlg.showingProperty());
		dlg.initOwner(owner);
		dlg.setHeaderText("LAP ProSoft Server");
		dlg.getDialogPane().setContent(pane);
		dlg.showAndWait();
		
		CooLAPClient client = null;
		if(dlg.getResult().equals(ButtonType.OK))
		{
			try
			{
				client = new CooLAPClient(txtIp.getText(), 
					Integer.valueOf(txtPort.getText()));
			}
			catch(Exception e)
			{
				showErrorDialog(owner, "Fehler beim Verbinden",
					e.getMessage());
				CooLog.error("Could not connect to LAP-Software", e);
			}
		}
		
		return client;
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