package geel.track;
import java.util.ArrayList;
import lejos.nxt.Sound;

public class TrackTracker {
	
	// De waarde van de barcode die een rechtse bocht aangeeft
	private final static char rechts = '6';
	// De waarde van de barcode die een linkse bocht aangeeft
	private final static char links = '3';

	private final static char rechtdoor = '1';
	
	private final static char clear = 'c';
	
	
	private static ArrayList<Point> points;
	private static Dir d;
	private static Point origin = new Point(0,0);
	private static boolean trackCompleted;
	
	public boolean getTrackCompleted(){
		return trackCompleted;
	}
	
	public TrackTracker() {
		points = new ArrayList<Point>(35);
		points.add(origin);
		d= Dir.UP;
		
		trackCompleted = false;
	}
	
	public static void updateTrack(Character ch) {
		
		System.out.println("Turtle " + ch);
		
		Point last = points.get(points.size()-1);
		if(ch.equals(rechts)) {
			d = d.right();
			points.add(d.apply(last));
		}else if(ch.equals(links)){
			d = d.left();
			points.add(d.apply(last));
		}else if(ch.equals(rechtdoor)){
			d = d.straight();
			points.add(d.apply(last));
		}else if(ch.equals(clear)){
			points.clear();
			points.add(origin);
			d = Dir.UP;
		}
		
		Point newb = points.get(points.size()-1);						
		System.out.println(newb.getX() + ", " + newb.getY() + "");
		
		trackCompleted(newb);
	}

	private static void trackCompleted(Point lastPoint) {
		if(lastPoint.equals(origin) && points.size() > 3) {
			trackCompleted = true;
			Sound.beepSequenceUp();
			System.out.println("Turtle " + 'p');
		}
	}
	
	

}
