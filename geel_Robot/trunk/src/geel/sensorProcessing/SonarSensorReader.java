package geel.sensorProcessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

/**
 * dedicated sonar sensor data producer that reads directly form the NXT {@link SensorPort}
 * 
 * {@link SensorDataListener} objects can register themselves if they want to
 * receive sonar sensor data.
 * 
 * @author jeroendv
 *
 */
public class SonarSensorReader implements SensorDataProducer,
		SensorPortListener {
	
	
	/**
	 * list of all the sensor listeners of the sonar sensor
	 * filled by addListeners(..)
	 */
	private List<SensorDataListener> listeners = new ArrayList<SensorDataListener>();
	
	/**
	 * initilize  a new {@link SonarSensorReader} on a certain SensorPort
	 * @param port the port the sonar sensor is attached to.
	 */
	public SonarSensorReader(SensorPort port){
		port.addSensorPortListener(this);
	}

	@Override
	public void addListener(SensorDataListener listener) {
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
