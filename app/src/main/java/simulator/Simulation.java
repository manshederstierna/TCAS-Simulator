package simulator;

import java.util.*;
import tcas.CollisionPrediction;
import tcas.CollisionDetector;
import tcas.TCAS;
import tcas.AircraftState;
import tcas.ResolutionPair;
import tcas.ResolutionType;

public class Simulation{
	Airspace as;
	private final CollisionDetector detector = new CollisionDetector();
	List<EncounterResult> encounters = new ArrayList<>();
	int tickrate = 0;
	
	public Simulation(Airspace as){
		this.as = as;	
	}
	
	
	public void addAircraft(Aircraft ac){
		as.addAircraft(ac);
	}
	
	public void setTickrate(int n){
		tickrate = n;
	}
	
	public List<Aircraft> getAircraft(){
		return as.getAircraft();
	}
	
	public List<EncounterResult> getEncounters() {
		return encounters;
	}
	
    public void update(double dt) {
	    encounters.clear();
		List<Aircraft> aircraft = as.getAircraft();

		for (int i = 0; i < aircraft.size(); i++) {
			for (int j = i + 1; j < aircraft.size(); j++) {
				Aircraft a = aircraft.get(i);
				Aircraft b = aircraft.get(j);
				CollisionPrediction prediction =
					detector.predict(a, b);

				AircraftState state =
					TCAS.interpret(prediction);
					
				ResolutionPair resolution;
				
				if (state == AircraftState.RESOLUTION_ADVISORY) {
					resolution = TCAS.resolve(a, b, prediction);
				} else {
					resolution = new ResolutionPair(
						ResolutionType.TCAS_MAINTAIN,
						ResolutionType.TCAS_MAINTAIN
					);
				}
				
				a.applyResolution(resolution.forAircraftA());
				b.applyResolution(resolution.forAircraftB());
				
				encounters.add(
					new EncounterResult(
						new AircraftPair(a, b),
						prediction,
						state,
						resolution
					)
				);
			}
		}
		
		for (Aircraft airplane : aircraft) {
			airplane.update(dt);
		}	
    }
	
}