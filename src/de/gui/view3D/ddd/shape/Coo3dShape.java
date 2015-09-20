package de.gui.view3D.ddd.shape;

import de.coordz.data.CooData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;

public interface Coo3dShape<T extends CooData>
{
	public StringProperty nameProperty();
	public BooleanProperty nameVisibleProperty();
}
