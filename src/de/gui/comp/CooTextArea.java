/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import java.util.Objects;

import javafx.beans.property.Property;
import javafx.scene.control.TextArea;

public class CooTextArea extends TextArea
{
	protected Property<String> lastBindedBidirectional;
	
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
}