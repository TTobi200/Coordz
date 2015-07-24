/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.addElement;

import java.util.Objects;

import javafx.beans.property.*;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooContact extends CooData
{
	/** {@link StringProperty} for the contact first name */
	protected StringProperty firstName;
	/** {@link StringProperty} for the contact last name */
	protected StringProperty lastName;
	/** {@link StringProperty} for the contact phone */
	protected StringProperty phone;
	/** {@link StringProperty} for the contact mail */
	protected StringProperty mail;

	public CooContact()
	{
		firstName = new SimpleStringProperty();
		lastName = new SimpleStringProperty();
		phone = new SimpleStringProperty();
		mail = new SimpleStringProperty();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element contact = addElement(doc, root, "Contact");
		contact.setAttribute("FirstName", firstName.get());
		contact.setAttribute("LastName", lastName.get());
		contact.setAttribute("Phone", phone.get());
		contact.setAttribute("Mail", mail.get());
	}

	@Override
	public void fromXML(Element contact)
	{
		if(Objects.nonNull(contact))
		{
			firstName.set(contact.getAttribute("FirstName"));
			lastName.set(contact.getAttribute("LastName"));
			phone.set(contact.getAttribute("Phone"));
			mail.set(contact.getAttribute("Mail"));
		}
	}
	
	public StringProperty firstNameProperty()
	{
		return firstName;
	}
	
	public StringProperty lastNameProperty()
	{
		return lastName;
	}
	
	public StringProperty phoneroperty()
	{
		return phone;
	}
	
	public StringProperty mailProperty()
	{
		return mail;
	}
}