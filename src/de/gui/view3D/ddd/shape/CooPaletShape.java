package de.gui.view3D.ddd.shape;

import de.coordz.data.base.CooPalet;
import de.gui.view3D.ddd.util.CooDistance;
import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;

public class CooPaletShape extends CooGroupShape<CooPalet>
{
	public static final Coo3dShapeSupplier<CooPalet, CooPaletShape> SUPPLIER = CooPaletShape::new;

	protected CooPaletShape(CooPalet palet)
	{
		super(palet);

		Box box = new Box();

		box.depthProperty().bind(palet.widthProperty());
		box.widthProperty().bind(palet.lengthProperty());
		box.heightProperty().bind(new SimpleLongProperty(CooDistance.ofDeziMetres(1).toMilliMetres()));

		PhongMaterial greyMaterial = new PhongMaterial();
		greyMaterial.setDiffuseColor(Color.BROWN);
		greyMaterial.setSpecularColor(Color.BROWN);
		box.setMaterial(greyMaterial);

		getChildren().add(box);
	}

	@Override
	public Node getShapeNode()
	{
		return this;
	}

	@Override
	public StringProperty nameProperty()
	{
		return getData().nameProperty();
	}
}
