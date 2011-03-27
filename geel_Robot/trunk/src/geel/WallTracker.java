package geel;

import geel.behaviours.MuurvolgerBehavior;
import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class WallTracker {
	/*
	 * references to robot sensory input classes
	 */
	private static UltrasonicSensor sonar = new UltrasonicSensor(RobotSpecs.sonarSensorPort);
	
	/*
	 * references to robot left and right motor classes
	 */
	private static RMotor motorLeft = new RMotor(RobotSpecs.leftMotorPort);
	private static RMotor motorRight = new RMotor(RobotSpecs.rightMotorPort);
	

    // statische variabele voor de gelezen code uit te voeren.
    // deze mogen niet private zijn, worden aangepast in barcodebehavior.
    // Variabele voor de snelheid van de robot
    private static int speed = 900;

    


    public static void main(String args[]) throws Exception {
//    	int timeout = 0;
//		RConsole.openBluetooth(timeout);
		//block until 
    	    	
        /* I'm alive :-) */
    	Sound.setVolume(8);
        Sound.beepSequenceUp();
        
        /* calibrate sensors*/
//        calibrateSonar(); 

		


        /* instantiate an array of behaviors that will control the robot
         * from lowest to highest  behavior priority
         */
        Behavior[] bArray = new Behavior[]{
        	new MuurvolgerBehavior(sonar, speed, motorRight, motorLeft),
        };
        
        
        /* instantiate an arbitrator */
        System.out.println("press enter to start");
        Button.ENTER.waitForPress();
        System.out.println("program starting");
        (new Arbitrator(bArray)).start();
    }

    /**
     * calibrate sonar sensor by resetting it.
     * note this method blocks until sonar is successfully reset
     */
	private static void calibrateSonar() {
		sonar.reset();
	}
    
   
}