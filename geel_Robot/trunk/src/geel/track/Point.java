package geel.track;

public class Point {
	public int x;
	public int y;
	public Point(int x,int y) {
		this.x= x;
		this.y=y;
	}
	
	public boolean equals(Point point) {
		if(point.x == this.x && point.y == this.y) {
			return true;
		}
		return false;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}

}
