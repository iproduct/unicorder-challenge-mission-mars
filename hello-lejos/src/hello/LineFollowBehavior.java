package hello;

public class LineFollowBehavior implements Behavior {
	private double distance = 10;
	
	public LineFollowBehavior(double distance) {
		this.distance = distance;
	}

	@Override
	public MovementStatus execute(Robot robot) {
		
		return MovementStatus.OK;
	}

}
