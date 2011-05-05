package geel.sensorProcessing;

import geel.RobotSpecs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

/**
 * dedicated sonar sensor data poller.
 * A {@link SonarSensorReader} object is the root of a producer, listeners tree and as 
 * such drives the processing of the sonar data.
 * 
 * This implementation will periodically poll the sonar sensor with a certain sample frequency
 * 
 * SonarSensorReader objects should be the only objects that directly read the sensor.
 * Any other object that requires sonar sensor data should implements the {@link SensorDataListener}
 * interface and register itself with a SonarSensorReader object. It should not try to access or
 * read the sonar sensor data in any other way.
 * 
 * the sonar sensor data is in the range [0, 255] and roughly corresponds to the distance in cm.
 * 
 * note that only one SonarSensorReader can be assigned to a sonar sensor.
 * 
 * 
 * @author jeroendv
 *
 */
public class SonarSensorReader implements SensorDataProducer {
	
	/**
	 * a list of sensor ports to which a SonarSensorReader has been assigned
	 * to prevent the creation of two {@link SonarSensorReader} for the same port.
	 */
	private static ArrayList<SensorPort> assignedPorts = new ArrayList<SensorPort>();
	
	
	
	/**
	 * list of all the sensor listeners of the sonar sensor
	 * filled by addListeners(..)
	 */
	private List<SensorDataListener> listeners = new ArrayList<SensorDataListener>();
	
	private UltrasonicSensor sonar;
	
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
	

	/*
	 * a Runnable task that will poll the sonar sensor as fast as possible;
	 */
	private Runnable SonarPollingTask= new Runnable(){

		@Override
		public void run() {
			int distance;
			long timestamp;
			while(!isStopped){
				timestamp = System.currentTimeMillis();
				if(timestamp >=lastSampleTimestamp+samplePeriod){
					
					distance = sonar.getDistance();
					lastSampleTimestamp = System.currentTimeMillis();
					// blocks till new sonar data is available
					
					/* note:
					 * we make use of the hack in the UltrasonicSensor that limits 
					 * the sampling frequency due to weird problems otherwise
					 */
					
					informListeners(timestamp, distance);
				}else{
					//ignore sample
					
					Delay.msDelay(lastSampleTimestamp + samplePeriod - timestamp);
				}
			}
		}
		
		
	};
	
	/**
	 * a reference to the sonarPolling thread
	 * which is null if and only if there is no sonar thread running.
	 * set and unset by start() and stop();
	 */
	private Thread sonarPollingThread;


	/**
	 * called by stop() to indicate that the SonarPollThread should cleanly die
	 */
	private boolean isStopped;
	
	
	/**
	 * initilize a new {@link SonarSensorReader} on a certain SensorPort.
	 * This constructor blocks until the sonar sensor is up and running.
	 * 
	 * @param port the port the sonar sensor is attached to.
	 * 
	 * @throw IllegalArgumentException
	 * 	if the port already has SonarSensorReader assigned to it
	 */
	public SonarSensorReader(SensorPort port){
		if(SonarSensorReader.assignedPorts.contains(port)){
			throw new IllegalArgumentException("port "+port.getId()+" already has SonarSensorReader assigned to it");
		}else{
			SonarSensorReader.assignedPorts.add(port);
			
			//initialize the sonar sensor
			this.sonar = new UltrasonicSensor(port);
			this.sonar.reset();
			
			//reset() blocks till sonar sensor is reset.
			//ie. if no sonar sensor is attached to port then it will block indefinitely
			
			this.stop();
		}
	}

	@Override
	public void addListener(SensorDataListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * called by SensorPollThread to inform all listeners of new data by calling 
	 * listener.processNewSensorData(..)
	 * @param timestamp 
	 * 
	 * @param aNewValue
	 */
	private void informListeners(long timestamp, int aNewValue) {
		Iterator<SensorDataListener> it = listeners.iterator();
		while(it.hasNext()){
			SensorDataListener listener = it.next();
			listener.processNewSensorData(timestamp, aNewValue);
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
	 * start polling the Sonar sensor as fast as possible
	 * multiple calls have no effect.
	 */
	public void start(){
		//reset flag
		this.isStopped = false;
		
		//start thread if not yet started
		if(this.sonarPollingThread == null){
			this.sonarPollingThread = new Thread(SonarPollingTask);
			this.sonarPollingThread.start();
		}else{
			//do nothing as the thread is already running
		}
	}
	
	/**
	 * Stop polling the sonar sensor.
	 * blocks until polling Thread died
	 */
	public void stop(){
		this.isStopped = true;
		
		// wait for polling thread to cleanly exit
		while(this.sonarPollingThread!= null && this.sonarPollingThread.isAlive()){
			//give sonarPollingThread a chance to exit cleanly
			Thread.yield();
		}
		
		this.sonarPollingThread = null;
	}

}
