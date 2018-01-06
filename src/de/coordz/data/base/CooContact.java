/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.addElement;

import java.util.Objects;

import org.w3c.dom.*;

import de.coordz.db.gen.dao.DaoContact;
import de.coordz.db.xml.CooDBXML;

public class CooContact extends DaoContact implements CooDBXML
{
	@Override
	public void toXML(Document doc, Element root)
	{
		Element contact = addElement(doc, root, "Contact");
		contact.setAttribute("FirstName", firstNameProperty().get());
		contact.setAttribute("LastName", lastNameProperty().get());
		contact.setAttribute("Phone", phoneProperty().get());
		contact.setAttribute("Mail", mailProperty().get());
	}

	@Override
	public void fromXML(Element contact)
	{
		if(Objects.nonNull(contact))
		{
			firstNameProperty().set(contact.getAttribute("FirstName"));
			lastNameProperty().set(contact.getAttribute("LastName"));
			phoneProperty().set(contact.getAttribute("Phone"));
			mailProperty().set(contact.getAttribute("Mail"));
		}
	}
}