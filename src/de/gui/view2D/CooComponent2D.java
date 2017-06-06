/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view2D;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for {@link CooComponent2D} to display
 */
public abstract interface CooComponent2D
{
	/**
	 * Method to draw this {@link CooComponent2D} on 
	 * committed {@link GraphicsContext}.
	 * @param gc = the {@link GraphicsContext} to draw on
	 * @param x = the x-coordinate of this {@link CooComponent2D}
	 * @param y = the y-coordinate of this {@link CooComponent2D}
	 */
	public abstract void draw(GraphicsContext gc, double x, double y);
}