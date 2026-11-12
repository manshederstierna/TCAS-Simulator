package simulator;

import tcas.*;

public record EncounterResult(
    Aircraft a,
    Aircraft b,
    CollisionPrediction prediction,
    AircraftState state,
    ResolutionPair resolution
) {}