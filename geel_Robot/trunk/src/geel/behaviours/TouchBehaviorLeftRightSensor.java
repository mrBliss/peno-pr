package geel.behaviours;

import geel.RMotor;
import geel.RobotSpecs;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.Behavior;

public class TouchBehaviorLeftRightSensor implements Behavior {
	
	private RMotor motorLeft, motorRight;
	
	private TouchSensor touchLeft,touchRight;
	
	/* 
     * flag indicating the action to be performed by this behavior
     * it is set by takeControl() and used by action().
     * 
     * note that action can only called by the arbitrator if takeControl(..) returns true, 
     * at which point it also sets this flag 
     */
    private int action = noAction;
    
    //list of possible action values;
    private static final int noAction = -1;
    private static final int turnLeft= 0;
    private static final int turnRight= 1;

	public TouchBehaviorLeftRightSensor(TouchSensor touchSensorLeft,
			TouchSensor touchSensorRight, RMotor rMotor, RMotor lMotor) {
		this.motorLeft = lMotor;
		this.motorRight = rMotor;
		
		this.touchLeft = touchSensorLeft;
		this.touchRight = touchSensorRight;
	}

	@Override
	public void action() {
		
		if(this.action == turnLeft){
			turnLeft();
		}else if(this.action == turnRight){
			turnRight();
		}else{
			// problem somehow resolved itself, as the action has already been reset because
			//none of the sensor are pressed at the moment
		}

	}
	
	private void turnLeft(){
		//todo: magic numbers
		System.out.println("turn left");
		
		// stop the robot
		motorLeft.stop();
		motorRight.stop();
		
		//drive backwards
		motorLeft.setSpeed(300);
		motorRight.setSpeed(300);
		
		motorLeft.rotate(-360,true);
		motorRight.rotate(-360,true);
		
		//block till movement is complete
		while(motorLeft.isMoving() || motorRight.isMoving()){
			Thread.yield();
		}
		
		
		
		//small left turn
		motorLeft.setSpeed(100);
		motorRight.setSpeed(100);
		
		motorLeft.rotate(-90,true);
		motorRight.rotate(90,true);
		
		//block till movement is complete
		while(motorLeft.isMoving() || motorRight.isMoving()){
			Thread.yield();
		}
		
		
		//reset speed, drive forward again and release control of the motors
		motorLeft.setSpeed(700);
		motorRight.setSpeed(700);
		
		motorLeft.forward();
		motorRight.forward();
		
	}
	
	private void turnRight(){
		//todo: magic numbers
		System.out.println("turn right");
		
		// stop the robot
		motorLeft.stop();
		motorRight.stop();
		
		//drive backwards
		motorLeft.setSpeed(300);
		motorRight.setSpeed(300);
		
		motorLeft.rotate(-360,true);
		motorRight.rotate(-360,true);
		
		//block till movement is complete
		while(motorLeft.isMoving() || motorRight.isMoving()){
			Thread.yield();
		}
		
		//small right turn
		motorLeft.setSpeed(100);
		motorRight.setSpeed(100);
 
		motorLeft.rotate(90,true);
		motorRight.rotate(-90,true);
		
		//block till movement is complete
		while(motorLeft.isMoving() || motorRight.isMoving()){
			Thread.yield();
		}

		//reset speed, drive forward again and release control of the motors
		motorLeft.setSpeed(700);
		motorRight.setSpeed(700);
		
		motorLeft.forward();
		motorRight.forward();
		
	}

	@Override
	public void suppress() {
		//action is short, suppressing it is not needed
	}

	@Override
	public boolean takeControl() {
    	// check if this behavior needs to take action
    	// and set the appropriate action flag
		if(this.touchLeft.isPressed() == true){
			if(isActionAlreadySet() == false){
				//action is only set once by the touch sensor first pressed.
				this.action = turnRight;
			}
			
			return true;
		}else if(this.touchRight.isPressed() == true){
			if( isActionAlreadySet()==false){
				//action is only set once by the touch sensor first pressed.
				this.action = turnLeft;
			}
			
			return true;
		}else{
			this.action = noAction;
			return false;
		}
	}

	/**
	 * check if an action has been set.
	 * note that the action is automatically reset if both  the touchsensors 
	 * are no longer pressed.
	 * @return
	 */
	private boolean isActionAlreadySet() {
		return this.action != noAction;
	}

}
