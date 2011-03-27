import geel.RobotSpecs;
import lejos.nxt.UltrasonicSensor;


/**
 * print the sonar value on the screen
 * 
 * @author jeroendv
 *
 */
public class SonarTest {
	
	public static void main(String[] args) throws InterruptedException {
		
		UltrasonicSensor sonar = new UltrasonicSensor(RobotSpecs.sonarSensorPort);
		
		while(true){
			int distance = sonar.getDistance();
			System.out.println(distance);
			
			long periodMs = 200;
			Thread.sleep(periodMs);
		}
		
		
	}

}
