package tcas;

public record CollisionPrediction(boolean conflict, double timeToClosestApproach, double closestDistance){}