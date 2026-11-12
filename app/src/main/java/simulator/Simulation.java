package simulator;

import java.util.*;
import tcas.CollisionPrediction;
import tcas.CollisionDetector;
import tcas.TCAS;

public class Simulation{
	Airspace as;
	Map<AircraftPair, CollisionPrediction> mp = new HashMap<>();
	CollisionDetector cs = new CollisionDetector();
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
   public void update(double dt) {
    List<Aircraft> aircraft = as.getAircraft();

    for (Aircraft airplane : aircraft) {
        airplane.update(dt);
    }

    mp.clear();

    for (int i = 0; i < aircraft.size(); i++) {
        for (int j = i + 1; j < aircraft.size(); j++) {
            Aircraft a = aircraft.get(i);
            Aircraft b = aircraft.get(j);
			CollisionPrediction cp = cs.predict(a,b);
			ResolutionPair = TCAS.resolve(a,b,cp);
			
			
            // predict
            // interpret
            // resolve
        }
    }
}
	
	
	
}