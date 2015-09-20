package de.gui.view3D.ddd.shape;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import de.coordz.data.CooData;
import javafx.scene.Node;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * A factory for creating 3d shapes for coodata-objects.<br>
 * When deciding which supplier to use:
 * <ol>
 * <li> look whether a supplier for exactly that instance is given
 * <li> if not look whether their is a supplier for the instances class
 * <li> <b>Only one class is checked, superclasses etc are not checked.</b>
 * </ol>
 *
 * @author ddd
 * @version 1.0
 */
public class Coo3dShapeFactory
{
	/** mapping to supplier by class-object */
	private Map<Class<CooData>, Coo3dShapeSupplier<?, ?>> shapeSupplier;
	/** mapping to supplier by object */
	private Map<CooData, Coo3dShapeSupplier<?, ?>> specShapeSupplier;

	public Coo3dShapeFactory()
	{
		shapeSupplier = new HashMap<>();
		specShapeSupplier = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public <T extends CooData> void setSupplierFor(Class<T> clazz, Coo3dShapeSupplier<T, ?> supplier)
	{
		shapeSupplier.put((Class<CooData>)clazz, supplier);
	}

	public <T extends CooData> void setSupplierFor(T data, Coo3dShapeSupplier<T, ?> supplier)
	{
		specShapeSupplier.put(data, supplier);
	}

	@SuppressWarnings("unchecked")
	public <T extends CooData, R extends Node & Coo3dShape<T>> R creShapeFor(T data)
	{
		if(specShapeSupplier.containsKey(data))
		{
			return ((Coo3dShapeSupplier<T, R>) specShapeSupplier.get(data)).apply(data);
		}
		return ((Coo3dShapeSupplier<T, R>) shapeSupplier.get(data.getClass())).apply(data);
	}

	/**
	 * <b>Not imploemented yet</b>
	 *
	 * @param reader
	 */
	public void load(Reader reader)
	{
		// TODO $Ddd 20.09.15 implement
		throw new NotImplementedException();
	}
}
