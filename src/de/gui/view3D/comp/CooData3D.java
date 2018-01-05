/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view3D.comp;

import de.coordz.db.xml.CooDBXML;
import de.util.CooXformUtil;
import javafx.beans.property.*;
import javafx.scene.control.Label;

public abstract class CooData3D <T extends CooDBXML> extends CooXformUtil
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
