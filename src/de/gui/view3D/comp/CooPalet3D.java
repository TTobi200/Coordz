/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view3D.comp;

import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import de.coordz.data.base.CooPalet;

public class CooPalet3D extends CooData3D<CooPalet>
{
	protected PhongMaterial greyMaterial;
	protected Box palet;

	public CooPalet3D()
	{
		palet = new Box();
		greyMaterial = new PhongMaterial();

		greyMaterial.setDiffuseColor(Color.DARKGREY);
		greyMaterial.setSpecularColor(Color.GREY);
		palet.setMaterial(greyMaterial);

		getChildren().add(palet);
	}

	@Override
	public void dataChanged(CooPalet data)
	{
		palet.widthProperty().bind(
			data.widthProperty());
		// TODO add a height prop to CooPalet
		// palet.heightProperty().bind(
		// data.lengthProperty());
		palet.depthProperty().bind(
			data.lengthProperty());
	}
	
	@Override
	public String toString()
	{
		return "3D-Palette";
	}
}