package geel.behaviours;

import geel.RMotor;
import geel.Robot;
import geel.track.TrackTracker;
import lejos.nxt.Motor;
import lejos.robotics.subsumption.Behavior;

/**
 * 
 * @author s0187958
 * 
 */
public class ArcBehavior implements Behavior {

	// De waarde van de barcode die een rechtse bocht aangeeft
	private final char rechts = '6';
	// De waarde van de barcode die een linkse bocht aangeeft
	private final char links = '3';
	private final char rechtdoor = '1';
	// De waarden van de omgekeerde barcodes
	private final char omkeren1 = '9';
	private final char omkeren2 = 'C';
	private final char omkeren3 = 'E';
	private int aantalOmgekeerd;
	private int speed;
	private RMotor motorLeft, motorRight;
	private boolean supressed = false;
	private boolean dirLeft;

	// private UltrasonicSensor ultra;
	/**
	 * Constructor
	 * 
	 * @param motorLeft
	 * @param motorRight
	 * @param pilot
	 */
	public ArcBehavior(int speed, RMotor motorRight, RMotor motorLeft,
			boolean dirLeft) {
		this.speed = speed;
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
		this.dirLeft = dirLeft;
	}

	/**
	 * Actie die ondernomen wordt wanneer de arcbehavior actief is
	 */
	@Override
	public void action() {
		// Main.muurvolgerBehavior.muur = false;
		motorLeft.resetTachoCount();
		motorRight.resetTachoCount();
		motorLeft.forward();
		motorRight.forward();

		supressed = false;

		Character ch = Robot.code;

		if (!Robot.trackTracker.getTrackCompleted()) {
			TrackTracker.updateTrack(ch);
		}

		if (ch.equals(rechts)) {
			aantalOmgekeerd = 0;
			// if (Main.bochtCounter < 4)
			// Main.bochtCounter++;
			motorLeft.setSpeed(speed);
			motorRight.setSpeed(speed - 400);
			while (motorLeft.getTachoCount() < 850 && !supressed) {
				Thread.yield();
			}
			if (!dirLeft) {
				motorRight.setSpeed(speed);
				while (motorLeft.getTachoCount() < 1250 && !supressed) {
					Thread.yield();
				}
			}
		} else if (ch.equals(links)) {
			aantalOmgekeerd = 0;
			// if (Main.bochtCounter < 4)
			// Main.bochtCounter++;
			motorLeft.setSpeed(speed - 400);
			motorRight.setSpeed(speed);
			while (motorRight.getTachoCount() < 1000 && !supressed) {
				Thread.yield();
			}
			if (dirLeft) {
				motorLeft.setSpeed(speed);
				while (motorRight.getTachoCount() < 1250 && !supressed) {
					Thread.yield();
				}
			}
		} else if (ch.equals(rechtdoor)) {
			aantalOmgekeerd = 0;
//			Main.bochtCounter = 2f;
			// do nothing
		} else if (ch.equals(omkeren1) || ch.equals(omkeren2) || ch.equals(omkeren3)) {

			if (aantalOmgekeerd == 0) {
				aantalOmgekeerd = 1;
			} else {
				Robot.openSpaceCounter = 4;
				// Main.bochtCounter = 2f;
				if (Robot.lastDistance > 40) {
					motorLeft.forward();
					motorRight.backward();
				} else {
					motorRight.forward();
					motorLeft.backward();
				}
				try {
					Thread.sleep(1100);
				} catch (InterruptedException e) {
				}
			}
		} else {
		}

		Robot.arc = false;
	}

	/**
	 * Actie die ondernomen wordt wanneer de arcbehavior verlaten wordt
	 */
	@Override
	public void suppress() {
		supressed = true;
	}

	/**
	 * Beslist wanneer de arcbehavior actief wordt.
	 */
	@Override
	public boolean takeControl() {
		return Robot.arc;
	}
}