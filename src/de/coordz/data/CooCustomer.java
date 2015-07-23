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
	protected CooPalet palet;
	
	public CooCustomer()
	{
		contacts = new ArrayList<CooContact>();
		palet = new CooPalet();
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
		contacts.forEach(c -> c.toXML(doc, customer));
		
		palet.toXML(doc, customer);
	}
	
	@Override
	public void fromXML(Element root)
	{
		Element customer = getSingleElement(root, "Customer");
		if(Objects.nonNull(customer))
		{
			name = customer.getAttribute("Name");
			adress = customer.getAttribute("Adress");
			street = customer.getAttribute("Street");
			plz = customer.getAttribute("PLZ");
			location = customer.getAttribute("Location");
			
			// Load all contacts
			contacts.forEach(c -> c.fromXML(customer));
			
			palet.fromXML(customer);
		}
	}
}