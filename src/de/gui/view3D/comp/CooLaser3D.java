/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view3D.comp;

import javafx.scene.paint.*;
import javafx.scene.shape.*;
import de.coordz.data.base.CooLaser;

public class CooLaser3D extends CooData3D<CooLaser>
{
	protected PhongMaterial greyMaterial;
	protected PhongMaterial greenMaterial;
	protected Box laser;
	
	public CooLaser3D()
	{
		laser = new Box(20, 10, 10);
		greyMaterial = new PhongMaterial();
		greenMaterial = new PhongMaterial();

		greyMaterial.setDiffuseColor(Color.DARKGREY);
		greyMaterial.setSpecularColor(Color.GREY);
		greenMaterial.setDiffuseColor(Color.DARKGREEN);
		greenMaterial.setSpecularColor(Color.GREEN);
		
		laser.setMaterial(greyMaterial);

		TriangleMesh laserBeamMesh = new TriangleMesh();
		laserBeamMesh.getTexCoords().addAll(0, 0);
		float h = 150;                     // Height
		float s = 300;                     // Side
		laserBeamMesh.getPoints().addAll(
		        0,    0,    0,             // Point 0 - Top
		        0,    -h,    -s/2,         // Point 1 - Front
		        -s/2, -h,    0,            // Point 2 - Left
		        s/2,  -h,    0,            // Point 3 - Back
		        0,    -h,    s/2           // Point 4 - Right
		    );
		laserBeamMesh.getFaces().addAll(
	        0,0,  2,0,  1,0,          // Front left face
	        0,0,  1,0,  3,0,          // Front right face
	        0,0,  3,0,  4,0,          // Back right face
	        0,0,  4,0,  2,0,          // Back left face
	        4,0,  1,0,  2,0,          // Bottom rear face
	        4,0,  3,0,  1,0           // Bottom front face
	    ); 
		MeshView pyramid = new MeshView(laserBeamMesh);
		pyramid.setDrawMode(DrawMode.LINE);
		pyramid.setMaterial(greenMaterial);
		
		getChildren().addAll(laser, pyramid);
	}

	@Override
	public void dataChanged(CooLaser data)
	{
		nameProperty().bind(data.nameProperty());
	}
	
	@Override
	public String toString()
	{
		return "3D-Laser";
	}
}