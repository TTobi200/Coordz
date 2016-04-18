package de.gui.view3D.ddd.shape;

import de.coordz.data.CooData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

/**
 * basic interface for all objects that want do show a 3dshape for a {@link CooData}
 *
 * @author ddd
 *
 * @param <T>
 */
public interface Coo3dShape<T extends CooData>
{
	/**
	 * @return a property holding the name of the coodata
	 */
	public StringProperty nameProperty();
	/**
	 * @return a property holding the visibility-state of the name of the coodata
	 */
	public BooleanProperty nameVisibleProperty();
	/**
	 * @return a property holding the visibility-state of the shape
	 */
	public BooleanProperty visibleProperty();
	/**
	 * @return The shape itself as node
	 */
	public Node getShapeNode();
}
