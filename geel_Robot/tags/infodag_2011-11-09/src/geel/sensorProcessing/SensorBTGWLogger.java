package geel.sensorProcessing;

import geel.BTGW.infrastructure.BTGateway;
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
 * (requires an up and running BTGateway)
 *  
 * @author jeroendv
 *
 */
public class SensorBTGWLogger {
	
	private int color;
	private boolean updatedColor;
	
	private int lightValue;
	private boolean updatedLightValue;
	
	private int rawSonarValue;
	private boolean updatedRawSonarValue;
	
	private int processedSonarValue;
	private boolean updatedProcessedSonarValue;
	
	private TouchSensor touchSensor;
	

	public SensorBTGWLogger(LightSensorReader lightSensorReader,
			LightColorIdentification lightColorIdentification, 
			SonarSensorReader sonarSensorReader, 
			SensorDataProducer processSonarValues, 
			TouchSensor touchSensor) {
		
		// register listeners for the sensor data
		lightSensorReader.addListener(new SensorDataListener() {
			
			@Override
			public void processNewSensorData(Long time, int value) {
				setLightValue(value);
			}
		});
		
		lightColorIdentification.addListener(new SensorDataListener() {
			
			@Override
			public void processNewSensorData(Long time, int value) {
				setColor(value);
			}
		});
		
		sonarSensorReader.addListener(new SensorDataListener() {
			
			@Override
			public void processNewSensorData(Long time, int value) {
				setRawSonarValue(value);
			}
		});
		
		processSonarValues.addListener(new SensorDataListener() {
			
			@Override
			public void processNewSensorData(Long time, int value) {
				setProcessedSonarValue(value);
			}
		});
		
		this.touchSensor = touchSensor;
	}
	
	
	private void setColor(int color) {
		this.color = color;
		
		this.updatedColor = true;
		this.sendStatusUpdate();
	}


	private void setLightValue(int lightValue) {
		this.lightValue = lightValue;
		
		this.updatedLightValue = true;
		this.sendStatusUpdate();
	}


	private void setRawSonarValue(int rawSonarValue) {
		this.rawSonarValue = rawSonarValue;
		
		this.updatedRawSonarValue = true;
		this.sendStatusUpdate();
	}


	private void setProcessedSonarValue(int processedSonatValue) {
		this.processedSonarValue = processedSonatValue;
		
		this.updatedProcessedSonarValue = true;
		this.sendStatusUpdate();
	}
	
	/**
	 * Send a {@link BTGWPacketStatusUpdate} if all sensors are updated
	 */
	private synchronized void sendStatusUpdate(){
		if(	this.updatedColor && 
			this.updatedLightValue && 
			this.updatedProcessedSonarValue && 
			this.updatedRawSonarValue){
		
			// send packet
			BTGWPacketStatusUpdate p = new BTGWPacketStatusUpdate(this.color, 
					this.lightValue, 
					this.rawSonarValue, 
					this.processedSonarValue, 
					this.touchSensor.isPressed());
			BTGateway.getInstance().sendPacket(p);
			
			//reset updated flag
			this.updatedColor = false;
			this.updatedLightValue = false;
			this.updatedProcessedSonarValue = false;
			this.updatedRawSonarValue = false;
		}
	}

}
