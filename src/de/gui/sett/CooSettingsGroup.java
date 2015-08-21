/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.sett;
import java.util.*;
import java.util.prefs.Preferences;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import de.gui.sett.CooSettingsDialog.SettingType;

public class CooSettingsGroup
{
	private TreeView<String> tree;
	private TreeItem<String> treeItm;
	private Map<String, Node> fields;
	private Map<String, Property<?>> props;
	private GridPane gridFields;
	private int row = 1;

	public CooSettingsGroup(TreeView<String> tree, TreeItem<String> treeItm)
	{
		this.treeItm = treeItm;
		this.tree = tree;
		fields = new HashMap<String, Node>();
		props = new HashMap<String, Property<?>>();
		gridFields = new GridPane();
		gridFields.setHgap(5d);
		gridFields.setVgap(5d);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setFieldValue(String field, String value)
	{
		Node n = fields.get(field);

		if(n instanceof TextField)
		{
			((TextField)n).setText(value);
		}
		else if(n instanceof CheckBox)
		{
			((CheckBox)n).setSelected(Boolean.valueOf(value));
		}
		else if(n instanceof ComboBox<?>)
		{
			((ComboBox)n).getSelectionModel().select(value);
		}
	}

	public void save(String name, Preferences settings)
	{
		Preferences node = settings.node(name);
		fields.keySet().forEach(f ->
		{
			// Get the field for name
			Node n = fields.get(f);
			// Get the property for field
			Property<?> prop = props.get(f);

			if(n instanceof TextField)
			{
				// TODO maybe support putInt, Double etc by parsing text
				String txt = ((TextField)n).getText();
				node.put(f, Objects.isNull(txt) ? "" : txt);
				((SimpleStringProperty)prop).setValue(txt);
			}
			else if(n instanceof CheckBox)
			{
				boolean selected = ((CheckBox)n).isSelected();
				node.put(f, String.valueOf(selected));
				((SimpleBooleanProperty)prop).setValue(selected);
			}
			else if(n instanceof ComboBox<?>)
			{
				Object value = ((ComboBox<?>)n).getValue();

				node.put(f, Objects.nonNull(value) ?
								String.valueOf(value) : "");

				if(Objects.nonNull(value))
				{
					((SimpleStringProperty)prop).setValue(
						String.valueOf(value));
				}
			}
		});
	}

	public void searchField(String text)
	{
		ObservableList<TreeItem<String>> children =
						tree.getRoot().getChildren();
		boolean matches = fields.containsKey(text);

		for(String f : fields.keySet())
		{
			if(f.toLowerCase().matches("(.*)" +
				text.toLowerCase() + "(.*)"))
			{
				matches = true;
				break;
			}
		}

		if(text.isEmpty())
		{
			fields.values().forEach(f ->
				setFieldBorder(f, false));
		}

		if(text.isEmpty() || matches)
		{
			if(!children.contains(treeItm))
			{
				children.add(treeItm);
			}

			setFieldBorder(fields.get(text), matches);
			return;
		}

		children.remove(treeItm);
	}

	private void setFieldBorder(Node node, boolean visible)
	{
		if(Objects.nonNull(node))
		{
			node.setStyle(visible ? "-fx-border-color: red" : null);
		}
	}

	public void addSetting(String name, SettingType type)
	{
		addSetting(name, type, new String[0]);
	}

	public Property<?> addSetting(String name, SettingType type,
					String... values)
	{
		Node sett = null;
		Property<?> prop = null;
		String value = values.length > 0 ? values[0] : "";

		switch(type)
		{
			case BOOLEAN:
				boolean initValue = Boolean.valueOf(value);
				prop = new SimpleBooleanProperty(initValue);
				sett = new CheckBox();
				((CheckBox)sett).setSelected(initValue);
				break;
			case COMBO:
				prop = new SimpleStringProperty();
				sett = new ComboBox<String>(FXCollections.observableArrayList(
					Arrays.asList(values)));
				((ComboBox<?>)sett).setMaxWidth(
					Double.POSITIVE_INFINITY);
				break;
			case TEXT:
				prop = new SimpleStringProperty(value);
				sett = new TextField(value);
				break;
		}

		if(Objects.nonNull(sett))
		{
			gridFields.add(new Label(name), 1, row); // column=1
			gridFields.add(sett, 2, row); // column=2
			GridPane.setHgrow(sett, Priority.ALWAYS);

			fields.put(name, sett);
			props.put(name, prop);
			row++;
		}

		return prop;
	}

	public TreeItem<String> getTreeItm()
	{
		return treeItm;
	}

	public Map<String, Node> getFields()
	{
		return fields;
	}

	public Map<String, Property<?>> getProps()
	{
		return props;
	}

	public GridPane getGridFields()
	{
		return gridFields;
	}
}