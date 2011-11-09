package geel.sensorProcessing;

/**
 * All sensor data are integers and this class implements the listener part of the observer pattern
 * to listen to changes in the data.
 * 
 * All classes that want to receive sensor data should implement this interface and register themselves
 * with a {@link SensorDataProducer}.
 *  
 * 
 * @author jeroendv
 *
 */
public interface SensorDataListener {
	
	
	/**
	 * this method is called when new sensor data is available
	 * 
	 * @param time timestamp in ms of the sensor data
	 * @param value of the sensor data
	 */
	public void processNewSensorData(Long time, int value);

}
