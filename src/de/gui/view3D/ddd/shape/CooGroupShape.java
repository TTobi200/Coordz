package de.gui.view3D.ddd.shape;

import de.coordz.data.CooData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 * base class for {@link Coo3dShape} that are based on a group
 *
 * @author ddd
 *
 * @param <T>
 */
public abstract class CooGroupShape<T extends CooData> extends Group implements Coo3dShape<T>
{
	private final T DATA;

	private BooleanProperty nameVisible;

	public CooGroupShape(T data)
	{
		DATA = data;

		nameVisible = new SimpleBooleanProperty();
	}

	@Override
	public Node getShapeNode()
	{
		return this;
	}

	@Override
	public BooleanProperty nameVisibleProperty()
	{
		return nameVisible;
	}

	/**
	 * @return the data this group shows
	 */
	public T getData()
	{
		return DATA;
	}
}
