package tcas;

import simulator.Aircraft;

public class TCAS {
    public static AircraftState interpret(CollisionPrediction prediction) {
        if (!prediction.conflict()) {
            return AircraftState.NORMAL;
        }

        if (prediction.timeToClosestApproach() < 20) {
            return AircraftState.RESOLUTION_ADVISORY;
        }

        if (prediction.timeToClosestApproach() < 60) {
            return AircraftState.TRAFFIC_ADVISORY;
        }

        return AircraftState.NORMAL;
    }
	
	public static ResolutionPair resolve(Aircraft a, Aircraft b, CollisionPrediction prediction){
		
		if (interpret(prediction) != AircraftState.RESOLUTION_ADVISORY) {
			return new ResolutionPair(
			ResolutionType.TCAS_MAINTAIN,
			ResolutionType.TCAS_MAINTAIN
			);
		}
		
		double t = prediction.timeToClosestApproach();
		double raVerticalSpeed = 5.0; // rate of descent/ascent
		
		double aAltitudeOption1 =
			a.getPosition().getY() + raVerticalSpeed * t;

		double bAltitudeOption1 =
			b.getPosition().getY() - raVerticalSpeed * t;
			
		double separationOption1 =
			Math.abs(aAltitudeOption1 - bAltitudeOption1);
			
		double aAltitudeOption2 =
			a.getPosition().getY() - raVerticalSpeed * t;

		double bAltitudeOption2 =
			b.getPosition().getY() + raVerticalSpeed * t;

		double separationOption2 =
			Math.abs(aAltitudeOption2 - bAltitudeOption2);
			
		if(separationOption1 > separationOption2){
			return new ResolutionPair(
				ResolutionType.TCAS_CLIMB,
				ResolutionType.TCAS_DESCEND
			);
		} else{
			return new ResolutionPair(
				ResolutionType.TCAS_DESCEND,
				ResolutionType.TCAS_CLIMB
			);
		}
	}
	
}