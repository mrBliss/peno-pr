package geel.behaviours;


import geel.RMotor;
import geel.RobotSpecs;
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
	
	private RMotor motorLeft, motorRight;



	/**
	 * @param motorLeft 
	 * @param motorRight 
	 * 
	 */
	public TouchBehavior(TouchSensor touch, RMotor motorRight, RMotor motorLeft) {
		this.touch = touch;
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
	}

	/**
	 * move backward and turn to the side in which the sonar is not pointing
	 * 
	 *  (non-Javadoc)
	 * @see lejos.robotics.subsumption.Behavior#action()
	 */
	@Override
	public void action() {
		
		//todo: magic numbers
		
		
		motorLeft.setSpeed(900);
		motorRight.setSpeed(900);
		
		motorLeft.backward();
		motorRight.backward();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			
		}
		if(RobotSpecs.sonarPointsLeft){
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