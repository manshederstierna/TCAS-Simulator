package simulator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {

    @Test
    void resolutionAdvisoryCreatesVerticalSeparation() {
        Airspace airspace = new Airspace(new ArrayList<>());
        Simulation simulation = new Simulation(airspace);

        Aircraft a = new Aircraft(
            "A",
            new Vector3(-100, 0, 0),
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

        simulation.addAircraft(a);
        simulation.addAircraft(b);

        // Initially both aircraft are at the same altitude.
        assertEquals(
            0.0,
            Math.abs(
                a.getPosition().getY()
                - b.getPosition().getY()
            ),
            0.0001
        );

        // Run one second of simulation.
        simulation.update(1.0);

        double verticalSeparation =
            Math.abs(
                a.getPosition().getY()
                - b.getPosition().getY()
            );

        assertTrue(verticalSeparation > 0);
    }
}