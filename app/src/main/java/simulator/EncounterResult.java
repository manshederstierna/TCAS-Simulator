package simulator;

import tcas.*;

public record EncounterResult(
	AircraftPair pair,
    CollisionPrediction prediction,
    AircraftState state,
    ResolutionPair resolution
) {}