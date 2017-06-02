/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view2D;

import javafx.collections.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class CooView2D extends BorderPane 
{
	final int cubeSize = 20;
	final int textSize = 15;
	
	public CooView2D()
	{
		ResizableCanvas canvas = new ResizableCanvas();
		canvas.widthProperty().bind(widthProperty());
		canvas.heightProperty().bind(heightProperty());
		
		// Add to children, otherwise size handling in 
		// tabpane does not work correctly
		getChildren().add(canvas);
	}
	
	private void drawShapes(GraphicsContext gc) 
	{
		// FORTEST $TO: Define a list with targets to display
		ObservableList<String> targets = FXCollections.observableArrayList(
			"1", "2", "3", "4", "5", "6");

		double offset = 30;
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
				x += offset;
				y += offset;
			}
			else if(i > targets.size() / 2)
			{
				// We are still over the half of targets
				x += // Add the part of width 
					 getWidth() / (targets.size() / 2) + 
					 // Middle in part of width
					 (getWidth() / (targets.size() / 2) / 2) -
					 // Remove cube size, text size and the offset
					 cubeSize - textSize - offset;
			}
			else if(i == targets.size() / 2)
			{
				// We have reached the half of targets
				// This one now is on the other site
				y = (int)(getHeight() - offset);
			}
			else
			{
				// Last targets go back to start position x
				y = (int)(getHeight() - offset);
				x -= // Add the part of width 
					 getWidth() / (targets.size() / 2) + 
					 // Middle in part of width
					 (getWidth() / (targets.size() / 2) / 2) -
					 // Remove cube size, text size and the offset
					 cubeSize - textSize - offset;
			}
			
			// Add the target to view
			addTarget(gc, "T" + i, x, y);
		}
    }
	
	private void addTarget(GraphicsContext gc, String name, double x, double y)
	{
		Color targetColor = Color.GREEN;
		gc.setFill(targetColor);
		gc.fillRect(x, y, cubeSize, cubeSize);
		gc.setFill(Color.BLACK);
		gc.fillText(name, x + cubeSize, y + textSize);		
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