package geel;

import java.io.PrintStream;

import geel.BTGW.packets.*;
import geel.BTGW.robot.*;
import geel.BTGW.infrastructure.*;
import geel.behaviours.MuurvolgerBehavior;
import lejos.nxt.Button;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class WallTracker implements IBTGWCommandListener {
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
    	
    	//wait for remote console to connect
    	int timeout = 0;
		//RConsole.openBluetooth(timeout);
		//System.setOut(new PrintStream(RConsole.openOutputStream()));
    	  
    	BTGWRealConnectionTaker btconn = new BTGWRealConnectionTaker();
    	System.out.println("Waiting for BT...");
        btconn.connect();
        
        while(btconn.getStatus() != BTGWConnection.STATUS_CONNECTED) {
            try {
                    Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        
        BTGateway.addInstance(new BTGateway(btconn));

        /* instantiate an array of behaviors that will control the robot
         * from lowest to highest  behavior priority
         */
        Behavior[] bArray = new Behavior[]{
        	new MuurvolgerBehavior(sonar, speed, motorRight, motorLeft),
        };
        
        // Listen for pings
        BTGateway.getInstance().addListener(BTGWPacket.CMD_PING, new WallTracker());
        
        
        /* instantiate an arbitrator */
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

	public void handlePacket(BTGWPacket packet) {
		if(packet.getCommandCode() == BTGWPacket.CMD_PING) {
			System.out.println("PING? PONG!");
			BTGateway.getInstance().sendPacket(new BTGWPacketPong());
			//System.exit(0);
		}
		
	}
    
   
}
