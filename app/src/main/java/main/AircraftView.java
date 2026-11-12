package main;

import simulator.Aircraft;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import tcas.AircraftState;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

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
	
	public void setState(AircraftState state) {
        switch (state) {
            case NORMAL:
                material.setDiffuseColor(Color.GRAY);
                break;

            case TRAFFIC_ADVISORY:
                material.setDiffuseColor(Color.YELLOW);
                break;

            case RESOLUTION_ADVISORY:
                material.setDiffuseColor(Color.RED);
                break;
        }
    }
}