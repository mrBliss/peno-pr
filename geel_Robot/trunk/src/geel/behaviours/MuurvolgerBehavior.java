package geel.behaviours;

import geel.RobotSpecs;
import geel.TrackSpecs;
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
public class MuurvolgerBehavior implements Behavior {
	
	
    //set point of the sonar value to which the WallFollower should converge
    private static int sonarSetPoint = 32;
    
    //number of times an open space must be repeatedly detected for it not to be a fluke
    private static int openSpaceCounterThreshold = 3;
    
    
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
    private static final int openSpaceAction= 2;
    
	/*
	 * count the number of times that the sonar repeatedly detects a open space.
	 * reset otherwise.
	 * 
	 * see updateOpenSpaceCounter()
	 */
	private int openSpaceCounter = 0;
    
    //new and previous sonar sample value
    private int newDistance;
    private int prevDistance;
    

    /**
     * Thread that periodically samples the sonar sensor
     * keeping track of the previous and the current sensor value
     * and setting a newValue boolean to true to indicate a new sample has been taken.
     */
    private final Thread distanceSamplerThread = new Thread(new Runnable() {
    	
    	
    	public void run() {
    		long periodMs = 200;
    		while (true) {
    			// sample sonar sensor
    			prevDistance = newDistance;
    			newDistance = ultra.getDistance();
    			
    			//System.out.println(newDistance);
    			updateOpenSpaceCounter();
    			
    			try {
    				Thread.sleep(periodMs);
    			} catch (InterruptedException ex) {
    			}
    		}
    	}
    	
    	/**
    	 * Count the number of times we repeatedly see an open space in the 
    	 * direction of the sonar. 
    	 * (ie. distance > tileWidth)
    	 * The count is reset once if a wall is detected.
    	 */
    	private void updateOpenSpaceCounter() {
    		// if the sonar value if larger then the tile width then there is an open space
    		if (newDistance > TrackSpecs.tileWidth) {
    			openSpaceCounter++;
    		} else {
    			openSpaceCounter = 0;
    		}
    	}
    });
    
    /* 
     * set by suppress() called by the arbitrator
     * to indicate that action() should be suppressed
     */
    private boolean suppressAction = false;
    
    
    // references to the sonar sensor and robot motors  needed by this behaviour
    private UltrasonicSensor ultra;
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
    public MuurvolgerBehavior(UltrasonicSensor ultra, int speed, Motor motorRight, Motor motorLeft) {
        this.ultra = ultra;
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        
        //initialize the sonar old and new sample value to the setpoint
        prevDistance = sonarSetPoint;
        newDistance = sonarSetPoint;
        
        distanceSamplerThread.start();
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
    	} else if( isOpenSpaceDetected() ){
    		this.action = openSpaceAction;
    		return true;
    	}else{
    		this.action = noAction;
    		return false;
    	}
    }
    
	private long oldTiming = 0;
	private long newTiming = 0;
    
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
        
		if( this.action == openSpaceAction ) {
			oldTiming = newTiming;
			newTiming = System.currentTimeMillis();
//			System.out.println("A: turn to sonar "+ (newTiming -oldTiming));
			
            turnToOpenSpace();
            
			oldTiming = newTiming;
			newTiming = System.currentTimeMillis();
//			System.out.println("AE: turn to sonar "+ (newTiming -oldTiming));
        } 
		else if( this.action == turnFromWallAction ) {
			oldTiming = newTiming;
			newTiming = System.currentTimeMillis();
//			System.out.println("A: turn from wall "+ (newTiming -oldTiming));
			
            turnFromWall();
            
            oldTiming = newTiming;
            newTiming = System.currentTimeMillis();
//            System.out.println("AE: turn from wall " + (newTiming -oldTiming) );
        } else if( this.action == turnToWallAction ) {
			oldTiming = newTiming;
			newTiming = System.currentTimeMillis();
//			System.out.println("A: turn to wall "+ (newTiming -oldTiming));
			
            turnToWall();
            
			oldTiming = newTiming;
			newTiming = System.currentTimeMillis();
//			System.out.println("AE: turn to wall "+ (newTiming -oldTiming));
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
	 * make a 90 degrees turn using an arc motion in the direction that the
	 * sonar is pointed in and drive forward over the length of one tile.
	 * 
	 * this should be called if the sonar detects an open space, indicating 
	 * that track is turning.
	 */
    private void turnToOpenSpace() {
    	//fixme: magic numbers
        if (RobotSpecs.sonarPointsLeft) {
        	//arc to the left
        	//fixme: make abstraction of this!!
            motorLeft.setSpeed(300);
            motorRight.setSpeed(900);
        } else {
        	//arc to the right
            motorRight.setSpeed(400);
            motorLeft.setSpeed(900);
        }
        
        // move forward in case we are standing still
       	this.motorLeft.forward();
    	this.motorRight.forward();
        
        //continue motion long enough to complete arc motion
        //but break if action if suppressed
        int arcDurationMs = 800;
		long timeTarget = System.currentTimeMillis() + arcDurationMs;
        while (!suppressAction && System.currentTimeMillis() < timeTarget){
        	Thread.yield();
        }
        
        //drive straight for the length of a tile
        //but again break if action if suppressed
        motorLeft.setSpeed(900);
        motorRight.setSpeed(900);
        
        long timeToTravelTileMs = RobotSpecs.timeToTravel(900, TrackSpecs.tileWidth);
        timeTarget = System.currentTimeMillis() + timeToTravelTileMs;
        while (!suppressAction && System.currentTimeMillis() < timeTarget){
        	Thread.yield();
        }

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
	 * an open space is detected if there are enough 
	 * repeated samples greater then then tile width.
	 * 
	 * The threshold is used to filter out fluke distance measurements
	 */
	private boolean isOpenSpaceDetected() {
		//fixme: put this is seperate bahaviour?
		return openSpaceCounter > openSpaceCounterThreshold;
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