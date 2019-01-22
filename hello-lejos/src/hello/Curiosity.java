package hello;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;

public class Curiosity {
	private EV3 ev3 = (EV3) BrickFinder.getLocal();
	private TextLCD lcd = ev3.getTextLCD();
	private Keys keys = ev3.getKeys();
	//Motors
	private RegulatedMotor mA = new EV3LargeRegulatedMotor(MotorPort.A);
	private RegulatedMotor mB = new EV3LargeRegulatedMotor(MotorPort.B);
	private RegulatedMotor mC = new EV3LargeRegulatedMotor(MotorPort.C);
	// Sensors
	private EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
	private float[] samples;
	
	public Curiosity() {
		colorSensor.setCurrentMode(colorSensor.getRGBMode().getName());
		SensorMode mode = colorSensor.getRGBMode();
		samples = new float[mode.sampleSize()];
	}
	
	public void moveForward(double centimeters) {
		int motorDegrees = (int) (30 * centimeters);
		mB.rotate(motorDegrees , true); 
		mC.rotate(motorDegrees);
	}
	
	public void moveBackward(double centimeters) {
		int motorDegrees = (int) (30 * centimeters);
		mB.rotate(-motorDegrees , true);
		mC.rotate(-motorDegrees);
	}
	
	public void turnRight(double degrees) {
		int motorDegrees = (int) (510D * degrees /90);
		mB.rotate(motorDegrees , true);
		mC.rotate(-motorDegrees);
	}
	
	public void turnLeft(double degrees) {
		int motorDegrees = (int) (510D * degrees /90);
		mB.rotate(-motorDegrees , true);
		mC.rotate(motorDegrees);
	}
	
	public void grable(double degrees) {
		mA.rotate((int) (6.8 * degrees));
	}
	
	public void goSqare(double side) {
		for(int i = 0; i < 4; i++) {
			moveForward(side);
			turnRight(90);
		}
	}
	
	// Sensor methods
	
	public float[] sampleRGBColor() {
		colorSensor.fetchSample(samples, 0);
		return samples;
	}
	
	public void printRGBColor(float[] rgb) {
		lcd.drawString("R: " + rgb[0], 0, 3);
		lcd.drawString("G: " + rgb[1], 0, 4);
		lcd.drawString("B: " + rgb[2], 0, 5);
	}

	public static void main(String[] args) {
		Curiosity robot = new Curiosity();
		float[] rgb = robot.sampleRGBColor();
		robot.printRGBColor(rgb);
		robot.keys.waitForAnyPress(50000);
	}

}
