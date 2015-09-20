package de.gui.view3D.ddd;

import de.coordz.data.base.CooPalet;
import de.gui.CooDataChanged;
import de.gui.view3D.CooMeasurementChanged;
import de.gui.view3D.ddd.shape.Coo3dShapeFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class CooView3dDdd extends Control implements CooMeasurementChanged, CooDataChanged
{
	private ObjectProperty<CooPalet> selPalet;
	private Coo3dShapeFactory shapeFactory;

	public CooView3dDdd()
	{
		// TODO $Ddd 20.09.15 create a way to transport configurations
		// maybe a map or a new object???
		selPalet = new SimpleObjectProperty<>();

		shapeFactory = new Coo3dShapeFactory();
	}

	public final ObjectProperty<CooPalet> selPaletProperty()
	{
		return this.selPalet;
	}

	public final de.coordz.data.base.CooPalet getSelPalet()
	{
		return this.selPaletProperty().get();
	}

	public final void setSelPalet(final de.coordz.data.base.CooPalet selPalet)
	{
		this.selPaletProperty().set(selPalet);
	}

	public Coo3dShapeFactory getShapeFactory()
	{
		return shapeFactory;
	}

	@Override
	protected Skin<?> createDefaultSkin()
	{
		return new CooView3dDddSkin(this);
	}

}
