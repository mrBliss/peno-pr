import geel.RMotor;
import geel.RobotSpecs;
import geel.TrackSpecs;
import lejos.nxt.Button;
import lejos.nxt.Motor;


/**
 * 
 * drive over the length of one tile
 * 
 * @author jeroendv
 *
 */
public class DriveTest {
	
	
	
	public static void main(String[] args) throws InterruptedException {
		
		Motor motorLeft = new RMotor(RobotSpecs.leftMotorPort);
		Motor motorRight = new RMotor(RobotSpecs.rightMotorPort);
		
		int speed = 300;
		long timeToTravelTileMs = RobotSpecs.timeToTravel(speed, TrackSpecs.tileWidth);
		
		
		System.out.println("press enter to start test");
		Button.ENTER.waitForPress();
		

		//accelerate
		motorLeft.setSpeed(speed);
        motorRight.setSpeed(speed);
        motorLeft.forward();
        motorRight.forward();
        Thread.sleep(1000);
        
        //perform test
        int Ltacho = motorLeft.getTachoCount();
        int Rtacho = motorRight.getTachoCount();
        
        long timeTarget = System.currentTimeMillis() + timeToTravelTileMs;
        while (System.currentTimeMillis() < timeTarget){
        	Thread.yield();
        }
        
        motorLeft.stop();
        motorRight.stop();
        
        
        //print out traveled distance according to the tachometers
        System.out.println("left wheel distance:");
        System.out.println("    "+RobotSpecs.distance(motorLeft.getTachoCount()-Ltacho));
        System.out.println("right wheel distance:");
        System.out.println("    "+RobotSpecs.distance(motorRight.getTachoCount()-Rtacho));
        
        // exit program on button press
        Button.ESCAPE.waitForPress();
		
	}

}
