package tcas;

import org.junit.jupiter.api.Test;
import simulator.Aircraft;
import simulator.Vector3;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionDetectorTest {

    private final CollisionDetector detector = new CollisionDetector();

    @Test
    void headOnCollision() {
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

        CollisionPrediction prediction = detector.predict(a, b);

        assertTrue(prediction.conflict());
        assertEquals(10.0, prediction.timeToClosestApproach(), 0.0001);
        assertEquals(0.0, prediction.closestDistance(), 0.0001);
    }

    @Test
    void movingAwayFromEachOther() {
        Aircraft a = new Aircraft(
            "A",
            new Vector3(-100, 0, 0),
            new Vector3(-1, 0, 0),
            10,
            10
        );

        Aircraft b = new Aircraft(
            "B",
            new Vector3(100, 0, 0),
            new Vector3(1, 0, 0),
            10,
            10
        );

        CollisionPrediction prediction = detector.predict(a, b);

        assertFalse(prediction.conflict());
        assertEquals(0.0, prediction.timeToClosestApproach(), 0.0001);
        assertEquals(200.0, prediction.closestDistance(), 0.0001);
    }

    @Test
    void sameVelocityKeepsConstantDistance() {
        Aircraft a = new Aircraft(
            "A",
            new Vector3(0, 0, 0),
            new Vector3(1, 0, 0),
            10,
            10
        );

        Aircraft b = new Aircraft(
            "B",
            new Vector3(100, 0, 0),
            new Vector3(1, 0, 0),
            10,
            10
        );

        CollisionPrediction prediction = detector.predict(a, b);

        assertFalse(prediction.conflict());
        assertEquals(0.0, prediction.timeToClosestApproach(), 0.0001);
        assertEquals(100.0, prediction.closestDistance(), 0.0001);
    }

    @Test
    void crossingPathsCollision() {
        Aircraft a = new Aircraft(
            "A",
            new Vector3(-100, 0, 0),
            new Vector3(1, 0, 0),
            10,
            10
        );

        Aircraft b = new Aircraft(
            "B",
            new Vector3(0, 0, -100),
            new Vector3(0, 0, 1),
            10,
            10
        );

        CollisionPrediction prediction = detector.predict(a, b);

        assertTrue(prediction.conflict());
        assertEquals(10.0, prediction.timeToClosestApproach(), 0.0001);
        assertEquals(0.0, prediction.closestDistance(), 0.0001);
    }

    @Test
    void nearMissIsNotCollision() {
        Aircraft a = new Aircraft(
            "A",
            new Vector3(-100, 0, 0),
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

        CollisionPrediction prediction = detector.predict(a, b);

        assertFalse(prediction.conflict());
        assertEquals(10.0, prediction.timeToClosestApproach(), 0.0001);
        assertEquals(10.0, prediction.closestDistance(), 0.0001);
    }

    @Test
    void alreadyInsideMinimumDistanceIsConflict() {
        Aircraft a = new Aircraft(
            "A",
            new Vector3(0, 0, 0),
            new Vector3(1, 0, 0),
            10,
            10
        );

        Aircraft b = new Aircraft(
            "B",
            new Vector3(3, 0, 0),
            new Vector3(1, 0, 0),
            10,
            10
        );

        CollisionPrediction prediction = detector.predict(a, b);

        assertTrue(prediction.conflict());
        assertEquals(0.0, prediction.timeToClosestApproach(), 0.0001);
        assertEquals(3.0, prediction.closestDistance(), 0.0001);
    }
}