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
	
	/** {@link List} with all customer contacts */
	protected List<CooContact> contacts;
	/** {@link List} with all customer palets */
	protected List<CooPalet> palets;
	
	/** {@link Map} with all customer projects mapped to identifier */
	protected Map<String, CooProject> projects;
	
	public CooCustomer()
	{
		name = new SimpleStringProperty();
		address = new SimpleStringProperty();
		street = new SimpleStringProperty();
		plz = new SimpleStringProperty();
		location = new SimpleStringProperty();
		
		contacts = new ArrayList<CooContact>();
		palets = new ArrayList<CooPalet>();
		projects = new HashMap<String, CooProject>();
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
	
	public void addProject(String ident, CooProject project)
	{
		if(Objects.nonNull(project))
		{
			projects.put(ident, project);
		}
	}
	
	public List<CooContact> getContacts()
	{
		return contacts;
	}
	
	public List<CooPalet> getPalets()
	{
		return palets;
	}
	
	public Map<String, CooProject> getProjects()
	{
		return projects;
	}
	
	public StringProperty nameProperty()
	{
		return name;
	}
	
	public StringProperty adressProperty()
	{
		return address;
	}
	
	public StringProperty streetProperty()
	{
		return street;
	}
	
	public StringProperty plzProperty()
	{
		return plz;
	}
	
	public StringProperty locationProperty()
	{
		return location;
	}
}