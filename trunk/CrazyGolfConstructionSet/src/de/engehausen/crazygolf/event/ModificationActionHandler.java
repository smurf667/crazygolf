package de.engehausen.crazygolf.event;

public interface ModificationActionHandler {
	
	enum Direction { UP(0,-1), DOWN(0,1), LEFT(-1,0), RIGHT(1,0);
		private final int dx, dy;
		Direction(final int mx, final int my) {
			dx = mx;
			dy = my;
		}
		public int getDeltaX() {
			return dx;		
		}
		public int getDeltaY() {
			return dy;		
		}
	};
	enum Orientation { HORIZONTAL, VERTICAL };
	enum Boundary { TOP, BOTTOM, LEFT, RIGHT };
	
	void move(Direction direction);
	void center(Orientation orientation);
	void align(Boundary boundary);
	void toTop();
	void toBottom();
	
}
