package geel;

import geel.BTGW.infrastructure.BTGWConnection;
import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;
import geel.BTGW.packets.BTGWPacketPong;
import geel.BTGW.packets.BTGWPacketStatusUpdate;
import geel.BTGW.robot.BTGWRealConnectionTaker;
import geel.behaviours.Manual;
import geel.behaviours.MuurvolgerBehavior;
import geel.behaviours.TouchBehavior;
import geel.sensorProcessing.LightColorIdentification;
import geel.sensorProcessing.LightSensorReader;
import geel.sensorProcessing.SensorBTGWLogger;
import geel.sensorProcessing.SonarSensorReader;
import lejos.nxt.LightSensor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class BarCodeReader {
	
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
	
	
	public static void main(String args[]) throws Exception {

		// initialize BTgateway
		initilizeBTGateWay();
		addBackgroundBTGWListeners();
		
		//blocks till gateway is up and running
		
		
		TouchSensor touchSensor = new TouchSensor(RobotSpecs.touchSensorFrontPort);
		
		// create light sensor processing chain
		LightSensorReader lightSensorReader = new LightSensorReader(RobotSpecs.lightSensorPort);
		LightColorIdentification lightColorIdentification = new LightColorIdentification(lightSensorReader);
		
		
		
		// create sonar sensor processing chain
		SonarSensorReader sonarSensorReader = new SonarSensorReader(RobotSpecs.sonarSensorPort);
		//fixme: add the processed data producer
		new SensorBTGWLogger(lightSensorReader,lightColorIdentification, sonarSensorReader,sonarSensorReader,touchSensor);
		
		
		//add configurable classes to the configurator
		RobotBTGWConfigurator.register(lightColorIdentification);
		RobotBTGWConfigurator.registerWithBTGW();
		
		// start the light and sonar readers that drive the sensor processing chain
		lightSensorReader.start();
		sonarSensorReader.start();

		/*
		 * instantiate an array of behaviors that will control the robot from
		 * lowest to highest behavior priority
		 */
		Behavior[] bArray = new Behavior[] {
				new Manual(motorRight, motorLeft),
			};

		/* instantiate an arbitrator */
		System.out.println("program starting");

		(new Arbitrator(bArray)).start();
		
		//FIXME: the arbitrator never returns so only a ugly forced System.exit(0) can be used kill the program
		
		// stop the light and sonar readers that drive the sensor processing chain
		lightSensorReader.stop();
		sonarSensorReader.stop();
	}
	
	
	/**
	 * add background listerners to the BTGateway
	 */
	private static void addBackgroundBTGWListeners() {
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
	 * initialize a Bluetooth Gateway for communication between robot and pc and
	 * make it globally accessible through BTGateWay.getinstance()
	 * 
	 * note that this methods blocks until the gate way is ready for use
	 */
	private static void initilizeBTGateWay() {
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
