package de.engehausen.crazygolf.event;

/**
 * Handler for modification actions.
 */
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

	/**
	 * Move into the given direction.
	 * @param direction the direction into which to move.
	 */
	void move(Direction direction);
	
	/**
	 * Center along the given orientation.
	 * @param orientation the orientation along which to center.
	 */
	void center(Orientation orientation);
	
	/**
	 * Align along a boundary.
	 * @param boundary the boundary along which to align.
	 */
	void align(Boundary boundary);
	
	/**
	 * Move to top layer.
	 */
	void toTop();
	
	/**
	 * Move to bottom layer.
	 */
	void toBottom();
	
}
