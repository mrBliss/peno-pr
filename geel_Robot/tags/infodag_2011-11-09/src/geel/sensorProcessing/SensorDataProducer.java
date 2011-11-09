package geel.sensorProcessing;



/**
 * All sensor data are integers and this class implements the producer part of the observer pattern.
 * 
 * {@link SensorDataListener} object can register themselves 
 * and this producer will then call the  processNewSensorData(..) on each listener
 * if new sensor data is available
 * @author jeroendv
 *
 */
public interface SensorDataProducer {

	/**
	 * add a listener
	 * @param listener
	 */
	public abstract void addListener(SensorDataListener listener);

}