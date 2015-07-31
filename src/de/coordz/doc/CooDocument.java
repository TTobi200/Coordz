/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.doc;

import java.io.File;
import java.util.Objects;

import javafx.collections.*;

public abstract class CooDocument
{
	public enum Content
	{
		CUSTOMER, CONTACTS, PALETS, 
		PROJECT, 
		LAP_SOFTWARE, 
		STATIONS, 
		REGION_DIVIDING, LASER,
		MEASUREMENTS, RETICLES, TARGETS, 
		VERIFY_MEASUREMENT, SPECIFICATION, RESULT, 
		GATEWAY
	}

	protected ObservableList<CooDocument.Content> content;

	public abstract void save(File file);

	public CooDocument()
	{
		content = FXCollections.observableArrayList();
	}

	public void addContent(CooDocument.Content... content)
	{
		if(Objects.nonNull(content))
		{
			this.content.addAll(content);
		}
	}

	public ObservableList<CooDocument.Content> getContent()
	{
		return content;
	}
}