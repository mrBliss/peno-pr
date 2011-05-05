package geel.behaviours;

import geel.RMotor;
import geel.RobotSpecs;
import geel.TrackSpecs;
import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.packets.BTGWPacketMessage;
import geel.sensorProcessing.BarcodeScanner;
import lejos.robotics.subsumption.Behavior;

/**
 * Behavior that will turn the corner if the next tile is a left or right turn.
 * Tile types are identified by the barcodes specified in TrackSpecs.
 * 
 * 
 * @author jeroendv
 *
 */
public class turnCorner implements Behavior {
	
	private boolean  isSuppressed;
	
	
	private int speed = RobotSpecs.defaultTurnSpeed;
	
	private int radius =  RobotSpecs.defaultTurnRadius;
	
	private int action = 0;
	
	private static final int NoAction = 0;
	private static final int TurnLeftAction = 1;
	private static final int TurnRightAction =2;
	


	private BarcodeScanner barcodeScanner;
	private long actionTimestamp = 0;
	
	private RMotor lMotor, rMotor;
	

	public turnCorner(BarcodeScanner barcodeScanner, RMotor lMotor, RMotor rMotor) {
		super();
		this.barcodeScanner = barcodeScanner;
		this.lMotor = lMotor;
		this.rMotor = rMotor;
	}
	
	
	@Override
	public void action() {
		this.isSuppressed = false;
		
		BTGateway.getInstance().sendPacket(new BTGWPacketMessage("performing turn"));
		
		//drive forward for 10 cm
        //reset to driving straight
		this.lMotor.setSpeed(speed);
		this.rMotor.setSpeed(speed);
		this.lMotor.forward();
		this.rMotor.forward();
		
		/* wait long enough to drive forward for 10 cm
		 * but break immediately if suppressed
		 */
		int distance = 10;
		int DurationMs = (int) ((distance*2*180*10*1000)/(RobotSpecs.wheelDiameter*this.speed*Math.PI));
		long timeTarget = System.currentTimeMillis() + DurationMs;
        while (!isSuppressed && System.currentTimeMillis() < timeTarget){
        	Thread.yield();
        }
		
		//make turn of 90 degrees with this.radius
		int speedL = (this.speed*(this.radius -RobotSpecs.WheelBasis/2))/this.radius;
		int speedH = (this.speed*(this.radius +RobotSpecs.WheelBasis/2))/this.radius;
		
		
		if(action == TurnLeftAction){
			this.lMotor.setSpeed(speedL);
			this.rMotor.setSpeed(speedH);
		}else if(action == TurnRightAction){
			this.lMotor.setSpeed(speedH);
			this.rMotor.setSpeed(speedL);
		}else{
			//should never happen
		}
		
		this.lMotor.forward();
		this.rMotor.forward();
		
		/* wait long enough to compleet turn of 90 degrees
		 * but break immediately if suppressed
		 */
		int arcDurationMs = (this.radius*180*10*1000)/(RobotSpecs.wheelDiameter*this.speed);
		timeTarget = System.currentTimeMillis() + arcDurationMs;
        while (!isSuppressed && System.currentTimeMillis() < timeTarget){
        	Thread.yield();
        }
		
        //reset to driving straight
		this.lMotor.setSpeed(speed);
		this.rMotor.setSpeed(speed);
		
		this.actionTimestamp = System.currentTimeMillis();
		BTGateway.getInstance().sendPacket(new BTGWPacketMessage("performed turn"));
	}

	@Override
	public void suppress() {
		this.isSuppressed = true;

	}

	@Override
	public boolean takeControl() {
		/* check if the barcode is more recent then my last action, to ensure that
		 * I do turn multiple times on one tile 
		 */
		if( isRightTurnTile() && this.barcodeScanner.getLastReadBarcodeTimeStamp() > this.actionTimestamp){
			this.action = TurnRightAction;
			return true;
		}
		if( isLeftTurnTile() && this.barcodeScanner.getLastReadBarcodeTimeStamp() > this.actionTimestamp){
			this.action = TurnLeftAction;
			return true;
		}
		
		this.action = NoAction;
		return false;
	}


	private boolean isLeftTurnTile() {
		return this.barcodeScanner.getLastReadBarcode().equals(TrackSpecs.Tile.LEFT_TURN.barcode);
	}


	private boolean isRightTurnTile() {
		return this.barcodeScanner.getLastReadBarcode().equals(TrackSpecs.Tile.RIGHT_TURN.barcode);
	}

}
