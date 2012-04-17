import lejos.nxt.ADSensorPort;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.TachoMotorPort;
import geel.RMotor;
import geel.RobotSpecs;


public class test {

	
	public static void main(String args[]) throws InterruptedException {
		RMotor motor = new RMotor(MotorPort.A);
		
		Button.waitForPress();
		
		motor.forward();
		
		for(int i =0; i<10; i++){
			System.out.println(motor.getTachoCount());
			Thread.sleep(100);
		}
		
		
		
		try {
			Thread.sleep(3*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
	}

}
