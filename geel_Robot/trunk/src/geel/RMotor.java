package geel;


import lejos.nxt.Motor;
import lejos.nxt.TachoMotorPort;


public class RMotor extends Motor{

	/**
	 * Motorklasse om forward en backward in de goede richting te zetten
	 * @param port
	 */
	public RMotor(TachoMotorPort port) {
		super(port);
	}
	
    @Override
	public void forward() {
		super.backward();
	}
	
    @Override
	public void backward() {
		super.forward();
	}
	
    @Override
	public int getTachoCount() {
		return - super.getTachoCount();
	}
	
	/**
	 * rotate motor over atleast <distance> degrees
	 * positive values result in forward rotation 
	 * negative values result in backward rotation
	 * 
	 * @param distance
	 */
	public void travel(int distance) {
		resetTachoCount();
		
		if(distance > 0) {
			forward();
			while(getTachoCount() < distance){
				Thread.yield();
			}
		} else {
			backward();
			while(getTachoCount() > distance){
				Thread.yield();
			}
		}
	}

}
