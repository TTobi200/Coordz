package de.gui.view3D.ddd.shape;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import de.coordz.data.CooData;
import de.coordz.data.base.*;
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
public class Coo3dShapeFactory implements Coo3dShapeSupplier<CooData, Coo3dShape<CooData>>
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Coo3dShapeFactory creStdFactory()
	{
		Coo3dShapeFactory factory = new Coo3dShapeFactory();

		factory.setSupplierFor(CooPalet.class, CooPaletShape.SUPPLIER);
		factory.setSupplierFor(CooLaser.class, CooLaserShape.SUPPLIER);
		factory.setSupplierFor(CooRectangle.class, CooRectangleShape.SUPPLIER);
		factory.setSupplierFor(CooTarget.class, CooTargetShape.SUPPLIER);
		factory.setSupplierFor(CooReticle.class, (Coo3dShapeSupplier)CooTargetShape.SUPPLIER);
		factory.setSupplierFor(CooTotalstation.class, CooTotalStationShape.SUPPLIER);

		return factory;
	}

	/** mapping to supplier by class-object */
	private Map<Class<CooData>, Coo3dShapeSupplier<?, ?>> shapeSupplier;

	public Coo3dShapeFactory()
	{
		shapeSupplier = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public <T extends CooData> void setSupplierFor(Class<T> clazz, Coo3dShapeSupplier<T, ?> supplier)
	{
		shapeSupplier.put((Class<CooData>)clazz, supplier);
	}

	@SuppressWarnings("unchecked")
	public <T extends CooData, R extends Coo3dShape<T>> R creShapeFor(T data)
	{
		return ((Coo3dShapeSupplier<T, R>) shapeSupplier.get(data.getClass())).apply(data);
	}

	/**
	 * <b>Not implemented yet</b>
	 *
	 * @param reader
	 */
	public void load(Reader reader)
	{
		// TODO $Ddd 20.09.15 implement
		throw new NotImplementedException();
	}

	@Override
	public Coo3dShape<CooData> apply(CooData cooData)
	{
		return creShapeFor(cooData);
	}
}
