package hello;

public class LineFollowBehavior implements Behavior {
	private double distance = 10;

	public LineFollowBehavior(double distance) {
		this.distance = distance;
	}

	@Override
	public MovementStatus execute(Robot robot) {
		double distanceTraveled = 0;
		robot.getMotorB().setSpeed(150);
		robot.getMotorC().setSpeed(150);
		do {
			double angle = 15;
			MovementStatus status;
			do {
				System.out.println("Turn Left " + angle);
				status = robot.turnLeftWhileNotColor(angle, Color.RED);
				distanceTraveled += robot.getState().getLastTravelledDistance();
				if (status == MovementStatus.STOP_COLOR) break;
				
				System.out.println("Turn Right " + 2 * angle);
				status = robot.turnRightWhileNotColor(2 * angle, Color.RED);
				distanceTraveled += robot.getState().getLastTravelledDistance();
				if (status == MovementStatus.STOP_COLOR) break;

				System.out.println("Turn Left " + angle);
				status = robot.turnLeftWhileNotColor(angle, Color.RED);
				distanceTraveled += robot.getState().getLastTravelledDistance();
				if (status == MovementStatus.STOP_COLOR) break;
				
				angle += 15;
			} while (status != MovementStatus.STOP_COLOR && angle <= 90);

			if (status != MovementStatus.STOP_COLOR) {
				return MovementStatus.ERROR;
			} else {
				System.out.println("Move Forward");
				robot.moveForwardWhileNoColorAndNoObstacle(8);
				distanceTraveled += robot.getState().getLastTravelledDistance();
//				robot.moveForwardWhileNoColorAndNoObstacle(10, Color.OTHER);
//				distanceTraveled += robot.getState().getLastTravelledDistance();
			}

		} while (distanceTraveled < distance);
		return MovementStatus.OK;
	}

}
