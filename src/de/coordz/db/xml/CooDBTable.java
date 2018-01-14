/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db.xml;

import static de.util.CooXmlDomUtil.*;

import java.util.*;

import org.w3c.dom.*;

import de.coordz.CooSystem;
import de.coordz.db.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

public class CooDBTable implements CooDBXML, CooDBSQL
{
	protected StringProperty pKey;
	protected StringProperty fKey;
	protected ObjectProperty<CooDBValTypes> pKeyType;
	protected ObjectProperty<CooDBValTypes> fKeyType;
	protected StringProperty name;
	protected StringProperty comment;
	
	protected List<CooDBColumn> columns;
	
	public CooDBTable()
	{
		comment = new SimpleStringProperty();
		name = new SimpleStringProperty();
		pKey = new SimpleStringProperty();
		fKey = new SimpleStringProperty();
		pKeyType = new SimpleObjectProperty<>();
		fKeyType = new SimpleObjectProperty<>();
		columns = FXCollections.observableArrayList();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		
	}

	@Override
	public void fromXML(Element table)
	{
		if(Objects.nonNull(table))
		{
			name.set(table.getAttribute("NAME"));
			
			// Get the primary key and its type
			Element pKey = getSingleElement(table, "PKEY");
			this.pKey.set(pKey.getAttribute("VALUE"));
			this.pKeyType.set(CooDBValTypes.valueOf(pKey.getAttribute("TYPE")));
			
			// Get the foreign key and its type
			Element fKey = getSingleElement(table, "FKEY");
			Boolean fKeyPresent = Objects.nonNull(fKey);
			this.fKey.set(fKeyPresent ? fKey.getAttribute("VALUE") : null);
			this.fKeyType.set(fKeyPresent ? CooDBValTypes.valueOf(fKey.getAttribute("TYPE")) : null);
			
			// Get the comment
			Element comment = getSingleElement(table, "COMMENT");
			this.comment.set(comment.getAttribute("VALUE"));
			
			// Load all columns
			Element columns = getSingleElement(table, "COLUMNS");
			addToList("COLUMN", columns,
					CooDBColumn.class, this.columns);
		}
	}
	
	@Override
	public String toSQL()
	{
		StringBuilder stmt = new StringBuilder("CREATE TABLE ");
		stmt.append(name.get());
		stmt.append(" (");
		for(int i = 0; i < columns.size(); i++)
		{
			CooDBColumn column  = columns.get(i);
			CooDBValTypes type = column.typeProperty().get();
			
			// Escape the column name - DB specified
			stmt.append(CooSystem.getDatabase().escapeColumn(
				column.nameProperty().get().toUpperCase()))
				.append(" ")
				// Get the specified type for actual used database
				.append(CooSystem.getDatabase().getDataType(type, column.nameProperty().get()))
				.append(type.equals(CooDBValTypes.VARCHAR) ? 
					("(" + column.lengthProperty().get() + ")") : "")
				.append(column.nullableProperty().not().get() ?
					" NOT NULL" : "")
				.append(column.nameProperty().get().equals(pKey.get()) ?
					" PRIMARY KEY" : "")
				.append(i < (columns.size() -1) ? ", " : "");
		}
		stmt.append(")");
		
		return stmt.toString();
	}

	public StringProperty nameProperty()
	{
		return name;
	}
	
	public StringProperty pKeyProperty()
	{
		return pKey;
	}
	
	public StringProperty fKeyProperty()
	{
		return fKey;
	}
	
	public ObjectProperty<CooDBValTypes> pKeyTypeProperty()
	{
		return pKeyType;
	}
	
	public ObjectProperty<CooDBValTypes> fKeyTypeProperty()
	{
		return fKeyType;
	}
	
	public StringProperty commentProperty()
	{
		return comment;
	}
	
	public List<CooDBColumn> getColumns()
	{
		return columns;
	}
}