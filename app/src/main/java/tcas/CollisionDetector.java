package tcas;

import simulator.*;

public class CollisionDetector{
	private final double minimumDistance = 5;
	
	
	public CollisionPrediction predict(Aircraft a, Aircraft b) {
		Vector3 aPos = a.getPosition();
		Vector3 bPos = b.getPosition();

		Vector3 aVelocity = calculateVelocityVector(a);
		Vector3 bVelocity = calculateVelocityVector(b);

		Vector3 relativePosition = bPos.subtract(aPos);
		Vector3 relativeVelocity = bVelocity.subtract(aVelocity);

		double denominator = relativeVelocity.dot(relativeVelocity);

		if (denominator < 1e-9) {
			double dClosest = relativePosition.length();

			return new CollisionPrediction(
				dClosest <= minimumDistance,
				0,
				dClosest
			);
		}

		double tClosest =
			-(relativePosition.dot(relativeVelocity) / denominator);

		if (tClosest < 0) {
			tClosest = 0;
		}

		Vector3 closestVector =
			relativePosition.add(relativeVelocity.scale(tClosest));

		double dClosest = closestVector.length();

		return new CollisionPrediction(
			dClosest <= minimumDistance,
			tClosest,
			dClosest
		);
	}
	
	
	public Vector3 calculateVelocityVector(Aircraft a) {
		Vector3 horizontalVelocity =
			a.getDirection().normalize().scale(a.getSpeed());

		return new Vector3(
			horizontalVelocity.getX(),
			a.getVerticalSpeed(),
			horizontalVelocity.getZ()
		);
	}
}