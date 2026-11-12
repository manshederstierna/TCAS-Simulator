package tcas;

public class TCAS{
	public static AircraftState interpret(CollisionPrediction prediction){
		if(!prediction.conflict()){
			return AircraftState.NORMAL;
		} 
		
		if (prediction.timeToClosestApproach() < 20) {
			return AircraftState.RESOLUTION_ADVISORY;
		}

		if (prediction.timeToClosestApproach() < 60) {
			return AircraftState.TRAFFIC_ADVISORY;
		}
			
		return AircraftState.RESOLUTION_ADVISORY;
	}
}