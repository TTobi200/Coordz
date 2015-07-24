/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data;

import static de.util.CooXmlDomUtil.*;

import java.util.*;

import org.w3c.dom.*;

import de.coordz.data.base.*;

public class CooCustomer extends CooData
{
	protected String name;
	protected String adress;
	protected String street;
	protected String plz;
	protected String location;
	
	protected List<CooContact> contacts;
	protected List<CooPalet> palets;
	
	public CooCustomer()
	{
		contacts = new ArrayList<CooContact>();
		palets = new ArrayList<CooPalet>();
	}
	
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element customer = addElement(doc, root, "Customer");
		customer.setAttribute("Name", name);
		customer.setAttribute("Adress", adress);
		customer.setAttribute("Street", street);
		customer.setAttribute("PLZ", plz);
		customer.setAttribute("Location", location);
		
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
			name = customer.getAttribute("Name");
			adress = customer.getAttribute("Adress");
			street = customer.getAttribute("Street");
			plz = customer.getAttribute("PLZ");
			location = customer.getAttribute("Location");
			
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
}