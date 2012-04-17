package geel.behaviours;

import lejos.robotics.subsumption.Behavior;

/**
 * This behavior implements a pid controller for following the wall.
 * 
 * it uses the sonar distance measurement as input to control the left and right motorspeed
 *
 *
 */
public class WallPIDController implements Behavior {
	
    /* 
     * set by suppress() called by the arbitrator
     * to indicate that action() should be suppressed
     */
    private boolean suppressAction = false;

	@Override
	public void action() {
		//reset suppress flag
    	this.suppressAction = false;
		
	}

	@Override
	public void suppress() {
		this.suppressAction = true;
		
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return false;
	}

}
