/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.doc;

import java.io.File;
import java.util.*;

import javafx.collections.*;
import javafx.stage.FileChooser;
import de.coordz.data.*;

public abstract class CooDocument
{
	public enum Content
	{
		CUSTOM(""),
		
		TITLE_PAGE("Titelseite"),
		HEADER_FOOTER("Header und Footer"),
		
		CUSTOMER("Kunde"), CONTACTS("Kontakte"), PALETS("Paletten"), 
		
		PROJECT("Projekte"), 
		LAP_SOFTWARE("LAP Software"), 
		STATIONS("Stationen"), 
		TOTALSTATION("Totalstation"),
		REGION_DIVIDING("Bereichsaufteilung"), LASER("Laser"),
		MEASUREMENTS("Messungen"), RETICLES("Zielmarken"), TARGETS("Targets"), 
		VERIFY_MEASUREMENT("Kontrollmessung"), SPECIFICATION("Vorgabe"), RESULT("Ergebnis"), 
		GATEWAY("Gateway");
		
		private String name;

		private Content(String name, Content... subContent)
		{
			this.name = name;
		}
		
		public Content setName(String name)
		{
			this.name = name;
			return this;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}

	protected ObservableList<CooDocument.Content> content;

	public abstract void save(File file, CooCustomer customer, CooProject... projects);
	
	public abstract FileChooser.ExtensionFilter getFileFilter();
	
	public abstract List<Content> getAvailableContent();

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
	
	@Override
	public String toString()
	{
		return getFileFilter().getDescription();
	}
}