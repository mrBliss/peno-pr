package geel.sensorProcessing;

import geel.RobotSpecs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

/**
 * dedicated light sensor data poller that reads directly from the NXT {@link SensorPort}
 * A {@link LightSensorReader} object is the root of a producer, listeners tree and as 
 * such drives the processing of the sonar data.
 * 
 * This implementation will detect changes in the light sensor output as soon as they occur
 * by directly subscribing itself as a listener with the sensor itself. 
 * But will only push those changes to its listeners at a given maximum sample frequency.
 * 
 * LightSensorReaderObjects should be the only objects that directly read the sensor.
 * Any other object that requires light sensor data should implements the {@link SensorDataListener}
 * interface and register itself with a LightSensorReader object. It should not try to access or
 * read the light sensor data in any other way.
 * 
 * the light sensor data is in the range [0, 1023] where 0 means dark and 1023 means bright.
 * 
 * note that only one LightSensorReader can be assigned to a sonar sensor.
 * 
 * @author jeroendv
 *
 */
public class LightSensorReader implements SensorPortListener, SensorDataProducer{
	
	/**
	 * a list of sensor ports to which a LightSensorReader has been assigned
	 * to prevent the creation of two {@link LightSensorReader} for the same port.
	 */
	private static ArrayList<SensorPort> assignedPorts = new ArrayList<SensorPort>();
	
	private boolean isStopped;
	
	/**
	 * the sample period in millisecond 
	 * TODO make configurable
	 */
	private int samplePeriod = RobotSpecs.defaultSamplePeriod;
	
	/**
	 * the timestamp of the most recent sample
	 * returned by System.currentTimeMillis()
	 */
	private long lastSampleTimestamp = 0;
	

	/**
	 * list of all the sensor listeners of the light sensor
	 * filled by addListeners(..)
	 */
	private List<SensorDataListener> listeners = new ArrayList<SensorDataListener>();
	
	
	/**
	 * initilize  a new {@link LightSensorReader} on a certain SensorPort
	 * 
	 * @param port the port the light sensor is attached to.
	 * 
	 * @throw IllegalArgumentException
	 * 	if the port already has SonarSensorReader assigned to it
	 */
	public LightSensorReader(SensorPort port){
		if(LightSensorReader.assignedPorts.contains(port)){
			throw new IllegalArgumentException("port "+port.getId()+" already has LightSensorReader assigned to it");
		}else{
			LightSensorReader.assignedPorts.add(port);
			
			// turn on the floodlight
			new LightSensor(port,true);
			
			port.addSensorPortListener(this);
			this.isStopped = true;
		}
	}
	
	
	/* (non-Javadoc)
	 * @see geel.sensorProcessing.SensorDataProducer#addListener(geel.sensorProcessing.SensorListener)
	 */
	public void addListener(SensorDataListener listener){
		this.listeners.add(listener);
	}

	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		if(!this.isStopped){
			long timestamp = System.currentTimeMillis();
			if(timestamp >=(this.lastSampleTimestamp+this.samplePeriod) ){
				this.lastSampleTimestamp = timestamp;
				
				//normalization so that 0 = dark and 1023 is bright
				int lightValue = 1023-aNewValue;
				
				Iterator<SensorDataListener> it = this.listeners.iterator();
				while(it.hasNext()){
					SensorDataListener listener = it.next();
					
					listener.processNewSensorData(timestamp,lightValue);
				}
			}else{
				//ignore the new value if the sample period has not yet passed.
			}
		}
	}
	
	/**
	 * @return the sample period
	 */
	public int getSamplePeriod() {
		return samplePeriod;
	}


	/**
	 * set sample period in milliseconds
	 * 
	 * @param samplePeriod
	 * @throws IllegalArgumentException
	 * 	if sampleperiod <=0
	 */
	private void setSamplePeriod(int samplePeriod) {
		if(samplePeriod <= 0){
			throw new IllegalArgumentException("sample period must be >0");
		}
		
		this.samplePeriod = samplePeriod;
	}
	/**
	 * start polling the light sensor as fast as possible
	 * multiple calls have no effect
	 */
	public void start(){
		this.isStopped = false;
	}
	
	/**
	 * stop polling the light sensor
	 */
	public void stop(){
		this.isStopped = true;
	}

}
