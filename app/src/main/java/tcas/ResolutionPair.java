package tcas;

public record ResolutionPair(
    ResolutionType forAircraftA,
    ResolutionType forAircraftB
) {}