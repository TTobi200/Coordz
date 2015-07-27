/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2014 T.Ohm . All Rights Reserved.
 */
package de.util;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.TextInputControl;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class CooGuiUtil
{
	public static void setModality(Stage stage, Window parent)
	{
		if(parent != null && parent.isShowing())
		{
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(parent);
			relativeToOwner(stage, parent);
		}
	}

	public static void relativeToOwner(Window stage, Window parent)
	{
		stage.setX(parent.getX()
					+ (parent.getWidth() / 2 - (stage.getWidth() / 2)));
		stage.setY(parent.getY() + (parent.getHeight() / 2)
					- (stage.getHeight() / 2));
	}

	public static void grayOutParent(Window s,
					ReadOnlyBooleanProperty showingProperty)
	{
		Parent root = s.getScene().getRoot();
		StackPane stack;

		if(!(root instanceof StackPane))
		{
			stack = new StackPane(root);
			s.getScene().setRoot(stack);
		}
		else
		{
			stack = (StackPane)root;
		}

		// stack can never be null
//		if(stack != null)
		{
			Pane grayPane = new Pane();
			grayPane.setBackground(new Background(new BackgroundFill(
				Color.GRAY,
				CornerRadii.EMPTY, Insets.EMPTY)));
			grayPane.setOpacity(0.4);
			stack.setEffect(new BoxBlur());
			stack.getChildren().add(grayPane);

			showingProperty.addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(
								ObservableValue<? extends Boolean> observable,
								Boolean oldValue,
								Boolean newValue)
				{
					if(!newValue.booleanValue())
					{
						stack.setEffect(null);
						stack.getChildren().remove(grayPane);
						showingProperty.removeListener(this);
					}
				}
			});
		}
	}
	
	public static double getScreenXCenter(Stage s)
	{
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		return (primScreenBounds.getWidth() - s.getWidth()) / 2;
	}
	
	public static double getScreenYCenter(Stage s)
	{
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		return (primScreenBounds.getHeight() - s.getHeight()) / 4;
	}
	
	public static void cut(Scene scene)
	{
		Node focusOwn = scene.focusOwnerProperty().get();

		if(focusOwn instanceof TextInputControl)
		{
			((TextInputControl)focusOwn).cut();
		}
	}

	public static void copy(Scene scene)
	{
		Node focusOwn = scene.focusOwnerProperty().get();

		if(focusOwn instanceof TextInputControl)
		{
			((TextInputControl)focusOwn).copy();
		}
	}

	public static void paste(Scene scene)
	{
		Node focusOwn = scene.focusOwnerProperty().get();

		if(focusOwn instanceof TextInputControl)
		{
			((TextInputControl)focusOwn).paste();
		}
	}
}