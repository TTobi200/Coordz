/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view3D.comp;

import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import de.coordz.data.CooData;

public class CooAxis3D extends CooData3D<CooData>
{
	protected PhongMaterial redMaterial;
	protected PhongMaterial greenMaterial;
	protected PhongMaterial blueMaterial;

	protected Box xAxis;
	protected Box yAxis;
	protected Box zAxis;

	public CooAxis3D()
	{
		redMaterial = new PhongMaterial();
		greenMaterial = new PhongMaterial();
		blueMaterial = new PhongMaterial();
		xAxis = new Box(240.0, 1, 1);
		yAxis = new Box(1, 240.0, 1);
		zAxis = new Box(1, 1, 240.0);
		
		redMaterial.setDiffuseColor(Color.DARKRED);
		redMaterial.setSpecularColor(Color.RED);
		greenMaterial.setDiffuseColor(Color.DARKGREEN);
		greenMaterial.setSpecularColor(Color.GREEN);
		blueMaterial.setDiffuseColor(Color.DARKBLUE);
		blueMaterial.setSpecularColor(Color.BLUE);
		xAxis.setMaterial(redMaterial);
		yAxis.setMaterial(greenMaterial);
		zAxis.setMaterial(blueMaterial);
		
		getChildren().addAll(xAxis, yAxis, zAxis);
	}

	@Override
	public void dataChanged(CooData data)
	{
	}
	
	@Override
	public String toString()
	{
		return "3D-Achsen";
	}
}