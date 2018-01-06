package de.gui.view3D.ddd.shape;

import de.coordz.data.base.CooRectangle;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;

public class CooRectangleShape extends CooGroupShape<CooRectangle>
{
	public static final Coo3dShapeSupplier<CooRectangle, CooRectangleShape> SUPPLIER = CooRectangleShape::new;

	public CooRectangleShape(CooRectangle rectangle)
	{
		super(rectangle);

		Box box = new Box();

		box.depthProperty().bind(rectangle.widthProperty());
		box.widthProperty().bind(rectangle.lengthProperty());
		box.heightProperty().bind(rectangle.heightProperty());

		translateXProperty().bind(rectangle.xProperty());
		translateYProperty().bind(rectangle.yProperty());
		translateZProperty().bind(rectangle.zProperty());

		PhongMaterial greyMaterial = new PhongMaterial();
		greyMaterial.setDiffuseColor(Color.BLUE);
		greyMaterial.setSpecularColor(Color.BLUE);
		box.setMaterial(greyMaterial);

		getChildren().add(box);
	}

	@Override
	public StringProperty nameProperty()
	{
		return getData().nameProperty();
	}
}
