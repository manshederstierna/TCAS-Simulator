package main;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import simulator.Aircraft;
import tcas.AircraftState;

public class AircraftView {

    private final Aircraft aircraft;

    private final Group root;
    private final Sphere model;
    private final PhongMaterial material;

    private final Deque<Sphere> trail = new ArrayDeque<>();

    private int trailCounter = 0;

    public AircraftView(Aircraft aircraft) {
        this.aircraft = aircraft;

        root = new Group();

        model = new Sphere(10);

        material = new PhongMaterial(Color.WHITE);
        model.setMaterial(material);

        root.getChildren().add(model);

        update();
    }

    public void update() {
        double x = aircraft.getPosition().getX();
        double y = aircraft.getPosition().getY();
        double z = aircraft.getPosition().getZ();

        model.setTranslateX(x);
        model.setTranslateY(-y);
        model.setTranslateZ(z);

        trailCounter++;

        if (trailCounter >= 5) {
            trailCounter = 0;
            addTrailPoint(x, y, z);
        }
    }

    private void addTrailPoint(double x, double y, double z) {
        Sphere point = new Sphere(3);

        PhongMaterial trailMaterial =
            new PhongMaterial(Color.DARKGRAY);

        point.setMaterial(trailMaterial);

        point.setTranslateX(x);
        point.setTranslateY(-y);
        point.setTranslateZ(z);

        trail.addLast(point);
        root.getChildren().add(point);
    }

    public void setState(AircraftState state) {
        switch (state) {
            case NORMAL:
                material.setDiffuseColor(Color.WHITE);
                break;

            case TRAFFIC_ADVISORY:
                material.setDiffuseColor(Color.YELLOW);
                break;

            case RESOLUTION_ADVISORY:
                material.setDiffuseColor(Color.RED);
                break;
        }
    }

    public Node getNode() {
        return root;
    }

    public Node getModelNode() {
        return model;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }
}