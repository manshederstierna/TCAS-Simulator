package main;

import simulator.*;
import java.util.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.scene.transform.Rotate;
import javafx.animation.AnimationTimer;



public class App extends Application {
	Airspace as;
	Simulation sim;
	Map<Aircraft, AircraftView> aircraftViews;
	
	@Override
	public void init() {
		this.aircraftViews = new HashMap<>();
		this.as = new Airspace();

		as.addAircraft(
			new Aircraft(
				"737",
				new Vector3(50, 50, 50),
				new Vector3(1, 0, 0),
				10,
				10
			)
		);

		as.addAircraft(
			new Aircraft(
				"777",
				new Vector3(0, 0, 0),
				new Vector3(0, 0, 1),
				10,
				10
			)
		);

		for (Aircraft airplane : as.getAircraft()) {
			aircraftViews.put(
				airplane,
				new AircraftView(airplane)
			);
		}

		this.sim = new Simulation(as);
	}
	
	private void updateGraphics() {
		for (AircraftView view : aircraftViews.values()) {
			view.update();
		}
	}

    @Override
	public void start(Stage stage) {
		Group grid = new Group();
		int size = 500;
		
		Box xAxis = new Box(size * 2, 3, 3);
		Box yAxis = new Box(3, size * 2, 3);
		Box zAxis = new Box(3, 3, size * 2);

		xAxis.setMaterial(new PhongMaterial(Color.RED));
		yAxis.setMaterial(new PhongMaterial(Color.GREEN));
		zAxis.setMaterial(new PhongMaterial(Color.BLUE));

		grid.getChildren().addAll(xAxis, yAxis, zAxis);
		PhongMaterial material = new PhongMaterial(Color.GRAY);


		AnimationTimer timer = new AnimationTimer() {
			private long previousTime = 0;

			@Override
			public void handle(long now) {
				if (previousTime == 0) {
					previousTime = now;
					return;
				}

				double dt = (now - previousTime) / 1_000_000_000.0;
				previousTime = now;

				sim.update(dt);

				updateGraphics();
			}
		};
		timer.start();
		Group aircraftGroup = new Group();

		for (AircraftView view : aircraftViews.values()) {
			aircraftGroup.getChildren().add(view.getNode());
		}
		
		Group root = new Group(grid,aircraftGroup);
		Scene scene = new Scene(root, 800, 600, true);
		scene.setFill(Color.BLACK);
		
		PerspectiveCamera camera = new PerspectiveCamera(true);

		camera.setNearClip(0.1);
		camera.setFarClip(10000);
		camera.setFieldOfView(60);

		camera.setTranslateZ(-1200);
		camera.setTranslateY(-250);
		camera.setTranslateX(-250);

		Rotate pitch = new Rotate(0, Rotate.X_AXIS);
		Rotate yaw = new Rotate(10, Rotate.Y_AXIS);

		camera.getTransforms().addAll(yaw, pitch);
		scene.setCamera(camera);
		
		stage.setScene(scene);
		stage.show();
	}

    public static void main(String[] args) {
        launch(args);
    }
}