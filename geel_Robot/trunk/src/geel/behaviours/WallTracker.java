package geel.behaviours;

import geel.RobotSpecs;
import geel.TrackSpecs;
import geel.sensorProcessing.SensorDataListener;
import geel.sensorProcessing.SonarSensorReader;
import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

/**
 * 
 * Behavior that will steer the robot based on the sonar input in order to 
 * keep a constant distance to the wall.
 * 
 * see takeControl() and action()
 * 
 * @author team geel
 * 
 */
public class WallTracker implements Behavior {
	
	
    //set point of the sonar value to which the WallFollower should converge
    private static int sonarSetPoint = 32;
    

    
    
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
    private static final int turnFromWallAction= 0;
    private static final int turnToWallAction= 1;
    
    
    //new and previous sonar sample value
    private int newDistance;
    private int prevDistance;
    	
    
    /* 
     * set by suppress() called by the arbitrator
     * to indicate that action() should be suppressed
     */
    private boolean suppressAction = false;
    
    
    // references to the sonar sensor and robot motors  needed by this behaviour
	private Motor motorLeft;
	private Motor motorRight;



    /**
     * Constructor
     *
     * @param ultra
     * @param motorLeft
     * @param motorRight
     * @param speed
     * @param speed
     * @param turnSpeed
     */
    public WallTracker(SonarSensorReader reader, Motor motorRight, Motor motorLeft) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        
        
        //initialize the sonar old and new sample value to the setpoint
        prevDistance = sonarSetPoint;
        newDistance = sonarSetPoint;
        
        reader.addListener(new SensorDataListener() {
        	
        	@Override
        	public void processNewSensorData(Long time, int value) {
        		//ignore sonar values of 255
        		if(value != 255){
        			prevDistance = newDistance;
        			newDistance = value;
        		}
        		
        	}
        });
    }
    
    /**
     * used by the arbitrator to check if this behavior want to be active.
     * which is the case if the robot is to far or to close to the wall
     * or detects an open space in the direction of it sonar
     */
    @Override
    public boolean takeControl() {
    	// check if this behavior needs to take action
    	// and set the appropriate action flag
    	if (isWallToClose() && isGettingCloser() ){
    		this.action = turnFromWallAction; 
    		return true;
    	} else if( isWallToFar() && isGettingFurther() ){
    		this.action = turnToWallAction; 
    		return true;
    	}else{
    		this.action = noAction;
    		return false;
    	}
    }
    
    /**
     * action of this behavior.
     *  
     * returns if suppressed or motion completed,
     * after which the robot drives straight again.
     */
    @Override
    public void action() {
    	
    	//reset suppress flag
    	this.suppressAction = false;
        
		if( this.action == turnFromWallAction ) {
            turnFromWall();
        } else if( this.action == turnToWallAction ) {
            turnToWall();
        }else{
        	System.out.println("bug: this should never happen");
        }
        
    }

    /**
     * turn away form the wall that the sonar is pointing to
     * then continue straight on
     */
    private void turnFromWall() {
    	//fixme: magic numbers
        if (RobotSpecs.sonarPointsLeft) {
        	//arc to the right
            motorLeft.setSpeed(900);
            motorRight.setSpeed(100);
        } else {
        	//arc to the left
        	motorLeft.setSpeed(100);
            motorRight.setSpeed(900);
        }
        
        // move forward in case we are standing still
       	this.motorLeft.forward();
    	this.motorRight.forward();

        //continue arc motion long enough to complete arc motion
        //but break if action if suppressed
        int arcDurationMs = 50;
		long target = System.currentTimeMillis() + arcDurationMs;
        while (!suppressAction && System.currentTimeMillis() < target) {
            Thread.yield();
        }
        
        //continue straight
        motorLeft.setSpeed(900);
        motorRight.setSpeed(900);
    }

    /**
     * turn towards the wall that the sonar is pointing to
     * then continue straight on
     */
    private void turnToWall() {
    	//fixme: magic numbers
        if (RobotSpecs.sonarPointsLeft) {
        	//arc left
            motorLeft.setSpeed(200);
            motorRight.setSpeed(900);
        } else {
        	//arc right
            motorRight.setSpeed(200);
            motorLeft.setSpeed(900);
        }
        
        // move forward in case we are standing still
       	this.motorLeft.forward();
    	this.motorRight.forward();

        //continue arc motion long enough to complete arc motion
        //but break if action if suppressed
        int arcDurationMs = 75;
		long target = System.currentTimeMillis() + arcDurationMs;
        while ( !suppressAction && System.currentTimeMillis() < target ) {
        	Thread.yield();
        }
        
        //continue straight
        motorLeft.setSpeed(900);
        motorRight.setSpeed(900);
    }



    /**
     * return true if the measured distance is larger then the distance setpoint
     */
	private boolean isWallToFar() {
		return newDistance > sonarSetPoint;
	}

	/**
	 * return true if the measured distance is smaller then the distance setpoint
	 */
	private boolean isWallToClose() {
		return newDistance < sonarSetPoint;
	}


    /**
     * used by the arbitrator to suppress this behaviour
     */
    @Override
    public void suppress() {
        this.suppressAction = true;
    }
    
    
	/**
	 * check  if robot is moving away from the wall the sonar is pointed to
	 * based on the current and previous distance sample
	 * (note that there is a certain amount of noise of distance measurement)
	 * @return
	 */
	private boolean isGettingFurther() {
		return newDistance > prevDistance;
	}
	
	/**
	 * check if robot is moving towards from the wall the sonar is pointed to.
	 * based on the current and previous distance sample
	 * (note that there is a certain amount of noise of distance measurement)
	 * @return
	 */
	private boolean isGettingCloser() {
		return newDistance < prevDistance;
	}
}