package main;

import simulator.*;
import java.util.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.scene.transform.Rotate;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;


public class App extends Application {
	Airspace as;
	Simulation sim;
	Map<Aircraft, Node> aircraftModels;
	
	@Override
	public void init() {
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

		this.sim = new Simulation(as);
		this.aircraftModels = new HashMap<>();

		for (var airplane : as.getAircraft()) {
			aircraftModels.put(airplane, new Sphere(10));
		}
	}
	
	private void updateGraphics() {
		for (Aircraft aircraft : as.getAircraft()) {
			Node model = aircraftModels.get(aircraft);

			model.setTranslateX(aircraft.getPosition().getX());
			model.setTranslateY(aircraft.getPosition().getY());
			model.setTranslateZ(aircraft.getPosition().getZ());
		}
	}

    @Override
	public void start(Stage stage) {
		Group grid = new Group();
		int size = 500;
		int spacing = 50;
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

		for (Node model : aircraftModels.values()) {
			aircraftGroup.getChildren().add(model);
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