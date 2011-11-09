package pcpanic;

import java.util.LinkedList;
import java.util.Stack;

import ch.aplu.turtle.Turtle;
import ch.aplu.turtle.TurtlePane;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.EmptyStackException;

public class TrackShower {

	private Turtle speedo;
	private Stack<OPS> stack = new Stack<OPS>();
	private Stack<Character> codestack = new Stack<Character>();
	private LinkedList<Character> savedTrack = new LinkedList<Character>();
	private Point2D.Double startPosition;
	// De waarde van de barcode die een rechtse bocht aangeeft
	public final static char right = '6';
	// De waarde van de barcode die een linkse bocht aangeeft
	public final static char left = '3';
	public final static char forward = '1';
	public final static char clear = 'c';
	public final static char poop = 'p';
	public final static int ANGLE = 90;
	public final static int LENGTH = 80;
	public final static int black = '0';
	public final static int white = 'F';
	public final static int trackPollution = 'G';
	private int rounds;

	public TrackShower(TurtlePane p) {
		speedo = new Turtle(p);
		speedo.wrap();
		speedo.getPen().setColor(Color.yellow);
		speedo.setColor(Color.yellow);
		speedo.speed(500);
		startPosition = speedo.getPos();

	}

	public void forward() {
		speedo.forward(LENGTH);
		stack.add(OPS.FORWARD);
		codestack.push(forward);
	}

	public void right() {
		speedo.right(ANGLE);
		speedo.forward(LENGTH);
		stack.add(OPS.RIGHT);
		codestack.push(right);
	}

	public void left() {
		speedo.left(ANGLE);
		speedo.forward(LENGTH);
		stack.add(OPS.LEFT);
		codestack.push(right);
	}

	public void erase(OPS o) {
		Color last = speedo.getPen().getColor();
		speedo.left(2 * ANGLE);
		speedo.penErase();
		speedo.forward(LENGTH);
		speedo.left(2 * ANGLE);

		speedo.getPen().setColor(last);

		if (o == OPS.LEFT) {
			speedo.right(ANGLE);
		} else if(o ==OPS.RIGHT){
			speedo.left(ANGLE);
		}

	}

	public void clear() {
		speedo.clean();
		speedo.home();
		stack.clear();
		codestack.clear();
		startPosition = speedo.getPos();
	}

	public void moveTurtle(Character code) {


		if (code.equals(left)) {
			left();
		} else if (code.equals(right)) {
			right();
		} else if (code.equals(forward)) {
			forward();
		} else if (code.equals(clear) ) {
			clear();
		} else if (code.equals(black) || code.equals(white) || code.equals(trackPollution)){
			//Negeren
		} else {
			clear();
		}
		
		if (trackCompleted() && (code.equals(left) || code.equals(right) || code.equals(forward))) {
				saveTrack();
				rounds++;
				System.out.println("TRACK COMPLETED, rounds:" + rounds);
				// speedo.wrap();
				switchPenColor();
				speedo.speed(500);
				startPosition = speedo.getPos();
			} 
		}


		public void switchPenColor(){
			if(speedo.getPen().getColor() == Color.yellow)
				speedo.getPen().setColor(Color.red);
			else speedo.getPen().setColor(Color.yellow);
		}

		public void undo() throws EmptyStackException{
			erase(stack.pop());
			codestack.pop();
		}

		public boolean trackCompleted(){
			return samePosition(speedo.getPos(), startPosition);
		}

		public boolean samePosition(Double pos1,Double pos2){
			double marge = 10;
			return ((pos1.x <= pos2.x + marge && pos1.x >= pos2.x - marge) 
					&& (pos1.y <= pos2.y + marge && pos1.y >= pos2.y - marge));

		}

		//Hieronder staat de code om evt barcodes door te sturen

		public void saveTrack(){
			savedTrack.addAll(codestack);
		}

		public LinkedList<Character> getSavedTrack(){
			return savedTrack;
		}
	}
