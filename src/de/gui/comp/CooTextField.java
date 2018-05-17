/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import java.awt.event.*;
import java.util.Objects;

import javafx.beans.property.Property;
import javafx.scene.control.TextField;

public class CooTextField extends TextField
{
	protected Property<String> lastBindedBidirectional;
	private ActionListener listener;
	
	public CooTextField()
	{
		focusedProperty().addListener((obs, old, newv) -> 
		{
			// Update DAO when focused lost only
			if(Objects.nonNull(listener) && old != newv && !newv)
			{
				listener.actionPerformed(
					new ActionEvent(this, 0, ""));
			}
		});
	}
	
	public void bindBidirectional(Property<String> other)
	{
		if(Objects.nonNull(lastBindedBidirectional))
		{
			textProperty().unbindBidirectional(lastBindedBidirectional);
		}
		
		lastBindedBidirectional = other;
		
		textProperty().bindBidirectional(other);
	}
	
	public void unbindBidirectional()
	{
		if(Objects.nonNull(lastBindedBidirectional))
		{
			textProperty().unbindBidirectional(lastBindedBidirectional);
			clear();
		}
	}
	
	public void setOnFocusedLost(ActionListener listener)
	{
		this.listener = listener;
	}
}