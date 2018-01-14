/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data;

import static de.util.CooSQLUtil.loadList;
import static de.util.CooXmlDomUtil.*;

import java.sql.SQLException;
import java.util.*;

import org.w3c.dom.*;

import de.coordz.data.base.*;
import de.coordz.db.CooDB;
import de.coordz.db.gen.dao.DaoCustomer;
import de.coordz.db.gen.inf.*;
import de.coordz.db.xml.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.image.Image;

public class CooCustomer extends DaoCustomer implements CooDBXML, CooDBLoad
{
	/** {@link ObjectProperty} for the customer logo */
	protected ObjectProperty<Image> logo;
	
	/** {@link List} with all customer {@link CooContact} */
	protected ObservableList<CooContact> contacts;
	/** {@link List} with all customer {@link CooPalet} */
	protected ObservableList<CooPalet> palets;
	
	/** {@link List} with all customer projects */
	protected ObservableList<CooProject> projects;
	
	public CooCustomer()
	{
		logo = new SimpleObjectProperty<>();
		
		contacts = FXCollections.observableArrayList();
		palets = FXCollections.observableArrayList();
		projects = FXCollections.observableArrayList();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element customer = addElement(doc, root, "Customer");
		customer.setAttribute("Name", nameProperty().get());
		customer.setAttribute("Street", streetProperty().get());
		customer.setAttribute("PLZ", plzProperty().get());
		customer.setAttribute("Location", locationProperty().get());
		
		// Add all contacts
		Element contacts = addElement(doc, customer, "Contacts");
		this.contacts.forEach(s -> s.toXML(doc, contacts));
		
		// Add all palets
		Element palets = addElement(doc, customer, "Palets");
		this.palets.forEach(s -> s.toXML(doc, palets));
	}
	
	@Override
	public void fromXML(Element customer)
	{
		if(Objects.nonNull(customer))
		{
			nameProperty().set(customer.getAttribute("Name"));
			streetProperty().set(customer.getAttribute("Street"));
			plzProperty().set(customer.getAttribute("PLZ"));
			locationProperty().set(customer.getAttribute("Location"));
			
			// Load all contacts
			Element contacts = getSingleElement(customer,
							"Contacts");
			addToList("Contact", contacts,
					CooContact.class, this.contacts);
			
			// Load all palets
			Element palets = getSingleElement(customer,
							"Palets");
			addToList("Palet", palets,
					CooPalet.class, this.palets);
		}
	}
	
	@Override
	public void fromDB(CooDB database) throws SQLException
	{
		// FORTEST Select the contacts
		contacts.setAll(loadList(database, InfContact.TABLE_NAME, 
			InfContact.CUSTOMERID, CooContact.class, 
			customerIdProperty().get()));
		// FORTEST Select the palets
		palets.setAll(loadList(database, InfPalet.TABLE_NAME, 
			InfPalet.CUSTOMERID, CooPalet.class,
			customerIdProperty().get()));
		// FORTEST Select the projects
		projects.setAll(loadList(database, InfProject.TABLE_NAME, 
			InfProject.CUSTOMERID, CooProject.class,
			customerIdProperty().get()));
	}
	
	/**
	 * Method to add {@link CooProject} to this customer.
	 * @param ident = the project identifier
	 * @param project = the {@link CooProject} to add
	 */
	public void addProject(CooProject project)
	{
		if(Objects.nonNull(project))
		{
			projects.add(project);
		}
	}
	
	/**
	 * Method to access {@link CooContact}
	 * @return {@link #contacts}
	 */
	public ObservableList<CooContact> getContacts()
	{
		return contacts;
	}
	
	/**
	 * Method to access {@link CooPalet}
	 * @return {@link #palets}
	 */
	public ObservableList<CooPalet> getPalets()
	{
		return palets;
	}
	
	/**
	 * Method to access {@link CooProject}
	 * @return {@link #projects}
	 */
	public  ObservableList<CooProject> getProjects()
	{
		return projects;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #logo}
	 */
	public ObjectProperty<Image> logoProprty()
	{
		return logo;
	}
}