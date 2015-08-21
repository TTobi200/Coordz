/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.sett;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.prefs.*;

import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.web.*;
import javafx.stage.*;

import com.sun.webkit.WebPage;

import de.util.CooFileUtil;

public class CooSettingsDialog extends Stage
{
	public enum SettingType
	{
		TEXT, BOOLEAN, COMBO
	};

	public static final String FXML = "CooSettingsDialog.fxml";

	private static final double WIDTH = 750;
	private static final double HEIGHT = 450;

	@FXML
	protected TreeView<String> treeView;
	@FXML
	protected StackPane stackPane;
	@FXML
	protected Label lblSettName;
	@FXML
	protected TextField txtSearch;
	@FXML
	protected Button btnCancel;

	protected WebView webView;
	protected Map<String, CooSettingsGroup> groups;
	protected Preferences settings;
	
	private ObjectProperty<EventHandler<ActionEvent>> onCancel = 
		new ObjectPropertyBase<EventHandler<ActionEvent>>()
	{
		@Override
		public Object getBean()
		{
			return CooSettingsDialog.this;
		}

		@Override
		public String getName()
		{
			return "onCancel";
		}
	};

	private ObjectProperty<EventHandler<ActionEvent>> onSave = 
		new ObjectPropertyBase<EventHandler<ActionEvent>>()
	{
		@Override
		public Object getBean()
		{
			return CooSettingsDialog.this;
		}

		@Override
		public String getName()
		{
			return "onSave";
		}
	};
	
	public CooSettingsDialog(Stage owner, String name, Image logo)
	{
		try
		{
			init(owner, name, logo);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private void init(Stage owner, String name, Image logo) throws IOException
	{
		Parent root = CooFileUtil.loadFXML(
			this, CooFileUtil.FXML_FOLDER +
					CooFileUtil.IN_JAR_SEPERATOR + FXML);

		groups = new HashMap<String, CooSettingsGroup>();
		TreeItem<String> rootItm = new TreeItem<String>(name);

		setScene(new Scene(root));
		getIcons().add(logo);
		setTitle(new StringBuilder(name).append(
			" - Einstellungen").toString());
		setWidth(WIDTH);
		setHeight(HEIGHT);
		initModality(Modality.WINDOW_MODAL);
		initOwner(owner);
		setX(owner.getX() + owner.getWidth() /
			2 - WIDTH / 2);
		setY(owner.getY() + owner.getHeight() /
			2 - HEIGHT / 2);

		rootItm.setExpanded(true);
		treeView.setRoot(rootItm);
		treeView.getSelectionModel()
			.selectedItemProperty()
			.addListener((o, c, n) -> selectionChanged(n));
		
		showingProperty().addListener((o, c, n) -> 
			createTitlePage(name));
		
		try
		{
			load(Preferences.userRoot().node(name));
		}
		catch(BackingStoreException e)
		{
			e.printStackTrace();
		}
	}

	protected void createTitlePage(String name)
	{
		webView = new WebView();
		final WebEngine webEngine = webView.getEngine();
		final StringBuilder html = new StringBuilder(
			"<body style='background : rgba(0,0,0,0);'><font face=\"arial\">");
		html.append("<h1>")
			.append(name)
			.append(" - Einstellungen")
			.append("</h1>")
			.append("Hier finden Sie alle Einstellungen für ")
			.append(name)
			.append(". Konfigurierbar sind folgende Dinge der Software:")
			.append("<ul>");
		groups.keySet().forEach(
			k -> html.append("<li>").append(k).append("</li>"));
		html.append("</ul>")
			.append(
				"Wählen Sie nun links die gewünschten Einstellungen aus, oder benutzen Sie "
								+ "einfach die Suche um schnell an den gewünschten Parameter zu gelangen.")
			.append(
				"<br><br>Zum Schluss klicken Sie dann auf Speichern um die Änderungen zu übernehmen.")
			.append("</body>");

		webEngine.loadContent(html.toString());
		webView.setDisable(true);

		webEngine.documentProperty()
			.addListener(l ->
			{
				try
				{
					// Use reflection to retrieve the WebEngine's private
					// 'page' field.
					Field f = webEngine.getClass().getDeclaredField("page");
					f.setAccessible(true);
					WebPage page = (WebPage)f.get(webEngine);

					page.setBackgroundColor((new Color(0, 0, 0, 0)).getRGB());
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			});
		stackPane.getChildren().add(webView);
	}

	public Property<?> addSetting(String group, String name,
					SettingType type, String... values)
	{
		CooSettingsGroup settGroup = groups.containsKey(group) ?
						groups.get(group) : addGroup(group);

		return settGroup.addSetting(name, type, values);
	}

	protected CooSettingsGroup addGroup(String group)
	{
		if(groups.containsKey(group))
		{
			return groups.get(group);
		}

		TreeItem<String> newItm = new TreeItem<String>(group);
		CooSettingsGroup settGroup = new CooSettingsGroup(treeView, newItm);
		treeView.getRoot().getChildren().add(newItm);

		groups.put(group, settGroup);
		return settGroup;
	}

	protected void load(Preferences settings) throws BackingStoreException
	{
		this.settings = settings;
		for(String name : settings.childrenNames())
		{
			Preferences node = settings.node(name);
			CooSettingsGroup group = addGroup(name);

			Arrays.asList(node.keys()).forEach(
				k -> group.setFieldValue(k, node.get(k, "")));

			groups.put(name, group);
		}
	}

	@FXML
	protected void save()
	{
		groups.keySet().forEach(name ->
			groups.get(name).save(name, settings));
		getOnSave().handle(new ActionEvent());
	}

	@FXML
	protected void cancel()
	{
		getOnCancle().handle(new ActionEvent());
	}

	@FXML
	protected void search()
	{
		String text = txtSearch.getText();
		if(Objects.nonNull(text))
		{
			groups.values().forEach(g ->
			{
				g.searchField(text);
			});

			treeView.getSelectionModel().select(1);
		}
	}
	
	protected void selectionChanged(TreeItem<String> n)
	{
		// Always clear the settings pane
		clearSetting();

		if(Objects.nonNull(n) && treeView.getRoot().equals(n))
		{
			stackPane.getChildren().add(webView);
			return;
		}

		String name = n.getValue();
		lblSettName.setText(name);
		lblSettName.setGraphic(n.getGraphic());
		stackPane.getChildren().add(
			groups.get(name).getGridFields());
	}

	protected void clearSetting()
	{
		stackPane.getChildren().clear();
		lblSettName.setText(null);
		lblSettName.setGraphic(null);
	}
	
	public final ObjectProperty<EventHandler<ActionEvent>> onCancelProperty()
	{
		return onCancel;
	}

	public final EventHandler<ActionEvent> getOnCancle()
	{
		return onCancelProperty().get();
	}

	public final void setOnCancel(EventHandler<ActionEvent> value)
	{
		onCancelProperty().set(value);
	}

	public final ObjectProperty<EventHandler<ActionEvent>> onSaveProperty()
	{
		return onSave;
	}

	public final EventHandler<ActionEvent> getOnSave()
	{
		return onSaveProperty().get();
	}

	public final void setOnSave(EventHandler<ActionEvent> value)
	{
		onSaveProperty().set(value);
	}
}