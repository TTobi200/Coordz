package de.gui.view3D.ddd.shape;

import de.coordz.db.xml.CooDBXML;
import javafx.beans.property.*;
import javafx.scene.*;

/**
 * base class for {@link Coo3dShape} that are based on a group
 *
 * @author ddd
 *
 * @param <T>
 */
public abstract class CooGroupShape<T extends CooDBXML> extends Group implements Coo3dShape<T>
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
