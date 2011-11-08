package geel.behaviours;

import geel.Robot;
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
    
    //new and previous sonar sample value
    private int newDistance;
    private int prevDistance;
    
    // boolean indicating if a new sonar sample is available
    private boolean newSonarSampleAvailable = false;

    
    // boolean indicating if the behavior is being suppressed
    private boolean supressed = false;
    
    
    // references to the sonar sensor and robot motors  needed by this behaviour
    private UltrasonicSensor ultra;
	private Motor motorLeft;
	private Motor motorRight;
    
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
                Robot.lastDistance = newDistance;
                
                newSonarSampleAvailable = true;
                        
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
			    Robot.openSpaceCounter++;
			} else {
			    Robot.openSpaceCounter = 0;
			}
		}
    });






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
     * arc away form the wall that the sonar is pointing to.
     * 
     */
    private void arcFromWall() {
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

        //continue arc motion long enough
        int arcDurationMs = 50;
		long target = System.currentTimeMillis() + arcDurationMs;
        while (System.currentTimeMillis() < target && !supressed) {
            Thread.yield();
        }
        
        //continue straight
        motorLeft.setSpeed(900);
        motorRight.setSpeed(900);
    }

    /**
     * arc towards the wall that the sonar is pointing to.
     */
    private void arcToWall() {
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

        //continue arc motion long enough
        int arcDurationMs = 75;
		long target = System.currentTimeMillis() + arcDurationMs;
        while (System.currentTimeMillis() < target && !supressed) {
        	Thread.yield();
        }
        
        //continue straight
        motorLeft.setSpeed(900);
        motorRight.setSpeed(900);
    }

	/**
	 * make a 90 degrees turn using an arc motion in the direction that the
	 * sonar is pointed in and drive forward until the sonar again detects a
	 * wall. 
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
        
        //continue arc motion long enough
        int arcDurationMs = 800;
		long timeTarget = System.currentTimeMillis() + arcDurationMs;
        while (!supressed && System.currentTimeMillis() < timeTarget){
        	Thread.yield();
        }
        
        // drive straight until there is again a wall detected
        motorLeft.setSpeed(900);
        motorRight.setSpeed(900);
        while (newDistance > 80 && !supressed){
        	Thread.yield();
        }

    }

    /**
     * action of this behavior.
     * 
     *  + if we are to close to the wall then the robot arcs away form it.
     *  + if we are to far form the wall then the robat arcs towards it.
     *  + if we detect an open space in the sonar direction then the robot turn towards it
     *  and drives forward until it again detects a wall
     */
    @Override
    public void action() {
        
		if (isOpenSpaceDetected()) {		
            turnToOpenSpace();
        } else if (isWallToClose()) {
            arcFromWall();
        } else if (isWallToFar()) {
            arcToWall();
        }
        
        //indicate that the sonar sample has been processed.
        newSonarSampleAvailable = false;
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
		return Robot.openSpaceCounter > openSpaceCounterThreshold;
	}

    /**
     * Actie die ondernomen wordt wanneer de muurvolgerbehavior verlaten wordt
     */
    @Override
    public void suppress() {
        supressed = true;
    }

    /**
     * Beslist wanneer de muurvolgerbehavior actief wordt.
     */
    @Override
    public boolean takeControl() {
        return (newSonarSampleAvailable && ((isWallToClose() && newDistance < prevDistance) || (isWallToFar() && newDistance > prevDistance) || (Robot.openSpaceCounter > 3)));

    }
}