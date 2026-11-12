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
import tcas.AircraftState;
import javafx.scene.paint.PhongMaterial;
import tcas.AircraftState;
import tcas.ResolutionPair;
import tcas.ResolutionType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.geometry.Point3D;
import javafx.scene.SubScene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.StackPane;

public class App extends Application {
	Airspace as;
	Simulation sim;
	Map<Aircraft, AircraftView> aircraftViews;
	private Map<Aircraft, Label> aircraftLabels;
	private Pane overlay;
	@Override
	public void init() {
		this.aircraftViews = new HashMap<>();
		this.as = new Airspace();
		this.aircraftLabels = new HashMap<>();
		as.addAircraft(
			new Aircraft(
				"AFR447",
				new Vector3(-500, 150, -250),
				new Vector3(2, 0, 1),
				8,
				10
			)
		);

		as.addAircraft(
			new Aircraft(
				"KLM4805",
				new Vector3(250, 150, -500),
				new Vector3(-1, 0, 2),
				8,
				10
			)
		);
		

		for (Aircraft airplane : as.getAircraft()) {
			aircraftViews.put(
				airplane,
				new AircraftView(airplane)
			);
			
			Label label = new Label();
			label.setTextFill(Color.WHITE);
			label.setStyle(
			"-fx-font-family: monospace;" +
			"-fx-font-size: 14px;" +
			"-fx-font-weight: bold;"
			);

			aircraftLabels.put(airplane, label);
		}

		this.sim = new Simulation(as);
	}
	
	private String makeLabel(Aircraft aircraft, AircraftState state, ResolutionType resolution) {
		String callsign = aircraft.getCallsign();

		return switch (state) {
			case NORMAL ->
				callsign;

			case TRAFFIC_ADVISORY ->
				callsign + "  TA";

			case RESOLUTION_ADVISORY -> {
				String command = switch (resolution) {
					case TCAS_CLIMB -> "CLIMB";
					case TCAS_DESCEND -> "DESCEND";
					case TCAS_MAINTAIN -> "MAINTAIN";
				};

				yield callsign + "  RA  " + command;
			}
		};
	}
	
	private void updateGraphics() {

		for (AircraftView view : aircraftViews.values()) {
			view.update();
			view.setState(AircraftState.NORMAL);

			Aircraft aircraft = view.getAircraft();

			aircraftLabels.get(aircraft).setText(
				aircraft.getCallsign()
			);
		}

		for (EncounterResult encounter : sim.getEncounters()) {
			Aircraft a = encounter.pair().a();
			Aircraft b = encounter.pair().b();

			AircraftState state = encounter.state();
			ResolutionPair resolution = encounter.resolution();

			aircraftViews.get(a).setState(state);
			aircraftViews.get(b).setState(state);

			aircraftLabels.get(a).setText(
				makeLabel(
					a,
					state,
					resolution.forAircraftA()
				)
			);

			aircraftLabels.get(b).setText(
				makeLabel(
					b,
					state,
					resolution.forAircraftB()
				)
			);
		}

		for (Aircraft aircraft : as.getAircraft()) {
			AircraftView view = aircraftViews.get(aircraft);
			Label label = aircraftLabels.get(aircraft);

			Point3D screen =
				view.getModelNode().localToScene(
					Point3D.ZERO,
					true
				);

			label.relocate(
				screen.getX() + 12,
				screen.getY() - 25
			);
		}
	}

  @Override
	public void start(Stage stage) {

		/*
		 * 3D grid / axes
		 */
		Group grid = new Group();

		int size = 700;

		Box xAxis = new Box(size * 2, 3, 3);
		Box yAxis = new Box(3, size * 2, 3);
		Box zAxis = new Box(3, 3, size * 2);

		xAxis.setMaterial(new PhongMaterial(Color.RED));
		yAxis.setMaterial(new PhongMaterial(Color.GREEN));
		zAxis.setMaterial(new PhongMaterial(Color.BLUE));

		grid.getChildren().addAll(
			xAxis,
			yAxis,
			zAxis
		);


		/*
		 * Aircraft models
		 */
		Group aircraftGroup = new Group();

		for (AircraftView view : aircraftViews.values()) {
			aircraftGroup.getChildren().add(
				view.getNode()
			);
		}


		/*
		 * Complete 3D world
		 */
		Group worldRoot = new Group(
			grid,
			aircraftGroup
		);


		/*
		 * 3D SubScene
		 */
		SubScene world = new SubScene(
			worldRoot,
			800,
			600,
			true,
			SceneAntialiasing.BALANCED
		);

		world.setFill(Color.BLACK);


		/*
		 * Camera
		 */
		PerspectiveCamera camera =
			new PerspectiveCamera(true);

		camera.setNearClip(0.1);
		camera.setFarClip(10000);
		camera.setFieldOfView(60);

		camera.setTranslateZ(-1200);
		camera.setTranslateY(-250);
		camera.setTranslateX(-250);

		Rotate pitch =
			new Rotate(
				0,
				Rotate.X_AXIS
			);

		Rotate yaw =
			new Rotate(
				10,
				Rotate.Y_AXIS
			);

		camera.getTransforms().addAll(
			yaw,
			pitch
		);

		world.setCamera(camera);


		/*
		 * 2D overlay containing aircraft labels
		 */
		overlay = new Pane();

		overlay.setMouseTransparent(true);

		for (Label label : aircraftLabels.values()) {
			overlay.getChildren().add(label);
		}


		/*
		 * Stack the 2D overlay on top
		 * of the 3D SubScene.
		 */
		StackPane root = new StackPane();

		root.getChildren().addAll(
			world,
			overlay
		);


		/*
		 * Main scene
		 */
		Scene scene = new Scene(
			root,
			800,
			600
		);


		/*
		 * Simulation loop
		 */
		AnimationTimer timer = new AnimationTimer() {

			private long previousTime = 0;

			@Override
			public void handle(long now) {

				if (previousTime == 0) {
					previousTime = now;
					return;
				}

				double dt =
					(now - previousTime)
					/ 1_000_000_000.0;

				previousTime = now;

				sim.update(dt);

				updateGraphics();
			}
		};

		timer.start();


		/*
		 * Window
		 */
		stage.setTitle("TCAS Simulator");

		stage.setScene(scene);

		stage.show();
	}

    public static void main(String[] args) {
        launch(args);
    }
}