/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view2D;

import java.util.Objects;

import de.coordz.data.*;
import de.coordz.data.base.*;
import de.gui.*;
import de.gui.view2D.comp.CooTarget2D;
import javafx.collections.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.BorderPane;

public class CooView2D extends BorderPane implements CooDataChanged, CooMeasurementChanged
{
	/** The offset from border to each target */
	protected final int OFFSET = 30;
	
	/** {@link ObservableList} with {@link CooTarget2D} */
	protected ObservableList<CooTarget2D> targets;
	/** The {@link ResizableCanvas} to draw on */
	private ResizableCanvas canvas;
	
	public CooView2D()
	{
		targets = FXCollections.observableArrayList();
		
		canvas = new ResizableCanvas();
		canvas.widthProperty().bind(widthProperty());
		canvas.heightProperty().bind(heightProperty());
		
		// Add to children, otherwise size handling in 
		// tabpane does not work correctly
		getChildren().add(canvas);
	}
	
	private void drawShapes(GraphicsContext gc) 
	{
		double x = 0;
		double y = 0;
		
		// TODO $TO: Change the positioning of targets
		// TODO $TO: Implement the connection to data
		// Loop backwards through target list
		for(int i = targets.size(); i > 0; i--)
		{
			if(i == targets.size())
			{
				// First run, add the normal offset
				x += OFFSET;
				y += OFFSET;
			}
			else if(i > targets.size() / 2)
			{
				// We are still over the half of targets
				x += // Add the part of width 
					 getWidth() / (targets.size() / 2) + 
					 // Middle in part of width
					 (getWidth() / (targets.size() / 2) / 2) -
					 // Remove cube size, text size and the offset
					 CooTarget2D.CUBE_SIZE - CooTarget2D.TEXT_SIZE - OFFSET;
			}
			else if(i == targets.size() / 2)
			{
				// We have reached the half of targets
				// This one now is on the other site
				y = (int)(getHeight() - OFFSET);
			}
			else
			{
				// Last targets go back to start position x
				y = (int)(getHeight() - OFFSET);
				x -= // Add the part of width 
					 getWidth() / (targets.size() / 2) + 
					 // Middle in part of width
					 (getWidth() / (targets.size() / 2) / 2) -
					 // Remove cube size, text size and the offset
					 CooTarget2D.CUBE_SIZE - CooTarget2D.TEXT_SIZE - OFFSET;
			}
			
			// Draw the target on canvas
			targets.get(i - 1).draw(gc, x, y);
		}
    }
	
	@Override
	public void measurementChanged(CooMeasurement measurement)
	{
		// Clear all visible targets
		targets.clear();
		
		if(Objects.nonNull(measurement))
		{
			// Display the targets from measurement
			measurement.getTargets().forEach(t
				-> targets.add(new CooTarget2D(t)));
			
			measurement.getTargets().addListener(
				new ListChangeListener<CooTarget>()
			{
				@SuppressWarnings("unlikely-arg-type")
				@Override
				public void onChanged(Change<? extends CooTarget> c)
				{
					while(c.next())
					{
						// Someone changed the targets
						for(CooTarget t : c.getRemoved())
						{
							targets.remove(t);
						}
						for(CooTarget t : c.getAddedSubList())
						{
							targets.add(new CooTarget2D(t));
						}
					}
					
					// Repaint the canvas
					canvas.repaint();
				}
			});
		}
		
		// Repaint the canvas
		canvas.repaint();
	}
	
	@Override
	public void customerChanged(CooCustomer customer)
	{
		// Call the measurement changed without data
		measurementChanged(null);
	}
	
	@Override
	public void projectChanged(CooProject project)
	{
		// Call the measurement changed without data
		measurementChanged(null);
	}
	
	private class ResizableCanvas extends Canvas
	{
		public ResizableCanvas()
		{
			// Repaint canvas when size changes.
			widthProperty().addListener(evt -> repaint());
			heightProperty().addListener(evt -> repaint());
		}

		private void repaint()
		{
			// Get the graphics content
			GraphicsContext gc = getGraphicsContext2D();
			// Clear the whole canvas
			gc.clearRect(0, 0, getWidth(), getHeight());
			// Draw the shapes
			drawShapes(gc);
		}

		@Override
		public boolean isResizable()
		{
			return true;
		}
		
		@Override
		public double prefWidth(double height)
		{
			return getWidth();
		}

		@Override
		public double prefHeight(double width)
		{
			return getHeight();
		}
	}
}