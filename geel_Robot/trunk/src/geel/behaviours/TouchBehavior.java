package geel.behaviours;


import geel.RMotor;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.Behavior;

/**
 * 
 */

/**
 * @author s0206928
 * 
 */
public class TouchBehavior implements Behavior {

	private TouchSensor touch;
	
	private int speed;
	private RMotor motorLeft, motorRight;

	private Boolean left;


	/**
	 * @param motorLeft 
	 * @param motorRight 
	 * 
	 */
	public TouchBehavior(TouchSensor touch, int speed, RMotor motorRight, RMotor motorLeft, Boolean left) {
		this.touch = touch;
		this.speed = speed;
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
		this.left = left;
	}

	@Override
	public void action() {
		
		
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
		
		motorLeft.backward();
		motorRight.backward();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			
		}
		if(left){
			motorLeft.forward();
    		motorRight.backward();        		
    	}
		else{
    		motorLeft.backward();
    		motorRight.forward();
		}
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			
		}

	}

	@Override
	public void suppress() {
	}

	@Override
	public boolean takeControl() {
		return touch.isPressed();
	}

}