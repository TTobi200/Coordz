package de.gui.view3D.ddd;

import de.coordz.data.CooCustomer;
import de.coordz.data.CooProject;
import de.gui.CooDataChanged;
import de.gui.view3D.CooMeasurementChanged;
import de.gui.view3D.ddd.shape.Coo3dShapeFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class CooView3dDdd extends Control implements CooMeasurementChanged, CooDataChanged
{
	private ObjectProperty<CooCustomer> selCustomer;
	private ObjectProperty<CooProject> curProject;

	private Coo3dShapeFactory shapeFactory;

	public CooView3dDdd()
	{
		// TODO $Ddd 20.09.15 create a way to transport configurations
		// maybe a map or a new object???
		shapeFactory = Coo3dShapeFactory.creStdFactory();
		curProject = new SimpleObjectProperty<>(null);
		selCustomer = new SimpleObjectProperty<>(null);
	}

	@Override
	public void customerChanged(CooCustomer customer)
	{
		selCustomer.set(customer);
	}

	@Override
	public void projectChanged(CooProject project)
	{
		curProject.set(project);
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

	protected final ObjectProperty<CooCustomer> customerProperty()
	{
		return selCustomer;
	}

	protected final ObjectProperty<CooProject> projectProperty()
	{
		return curProject;
	}
}
