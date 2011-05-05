package geel.sensorProcessing;

import geel.RMotor;
import geel.RobotSpecs;
import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.packets.BTGWPacketMessage;

/**
 * This object will check if the sensor pollspeed is to low,
 * using the time stamps on the lightsensor data and the tacho count of the motors.
 * 
 * If the sample length of the light sensor is greater then the witdh 
 * of a single bit in the barcode then we are sampling to slow and
 * it will report this through using a {@link BTGWPacketMessage}
 * 
 * @author jeroendv
 *
 */
public class LightSensorPollSpeedCheck implements SensorDataListener {
	
	private RMotor rMotor, lMotor;
	
	/*
	 * values of the left and right tacho count at the moment of the last light sensor sample
	 */
	private int lastLTachocount;
	private int lastRTachocount;
	
	public LightSensorPollSpeedCheck(LightSensorReader sensorDataProducer,RMotor lMotor, RMotor rMotor) {
		sensorDataProducer.addListener(this);
		
		this.rMotor = rMotor;
		this.lMotor = lMotor;
	}

	@Override
	public void processNewSensorData(Long time, int value) {
		int rTachocount = this.rMotor.getTachoCount();
		int lTachocount = this.lMotor.getTachoCount();
		
		int rTachocountDelta = rTachocount - lastRTachocount;
		int lTachocountDelta = lTachocount - lastLTachocount;
		
		int mm2cm = 10;
		/* calc travelled distance since last sensor sample
		 * which is the mean of the travelled distance of the left and right wheel
		 */
		float travelledDistance = (float) (((rTachocountDelta + lTachocountDelta)*RobotSpecs.wheelDiameter*Math.PI)/(2*mm2cm*360));
		
		if( Math.abs(travelledDistance) > 2 ){
			BTGateway.getInstance().sendPacket(new BTGWPacketMessage("Sample length is to large: '"+travelledDistance+"' > 2 cm"));
		}
		
		//update tacho count
		this.lastLTachocount = lTachocount;
		this.lastRTachocount = rTachocount;
	}

}
