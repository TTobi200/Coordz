/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view3D;

import java.util.*;

import org.controlsfx.control.CheckComboBox;

import de.coordz.data.*;
import de.coordz.data.base.CooPalet;
import de.gui.*;
import de.gui.view3D.comp.*;
import de.util.CooXformUtil;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class CooView3D extends BorderPane implements CooMeasurementChanged, CooDataChanged
{
	protected final Group root = new Group();
	protected final Group axisGroup = new Group();
	protected final CooXformUtil world = new CooXformUtil();
	protected final PerspectiveCamera camera = new PerspectiveCamera(true);
	protected final CooXformUtil cameraXform = new CooXformUtil();
	protected final CooXformUtil cameraXform2 = new CooXformUtil();
	protected final CooXformUtil cameraXform3 = new CooXformUtil();
	protected final double cameraDistance = 450;
	protected boolean timelinePlaying = false;
	protected double ONE_FRAME = 1.0 / 24.0;
	protected double DELTA_MULTIPLIER = 200.0;
	protected double CONTROL_MULTIPLIER = 0.1;
	protected double SHIFT_MULTIPLIER = 0.1;
	protected double ALT_MULTIPLIER = 0.5;
	protected double mousePosX;
	protected double mousePosY;
	protected double mouseOldX;
	protected double mouseOldY;
	protected double mouseDeltaX;
	protected double mouseDeltaY;
	
	protected CooPalet3D palet;
	protected CooAxis3D axis;
	protected ComboBox<CooPalet> cbPalets;
	protected CheckComboBox<CooData3D<?>> elements;
	protected CooLaser3D laser1;
	protected CooLaser3D laser2;
	
	public CooView3D()
	{
		System.setProperty("prism.dirtyopts", "false");
		elements = new CheckComboBox<>();
		
		buildScene();
		buildCamera();
		buildWorld();

		SubScene subScene = new SubScene(root, 780, 280, true, null);
		subScene.setFill(Color.GREY);
		handleKeyboard(subScene, world);
		handleMouse(subScene, world);

		subScene.setCamera(camera);
		
		getChildren().add(subScene);
		subScene.heightProperty().bind(heightProperty());
		subScene.widthProperty().bind(widthProperty());
		
		setTop(buildToolbar());
		setPrefSize(700, 500);
		
		subScene.setOnDragDetected(
		(MouseEvent event) ->
		{
			// Consume the drag event here
			// Only drag on TabPane
			event.consume();
		});
	}

	private void buildScene()
	{
		root.getChildren().add(world);
	}

	private void buildCamera()
	{
		root.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);
		cameraXform3.setRotateZ(180.0);

		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateZ(-cameraDistance);
		cameraXform.ry.setAngle(320.0);
		cameraXform.rx.setAngle(40);
	}

	private void buildWorld()
	{
		palet = new CooPalet3D();
		axis = new CooAxis3D();
		laser1 = new CooLaser3D();
		laser2 = new CooLaser3D();
		
		laser1.setTy(155);
		laser1.setTz(-100);
		laser2.setTy(155);
		laser2.setTz(100);
		
		addToWorld(axis, palet, laser1, laser2);
	}
	
	private Node buildToolbar()
	{
		ToolBar tools = new ToolBar();
		cbPalets = new ComboBox<>();
		CheckBox cbShowNames = new CheckBox("Namen anzeigen");
		
		// Display names
		elements.getItems()
			.forEach(
				el -> el.nameVisibleProperty().bind(
					cbShowNames.selectedProperty()));
		
		// Palet selection
		cbPalets.setPromptText("Palette auswählen...");
		cbPalets.getSelectionModel()
			.selectedItemProperty()
			.addListener(
				(obs, old, newV) -> palet.dataChanged(
					Objects.nonNull(newV) ? newV : new CooPalet()));
		
		// Elements visible selection
		elements.getCheckModel().checkAll();
		elements.getCheckModel()
			.getCheckedItems()
			.addListener(new ListChangeListener<CooData3D<?>>()
			{
				@Override
				public void onChanged(javafx.collections.ListChangeListener.
						Change<? extends CooData3D<?>> c)
				{
					elements.getItems().forEach(el -> el.setVisible(false));
					c.getList().forEach(el -> el.setVisible(true));
				}
			});
		
		tools.getItems().addAll(cbPalets, elements, cbShowNames);
		return tools;
	}
	
	private <T extends CooData> void addToWorld(CooData3D<?>... n)
	{
		Arrays.asList(n).forEach(el -> 
		{
			world.getChildren().add(el);
			elements.getItems().add(el);
		});
	}
	
	@Override
	public void customerChanged(CooCustomer customer)
	{
		if(Objects.nonNull(customer))
		{
			cbPalets.setItems(customer.getPalets());
		}
	}

	private void handleMouse(SubScene scene, final Node root)
	{
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me)
			{
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseOldX = me.getSceneX();
				mouseOldY = me.getSceneY();
			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me)
			{
				mouseOldX = mousePosX;
				mouseOldY = mousePosY;
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseDeltaX = (mousePosX - mouseOldX);
				mouseDeltaY = (mousePosY - mouseOldY);

				double modifier = 5.0;
				double modifierFactor = 0.1;

				if(me.isPrimaryButtonDown())
				{
					cameraXform.ry.setAngle(cameraXform.ry.getAngle()
											- mouseDeltaX * modifierFactor
											* modifier * 2.0); // +
					cameraXform.rx.setAngle(cameraXform.rx.getAngle()
											+ mouseDeltaY * modifierFactor
											* modifier * 2.0); // -
				}
				else if(me.isSecondaryButtonDown())
				{
					double z = camera.getTranslateZ();
					double newZ = z + mouseDeltaX * modifierFactor * modifier;
					camera.setTranslateZ(newZ);
				}
				else if(me.isMiddleButtonDown())
				{
					cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX
										* modifierFactor * modifier * 0.3); // -
					cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY
										* modifierFactor * modifier * 0.3); // -
				}
			}
		});
	}

	private void handleKeyboard(SubScene scene, final Node root)
	{
		// TODO Implement the keyboard handling
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event)
			{
				switch(event.getCode())
				{
					default:
						break;
				}
			}
		});
	}
}