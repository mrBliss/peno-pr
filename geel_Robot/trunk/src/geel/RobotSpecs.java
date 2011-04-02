package geel;
import lejos.nxt.BasicMotor;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.TachoMotorPort;


/**
 * class containing the robot specifications needed to make proper abstraction 
 * of the Hardware consisting of:
 *  + a motor for the left wheel
 *  + a motor for the right wheel
 *  + a sonar sensor on the side of the robot
 *  + a touch sensor on the front of the robot
 *  + a light sensor pointed to the ground
 * 
 *  
 * @author jeroendv
 *
 */
public class RobotSpecs {
	
	/**
	 * Wheel diameter in mm
	 */
	public static int wheelDiameter = 56;
	
	
	/**
	 * specify the to which side the sonar is pointed
	 */
	public static boolean sonarPointsLeft = true;
	
    
	/**
	 * specify the port to which each motor is attached
	 */
    
    public  static TachoMotorPort  leftMotorPort = MotorPort.A;
    public  static TachoMotorPort  rightMotorPort = MotorPort.C;
    public  static MotorPort  	   lightSource	  = MotorPort.B;
    
    /**
     * specify the port to which each sensor is attached
     */
    public  static SensorPort lightSensorPort = SensorPort.S1;
    public  static SensorPort touchSensorFrontPort = SensorPort.S2;
    public  static SensorPort sonarSensorPort = SensorPort.S3;


    /** 
     * the initial threshold values that map the light sensor values [0,1023]
     * to black, ground or white
     */
	public static int blackGroundThreshold = 300;
	public static int groundWhiteThreshold = 450;
    
    
    
    private static final int mm2cm = 10;


	/**
	 * default sample period in milli seconds
	 * 
	 * This period ensures that the light sensor sample length is smaller then 2 cm
	 * which is the width of 1 bit in the barcode
	 */
	public static final int defaultSamplePeriod = 30;
    
    
    
    /**
     * calculate time needed in millisecond to traverse a certain distance at
     * a given speed.
     * 
     * @param speed speed of the motor in degrees per second 
     * @param distance length to travers in cm
     * @return
     */
    public static long timeToTravel(int speed, int length){
    	return (long) ((length * 360 * 1000 * mm2cm)/(wheelDiameter*speed*Math.PI));
    }
    
    /**
     * 
     * calculate the traveled distance in cm given for a certain amount of wheel rotation
     * in degrees
     * 
     * @param degrees the number of degrees a wheel rotated
     * @return the distance traveled
     */
    public static float distance(int degrees){
    	return  (float) (wheelDiameter*degrees*Math.PI/(360*mm2cm));
    }
    
    

}
