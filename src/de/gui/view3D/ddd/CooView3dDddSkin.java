package de.gui.view3D.ddd;

import org.controlsfx.control.CheckComboBox;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

import de.coordz.data.base.CooPalet;
import de.gui.view3D.ddd.Coo3dAxis.CoordSystem;
import de.gui.view3D.ddd.Coo3dAxis.TransformOrder;
import de.gui.view3D.ddd.util.CooDddUtil;
import de.util.log.CooLog;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.*;

public class CooView3dDddSkin extends BehaviorSkinBase<CooView3dDdd, CooView3dDddBehavior>
{
	private static final CoordSystem VIEW_SYSTEM = CoordSystem.NX_NZ_NY;

	private BorderPane root;

	private SubScene scene2d;
	private SubScene scene3d;

	private Group root2d;
	private Group root3d;

	private Coo3dDddCamera camera;

	private Translate pivot;

	protected CooView3dDddSkin(CooView3dDdd control)
	{
		super(control, new CooView3dDddBehavior(control));

		final double WIDTH = 800;
		final double HEIGHT = 600;

		root = new BorderPane();

		root2d = new Group();
		root3d = new Group();

		scene2d = new SubScene(root2d, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
		scene3d = new SubScene(root3d, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);

		scene2d.setFill(Color.TRANSPARENT);
		scene3d.setFill(Color.GREY);

		scene2d.setMouseTransparent(true);
		// TODO $Ddd enable that the 3d-scene gets the key-events too.

		buildSample();

		scene3d.setCamera(initCamera());

		StackPane stack = new StackPane(scene3d, scene2d);
		stack.setMinWidth(100d);
		stack.setMinHeight(100d);

		root.setCenter(stack);

		root.setTop(buildToolbar());

		scene3d.widthProperty().bind(stack.widthProperty());
		scene3d.heightProperty().bind(stack.heightProperty());
		scene2d.widthProperty().bind(stack.widthProperty());
		scene2d.heightProperty().bind(stack.heightProperty());

		fixCameraBug();

		getChildren().add(root);
	}

	private Camera initCamera()
	{
		camera = new Coo3dDddCamera(TransformOrder.Y_X_Z_T, VIEW_SYSTEM);

		// position camera center to the center of the subscene
		// TODO $Ddd 20.09.15 why do binding of translateproperties does not work?
		Translate t = new Translate();
		t.xProperty().bind(scene3d.widthProperty().multiply(-.5));
		t.yProperty().bind(scene3d.heightProperty().multiply(-.5));
		camera.getTransforms().add(t);

		// add pivot to camera
		pivot = new Translate();
		camera.getTransforms().add(0, pivot);

		class MouseHandler
		{
			private double mx;
			private double my;

			public MouseHandler(SubScene scene, Coo3dDddCamera camera)
			{
				scene.setOnMousePressed(e ->
				{
					mx = e.getSceneX();
					my = e.getSceneY();
				});

				scene.setOnMouseDragged(e ->
				{
					final double FACTOR = .25;
					double deltaX = e.getSceneX() - mx;
					mx = e.getSceneX();
					double deltaY = e.getSceneY() - my;
					my = e.getSceneY();

					camera.rotateY(deltaX * FACTOR);
					camera.rotateX(-deltaY * FACTOR);
				});

				scene.setOnScroll(e ->
				{
					final double STEP = 10d;
					camera.moveZ(e.getDeltaY() * STEP);
				});
			}
		}

		new MouseHandler(scene3d, camera);

		return camera;
	}

	private void buildSample()
	{
		final double SIZE = 100d;

		Box boxX = new Box(SIZE * 10d, SIZE, SIZE);
		Box boxY = new Box(SIZE, SIZE * 10d, SIZE);
		Box boxZ = new Box(SIZE, SIZE, SIZE * 10d);

		boxX.setMaterial(new PhongMaterial(Color.BLUE));
		boxY.setMaterial(new PhongMaterial(Color.RED));
		boxZ.setMaterial(new PhongMaterial(Color.GREEN));

		boxX.setTranslateX(SIZE * 5d);
		boxY.setTranslateY(SIZE * 5d);
		boxZ.setTranslateZ(SIZE * 5d);

		root3d.getChildren().addAll(boxX, boxY, boxZ);
	}

	private Node buildToolbar()
	{
		ToolBar tools = new ToolBar();
		ComboBox<CooPalet> cbPalets = new ComboBox<CooPalet>();
		CheckBox cbShowNames = new CheckBox("Namen anzeigen");

		CheckComboBox<?> elements = new CheckComboBox<>();

		// TODO $Ddd get the original list
//		Bindings.bindContent(list1, cbPalets.getItems());

		// Palet selection
		cbPalets.setPromptText("Palette auswählen...");

		tools.getItems().addAll(cbPalets, elements, cbShowNames);
		return tools;
	}

	// TODO $Ddd 23.09.15 try to avoid the need of the fix
	private void fixCameraBug()
	{
		try
		{
			// don't use the return of the remove-call as afterwars createInverse() could throw an
			// exception. This way at least the original transform stays in the camera if an
			// exception occurs
			Transform t = CooDddUtil.getTransformFor(VIEW_SYSTEM).createInverse();

			// the coordsystem-transform is the first one added.
			// as we added the pivot-transform before it is now the second
			// so remove index 1
			camera.getTransforms().remove(1);
			// as the rotation got is from the view of the camera, we have to invert it to get the
			// same result when adding it to the world
			root3d.getTransforms().add(t);
		}
		catch(NonInvertibleTransformException e)
		{
			CooLog.error("Unable to fix-camera-bug, scene may look black now", e);
		}
	}
}
