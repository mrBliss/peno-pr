package geel.behaviours;


import geel.RMotor;
import lejos.robotics.subsumption.Behavior;


public class ForwardBehavior implements Behavior {
	
	private int speed = 800;
	private RMotor motorLeft, motorRight;


	public ForwardBehavior( RMotor motorRight, RMotor motorLeft) {
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
	}
	
	/**
	 * Deze behavior heeft als doel
	 * de wagen eenvoudigweg rechtdoor te laten rijden.
	 */
	public void action() {	
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
		
		motorLeft.forward();
		motorRight.forward();

	}

	/**
	 * Alle motoren stillegen.
	 * Klaar voor andere behaviors.
	 */
	public void suppress() {
	}

	/**
	 * Deze behavior is de standaard behavior met laagste prioriteit.
	 * Hiervoor zal de takeControl steeds true returnen.
	 */
	public boolean takeControl() {
		return true;
	}

}
