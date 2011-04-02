package geel;

import geel.BTGW.infrastructure.BTGWConnection;
import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;
import geel.BTGW.packets.BTGWPacketPong;
import geel.BTGW.robot.BTGWRealConnectionTaker;
import geel.behaviours.DriveStraightOnBarcode;
import geel.behaviours.ForwardBehavior;
import geel.behaviours.Manual;
import geel.behaviours.MuurvolgerBehavior;
import geel.behaviours.TouchBehavior;
import geel.behaviours.WallTracker;
import geel.behaviours.turnCorner;
import geel.sensorProcessing.BarcodeScanner;
import geel.sensorProcessing.LightColorIdentification;
import geel.sensorProcessing.LightSensorPollSpeedCheck;
import geel.sensorProcessing.LightSensorReader;
import geel.sensorProcessing.SensorBTGWLogger;
import geel.sensorProcessing.SonarSensorReader;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class PrRobot {

	
	
	public static void main(String args[]) throws Exception {
		//references to robot left and right motor classes 
		RMotor lMotor = new RMotor(RobotSpecs.leftMotorPort);
		RMotor rMotor = new RMotor(RobotSpecs.rightMotorPort);
		
		/*******************************************
		 * Bluetooth Gateway initialization
		 *******************************************/
		initilizeBTGateWay();
		addBackgroundBTGWListeners();
		
		//blocks till gateway is up and running
		
		
		
		/*******************************************
		 * Sensor processing chain initialization
		 *******************************************/
		// references to robot sensory input classes
		TouchSensor touchSensor = new TouchSensor(RobotSpecs.touchSensorFrontPort);
		LightSensorReader lightSensorReader = new LightSensorReader(RobotSpecs.lightSensorPort);
		SonarSensorReader sonarSensorReader = new SonarSensorReader(RobotSpecs.sonarSensorPort);
		
		
		// create light sensor processing chain
		LightColorIdentification lightColorIdentification = new LightColorIdentification(lightSensorReader);
		new LightSensorPollSpeedCheck(lightSensorReader, lMotor, rMotor);
		BarcodeScanner barcodeScanner = new BarcodeScanner(lightColorIdentification,lMotor, rMotor);
		
		// create sonar sensor processing chain
		//fixme: add the processed data producer
		new SensorBTGWLogger(lightSensorReader,lightColorIdentification, sonarSensorReader,sonarSensorReader,touchSensor);
		
		
		
		
		/*******************************************
		 * behavior and arbitrator initialization
		 *******************************************/

		/*
		 * instantiate an array of behaviors that will control the robot from
		 * lowest to highest behavior priority
		 */
		Behavior[] bArray = new Behavior[] {
				new ForwardBehavior(rMotor, lMotor),
//				new WallTracker(sonarSensorReader,rMotor, lMotor),
				new turnCorner(barcodeScanner, lMotor, rMotor),
				new DriveStraightOnBarcode(barcodeScanner, lMotor, rMotor),
				new TouchBehavior(touchSensor, rMotor, lMotor),
				new Manual(rMotor, lMotor),
			};
		
		Arbitrator arbitrator = new Arbitrator(bArray);
		
		
		
		
		/*******************************************
		 * RobotBTGWconfigurator initialization
		 *******************************************/
		RobotBTGWConfigurator.register(lightColorIdentification);
		
		RobotBTGWConfigurator.registerWithBTGW();

		
		
		
		/*******************************************
		 * starting the Robot
		 *******************************************/
		System.out.println("program starting");
		// start the light and sonar readers that drive the sensor processing chain
		lightSensorReader.start();
		sonarSensorReader.start();
		
		//start behaviour arbitrator
		arbitrator.start();
		//FIXME: the arbitrator never returns so only a ugly forced System.exit(0) can be used kill the program
		
		
		
		/*******************************************
		 * stopping the Robot
		 *******************************************/
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
