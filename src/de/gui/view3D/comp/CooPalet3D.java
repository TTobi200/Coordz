/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view3D.comp;

import de.coordz.data.base.CooPalet;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;

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
		// XXX $TO: Recalculate mm to cm in complete 3D View
		palet.widthProperty().bind(
			data.widthProperty().divide(10));
		// TODO add a height prop to CooPalet
		// palet.heightProperty().bind(
		// data.lengthProperty());
		palet.depthProperty().bind(
			data.lengthProperty().divide(10));
	}
	
	@Override
	public String toString()
	{
		return "3D-Palette";
	}
}