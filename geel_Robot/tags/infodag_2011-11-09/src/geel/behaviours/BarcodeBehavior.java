package geel.behaviours;


import geel.RMotor;
import geel.Robot;
import geel.track.TrackTracker;

import java.util.ArrayList;
import java.util.Hashtable;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.robotics.subsumption.Behavior;

/**
 * @author s0206928
 * 
 */
public class BarcodeBehavior implements Behavior {

	// specifieke gegevens voor de lichtsensor
	private LightSensor light;
	static int blackmarge = 40;
	static int whitemarge = 90;

	private int buffer1 = 50;
	private int buffer2 = 50;

	private int speed;

	private RMotor motorLeft, motorRight;

	private static TrackTracker trackTracker = new TrackTracker();

	// hashtable met de juiste waardes die teruggeven moeten worden.
	@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
	private final Hashtable BARCODES = new Hashtable() {

		{
			put("0000000", new Character('0'));
			put("0001111", new Character('1'));
			put("0010110", new Character('2'));
			put("0011001", new Character('3'));
			put("0100101", new Character('4'));
			put("0101010", new Character('5'));
			put("0110011", new Character('6'));
			put("0111100", new Character('7'));
			put("1000011", new Character('8'));
			put("1001100", new Character('9'));
			put("1010101", new Character('A'));
			put("1011010", new Character('B'));
			put("1100110", new Character('C'));
			put("1101001", new Character('D'));
			put("1110000", new Character('E'));
			put("1111111", new Character('F'));
		}
	};

	/**
	 * Constructor voor de barcodebehavior
	 * 
	 * @param light
	 * @param console
	 * @param speed
	 * @param motorLeft
	 * @param motorRight
	 */
	public BarcodeBehavior(LightSensor light, int speed, RMotor motorRight,
			RMotor motorLeft) {
		this.light = light;
		this.speed = speed;
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;

	}
	
	private void printReadings(ArrayList<Integer> readings) {
		for(Integer i: readings) {
			System.out.print(""+i.intValue()+",");
		}
		System.out.println();
	}

	/**
	 * Laat deze behavior overnemen als de sensor meetwaardes tussen de marges
	 * meet.
	 */
	public boolean takeControl() {
		int lightValue = light.readValue();
		buffer1 = buffer2;
		buffer2 = lightValue;
		return (lightValue < blackmarge || lightValue > whitemarge);
	}

	/**
	 * Actie die ondernomen moet worden om de behavior te stoppen
	 */
	public void suppress() {
		motorLeft.stop();
		motorRight.stop();
	}

	/**
	 * Wat er moet gebeuren tijdens deze behavior
	 */
	public void action() {

		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);

		int metingen = 0;
		int done = 0;

		// arraylist waarin de waardes worden opgeslagen die de sensor meet.
		ArrayList<Integer> readings = new ArrayList<Integer>(120);

		readings.add(buffer1);
		readings.add(buffer2);

		motorLeft.forward();
		motorRight.forward();

		int minReadings = 5;

		// Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		while (!(done > minReadings) && metingen < 120) {
			metingen++;
			int lightValue = light.readValue();
			if (lightValue < blackmarge || lightValue > whitemarge) {
				done = 0;
			} else {
				done++;
			}
			readings.add(lightValue);
			// de ingelezen waardes worden opgeslaan
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}
		}

		if (metingen < 7 || metingen > 120) {
			Robot.code = new Character('G');
		} else {

			// Thread.currentThread().setPriority(Thread.NORM_PRIORITY);

			// laatste minReadings waardes verwijderen aangezien deze vals
			// worden opgeslaan. Zie done > minReadings
			 System.out.println("GROND:  " + readings.get(readings.size() - 1));
			for (int i = 0; i < minReadings; i++) {
				readings.remove(readings.size() - 1);
			}
			// de waardes gemeten tijdens het over de barcode rijden moeten
			// gedecodeerd worden.
			System.out.print("Print readings: ");
			printReadings(readings);
			
			Robot.code = decodeReadings(readings);
		}
		setMainArc(true);

		 if(!trackTracker.getTrackCompleted()){
		 //trackTracker.updateTrack();
		 System.out.println("Turtle " + Robot.code);}
	}

	/**
	 * Verander de arc-boolean in de Mainklasse.
	 * 
	 * @param arc
	 */
	private void setMainArc(boolean arc) {
		Robot.arc = arc;
	}

	/**
	 * Het verwerken van de ingelezen waardes, zet de 7 bits in een ArrayList
	 */
	private Character decodeReadings(ArrayList<Integer> readings) {

		// reset de vorige code
		ArrayList<Integer> codeArray = new ArrayList<Integer>();

		// Deelt de ingelezen codes op in zeven intervallen.
		int temp = readings.size() / 7;

		// Bereken elke bit per interval.
		int index = 0;
		while (index != 7) {
			// System.out.println("Interval" + index);
			int sum = 0;
			// for (int j = 0; j < temp; j++) {
			for (int j = 1; j < temp - 1; j++) {
				// for (int i = temp * index; i < (index + 1) * temp; i++) {
				int i = temp * index + j;
				try {
					int reading = readings.get(i);
					// System.out.print(reading + "   ");
					sum += reading;
				} catch (Exception e) {
					// Een deling door nul
				}

			}
			double gem = ((double) sum) / temp;
			// deze waarde moet nog naar een bit worden overgezet; 1 indien wit,
			// 0 indien zwart.
			if (gem > 60) {
				codeArray.add(1);
			} else {
				codeArray.add(0);
			}
			index++;
			// System.out.println("");
		}
		return solomonDecode(codeArray);
	}

	/**
	 * Het decoderen van de Arraylist. Zet de ArrayList om naar een String en
	 * print de overeenkomstige waarde uit de hashmap. Indien er 1 bitfout
	 * gemaakt wordt zal deze correct verbeterd worden.
	 */
	private Character solomonDecode(ArrayList<Integer> codeArray) {
		try {
			String code = "";
			for (int j = 0; j < 7; j++) {
				code += codeArray.get(j);
			}
			if (BARCODES.get(code) != null) {
				return (Character) BARCODES.get(code);
			} else {
				String code1 = "";
				boolean found = false;
				for (int i = 0; i < 7 && !found; i++) {
					if (codeArray.get(i) == 0)
						codeArray.set(i, 1);
					else
						codeArray.set(i, 0);
					for (int j = 0; j < 7; j++) {
						code1 += codeArray.get(j);
					}
					if (BARCODES.get(code1) != null) {
						found = true;
						code = code1;
					} else {
						code1 = "";
						if (codeArray.get(i) == 0)
							codeArray.set(i, 1);
						else
							codeArray.set(i, 0);
					}
				}
				return (Character) BARCODES.get(code);
			}
		} catch (Exception e) {
			return null;
		}
	}
}
