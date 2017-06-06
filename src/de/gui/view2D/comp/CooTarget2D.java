/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view2D.comp;

import de.coordz.data.base.CooTarget;
import de.gui.view2D.CooComponent2D;
import javafx.beans.property.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CooTarget2D implements CooComponent2D
{
	/** The size of an target cube */
	public static final int CUBE_SIZE = 15;
	/** The text size of target labels */
	public static final int TEXT_SIZE = 13;
	
	/** The corresponding {@link CooTarget} */
	protected ObjectProperty<CooTarget> target;
	/** The actual {@link State} of this target */
	protected ObjectProperty<State> state;
	
	public static enum State
	{
		/** Unknown target state */
		UNKNOWN(Color.BLUE),
		/** The target calibration is faulty */
		FAULTY(Color.RED),
		/** The target is calibrated successfully */
		CALIBRATED(Color.GREEN);
		
		/** The {@link Color} of the target state */
		protected Color color;

		private State(Color color)
		{
			this.color = color;
		}
	}
	
	public CooTarget2D(CooTarget target)
	{
		this.target = new SimpleObjectProperty<>(target);
		state = new SimpleObjectProperty<>(State.UNKNOWN);
	}

	@Override
	public void draw(GraphicsContext gc, double x, double y)
	{
		gc.setFill(state.get().color);
		// Draw the target cube
		gc.fillRect(x, y, CUBE_SIZE, CUBE_SIZE);
		gc.setFill(Color.BLACK);
		// Add the Target name left from target with an offset
		gc.fillText(target.get().nameProperty().get(), x + CUBE_SIZE + 5, y + TEXT_SIZE);	
	}
}