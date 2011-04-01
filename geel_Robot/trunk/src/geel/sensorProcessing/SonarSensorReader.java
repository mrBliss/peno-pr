package geel.sensorProcessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

/**
 * dedicated sonar sensor data producer that reads directly form the NXT {@link SensorPort}
 * 
 * {@link SensorDataListener} objects can register themselves if they want to
 * receive sonar sensor data.
 * 
 * the sonar sensor data is in the range [0, 255] and roughly corresponds to the distance in cm.
 * 
 * @author jeroendv
 *
 */
public class SonarSensorReader implements SensorDataProducer {
	
	
	/**
	 * list of all the sensor listeners of the sonar sensor
	 * filled by addListeners(..)
	 */
	private List<SensorDataListener> listeners = new ArrayList<SensorDataListener>();
	
	private UltrasonicSensor sonar ;
	
	
	/*
	 * a Runnable task that will poll the sonar sensor as fast as possible;
	 */
	private Runnable SonarPollingTask= new Runnable(){

		@Override
		public void run() {
			int distance;
			while(!isStopped){
				distance = sonar.getDistance();
				// blocks till new sonar data is available
				
				/* note:
				 * we make use of the hack in the UltrasonicSensor that limits 
				 * the sampling frequency due to weird problems otherwise
				 */
				
				informListeners(distance);
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
	 * initilize  a new {@link SonarSensorReader} on a certain SensorPort.
	 * This constructor blocks until the sonar sensor is up and running.
	 * 
	 * @param port the port the sonar sensor is attached to.
	 */
	public SonarSensorReader(SensorPort port){
		this.sonar = new UltrasonicSensor(port);
		
		this.sonar.reset();
		//reset() blocks till sonar sensor is reset.
		//ie. if no sonar sensor is attached to port then it will block indefinitely
		
		this.sonarPollingThread = null;
	}

	@Override
	public void addListener(SensorDataListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * called by SensorPollThread to inform all listeners of new data by calling 
	 * listener.processNewSensorData(..)
	 * 
	 * @param aNewValue
	 */
	private void informListeners(int aNewValue) {
		Iterator<SensorDataListener> it = listeners.iterator();
		while(it.hasNext()){
			SensorDataListener listener = it.next();
			listener.processNewSensorData(System.currentTimeMillis(), aNewValue);
		}

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
		while(this.sonarPollingThread.isAlive()){
			//give sonarPollingThread a chance to exit cleanly
			Thread.yield();
		}
		
		this.sonarPollingThread = null;
	}

}
