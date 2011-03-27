package geel.track;
//TODO
public enum Dir {
	UP {
		@Override
		public Point apply(Point p) {
			return new Point(p.x,p.y+1);
		}
		
		@Override
		public Dir straight() {
			return UP;
		}
		
		@Override
		public Dir left() {
			return LEFT;
		}

		@Override
		public Dir right() {
			return RIGHT;
		}
	},DOWN {
		@Override
		public Point apply(Point p) {
			return new Point(p.x,p.y-1);
		}

		@Override
		public Dir straight() {
			return UP;
		}

		@Override
		public Dir left() {
			return RIGHT;
		}

		@Override
		public Dir right() {

			return LEFT;
		}
	},LEFT {
		@Override
		public Point apply(Point p) {

			return new Point(p.x-1,p.y);
		}

		@Override
		public Dir straight() {
			return LEFT;
		}

		@Override
		public Dir left() {

			return DOWN;
		}

		@Override
		public Dir right() {

			return UP;
		}
	},RIGHT {
		@Override
		public Point apply(Point p) {

			return new Point(p.x+1,p.y);
		}

		@Override
		public Dir straight() {
			return RIGHT;
		}

		@Override
		public Dir left() {

			return UP;
		}

		@Override
		public Dir right() {

			return DOWN;
		}
	};
	
	public abstract Point apply(Point p);
	public abstract Dir left();
	public abstract Dir right();
	public abstract Dir straight();
	
	

}
