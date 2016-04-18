package de.gui.view3D.ddd.shape;

import de.coordz.data.base.CooTarget;
import de.gui.view3D.ddd.util.CooDistance;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class CooTargetShape extends CooGroupShape<CooTarget>
{
	public static final Coo3dShapeSupplier<CooTarget, CooTargetShape> SUPPLIER = CooTargetShape::new;

	public CooTargetShape(CooTarget target)
	{
		super(target);

		Box box = new Box(
			CooDistance.ofMetres(.5).toMilliMetres(),
			CooDistance.ofMetres(.5).toMilliMetres(),
			CooDistance.ofMetres(.5).toMilliMetres());

		translateXProperty().bind(target.xProperty());
		translateYProperty().bind(target.yProperty());
		translateZProperty().bind(target.zProperty());

		PhongMaterial greyMaterial = new PhongMaterial();
		greyMaterial.setDiffuseColor(Color.RED);
		greyMaterial.setSpecularColor(Color.RED);
		box.setMaterial(greyMaterial);

		getChildren().add(box);
	}

	@Override
	public StringProperty nameProperty()
	{
		return getData().nameProperty();
	}
}
