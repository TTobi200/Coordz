package de.gui.view3D.ddd.shape;

import de.coordz.data.base.CooLaser;
import de.gui.view3D.ddd.util.CooDistance;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class CooLaserShape extends CooGroupShape<CooLaser>
{
	public static final Coo3dShapeSupplier<CooLaser, CooLaserShape> SUPPLIER = CooLaserShape::new;

	public CooLaserShape(CooLaser laser)
	{
		super(laser);

		Box box = new Box(
			CooDistance.ofMetres(.5).toMilliMetres(),
			CooDistance.ofMetres(.5).toMilliMetres(),
			CooDistance.ofMetres(.5).toMilliMetres());

		translateXProperty().bind(laser.xProperty());
		translateYProperty().bind(laser.yProperty());
		translateZProperty().bind(laser.zProperty());

		PhongMaterial greyMaterial = new PhongMaterial();
		greyMaterial.setDiffuseColor(Color.GREEN);
		greyMaterial.setSpecularColor(Color.GREEN);
		box.setMaterial(greyMaterial);

		getChildren().add(box);
	}

	@Override
	public StringProperty nameProperty()
	{
		return getData().nameProperty();
	}
}
