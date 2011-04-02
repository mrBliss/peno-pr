package geel.sensorProcessing;

import geel.RobotSpecs;
import geel.TrackSpecs;
import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.packets.BTGWPacketMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lejos.nxt.Motor;

/**
 * This class will scan for barcodes based on a stream of color samples
 * which he gets from a {@link LightColorIdentification} object.
 * 
 * the start of a barcode is detected if a black or white sample arrives
 * at which point we start buffering the white and black samples and the 
 * respective tacho count of the weels until the end of the barcode is detected.
 * 
 * a barcode ends if a certain number of consecutive brown samples are measured,
 * at which point we check if the buffered list of black and white samples
 * could be a barcode.
 * Which is not the case if:
 *  + there are less then 7 samples
 *  + the sample length is larger then the  barcode bit width,
 *  which means that we have skipped a bit somewhere
 *  + the length of the barcode is less then 12 cm
 *  + the sample length is negative indicating that we are not traveling in a line
 * at which point we simply discard the buffered list.
 * 
 * if the list of samples is valid, we continu to split the length of the barcode
 * in 7 pieces and then organize a color popularity contest in each piece to 
 * determine the color.
 * 
 * Due to the fact that we use tacho counts and not timings, this barcode scanner
 * als works if drive over a barcode while changing speed.
 * We do however require that the barcode is crossed in a straight line!
 * 
 * note:
 * black = 1
 * white = 0
 *  
 * 
 * @author jeroendv
 *
 */
public class BarcodeScanner implements SensorDataListener{
	


	private List<Integer> colorList = new ArrayList<Integer>();
	private List<Float> distanceList = new ArrayList<Float>();
	
	/**
	 * flag indicating if this scanner is detecting a barcode
	 * and is in the process of finding it.
	 */
	private boolean barcodeDetected;
	
	/**
	 * flag inficating if the sample sequence is invalid
	 * and barcode detection may be terminated
	 * 
	 * it can only be set to true if barcodeDetected == true;
	 */
	private boolean invalidSampleSequence;
	
	
	/**
	 * The number of consecutive brown samples after which we 
	 * declare a that a barcode has ended.
	 */
	private int endBarcodeThreshold = RobotSpecs.defaultEndBarcodeThreshold;
	
	
	private int lastReadBarcode;
	private long lastReadBarcodeTimeStamp;
	
	/*
	 * references to the motors of the wheels to access tacho count values
	 * when a color sample arrives
	 */
	private Motor rMotor;
	private Motor lMotor;
	
	/*
	 * tacho count for previous color sample
	 */
	private int lastRTachocount;
	private int lastLTachocount;
	
	private int initialLTachocount;
	private int initialRTachocount;

	/**
	 * if a barcode is being detected, then this counter keeps track of the
	 * number of consecutive brown samples
	 * set and reset by bufferColorSample(..)
	 */
	private int consecutiveBrownSampleCounter;
	
	
	public BarcodeScanner(LightColorIdentification colorProducer,Motor lMotor, Motor rMotor){
		colorProducer.addListener(this);
		
		this.rMotor = rMotor;
		this.lMotor = lMotor;
	}

	/* 
	 * the barcode scanner can be in 1 of 3 states.
	 *  + no barcode is detected
	 *  + a barcode is being sampled
	 */
	@Override
	public synchronized void processNewSensorData(Long time, int value) {
		int lTacho = lMotor.getTachoCount();
		int rTacho = rMotor.getTachoCount();
		
		if(!this.barcodeDetected){
			if(value == TrackSpecs.BLACK_COLOR || value == TrackSpecs.WHITE_COLOR){
				// the start of a barcode has been detected
				BTGateway.getInstance().sendPacket(new BTGWPacketMessage("start of barcode detected"));
				
				//reset state flags
				this.barcodeDetected = true;
				this.invalidSampleSequence = false;
				
				//process sample
				this.bufferColorSample(value,lTacho,rTacho);
			}
		}else if(this.invalidSampleSequence){
			// barcode detection should be terminated
			this.colorList.clear();
			this.distanceList.clear();
			
			this.barcodeDetected = false;
			this.invalidSampleSequence = false;
			
		}else if(this.consecutiveBrownSampleCounter > this.endBarcodeThreshold){
			// color sequence is valid and the end of a barcode has been properly  detected
			
			this.deriveBarcode();
			
			//clear buffer list
			this.colorList.clear();
			this.distanceList.clear();
			
			//reset flags
			this.barcodeDetected = false;
			this.invalidSampleSequence = false;
		}else{
			// color sequence is valid and a barcode is in the process of being detected
			this.bufferColorSample(value,lTacho,rTacho);
		}
		
		
	}
	
	/**
	 * called when the start of a barcode is detected
	 * to buffer the incoming color samples.
	 * 
	 * this method will
	 *  + discard brown samples
	 *  + keep track of the travelled distance between black and white samples
	 *  + count the number of consecutive brown samples
	 * @param rTacho 
	 * @param lTacho 
	 */
	private void bufferColorSample(int color, int lTacho, int rTacho){
		
		float traveledDistance = this.traveledDistance(lTacho, rTacho);
		
		if(color == TrackSpecs.BLACK_COLOR || color == TrackSpecs.WHITE_COLOR){
			this.consecutiveBrownSampleCounter = 0;
			
					
			if( lTacho == this.lastLTachocount && rTacho == this.lastRTachocount && this.colorList.size() > 0){
				// don't buffer sample values if we are standing still
				return;
			}
			
			this.colorList.add(color);
			this.distanceList.add(traveledDistance);
		}else{
			//brown was sampled
			this.consecutiveBrownSampleCounter ++;
		}
		
		this.lastLTachocount = lTacho;
		this.lastRTachocount = rTacho;
	}
	
