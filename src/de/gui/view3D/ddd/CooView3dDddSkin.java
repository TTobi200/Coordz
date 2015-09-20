package de.gui.view3D.ddd;

import org.controlsfx.control.CheckComboBox;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

import de.coordz.data.base.CooPalet;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;

public class CooView3dDddSkin extends BehaviorSkinBase<CooView3dDdd, CooView3dDddBehavior>
{
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

		root = new BorderPane();

		root2d = new Group();
		root3d = new Group();

		scene2d = new SubScene(root2d, 780, 280, true, SceneAntialiasing.BALANCED);
		scene3d = new SubScene(root3d, 780, 280, true, SceneAntialiasing.BALANCED);

		buildSample();

		scene3d.setCamera(initCamera());

		root.setCenter(new StackPane(scene3d));

		root.setTop(buildToolbar());

		getChildren().add(root);
	}

	private Camera initCamera()
	{
		camera = new Coo3dDddCamera(Coo3dDddCameraTransformOrder.YXZT);

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

		scene2d.setFill(Color.RED);
		scene3d.setFill(Color.GREY);

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

		cbPalets.getSelectionModel()
			.selectedItemProperty().addListener(
				(p, o, n) -> getSkinnable().setSelPalet(n));
		getSkinnable().selPaletProperty()
			.addListener((
				p, o, n) -> cbPalets.getSelectionModel().select(n));

		// TODO $DeH get the original list
//		Bindings.bindContent(list1, cbPalets.getItems());

		// Palet selection
		cbPalets.setPromptText("Palette ausw�hlen...");

		tools.getItems().addAll(cbPalets, elements, cbShowNames);
		return tools;
	}
}