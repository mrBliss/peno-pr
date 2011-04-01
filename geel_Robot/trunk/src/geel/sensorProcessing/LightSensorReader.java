package geel.sensorProcessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lejos.nxt.ADSensorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

/**
 * dedicated light sensor data producer that reads directly form the NXT {@link SensorPort}
 * 
 * {@link SensorDataListener} objects can register themselves if they want to
 * receive light sensor data.
 * 
 * the light sensor data is in the range [0, 1023] where 0 means dark and 1023 means bright.
 * 
 * @author jeroendv
 *
 */
public class LightSensorReader implements SensorPortListener, SensorDataProducer{
	
	private boolean isStopped;
	
	/**
	 * list of all the sensor listeners of the light sensor
	 * filled by addListeners(..)
	 */
	private List<SensorDataListener> listeners = new ArrayList<SensorDataListener>();
	
	
	/**
	 * initilize  a new {@link LightSensorReader} on a certain SensorPort
	 * @param port the port the light sensor is attached to.
	 */
	public LightSensorReader(SensorPort port){
		port.addSensorPortListener(this);
		this.isStopped = true;
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
			//normalization so that 0 = dark and 1023 is bright
			int lightValue = 1023-aNewValue;
			
			Iterator<SensorDataListener> it = this.listeners.iterator();
			while(it.hasNext()){
				SensorDataListener listener = it.next();
				
				listener.processNewSensorData(System.currentTimeMillis(),lightValue);
			}
		}
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
