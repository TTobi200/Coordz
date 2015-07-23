/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.*;

import java.util.Objects;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooContact extends CooData
{
	protected String firstName;
	protected String lastName;
	protected String phone;
	protected String mail;

	@Override
	public void toXML(Document doc, Element root)
	{
		Element contact = addElement(doc, root, "Contact");
		contact.setAttribute("FirstName", firstName);
		contact.setAttribute("LastName", lastName);
		contact.setAttribute("Phone", phone);
		contact.setAttribute("Mail", mail);
	}

	@Override
	public void fromXML(Element root)
	{
		Element contact = getSingleElement(root, "Contact");
		if(Objects.nonNull(contact))
		{
			firstName = contact.getAttribute("FirstName");
			lastName = contact.getAttribute("LastName");
			phone = contact.getAttribute("Phone");
			mail = contact.getAttribute("Mail");
		}
	}
}