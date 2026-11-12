package simulator;

import java.util.*;

public class Airspace{
	ArrayList<Aircraft> aircrafts = new ArrayList<>();
	
	public Airspace(){}
	
	public Airspace(List<Aircraft> list){
		for(Aircraft a : list){
			aircrafts.add(a);
		}
	}
	
	public void addAircraft(Aircraft ac){
		aircrafts.add(ac);
	}
	
	public ArrayList<Aircraft> getAircraft(){
		return aircrafts;	
	}
}