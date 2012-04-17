import java.io.PrintStream;
import java.util.Date;

import geel.RMotor;
import geel.RobotSpecs;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.comm.RConsole;
import lejos.util.ButtonCounter;



/**
 * small program that periodically samples the light-sensor raw value
 * and sends them over the bluetooth connection to a FileLogger
 *
 * @author jeroendv
 *
 */
public class LightLog {

	
	public static void main(String[] args) throws InterruptedException {
	
	//fixme: add a FileLogger class and devise a short Protocol
	// that allows to set the file name, the file descriptor and the time stamped data
	
		RConsole.openBluetooth(0);
		System.setOut(new PrintStream(RConsole.openOutputStream()));
		
		RMotor motorLeft = new RMotor(RobotSpecs.leftMotorPort);
		RMotor motorRight = new RMotor(RobotSpecs.rightMotorPort);
		
		LightSensor light = new LightSensor(RobotSpecs.lightSensorPort,true);
		
		
		
		ButtonCounter buttonCounter = new ButtonCounter();
		buttonCounter.count("left and right speed [-9,9]");
		
		
		motorLeft.setSpeed(buttonCounter.getLeftCount()*100);
		motorRight.setSpeed(buttonCounter.getRightCount()*100);
		
		motorLeft.forward();
		motorRight.forward();
		
		Thread.sleep(1000);
		
		System.out.println("# "+ new Date());
		
		long startTime = System.currentTimeMillis();
		while(!Button.ESCAPE.isPressed()){
			System.out.println((System.currentTimeMillis()-startTime)+", "+light.getNormalizedLightValue());
			Thread.sleep(100);
		}

		
	}
}
