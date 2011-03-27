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
 * Given a sonar setpoint this behaviour will steer towards the wall if the sonar distance measure
 * is greater then the setpoint and steer away from the wall if the sonar distance measure is
 * smaller then the setpoint.
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
	 * count the number of times that the sonar repeatedly detects a open space.
	 * (ie. distance > tileWidth)
	 * reset to zero if a wall is detected
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
    	 * direction of the sonar. The count is reset once if a wall is detected.
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
    
    // boolean indication that this behaviour is busy performing an action
    // prevening this behaviour to be preemted by itself through the arbitrator.
    private boolean isActionInProgress = false;
    
    // boolean indicating if the behavior is being suppressed
    private boolean isSupressed = false;
    
    
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
        
        // behaviour is not performing actions after its created
        this.isActionInProgress = false;
        
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
    	
        return 	(isWallToClose() && isGettingCloser()) ||
        		(isWallToFar() && isGettingFurther()) ||
        		isOpenSpaceDetected();
    }
    
    /**
     * action of this behavior.
     * 
     *  + if we are to close to the wall then the robot turns away form it.
     *  + if we are to far form the wall then the robot turns towards it.
     *  + if we detect an open space in the sonar direction then the robot rotates towards it
     *  and drives forward over the length of one tile
     *  
     *  returns if suppressed or motion completed!
     *  after which the robot drives straight again.
     */
    @Override
    public void action() {
    	this.isActionInProgress = true;
    	
    	//reset suppressed flag
    	this.isSupressed = false;
        
		if (isOpenSpaceDetected()) {
			System.out.println("A: turn to sonar");
            turnToOpenSpace();
        } 
		else if (isWallToClose()) {
			System.out.println("A: turn from wall");
            turnFromWall();
        } else if (isWallToFar()) {
        	System.out.println("A: turn to wall");
            turnToWall();
        }
        
		this.isActionInProgress = false;
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
        while (System.currentTimeMillis() < target && !isSupressed) {
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
        while (System.currentTimeMillis() < target && !isSupressed) {
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
        while (!isSupressed && System.currentTimeMillis() < timeTarget){
        	Thread.yield();
        }
        
        //drive straight for the length of a tile
        //but again break if action if suppressed
        motorLeft.setSpeed(900);
        motorRight.setSpeed(900);
        
        long timeToTravelTileMs = RobotSpecs.timeToTravel(900, TrackSpecs.tileWidth);
        timeTarget = System.currentTimeMillis() + timeToTravelTileMs;
        while (!isSupressed && System.currentTimeMillis() < timeTarget){
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
		return openSpaceCounter > openSpaceCounterThreshold;
	}

    /**
     * used by the arbitrator to suppress this behaviour
     */
    @Override
    public void suppress() {
        isSupressed = true;
    }
    
    
	/**
	 * Robot is moving away from the wall the sonar is pointed to.
	 * @return
	 */
	private boolean isGettingFurther() {
		return newDistance > prevDistance;
	}
	
	/**
	 * Robot is moving towards from the wall the sonar is pointed to.
	 * @return
	 */
	private boolean isGettingCloser() {
		return newDistance < prevDistance;
	}
}