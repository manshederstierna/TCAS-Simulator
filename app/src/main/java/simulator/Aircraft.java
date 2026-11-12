package simulator;

public class Aircraft {
    private final String callsign;
    private Vector3 position;
    private Vector3 direction;
    private double speed;
	private double length;
	private double verticalSpeed = 0.0;
	
    public Aircraft(
        String callsign,
        Vector3 position,
        Vector3 direction,
        double speed,
		double length
    ) {
        this.callsign = callsign;
        this.position = position;
        this.direction = direction;
        this.speed = speed;
		this.length = length;
    }
	
	public double getLength(){
		return length;
	}
	
    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }
	public void update(double dt) {
		position.addScaled(direction, speed * dt);
	}
	
	public void applyResolution(ResolutionType resolution) {
		switch (resolution) {
			case TCAS_CLIMB:
				verticalSpeed = 5.0;
				break;

			case TCAS_DESCEND:
				verticalSpeed = -5.0;
				break;

			case TCAS_MAINTAIN:
				verticalSpeed = 0.0;
				break;
		}
	}
}