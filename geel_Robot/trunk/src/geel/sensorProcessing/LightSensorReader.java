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
 * @author jeroendv
 *
 */
public class LightSensorReader implements SensorPortListener, SensorDataProducer{
	
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
	}
	
	
	/* (non-Javadoc)
	 * @see geel.sensorProcessing.SensorDataProducer#addListener(geel.sensorProcessing.SensorListener)
	 */
	public void addListener(SensorDataListener listener){
		this.listeners.add(listener);
	}

	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		Iterator<SensorDataListener> it = listeners.iterator();
		
		while(it.hasNext()){
			SensorDataListener listener = it.next();
			
			listener.processNewSensorData(System.currentTimeMillis(), aNewValue);
		}
		
	}

}
