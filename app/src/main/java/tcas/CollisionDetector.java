package tcas;

import simulator.*;

public class CollisionDetector{
	private final double minimumDistance = 5;
	
	
	public CollisionPrediction predict(Aircraft a, Aircraft b){
		Vector3 aPos = a.getPosition();
		Vector3 bPos = b.getPosition();
		
		Vector3 aVelocityVector = calculateVelocityVector(a);
		Vector3 bVelocityVector = calculateVelocityVector(b);
		
		Vector3 relativePosition = bPos.subtract(aPos);
		Vector3 relativeVelocity = bVelocityVector.subtract(aVelocityVector);
		
		double tClosest = -(relativePosition.dot(relativeVelocity)/(relativeVelocity.dot(relativeVelocity)));	
		Vector3 closestVector = relativePosition.add(relativeVelocity.scale(tClosest));
		double dClosest = closestVector.length();

		boolean conflict = dClosest < minimumDistance;
		return new CollisionPrediction(conflict, tClosest, dClosest);
	}
	
	
	public Vector3 calculateVelocityVector(Aircraft a){
		return a.getDirection().normalize().scale(a.getSpeed());
	}
}