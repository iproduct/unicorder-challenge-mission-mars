package hello;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Robot {
	private EV3 ev3 = (EV3) BrickFinder.getLocal();
	private TextLCD lcd = ev3.getTextLCD();
	private Keys keys = ev3.getKeys();
	// Motors
	private RegulatedMotor mA = new EV3LargeRegulatedMotor(MotorPort.A);
	private RegulatedMotor mB = new EV3LargeRegulatedMotor(MotorPort.B);
	private RegulatedMotor mC = new EV3LargeRegulatedMotor(MotorPort.C);
	// Sensors
	private EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
	private EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
	private float[] samples;

	public Robot() {
		colorSensor.setCurrentMode(colorSensor.getRGBMode().getName());
		SensorMode mode = colorSensor.getRGBMode();
		samples = new float[mode.sampleSize()];
		touchSensor.setCurrentMode(touchSensor.getTouchMode().getName());
	}
	
	public MovementStatus execute(Behavior behavior) {
		return behavior.execute(this);
	}

	public void moveForward(double centimeters) {
		int motorDegrees = (int) (30 * centimeters);
		mB.rotate(motorDegrees, true);
		mC.rotate(motorDegrees);
	}

	public MovementStatus moveForwardWhileFootingAndNoObstacle(double centimeters) {
		int motorDegrees = (int) (30 * centimeters);
		mB.rotate(motorDegrees, true);
		mC.rotate(motorDegrees, true);
		
		// Go while there is footing
		float[] rgb;
		boolean isFooting, isObstacle;
		do {
			Delay.msDelay(20);
			rgb = sampleRGBColor();
			printRGBColor(rgb);
			isFooting = isFooting(rgb);
			isObstacle = isTouchingObstacle();
		} while(mB.isMoving() && mC.isMoving() && isFooting && !isObstacle);
		
		// else stop
		mB.stop(true);
		mC.stop(true);
		Sound.beep();
		
		if(!isFooting) return MovementStatus.NO_FOOTING;
		else if(isObstacle) return MovementStatus.OBSTACLE;
		else return MovementStatus.OK;
	}

	public void moveBackward(double centimeters) {
		int motorDegrees = (int) (30 * centimeters);
		mB.rotate(-motorDegrees, true);
		mC.rotate(-motorDegrees);
	}

	public void turnRight(double degrees) {
		int motorDegrees = (int) (510D * degrees / 90);
		mB.rotate(motorDegrees, true);
		mC.rotate(-motorDegrees);
	}

	public void turnLeft(double degrees) {
		int motorDegrees = (int) (510D * degrees / 90);
		mB.rotate(-motorDegrees, true);
		mC.rotate(motorDegrees);
	}

	public void grable(double degrees) {
		mA.rotate((int) (6.8 * degrees));
	}

	public void goSqare(double side) {
		for (int i = 0; i < 4; i++) {
			moveForward(side);
			turnRight(90);
		}
	}

	// Sensor methods

	public float[] sampleRGBColor() {
		colorSensor.fetchSample(samples, 0);
		return samples;
	}

	public boolean isTouchingObstacle() {
		touchSensor.fetchSample(samples, 0);
		return samples[0] == 1;
	}

	public void printRGBColor(float[] rgb) {
		lcd.drawString("R: " + rgb[0], 0, 3);
		lcd.drawString("G: " + rgb[1], 0, 4);
		lcd.drawString("B: " + rgb[2], 0, 5);
	}

	public boolean isFooting(float[] rgb) {
		return rgb[0] > 0.015 // Red color
				|| rgb[1] > 0.015 // Green color
				|| rgb[2] > 0.015; // Blue color
	}
	
	public void monitorObstacle() {
		boolean isObstacle;
		do {
			Delay.msDelay(20);
			isObstacle = isTouchingObstacle();
			lcd.drawString("Touch: " + isObstacle, 0, 2);
		} while (!isObstacle);
		Sound.buzz();
	}

	public static void main(String[] args) {
		Robot robot = new Robot();
		robot.execute(new GoSquareBehavior(35));
//		robot.moveForwardWhileFootingAndNoObstacle(50);
			
//		robot.moveForwardWhileFooting(50);
//		float[] rgb = robot.sampleRGBColor();
//		robot.printRGBColor(rgb);
//		robot.keys.waitForAnyPress(50000);
	}

}
