package geel;

import geel.BTGW.infrastructure.BTGWConnection;
import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.infrastructure.IBTGWCommandListener;
import geel.BTGW.packets.BTGWPacket;
import geel.BTGW.packets.BTGWPacketPong;
import geel.BTGW.robot.BTGWRealConnectionTaker;
import geel.behaviours.Manual;
import geel.behaviours.TouchBehaviorLeftRightSensor;
import geel.behaviours.WallTracker;
import geel.sensorProcessing.LightColorIdentification;
import geel.sensorProcessing.LightSensorReader;
import geel.sensorProcessing.SensorBTGWLogger2TouchSensors;
import geel.sensorProcessing.SonarSensorReader;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class WallTrack2Touch {
	

    
    /**
     * specify the port to which each sensor is attached
     * fixme: this an abberation from RobotSpecs, 
     * i.e. bad design find something more suited to handle different robot specs
     */
    public  static SensorPort lightSensorPort = SensorPort.S1;
    public  static SensorPort touchSensorPortFrontLeft = SensorPort.S2;
    public  static SensorPort touchSensorPortFrontRight = SensorPort.S4;
    public  static SensorPort sonarSensorPort = SensorPort.S3;


    
    


	
	
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
		TouchSensor touchSensorLeft = new TouchSensor(touchSensorPortFrontLeft);
		TouchSensor touchSensorRight = new TouchSensor(touchSensorPortFrontRight);
		LightSensorReader lightSensorReader = new LightSensorReader(lightSensorPort);
		SonarSensorReader sonarSensorReader = new SonarSensorReader(sonarSensorPort);
		
		
		// create light sensor processing chain
		LightColorIdentification lightColorIdentification = new LightColorIdentification(lightSensorReader);
		
		// create sonar sensor processing chain
		//fixme: add the processed data producer
		new SensorBTGWLogger2TouchSensors(lightSensorReader, lightColorIdentification, 
										  sonarSensorReader,sonarSensorReader,
										  touchSensorLeft,touchSensorRight);
		
		
		
		
		/*******************************************
		 * behavior and arbitrator initialization
		 *******************************************/

		/*
		 * instantiate an array of behaviors that will control the robot from
		 * lowest to highest behavior priority
		 */
		Behavior[] bArray = new Behavior[] {
//				new WallTracker(sonarSensorReader,rMotor, lMotor),
				new TouchBehaviorLeftRightSensor(touchSensorLeft,touchSensorRight, rMotor, lMotor),
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
