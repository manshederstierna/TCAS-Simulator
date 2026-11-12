# TCAS Simulator

A real-time 3D aircraft conflict detection and resolution simulator written in Java.

The simulator models aircraft motion in three-dimensional airspace, predicts potential loss-of-separation events using closest-point-of-approach analysis, and applies simplified TCAS-inspired Traffic Advisories (TA) and Resolution Advisories (RA).

The simulation is visualized in real time using JavaFX.

## Demo

![TCAS Simulator Demo](demo.gif)

The visualization shows aircraft trajectories and TCAS state transitions:

- **White** — normal flight
- **Yellow** — Traffic Advisory (TA)
- **Red** — Resolution Advisory (RA)
- **CLIMB / DESCEND** — active vertical resolution command

When a conflict is predicted, the simulator issues coordinated vertical resolution commands and maintains them until the aircraft are safely separated.

## Features

- Real-time 3D aircraft simulation
- Aircraft position, direction, speed, and vertical speed
- Relative motion prediction
- Closest Point of Approach (CPA) calculation
- Predicted minimum separation distance
- TCAS-inspired Traffic Advisory logic
- TCAS-inspired Resolution Advisory logic
- Coordinated climb / descend commands
- Resolution persistence until safe separation
- JavaFX 3D visualization
- Aircraft callsigns and advisory status
- Persistent trajectory trails
- Automated tests for collision prediction and TCAS logic

## Collision Prediction

For two aircraft, the simulator calculates their relative position and velocity:

```text
r = positionB - positionA
v = velocityB - velocityA
```

The time to closest point of approach is calculated as:

```text
tCPA = -(r · v) / (v · v)
```

The predicted relative position at CPA is then:

```text
rCPA = r + v * tCPA
```

and the minimum predicted separation is:

```text
dCPA = |rCPA|
```

If the predicted separation falls below the configured minimum distance, the encounter is treated as a potential conflict.

## TCAS Logic

The simulator uses simplified TCAS-inspired advisory thresholds.

A predicted conflict progresses through three states:

```text
NORMAL
   ↓
TRAFFIC ADVISORY
   ↓
RESOLUTION ADVISORY
```

A Resolution Advisory evaluates coordinated vertical maneuvers for the two aircraft and assigns opposing commands such as:

```text
Aircraft A: CLIMB
Aircraft B: DESCEND
```

The selected resolution remains active until the encounter has passed and sufficient separation has been restored.

This is a simplified educational simulation and is **not an implementation of certified TCAS II logic**.

## Architecture

The project separates simulation logic from visualization.

```text
simulator/
    Aircraft
    Airspace
    Simulation
    Vector3
    AircraftPair
    EncounterResult

tcas/
    CollisionDetector
    CollisionPrediction
    TCAS
    AircraftState
    ResolutionType
    ResolutionPair

main/
    App
    AircraftView
```

### Simulation layer

`Aircraft` represents the physical state of an aircraft, while `Simulation` advances the world state and evaluates aircraft encounters.

### Collision detection

`CollisionDetector` performs relative-motion and closest-point-of-approach calculations.

### TCAS layer

`TCAS` interprets collision predictions and determines advisory states and vertical resolution commands.

### Visualization

`AircraftView` renders aircraft and trajectory trails using JavaFX while `App` manages the real-time simulation loop and user-facing visualization.

## Testing

The project includes automated tests covering scenarios such as:

- Head-on encounters
- Crossing trajectories
- Aircraft moving away from each other
- Equal-velocity aircraft
- Near misses
- Existing loss of separation
- Traffic Advisory thresholds
- Resolution Advisory thresholds
- Coordinated climb / descend selection
- End-to-end simulation behavior

Run the tests with:

```bash
./gradlew test
```

## Running

Requirements:

- Java 25
- Gradle
- JavaFX

Run using:

```bash
./gradlew run --no-configuration-cache
```
