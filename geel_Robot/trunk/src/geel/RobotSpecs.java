package geel;
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
	 * specify the to which side the sonar is pointed
	 */
	public static boolean sonarPointsLeft = true;
	
    
	/**
	 * specify the port to which each motor is attached
	 */
    
    public  static TachoMotorPort  leftMotorPort = MotorPort.A;
    public  static TachoMotorPort  rightMotorPort = MotorPort.C;
    
    /**
     * specify the port to which each sensor is attached
     */
    public  static SensorPort lightSensorPort = SensorPort.S1;
    public  static SensorPort touchSensorFrontPort = SensorPort.S2;
    public  static SensorPort sonarSensorPort = SensorPort.S3;
    
    

}
