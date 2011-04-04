package de.engehausen.crazygolf.event;

/**
 * Listener that handles showing and hiding the vector fields
 * of a course.
 */
public interface VectorDisplayListener {

	/**
	 * Changes the visibility flag for the vector fields.
	 * @param flag <code>true</code> to show the vectors, <code>false</code> otherwise
	 */
	void setShowVector(boolean flag);
	
}
