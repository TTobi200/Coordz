package de.gui.view3D.ddd.shape;

import java.util.function.Function;

import de.coordz.db.xml.CooDBXML;

/**
 * a special function that expects a coodata and creates a node that is a coo3dshape for the given
 * object.
 *
 * @author ddd
 *
 * @param <T> type of the input to the function
 * @param <R> type of the result of the function
 */
@FunctionalInterface
public interface Coo3dShapeSupplier<T extends CooDBXML, R extends Coo3dShape<T>>
extends Function<T, R>
{
	/**
	 * @param data the coodata to create a shape for
	 * @return a coo3dshape for the given coodata
	 */
	@Override
	public R apply(T data);
}
