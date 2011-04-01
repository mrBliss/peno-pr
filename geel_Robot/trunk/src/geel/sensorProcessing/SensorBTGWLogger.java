package geel.sensorProcessing;

import geel.BTGW.packets.BTGWPacketStatusUpdate;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.TouchSensor;

/**
 * 
 * class that listens to both 
 *  + the sonar sensor
 *  + the lightsensor 
 *  + the processed light sensor
 *  + the filtered sonar values
 *  
 *  and sends a {@link BTGWPacketStatusUpdate} each time all four are updated.
 *  
 * @author jeroendv
 *
 */
public class SensorBTGWLogger implements SensorPortListener {
	
	

	public SensorBTGWLogger(LightSensorReader lightSensorReader,
			SonarSensorReader sonarSensorReader, TouchSensor touchSensor) {
		// TODO Auto-generated constructor stub
		// TODO Add references to processes light and sonar sensor
	}

	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		// TODO Auto-generated method stub

	}
	

}