	/**
	 * calculate the travelled distance since the start of the barcode
	 * 
	 * @param lTacho
	 * @param rTacho
	 */
	private float traveledDistance(int lTacho,int rTacho){
		if(this.colorList.size() == 0){
			//the first sample
			this.initialLTachocount = lTacho;
			this.initialRTachocount = rTacho;
			
			
			return 0;
		}else{
			int lTachocountDelta = lTacho - initialLTachocount;
			int rTachocountDelta = rTacho - initialRTachocount;
			
			int mm2cm = 10;
			/* calc travelled distance since last sensor sample
			 * which is the mean of the travelled distance of the left and right wheel
			 */
			float distance = (float) (((rTachocountDelta + lTachocountDelta)*RobotSpecs.wheelDiameter*Math.PI)/(2*mm2cm*360.0));
			
			return distance;
		}
	}
	
	/**
	 * derive a barcode of the collorBuffer
	 * which is called when the end of a barcode has been detected
	 */
	private void deriveBarcode(){
		
		
		int bufferSize = this.colorList.size();
		float totalBarCodeLength = this.distanceList.get(bufferSize-1);
		
		BTGateway.getInstance().sendPacket(new BTGWPacketMessage("end of barcode detected, length : "+
				totalBarCodeLength+"cm, "+bufferSize+" samples"));
		
		//check if barcode is long enough
//		if(totalBarCodeLength < 12){
//			/* to short to be a barcode
//			 * do not update the lastReadBarcode
//			 */
//			BTGateway.getInstance().sendPacket(new BTGWPacketMessage("invalid sample sequence: barcode is not long enough "
//					+totalBarCodeLength+"cm, "+bufferSize+" samples"));
//			return;
//		}
		
		//assign Tuples to pieces
		Integer[] whiteCount = new Integer[7];
		Integer[] blackCount = new Integer[7];
		
		for(int i = 0; i <7 ; i++){
			whiteCount[i] = new Integer(0);
			blackCount[i] = new Integer(0);
		}
		
		
		for(int i=0; i < bufferSize; i++){
			int color = this.colorList.get(i);
			float distance = this.distanceList.get(i);
			
			int pieceNb = (int) Math.floor((distance*7)/totalBarCodeLength);
			pieceNb = Math.max(0,Math.min(6, pieceNb)); // to prevent last sample from being in bin 7
			if(color == TrackSpecs.BLACK_COLOR){
				blackCount[pieceNb] = new Integer(blackCount[pieceNb].intValue() +1);
			}else if(color == TrackSpecs.WHITE_COLOR){
				whiteCount[pieceNb]  = new Integer(whiteCount[pieceNb].intValue() +1);
			}
		}
		
	
		int unknowBits = 0;
		//derive barcode
		int barcode = 0x0;
		int mask = 0x1;
		for(int i = 0; i <7 ; i++){
			if(blackCount[i] > whiteCount[i]){
				barcode = barcode | mask;
				BTGateway.getInstance().sendPacket(new BTGWPacketMessage("bit "+i+" is black"));
			}else if(blackCount[i] < whiteCount[i]){
				// bit is already set
				BTGateway.getInstance().sendPacket(new BTGWPacketMessage("bit "+i+" is white"));
			}else{
				/*
				 * barcode can not be determined
				 * so do nothing
				 * 
				 */
				unknowBits ++;
				BTGateway.getInstance().sendPacket(new BTGWPacketMessage("bit "+i+" couldn't be determined white = black samples = "+blackCount[i]+"set to white"));
			}
			mask = mask << 1;
		}
		
		//nearest neighbour
		barcode = this.solomonDecode(barcode);
		
		
		// update the barcode
		
		this.lastReadBarcode = barcode;
		this.lastReadBarcodeTimeStamp = System.currentTimeMillis();
		
		
		BTGateway.getInstance().sendPacket(new BTGWPacketMessage("barcode detected: "+TrackSpecs.barcodeToString(barcode)));
		
	}
	
	//FIXME: is this correct?
	private int solomonDecode(int barcode) {
		if( TrackSpecs.isBarcodeValid(barcode))
			return barcode;
		else{
			int mask = 0x01;
			for(int i =0; i >7 ; i++){
				//perturb barcode 
				int perturbedBarcode = barcode ^ mask;
				if(TrackSpecs.isBarcodeValid(perturbedBarcode)){
					BTGateway.getInstance().sendPacket(new BTGWPacketMessage("barcode "+barcode+" was corrected to "+perturbedBarcode));
					return perturbedBarcode;
				}
				mask = mask <<1;
			}
		}
		return barcode;
		
	}
	
	public int getLastReadBarcode() {
		return lastReadBarcode;
	}

	public long getLastReadBarcodeTimeStamp() {
		return lastReadBarcodeTimeStamp;
	}

	public boolean isBarcodeDetected() {
		return barcodeDetected;
	}

}
