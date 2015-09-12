/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.gui.view3D;

import java.util.Objects;

import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import org.controlsfx.control.CheckComboBox;

import de.coordz.data.*;
import de.coordz.data.base.CooPalet;
import de.gui.CooDataChanged;
import de.gui.view3D.comp.*;
import de.util.CooXformUtil;

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
	protected Timeline timeline;
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
	
	public CooView3D()
	{
		System.setProperty("prism.dirtyopts", "false");
		elements = new CheckComboBox<CooData3D<?>>();
		
		buildScene();
		buildCamera();
		buildAxes();
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

	private void buildAxes()
	{
		axis = new CooAxis3D();
		addToWorld(axis);
	}
	
	private void buildWorld()
	{
		palet = new CooPalet3D();
		addToWorld(palet);
	}
	
	private Node buildToolbar()
	{
		ToolBar tools = new ToolBar();
		cbPalets = new ComboBox<CooPalet>();
		
		cbPalets.setPromptText("Palette auswählen...");
		cbPalets.getSelectionModel()
			.selectedItemProperty()
			.addListener(
				(obs, old, newV) -> palet.dataChanged(
					Objects.nonNull(newV) ? newV : new CooPalet()));
		
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
		
		tools.getItems().addAll(cbPalets, elements);
		return tools;
	}
	
	private <T extends CooData> void addToWorld(CooData3D<?> n)
	{
		world.getChildren().add(n);
		elements.getItems().add(n);
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

				double modifier = 1.0;
				double modifierFactor = 0.1;

				if(me.isControlDown())
				{
					modifier = 0.1;
				}
				if(me.isShiftDown())
				{
					modifier = 10.0;
				}
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

	@Override
	public void customerChanged(CooCustomer customer)
	{
		if(Objects.nonNull(customer))
		{
			cbPalets.setItems(customer.getPalets());
		}
	}
	
	private void handleKeyboard(SubScene scene, final Node root)
	{
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event)
			{
				switch(event.getCode())
				{
					case Z:
						if(event.isShiftDown())
						{
							cameraXform.ry.setAngle(0.0);
							cameraXform.rx.setAngle(0.0);
							camera.setTranslateZ(-300.0);
						}
						cameraXform2.t.setX(0.0);
						cameraXform2.t.setY(0.0);
						break;
					case X:
						if(event.isControlDown())
						{
							if(axisGroup.isVisible())
							{
								axisGroup.setVisible(false);
							}
							else
							{
								axisGroup.setVisible(true);
							}
						}
						break;
					case S:
						if(event.isControlDown())
						{
						}
						break;
					case SPACE:
						if(timelinePlaying)
						{
							timeline.pause();
							timelinePlaying = false;
						}
						else
						{
							timeline.play();
							timelinePlaying = true;
						}
						break;
					case UP:
						if(event.isControlDown() && event.isShiftDown())
						{
							cameraXform2.t.setY(cameraXform2.t.getY() - 10.0
												* CONTROL_MULTIPLIER);
						}
						else if(event.isAltDown() && event.isShiftDown())
						{
							cameraXform.rx.setAngle(cameraXform.rx.getAngle()
													- 10.0 * ALT_MULTIPLIER);
						}
						else if(event.isControlDown())
						{
							cameraXform2.t.setY(cameraXform2.t.getY() - 1.0
												* CONTROL_MULTIPLIER);
						}
						else if(event.isAltDown())
						{
							cameraXform.rx.setAngle(cameraXform.rx.getAngle()
													- 2.0 * ALT_MULTIPLIER);
						}
						else if(event.isShiftDown())
						{
							double z = camera.getTranslateZ();
							double newZ = z + 5.0 * SHIFT_MULTIPLIER;
							camera.setTranslateZ(newZ);
						}
						break;
					case DOWN:
						if(event.isControlDown() && event.isShiftDown())
						{
							cameraXform2.t.setY(cameraXform2.t.getY() + 10.0
												* CONTROL_MULTIPLIER);
						}
						else if(event.isAltDown() && event.isShiftDown())
						{
							cameraXform.rx.setAngle(cameraXform.rx.getAngle()
													+ 10.0 * ALT_MULTIPLIER);
						}
						else if(event.isControlDown())
						{
							cameraXform2.t.setY(cameraXform2.t.getY() + 1.0
												* CONTROL_MULTIPLIER);
						}
						else if(event.isAltDown())
						{
							cameraXform.rx.setAngle(cameraXform.rx.getAngle()
													+ 2.0 * ALT_MULTIPLIER);
						}
						else if(event.isShiftDown())
						{
							double z = camera.getTranslateZ();
							double newZ = z - 5.0 * SHIFT_MULTIPLIER;
							camera.setTranslateZ(newZ);
						}
						break;
					case RIGHT:
						if(event.isControlDown() && event.isShiftDown())
						{
							cameraXform2.t.setX(cameraXform2.t.getX() + 10.0
												* CONTROL_MULTIPLIER);
						}
						else if(event.isAltDown() && event.isShiftDown())
						{
							cameraXform.ry.setAngle(cameraXform.ry.getAngle()
													- 10.0 * ALT_MULTIPLIER);
						}
						else if(event.isControlDown())
						{
							cameraXform2.t.setX(cameraXform2.t.getX() + 1.0
												* CONTROL_MULTIPLIER);
						}
						else if(event.isAltDown())
						{
							cameraXform.ry.setAngle(cameraXform.ry.getAngle()
													- 2.0 * ALT_MULTIPLIER);
						}
						break;
					case LEFT:
						if(event.isControlDown() && event.isShiftDown())
						{
							cameraXform2.t.setX(cameraXform2.t.getX() - 10.0
												* CONTROL_MULTIPLIER);
						}
						else if(event.isAltDown() && event.isShiftDown())
						{
							cameraXform.ry.setAngle(cameraXform.ry.getAngle()
													+ 10.0 * ALT_MULTIPLIER); // -
						}
						else if(event.isControlDown())
						{
							cameraXform2.t.setX(cameraXform2.t.getX() - 1.0
												* CONTROL_MULTIPLIER);
						}
						else if(event.isAltDown())
						{
							cameraXform.ry.setAngle(cameraXform.ry.getAngle()
													+ 2.0 * ALT_MULTIPLIER); // -
						}
						break;
					default:
						break;
				}
			}
		});
	}
}