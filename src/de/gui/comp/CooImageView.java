/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.comp;

import java.util.Objects;

import javafx.beans.property.Property;
import javafx.scene.image.*;

public class CooImageView extends ImageView
{
	protected Property<Image> lastBindedBidirectional;
	
	public void bindBidirectional(Property<Image> other)
	{
		if(Objects.nonNull(lastBindedBidirectional))
		{
			imageProperty().unbindBidirectional(lastBindedBidirectional);
		}
		
		lastBindedBidirectional = other;
		
		imageProperty().bindBidirectional(other);
	}
	
	public void unbindBidirectional()
	{
		if(Objects.nonNull(lastBindedBidirectional))
		{
			imageProperty().unbindBidirectional(lastBindedBidirectional);
			setImage(null);
		}
	}
}