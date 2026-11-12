package tcas;

import org.junit.jupiter.api.Test;
import simulator.Aircraft;
import simulator.Vector3;

import static org.junit.jupiter.api.Assertions.*;

public class TCASTest {

    @Test
    void noConflictMeansNormal() {
        CollisionPrediction prediction =
            new CollisionPrediction(false, 10.0, 100.0);

        assertEquals(
            AircraftState.NORMAL,
            TCAS.interpret(prediction)
        );
    }

    @Test
    void conflictWithin60SecondsMeansTrafficAdvisory() {
        CollisionPrediction prediction =
            new CollisionPrediction(true, 40.0, 0.0);

        assertEquals(
            AircraftState.TRAFFIC_ADVISORY,
            TCAS.interpret(prediction)
        );
    }

    @Test
    void conflictWithin20SecondsMeansResolutionAdvisory() {
        CollisionPrediction prediction =
            new CollisionPrediction(true, 10.0, 0.0);

        assertEquals(
            AircraftState.RESOLUTION_ADVISORY,
            TCAS.interpret(prediction)
        );
    }

    @Test
    void conflictMoreThan60SecondsAwayMeansNormal() {
        CollisionPrediction prediction =
            new CollisionPrediction(true, 100.0, 0.0);

        assertEquals(
            AircraftState.NORMAL,
            TCAS.interpret(prediction)
        );
    }

    @Test
    void lowerAircraftDescendsAndUpperAircraftClimbs() {
        Aircraft a = new Aircraft(
            "A",
            new Vector3(0, 0, 0),
            new Vector3(1, 0, 0),
            10,
            10
        );

        Aircraft b = new Aircraft(
            "B",
            new Vector3(100, 10, 0),
            new Vector3(-1, 0, 0),
            10,
            10
        );

        CollisionPrediction prediction =
            new CollisionPrediction(true, 10.0, 0.0);

        ResolutionPair resolution =
            TCAS.resolve(a, b, prediction);

        assertEquals(
            ResolutionType.TCAS_DESCEND,
            resolution.forAircraftA()
        );

        assertEquals(
            ResolutionType.TCAS_CLIMB,
            resolution.forAircraftB()
        );
    }

    @Test
    void upperAircraftClimbsAndLowerAircraftDescends() {
        Aircraft a = new Aircraft(
            "A",
            new Vector3(0, 10, 0),
            new Vector3(1, 0, 0),
            10,
            10
        );

        Aircraft b = new Aircraft(
            "B",
            new Vector3(100, 0, 0),
            new Vector3(-1, 0, 0),
            10,
            10
        );

        CollisionPrediction prediction =
            new CollisionPrediction(true, 10.0, 0.0);

        ResolutionPair resolution =
            TCAS.resolve(a, b, prediction);

        assertEquals(
            ResolutionType.TCAS_CLIMB,
            resolution.forAircraftA()
        );

        assertEquals(
            ResolutionType.TCAS_DESCEND,
            resolution.forAircraftB()
        );
    }

    @Test
    void noResolutionAdvisoryMeansMaintain() {
        Aircraft a = new Aircraft(
            "A",
            new Vector3(0, 0, 0),
            new Vector3(1, 0, 0),
            10,
            10
        );

        Aircraft b = new Aircraft(
            "B",
            new Vector3(100, 10, 0),
            new Vector3(-1, 0, 0),
            10,
            10
        );

        // Conflict exists, but CPA is 40 seconds away -> TA, not RA.
        CollisionPrediction prediction =
            new CollisionPrediction(true, 40.0, 0.0);

        ResolutionPair resolution =
            TCAS.resolve(a, b, prediction);

        assertEquals(
            ResolutionType.TCAS_MAINTAIN,
            resolution.forAircraftA()
        );

        assertEquals(
            ResolutionType.TCAS_MAINTAIN,
            resolution.forAircraftB()
        );
    }
}