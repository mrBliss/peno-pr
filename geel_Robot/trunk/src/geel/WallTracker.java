package geel;

import java.io.IOException;

import geel.BTGW.packets.*;
import geel.BTGW.robot.*;
import geel.BTGW.infrastructure.*;
import geel.behaviours.Manual;
import geel.behaviours.MuurvolgerBehavior;
import geel.behaviours.TouchBehavior;
import geel.sensorProcessing.LightColorIdentification;
import lejos.nxt.LightSensor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class WallTracker {

	/*
	 * references to robot sensory input classes
	 */
	private static UltrasonicSensor sonar = 
		new UltrasonicSensor(RobotSpecs.sonarSensorPort);
	private static LightSensor light = 
		new LightSensor(RobotSpecs.lightSensorPort, true);
	private static TouchSensor touch = 
		new TouchSensor(RobotSpecs.touchSensorFrontPort);

	/*
	 * references to robot left and right motor classes
	 */
	private static RMotor motorLeft = new RMotor(RobotSpecs.leftMotorPort);
	private static RMotor motorRight = new RMotor(RobotSpecs.rightMotorPort);

	// Variabele voor de snelheid van de robot
	private static int speed = 900;

	public static void main(String args[]) throws Exception {

		initilizeBTGateWay();

		addBackgroundListeners();
		

		// start a thread that periodically sends the sensory data
		Thread dataLogger = new Thread() {
			public void run() {
				while (true) {
					int _sonar = WallTracker.sonar.getDistance();
					int _light = WallTracker.light.readNormalizedValue();
					boolean _touch = WallTracker.touch.isPressed();

					int bogusGroundColor = LightColorIdentification.WHITE;
					BTGateway.getInstance().sendPacket(
							new BTGWPacketStatusUpdate(bogusGroundColor,_light, _sonar,_sonar, _touch));
					try {
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
					}
				}
			}
		};
		dataLogger.start();

		/*
		 * instantiate an array of behaviors that will control the robot from
		 * lowest to highest behavior priority
		 */
		Behavior[] bArray = new Behavior[] {
				new MuurvolgerBehavior(sonar,motorRight, motorLeft),
				new TouchBehavior(touch, motorRight, motorLeft),
				new Manual(motorRight, motorLeft)
			};

		/* instantiate an arbitrator */
		System.out.println("program starting");

		(new Arbitrator(bArray)).start();
	}

	/**
	 * add background listerners to the BTGateway
	 */
	private static void addBackgroundListeners() {
		/*
		 * when receiving a PING command: 
		 *  1) print 'ping pong' to sysout 
		 *  2) reply with a pong command
		 * 
		 * (non-Javadoc)
		 * 
		 * @see
		 * geel.BTGW.infrastructure.IBTGWCommandListener#handlePacket(geel.BTGW.
		 * packets.BTGWPacket)
		 */
		BTGateway.getInstance().addListener(BTGWPacket.CMD_PING,
				new IBTGWCommandListener() {

					@Override
					public void handlePacket(BTGWPacket packet) {
						if (packet.getCommandCode() == BTGWPacket.CMD_PING) {
							System.out.println("PING? PONG!");
							
							BTGateway.getInstance().sendPacket(
									new BTGWPacketPong());
						}

					}
				});
		
		/*
		 * kill robot program when DIE command is received
		 */
		BTGateway.getInstance().addListener(BTGWPacket.CMD_DIE,
				new IBTGWCommandListener() {

					@Override
					public void handlePacket(BTGWPacket packet) {
						if (packet.getCommandCode() == BTGWPacket.CMD_DIE) {
							System.out.println("Harakiri request!");
							// die fucker die 
							System.exit(0);
						}

					}
				});
	}

	/**
	 * calibrate sonar sensor by resetting it. note this method blocks until
	 * sonar is successfully reset
	 */
	private static void calibrateSonar() {
		sonar.reset();
	}

	/**
	 * initialize a Bluetooth Gateway for communication between robot and pc and
	 * make it globally accessible through BTGateWay.getinstance()
	 * 
	 * note that this methods blocks until the gate way is ready for use
	 */
	public static void initilizeBTGateWay() {
		// create BT connection
		BTGWRealConnectionTaker btconn = new BTGWRealConnectionTaker();
		System.out.println("Waiting for BT...");
		btconn.connect();

		// wait till BT properly connected
		while (btconn.getStatus() != BTGWConnection.STATUS_CONNECTED) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}

		// make the BTGW globally accessible through BTGateWay.getinstance()
		BTGateway.addInstance(new BTGateway(btconn));
	}

}
