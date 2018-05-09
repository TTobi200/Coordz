/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2018 T.Ohm . All Rights Reserved.
 */
package de.gui.sett;
import java.util.*;
import java.util.prefs.Preferences;

import de.gui.sett.CooSettingsDialog.SettingType;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CooSettingsGroup
{
	private TreeView<String> tree;
	private TreeItem<String> treeItm;
	private Map<String, Node> fields;
	private Map<String, String> defaultValues;
	private Map<String, Property<?>> props;
	private GridPane gridFields;
	private int row = 1;

	public CooSettingsGroup(TreeView<String> tree, TreeItem<String> treeItm)
	{
		this.treeItm = treeItm;
		this.tree = tree;
		fields = new HashMap<>();
		props = new HashMap<>();
		defaultValues = new HashMap<>();
		gridFields = new GridPane();
		gridFields.setHgap(5d);
		gridFields.setVgap(5d);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setFieldValue(String field, String value)
	{
		Node n = fields.get(field);
		// Always prefer the preference value
		String defValue = defaultValues.get(field);
		value = Objects.isNull(value) ? defValue : value;
		
		Property p = props.get(field);

		if(n instanceof TextField)
		{
			((TextField)n).setText(value);
			((StringProperty)p).setValue(value);
		}
		else if(n instanceof CheckBox)
		{
			((CheckBox)n).setSelected(Boolean.valueOf(value));
			((BooleanProperty)p).set(Boolean.valueOf(value));
		}
		else if(n instanceof ComboBox<?>)
		{
			((ComboBox)n).getSelectionModel().select(value);
			((StringProperty)p).setValue(value);
		}
	}

	public void saveUI(String name, Preferences settings)
	{
		Preferences node = settings.node(name);
		
		// Save values from fields
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
	
	public void saveWithoutUI(String name, Preferences settings)
	{
		Preferences node = settings.node(name);
		
		// Save values from properties if
		// not edited by user interface
		props.keySet().forEach(n ->
		{
			// Get the property for name
			Property<?> p = props.get(n);
			// Store the props as string
			node.put(n, Objects.isNull(p.getValue()) ? "" : 
				String.valueOf(p.getValue()));
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
		String value = "";
		
		if(values.length > 0)
		{
			value = values[0];
			defaultValues.put(name, value);
		}
		
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
				sett = new ComboBox<>(FXCollections.observableArrayList(
					Arrays.asList(values)));
				((ComboBox<?>)sett).setMaxWidth(
					Double.POSITIVE_INFINITY);
				((ComboBox<?>)sett).getSelectionModel().selectFirst();
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