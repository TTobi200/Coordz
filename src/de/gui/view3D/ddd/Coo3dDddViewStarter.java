package de.gui.view3D.ddd;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Coo3dDddViewStarter extends Application
{
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Scene scene = new Scene(new CooView3dDdd());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
