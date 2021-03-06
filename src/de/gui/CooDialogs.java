/*
 * $Header$
 * 
 * $Log$
 * Copyright � 2014 T.Ohm . All Rights Reserved.
 */
package de.gui;

import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import org.controlsfx.control.*;

import de.coordz.data.*;
import de.coordz.db.*;
import de.coordz.db.xml.CooDBModel;
import de.coordz.doc.CooDocument;
import de.coordz.doc.CooDocument.Content;
import de.coordz.lap.CooLAPClient;
import de.util.*;
import de.util.log.CooLog;
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
				&& p.getValue() instanceof LocalDate | Objects.isNull(p.getValue()))
			{
				n = new DatePicker((LocalDate)p.getValue());
				((DatePicker)n).setEditable(false);
				((DatePicker)n).getEditor().textProperty().bindBidirectional(p, 
					new LocalDateStringConverter());
			}
			else if(p instanceof ObjectProperty<?> 
				&& p.getValue() instanceof Timestamp | Objects.isNull(p.getValue()))
			{
				n = new DatePicker(((Timestamp)p.getValue()).toLocalDateTime().toLocalDate());
				((DatePicker)n).setEditable(false);
				((DatePicker)n).getEditor().textProperty().bindBidirectional(p, 
					new CooTimestampStringConverter());
			}
			else if(p instanceof ObjectProperty<?> 
				&& p.getValue() instanceof CooPaletType | Objects.isNull(p.getValue()))
			{
				n = new ComboBox<>(FXCollections.observableArrayList(
					CooPaletType.values()));
				((ComboBox)n).getSelectionModel().select(p.getValue());
				((ComboBox)n).getSelectionModel()
					.selectedItemProperty()
					.addListener((obs, old, newV) -> 
					{
						p.setValue(newV);
					});
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
	
	public static CooDB showConnectToDB(Window owner)
	{
		CooDBModel model = showDBModel(owner);
		
		CooDB database = null;
		if(Objects.nonNull(model))
		{
			try
			{
				database = model.dbTypeProperty().get()
					.getInstance().newInstance();
				database.connect(
					model.dbHostProperty().get(),
					model.dbPortProperty().get(),
					model.dbNameProperty().get(),
					model.dbUserProperty().get(),
					model.dbPasswordProperty().get(),
					Boolean.FALSE);
			}
			catch(Exception e)
			{
				showExceptionDialog(owner, 
					"Fehler beim Verbinden", e);
			}
		}
		
		return database;
	}
	
	public static CooDBModel showDBModel(Window owner)
	{
		GridPane pane = new GridPane();
		pane.setHgap(5d);
		pane.setVgap(5d);
		Label lblType = new Label("Type:");
		ComboBox<CooDBTypes> cbType = new ComboBox<>(
			FXCollections.observableArrayList(CooDBTypes.values()));
		Label lblHost = new Label("Host:");
		TextField txtHost = new TextField();
		Label lblPort = new Label("Port:");
		TextField txtPort = new TextField();
		Label lblName = new Label("Name:");
		TextField txtName = new TextField();
		Label lblUser = new Label("Benutzer:");
		TextField txtUser = new TextField();
		Label lblPassword = new Label("Passwort:");
		PasswordField txtPasword = new PasswordField();
		
		cbType.setMinWidth(280);
		txtHost.setMinWidth(280);
		txtPort.setMinWidth(280);
		txtName.setMinWidth(280);
		txtUser.setMinWidth(280);
		txtPasword.setMinWidth(280);

		GridPane.setRowIndex(lblType, 1);
		GridPane.setRowIndex(cbType, 1);
		GridPane.setRowIndex(lblHost, 2);
		GridPane.setRowIndex(txtHost, 2);
		GridPane.setRowIndex(lblPort, 3);
		GridPane.setRowIndex(txtPort, 3);
		GridPane.setRowIndex(lblName, 4);
		GridPane.setRowIndex(txtName, 4);
		GridPane.setRowIndex(lblUser, 5);
		GridPane.setRowIndex(txtUser, 5);
		GridPane.setRowIndex(lblPassword, 6);
		GridPane.setRowIndex(txtPasword, 6);

		GridPane.setColumnIndex(lblType, 1);
		GridPane.setColumnIndex(cbType, 2);
		GridPane.setColumnIndex(lblHost, 1);
		GridPane.setColumnIndex(txtHost, 2);
		GridPane.setColumnIndex(lblPort, 1);
		GridPane.setColumnIndex(txtPort, 2);
		GridPane.setColumnIndex(lblName, 1);
		GridPane.setColumnIndex(txtName, 2);
		GridPane.setColumnIndex(lblUser, 1);
		GridPane.setColumnIndex(txtUser, 2);
		GridPane.setColumnIndex(lblPassword, 1);
		GridPane.setColumnIndex(txtPasword, 2);
		
		CooDBModel model = new CooDBModel();
		cbType.getSelectionModel().selectedItemProperty()
			.addListener((obs, old, newV) ->
		{
			// Get the sample configuration
			getDefaultConfig(model, cbType.getSelectionModel()
				.getSelectedItem());
			
//			txtHost.setPromptText(model.dbHostProperty().get());
//			txtPort.setPromptText(model.dbPortProperty().get());
//			txtName.setPromptText(model.dbNameProperty().get());
//			txtUser.setPromptText(model.dbUserProperty().get());
//			txtPasword.setPromptText(model.dbPasswordProperty().get());
			
			txtHost.textProperty().bind(model.dbHostProperty());
			txtPort.textProperty().bind(model.dbPortProperty());
			txtName.textProperty().bind(model.dbNameProperty());
			txtUser.textProperty().bind(model.dbUserProperty());
			txtPasword.textProperty().bind(model.dbPasswordProperty());
		});
		
		// Auto select the default SQL server
		cbType.getSelectionModel().select(CooDBTypes.SQLSERVER);
		
		pane.getChildren().addAll(lblType, cbType, lblHost, 
			txtHost, lblPort, txtPort, lblName, txtName, 
			lblUser, txtUser, lblPassword, txtPasword);

		Alert dlg = new Alert(AlertType.CONFIRMATION);
		dlg.setTitle(CooMainFrame.TITLE);
		CooGuiUtil.grayOutParent(owner,
			dlg.showingProperty());
		dlg.initOwner(owner);
		dlg.setHeaderText("Datenbank verbinden");
		dlg.getDialogPane().setContent(pane);
		dlg.showAndWait();
		
		return dlg.getResult().equals(ButtonType.OK) ? model : null;
	}
	
	private static CooDBModel getDefaultConfig(CooDBModel model,
		CooDBTypes selectedItem)
	{
		model.dbTypeProperty().set(selectedItem);
		model.dbCreateProperty().set(Boolean.FALSE);

		// TODO $TO: Add this to system settings
		switch(selectedItem)
		{
			case DERBY:
			case MARIADB:
			case MYSQL:
			default:
				model.dbHostProperty().set("localhost");
				model.dbPortProperty().set("3306");
				model.dbNameProperty().set("CoordzDB");
				model.dbUserProperty().set("Coordz");
				model.dbPasswordProperty().set("P4$$w0rd");
				break;
			case ORACLE:
				model.dbHostProperty().set("localhost");
				model.dbPortProperty().set("1521");
				model.dbNameProperty().set("XE");
				model.dbUserProperty().set("Coordz");
				model.dbPasswordProperty().set("P4$$w0rd");
				break;
			case SQLSERVER:
				model.dbHostProperty().set("UTSRV29\\SQLEXPRESS_12");
				model.dbPortProperty().set("1434");
				model.dbNameProperty().set("Coords");
				model.dbUserProperty().set(System.getProperty("user.name"));
				model.dbPasswordProperty().set("**********");
				break;
		}
		
		return model;
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
				String ip = txtIp.getText();
				String port = txtPort.getText();
				
				// Use default ip and port if not set
				ip = (Objects.nonNull(ip) && !ip.isEmpty()) ? 
					ip : CooLAPClient.DEF_LAP_SOFTWARE_IP;
				port = (Objects.nonNull(port) && !port.isEmpty()) ?
					port : String.valueOf(CooLAPClient.LAP_SOFTWARE_PORT);
				
				client = new CooLAPClient(ip, 
					Integer.valueOf(port));
			}
			catch(Exception e)
			{
				showExceptionDialog(owner, 
					"Fehler beim Verbinden", e);
			}
		}
		
		return client;
	}

	public static <T> T showChooseDialog(Window owner, String header,
					String choiceText,
					HashMap<String, T> choices)
	{
		ChoiceDialog<String> dlg = new ChoiceDialog<>(null,
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
	
	public static File showOpenFileDialog(Window parent, String title)
	{
		return showOpenFileDialog(parent, title, null);
	}
	
	public static File showOpenFileDialog(Window parent, 
			String title, ExtensionFilter fileFilter)
	{
		FileChooser dlg = new FileChooser();
		dlg.setTitle(title);
		dlg.getExtensionFilters().add(fileFilter);
		return dlg.showOpenDialog(parent);
	}

	public static File showOpenImageDialog(Window parent, String title)
	{
		FileChooser dlg = new FileChooser();
		dlg.setTitle(title);
		dlg.getExtensionFilters().add(new FileChooser.ExtensionFilter(
			"Bilddateien", "*." + CooXMLDBUtil.CUSTOMER_LOGO_PIC_TYPE));

		return dlg.showOpenDialog(parent);
	}

	public static File showSaveFileDialog(Window parent, 
		String title, ExtensionFilter fileFilter)
	{
		FileChooser dlg = new FileChooser();
		dlg.setTitle(title);
		dlg.getExtensionFilters().add(fileFilter);

		return dlg.showSaveDialog(parent);
	}

	public static File showSaveFileDialog(Window parent, String title)
	{
		return showSaveFileDialog(parent, title, null);
	}
	

	public static void showToDocDialog(Window owner, CooCustomer customer,
					CooDocument... document)
	{
		CheckBoxTreeItem<Content> root = new CheckBoxTreeItem<>(
			Content.CUSTOM.setName("Inhalt"));
		CheckTreeView<Content> checkTreeView = new CheckTreeView<>(root);
		Button btnBrowse = new Button("Browse");
		TextField txtOutFile = new TextField();
		BorderPane contentPane = new BorderPane();
		ComboBox<CooDocument> documents = new ComboBox<>();
		CheckComboBox<CooProject> projects = new CheckComboBox<>();

		documents.getSelectionModel().selectedItemProperty().addListener((old, curr, newV) -> 
		{
			checkTreeView.getCheckModel().getCheckedItems().clear();
			root.getChildren().clear();
			root.setSelected(false);
			newV.getAvailableContent()
			.stream()
			.filter(c -> !c.equals(root.getValue()))
			.forEach(
				c -> root.getChildren().add(
					new CheckBoxTreeItem<>(c)));
		});

		btnBrowse.setOnAction(e ->
		{
			File f = showSaveFileDialog(owner,
				"Dokument erstellen", documents.getSelectionModel()
					.selectedItemProperty().get().getFileFilter());
			
			txtOutFile.textProperty().set(Objects.nonNull(f)
				? f.getAbsolutePath() : "");
		});

		btnBrowse.disableProperty().bind(
			documents.getSelectionModel()
			.selectedItemProperty().isNull());

		documents.setPromptText("Ausgabeformat w�hlen...");
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
			checkTreeView.getCheckModel().getCheckedItems()
				.forEach(i -> selItm.addContent(i.getValue()));

			selItm.save(new File(txtOutFile.getText()), customer,
				selPrj.toArray(new CooProject[0]));
		}
	}
}