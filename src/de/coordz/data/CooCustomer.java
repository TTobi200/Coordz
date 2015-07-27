/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data;

import static de.util.CooXmlDomUtil.*;

import java.util.*;

import javafx.beans.property.*;
import javafx.collections.*;

import org.w3c.dom.*;

import de.coordz.data.base.*;

public class CooCustomer extends CooData
{
	/** {@link StringProperty} for the customer name */
	protected StringProperty name;
	/** {@link StringProperty} for the customer address */
	protected StringProperty address;
	/** {@link StringProperty} for the customer street */
	protected StringProperty street;
	/** {@link StringProperty} for the customer plz */
	protected StringProperty plz;
	/** {@link StringProperty} for the customer location */
	protected StringProperty location;
	
	/** {@link List} with all customer {@link CooContact} */
	protected ObservableList<CooContact> contacts;
	/** {@link List} with all customer {@link CooPalet} */
	protected ObservableList<CooPalet> palets;
	
	/** {@link List} with all customer projects */
	protected ObservableList<CooProject> projects;
	
	public CooCustomer()
	{
		name = new SimpleStringProperty();
		address = new SimpleStringProperty();
		street = new SimpleStringProperty();
		plz = new SimpleStringProperty();
		location = new SimpleStringProperty();
		
		contacts = FXCollections.observableArrayList();
		palets = FXCollections.observableArrayList();
		projects = FXCollections.observableArrayList();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element customer = addElement(doc, root, "Customer");
		customer.setAttribute("Name", name.get());
		customer.setAttribute("Adress", address.get());
		customer.setAttribute("Street", street.get());
		customer.setAttribute("PLZ", plz.get());
		customer.setAttribute("Location", location.get());
		
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
			name.set(customer.getAttribute("Name"));
			address.set(customer.getAttribute("Adress"));
			street.set(customer.getAttribute("Street"));
			plz.set(customer.getAttribute("PLZ"));
			location.set(customer.getAttribute("Location"));
			
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
	 * @return {@link #name}
	 */
	public StringProperty nameProperty()
	{
		return name;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #address}
	 */
	public StringProperty adressProperty()
	{
		return address;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #street}
	 */
	public StringProperty streetProperty()
	{
		return street;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #plz}
	 */
	public StringProperty plzProperty()
	{
		return plz;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #location}
	 */
	public StringProperty locationProperty()
	{
		return location;
	}
}