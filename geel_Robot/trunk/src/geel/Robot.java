package geel;



import geel.behaviours.ArcBehavior;
import geel.behaviours.BarcodeBehavior;
import geel.behaviours.ForwardBehavior;
import geel.behaviours.Manual;
import geel.behaviours.MuurvolgerBehavior;
import geel.behaviours.TouchBehavior;
import geel.track.TrackTracker;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * The main class representing the robot itself.
 * it has references to its sensor and actuator classes and acts as the the central data storage 
 *  
 * 
 * 
 * @author jeroendv
 *
 */
public class Robot {

	/*
	 * references to robot sensory input classes
	 */
	private static LightSensor light = new LightSensor(RobotSpecs.lightSensorPort);
	private static UltrasonicSensor sonar = new UltrasonicSensor(RobotSpecs.sonarSensorPort);
	private static TouchSensor touch = new TouchSensor(RobotSpecs.touchSensorFrontPort);
	
	/*
	 * references to robot left and right motor classes
	 */
	private static RMotor motorLeft = new RMotor(RobotSpecs.leftMotorPort);
	private static RMotor motorRight = new RMotor(RobotSpecs.rightMotorPort);
	
	
	
	
	public static TrackTracker trackTracker = new TrackTracker();
	
	/*
	 * count the number of times that the sonar repeately detects a open space.
	 * (ie. distance > tileWidth)
	 * reset to zero if a wall is detected
	 */
	public static int openSpaceCounter;

	public static boolean arc = false;
	
	public static Character code;
	
	//fixme: don't use global vars
	public static int lastDistance;
   

    // statische variabele voor de gelezen code uit te voeren.
    // deze mogen niet private zijn, worden aangepast in barcodebehavior.
    // Variabele voor de snelheid van de robot
    private static int speed = 200;

    


    public static void main(String args[]) throws Exception {
//    	int timeout = 0;
//		RConsole.openBluetooth(timeout);
		//block until 
    	    	
        /* I'm alive :-) */
    	Sound.setVolume(8);
        Sound.beepSequenceUp();
        
        /* calibrate sensors*/
        calibrateLightSensor();
//        calibrateSonar(); 

		


        /* instantiate an array of behaviors that will control the robot
         * from lowest to highest  behavior priority
         */
        Behavior[] bArray = new Behavior[]{
        	new ForwardBehavior(speed, motorRight, motorLeft),
        	new BarcodeBehavior(light, speed, motorRight, motorLeft),
        	new MuurvolgerBehavior(sonar, motorRight, motorLeft),
        	new TouchBehavior(touch, motorRight, motorLeft),
        	new ArcBehavior(speed, motorRight, motorLeft, RobotSpecs.sonarPointsLeft),
        	new Manual(motorRight, motorLeft),
        };
        
        
        /* instantiate an arbitrator */
//        System.out.println("press enter to start");
//        Button.ENTER.waitForPress();
//        System.out.println("program starting");
        (new Arbitrator(bArray)).start();
    }

    /**
     * calibrate sonar sensor by resetting it.
     * note this method blocks until sonar is successfully reset
     */
	private static void calibrateSonar() {
		sonar.reset();
	}

    /**
     * interactively calibrate the light sensor 
     * by sampling a black underground and a white underground
     */
    private  static void calibrateLightSensor()  {
        // sample a black underground on 'enter'
        System.out.println("Zet de sensor op zwart en druk enter.");
        
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(light.readValue(), 1, 0);
        light.calibrateLow();       
        Sound.beep();
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//e.printStackTrace(System.out);
		}
		LCD.clear();


        // sample a white underground on 'enter'
        System.out.println("Zet de sensor op wit en druk enter.");
        
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(light.readValue(), 3, 0);
        light.calibrateHigh();
        Sound.beep();
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
        LCD.clear();
    }
    
    /**
     * interactively ask which side the sonar is pointed to.
     * 
     * note that this method must be called before the behaviours are made.
     */
    private static void setMuurvolger(){
    	boolean ready = false;
    	while(!ready){
    		System.out.println("Druk links / rechts voor de kant van de muursensor.");
            int butt = Button.waitForPress();
            if (butt == Button.ID_RIGHT) {
                System.out.println("rechtse sensor");
                RobotSpecs.sonarPointsLeft = false;
                ready = true;
            } else if (butt == Button.ID_LEFT) {
                System.out.println("linkse sensor");
                RobotSpecs.sonarPointsLeft = true;
                ready = true;  
            }
            LCD.clear();
    	}
    	LCD.clear();
    }
  
}
