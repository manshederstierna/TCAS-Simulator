package main;

import simulator.Aircraft;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class AircraftView {
    private final Aircraft aircraft;
    private final Sphere model;
    private final PhongMaterial material;

    public AircraftView(Aircraft aircraft) {
        this.aircraft = aircraft;

        this.model = new Sphere(10);

        this.material = new PhongMaterial(Color.GRAY);
        this.model.setMaterial(material);

        update();
    }

    public void update() {
        model.setTranslateX(aircraft.getPosition().getX());
        model.setTranslateY(aircraft.getPosition().getY());
        model.setTranslateZ(aircraft.getPosition().getZ());
    }

    public Node getNode() {
        return model;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }
}