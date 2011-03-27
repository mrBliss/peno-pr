import geel.RobotSpecs;
import lejos.nxt.Button;
import lejos.nxt.UltrasonicSensor;


/**
 * repeatedly print the sonar value on the screen until the escape is pressed
 * 
 * @author jeroendv
 *
 */
public class SonarTest {
	
	public static void main(String[] args) throws InterruptedException {
		
		UltrasonicSensor sonar = new UltrasonicSensor(RobotSpecs.sonarSensorPort);
		
		while(! Button.ESCAPE.isPressed()){
			
			int distance = sonar.getDistance();
			System.out.println(distance);
			
			long periodMs = 200;
			Thread.sleep(periodMs);
		}
		
		
	}

}
