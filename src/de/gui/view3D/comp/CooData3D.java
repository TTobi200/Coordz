/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view3D.comp;

import javafx.beans.property.*;
import javafx.scene.control.Label;
import de.coordz.data.CooData;
import de.util.CooXformUtil;

public abstract class CooData3D <T extends CooData> extends CooXformUtil
{
	protected Label lblName;
	
	public CooData3D()
	{
		lblName = new Label(toString());
		lblName.setRotate(180);
		getChildren().add(lblName);
	}
	
	public abstract void dataChanged(T data);
	
	public StringProperty nameProperty()
	{
		return lblName.textProperty();
	}
	
	public BooleanProperty nameVisibleProperty()
	{
		return lblName.visibleProperty();
	}
}
