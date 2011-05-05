package geel;


import lejos.nxt.Motor;
import lejos.nxt.TachoMotorPort;


/**
 * The RMotor class is a wrapper around the {@link Motor} class that reverses the forward and backward functional methods
 * in those cases where rotating the motors forward results in backward motion of the robot.
 * 
 * The reason that this is a wrapper and not a subclass of {@link Motor}, which would be preferable, is due to 
 * a bad implementation of {@link Motor}. The {@link Motor} class internally uses an regulator thread, which directly manipulate some fields
 * instead of using the forward() or backward()  primary methods. which means that this regulator will function incorrectly if these methods are
 * overwritten by a subclass because it is unaware of this change.
 * forcing us to make a wrapper class that does not overwrite these methods!
 * 
 * @author jeroendv
 *
 */
public class RMotor{
	
	/**
	 * reference to the motor to be controller by this object.
	 */
	private Motor motor;
	
	public RMotor(TachoMotorPort port) {
		this.motor = new Motor(port);
	}
	
	/**
	 * rotate the motor so that the robot moves forward.
	 */
	public void forward() {
		//forward motion of robot means backward motion of motor
		this.motor.backward();
	}
	/**
	 * rotate the motor so that the robot moves backward.
	 */
	public void backward() {
		//backward motion of robot means forward motion of motor
		this.motor.forward();
	}
	
	/**
	 * get the tacho count of the motor in the robots frame of reference
	 * 
	 * i.e. the tachocount increases for forward robot motions and decreases for backward robot motions
	 * 
	 * @return
	 */
	public int getTachoCount() {
		/* let 
		 * T  = tacho count in motor frame of reference
		 * T' = tacho count in the robot frame of reference
		 * then due to the inversion of forward() and backward() from the motor's to the robot's
		 * frame of reference then the following also holds
		 * 			T = -T'
		 */
		
		return - this.motor.getTachoCount();
	}
	
	
	/**
	 * rotate the motor over a given number of degrees
	 * a positive angle means forward rotation, a negative number means backward rotation
	 * 
	 * @param angle
	 */
	public void rotate(int angle,boolean immediateReturn){
		this.motor.rotate(-angle, immediateReturn);
	}

	public void resetTachoCount() {
		this.motor.resetTachoCount();
		
	}

	public void setSpeed(int i) {
		this.motor.setSpeed(i);
		
	}

	public void stop() {
		this.motor.stop();
	}

	public boolean isMoving() {
		return this.motor.isMoving();
	}

}
