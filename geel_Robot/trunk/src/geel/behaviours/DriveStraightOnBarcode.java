package geel.behaviours;

import geel.RobotSpecs;
import geel.sensorProcessing.BarcodeScanner;
import lejos.nxt.Motor;
import lejos.robotics.subsumption.Behavior;

/**
 * 
 * this behavior simply drives straight when on a barcode
 * @author jeroendv
 *
 */
public class DriveStraightOnBarcode implements Behavior {

	private boolean isSuppressed;
	private BarcodeScanner barcodeScanner;
	
	private Motor lMotor;
	private Motor rMotor;
	
	/*
	 * the speed of the robot when on a barcode
	 */
	private int speed= RobotSpecs.defaultBarcodeSpeed;
	

	public DriveStraightOnBarcode(BarcodeScanner barcodeScanner, Motor lMotor,
			Motor rMotor) {
		super();
		this.barcodeScanner = barcodeScanner;
		this.lMotor = lMotor;
		this.rMotor = rMotor;
	}

	/* drive straight
	 */
	@Override
	public void action() {
		this.isSuppressed = false;
		
		this.lMotor.setSpeed(this.speed);
		this.rMotor.setSpeed(this.speed);
		
		this.lMotor.forward();
		this.rMotor.forward();
		
	}

	@Override
	public void suppress() {
		this.isSuppressed = true;

	}

	/* 
	 * return true is the barcode scanner is detecting a barcode
	 */
	@Override
	public boolean takeControl() {
		return this.barcodeScanner.isBarcodeDetected();
	}

}
