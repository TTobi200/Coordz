/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view3D.comp;

import javafx.scene.control.Label;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import de.coordz.data.base.CooLaser;

public class CooLaser3D extends CooData3D<CooLaser>
{
	protected PhongMaterial greyMaterial;
	protected Box laser;
	protected Label lblName;
	
	public CooLaser3D()
	{
		laser = new Box(20, 10, 10);
		lblName = new Label();
		greyMaterial = new PhongMaterial();

		greyMaterial.setDiffuseColor(Color.DARKGREY);
		greyMaterial.setSpecularColor(Color.GREY);
		laser.setMaterial(greyMaterial);

		getChildren().addAll(laser, lblName);
	}

	@Override
	public void dataChanged(CooLaser data)
	{
		lblName.textProperty().bind(data.nameProperty());
	}
	
	@Override
	public String toString()
	{
		return "3D-Laser";
	}
}