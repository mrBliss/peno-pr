package geel.sensorProcessing;

import geel.Configurable;
import geel.RobotSpecs;
import geel.TrackSpecs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * this class listeners to light sensor data [0,1023] and maps this to 3 distinct color values:
 * 	+ ground (see {@link TrackSpecs}.GROUND_COLOR)
 *  + white (see {@link TrackSpecs}.WHITE_COLOR)
 *  + black (see {@link TrackSpecs}.BLACK_COLOR)
 * based on 2 threshold values which are configurable
 * 
 *  
 * @author jeroendv
 *
 */
public class LightColorIdentification implements SensorDataListener,
		SensorDataProducer,Configurable {
	
	/**
	 * integer codes for the 3 different colors identified by the robot
	 */
	public static final int BLACK = 0;
	public static final int GROUND = 1;
	public static final int WHITE = 2;
	
	public static final String  blackGroundThresholdId = "tresholdBrownBlack";
	private int blackGroundThreshold;
	
	private static final String groungWhiteThresholdId = "tresholdWhiteBrown";
	private int groundWhiteThreshold;
	
	private List<SensorDataListener>listeners = new ArrayList<SensorDataListener>();
	
	
	/**
	 * create a new LightColorIdentification object that listeners to a SensorDataProducer
	 * of the light sensor
	 * 
	 * @param dataProducer producer of light sensor data.
	 */
	public LightColorIdentification(LightSensorReader lightSensorReader) {
		lightSensorReader.addListener(this);
		
		//set some initial threshold values
		this.blackGroundThreshold = RobotSpecs.defaultBlackGroundThreshold;
		this.groundWhiteThreshold = RobotSpecs.defaultGroundWhiteThreshold;
	}
	
	
	/********************************************************
	 * Sensor data observer pattern methods
	 ********************************************************/
	

	@Override
	public void processNewSensorData(Long time, int value) {
		// map the light sensor data [0, 1023] to a color
		int color;
		if(value >= this.groundWhiteThreshold){
			color = WHITE;
		}else if(value >= this.blackGroundThreshold){
			color = GROUND;
		}else{
			color = BLACK;
		}
		
		//propagate this new sensor data to the listeners
		Iterator<SensorDataListener> it = listeners.iterator();
		
		while(it.hasNext()){
			SensorDataListener listener = it.next();
			listener.processNewSensorData(time, color);
		}
	}

	@Override
	public void addListener(SensorDataListener listener) {
		this.listeners.add(listener);
	}

	
	
	
	/********************************************************
	 * methods for configuration of parameters
	 ********************************************************/
	
	@Override
	public boolean getConfigurableBoolean(String id)
			throws IllegalArgumentException {
		throw new IllegalArgumentException("there are no boolean parameters");
	}

	@Override
	public float getConfigurableFloat(String id)
			throws IllegalArgumentException {
		throw new IllegalArgumentException("there are no float parameters");
	}

	@Override
	public int getConfigurableInt(String id) throws IllegalArgumentException {
		if(id.equals(blackGroundThresholdId)){
			return this.blackGroundThreshold;
		}else if(id.equals(groungWhiteThresholdId)){
			return this.groundWhiteThreshold;
		}else{
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String[] listConfigurableBooleans() {
		return new String[]{};
	}

	@Override
	public String[] listConfigurableFloat() {
		return new String[]{};
	}

	@Override
	public String[] listConfigurableIntegers() {
		return new String[]{blackGroundThresholdId,groungWhiteThresholdId};
	}

	@Override
	public void setConfigurableBoolean(String id, boolean value)
			throws IllegalArgumentException {
		throw new IllegalArgumentException("there are no boolean parameters to set");
	}

	@Override
	public void setConfigurableFloat(String id, float value)
			throws IllegalArgumentException {
		throw new IllegalArgumentException("there are no float parameters to set");
		
	}

	@Override
	public void setConfigurableInt(String id, int value)
			throws IllegalArgumentException {
		if(id.equals(blackGroundThresholdId)){
			this.blackGroundThreshold = value;
		}else if(id.equals(groungWhiteThresholdId)){
			this.groundWhiteThreshold = value;
		}else{
			throw new IllegalArgumentException();
		}
		
	}

}
