package de.gui.view3D;

import java.util.Objects;

import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import de.coordz.data.base.CooMeasurement;
import de.util.CooXformUtil;

public class CooView3D extends StackPane implements CooMeasurementChanged
{
	final Group root = new Group();
	final Group axisGroup = new Group();
	final CooXformUtil world = new CooXformUtil();
	final PerspectiveCamera camera = new PerspectiveCamera(true);
	final CooXformUtil cameraXform = new CooXformUtil();
	final CooXformUtil cameraXform2 = new CooXformUtil();
	final CooXformUtil cameraXform3 = new CooXformUtil();
	final double cameraDistance = 450;
	final CooXformUtil moleculeGroup = new CooXformUtil();
	private Timeline timeline;
	boolean timelinePlaying = false;
	double ONE_FRAME = 1.0 / 24.0;
	double DELTA_MULTIPLIER = 200.0;
	double CONTROL_MULTIPLIER = 0.1;
	double SHIFT_MULTIPLIER = 0.1;
	double ALT_MULTIPLIER = 0.5;
	double mousePosX;
	double mousePosY;
	double mouseOldX;
	double mouseOldY;
	double mouseDeltaX;
	double mouseDeltaY;
	
	protected CooXformUtil paletXform;
	protected Box palet;
	
	public CooView3D()
	{
		System.setProperty("prism.dirtyopts", "false");
		
		buildScene();
		buildCamera();
		buildAxes();
		buildPalet();
		
		SubScene subScene = new SubScene(root, 780, 280, true, null);
		subScene.setFill(Color.GREY);
		handleKeyboard(subScene, world);
		handleMouse(subScene, world);

		subScene.setCamera(camera);
		
		getChildren().add(subScene);
		subScene.heightProperty().bind(heightProperty());
		subScene.widthProperty().bind(widthProperty());
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
		final PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.DARKRED);
		redMaterial.setSpecularColor(Color.RED);

		final PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setDiffuseColor(Color.DARKGREEN);
		greenMaterial.setSpecularColor(Color.GREEN);

		final PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.DARKBLUE);
		blueMaterial.setSpecularColor(Color.BLUE);

		final Box xAxis = new Box(240.0, 1, 1);
		final Box yAxis = new Box(1, 240.0, 1);
		final Box zAxis = new Box(1, 1, 240.0);

		xAxis.setMaterial(redMaterial);
		yAxis.setMaterial(greenMaterial);
		zAxis.setMaterial(blueMaterial);

		axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
		world.getChildren().addAll(axisGroup);
	}

	private void buildPalet()
	{
		final PhongMaterial greyMaterial = new PhongMaterial();
		greyMaterial.setDiffuseColor(Color.DARKGREY);
		greyMaterial.setSpecularColor(Color.GREY);

		paletXform = new CooXformUtil();
		
		palet = new Box(70, 10, 100);
		palet.setMaterial(greyMaterial);
		
		paletXform.getChildren().add(palet);
		moleculeGroup.getChildren().add(paletXform);

		world.getChildren().addAll(moleculeGroup);
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
	public void measurementChanged(CooMeasurement measurement)
	{
		if(Objects.nonNull(measurement))
		{
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
							if(moleculeGroup.isVisible())
							{
								moleculeGroup.setVisible(false);
							}
							else
							{
								moleculeGroup.setVisible(true);
							}
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