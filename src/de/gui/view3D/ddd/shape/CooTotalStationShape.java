package de.gui.view3D.ddd.shape;

import de.coordz.data.base.CooTotalstation;
import de.gui.view3D.ddd.util.CooDistance;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class CooTotalStationShape extends CooGroupShape<CooTotalstation>
{
	public static final Coo3dShapeSupplier<CooTotalstation, CooTotalStationShape> SUPPLIER = CooTotalStationShape::new;

	public CooTotalStationShape(CooTotalstation station)
	{
		super(station);

		Box box = new Box(
			CooDistance.ofMetres(.5).toMilliMetres(),
			CooDistance.ofMetres(.5).toMilliMetres(),
			CooDistance.ofMetres(.5).toMilliMetres());

		translateXProperty().bind(station.xProperty());
		translateYProperty().bind(station.yProperty());
		translateZProperty().bind(station.zProperty());

		PhongMaterial greyMaterial = new PhongMaterial();
		greyMaterial.setDiffuseColor(Color.GREEN);
		greyMaterial.setSpecularColor(Color.GREEN);
		box.setMaterial(greyMaterial);

		getChildren().add(box);
	}

	@Override
	public StringProperty nameProperty()
	{
		return new SimpleStringProperty("");
	}
}
