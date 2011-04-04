package de.engehausen.crazygolf.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import de.engehausen.crazygolf.event.ElementModificationListener;
import de.engehausen.crazygolf.event.ElementSelectionListener;
import de.engehausen.crazygolf.event.ModificationActionHandler;
import de.engehausen.crazygolf.event.PlaceActionHandler;
import de.engehausen.crazygolf.event.TemplateSelectionListener;
import de.engehausen.crazygolf.event.VectorDisplayListener;
import de.engehausen.crazygolf.event.ZoomListener;
import de.engehausen.crazygolf.model.Elements;
import de.engehausen.mobile.crazygolf.Element;

/**
 * The element panel; composed of different "views", e.g.
 * <ul>
 * <li>element view
 * <li>actions view
 * <li>options view
 * <li>zoom view
 * </ul>
 */
public class ElementPanel extends JPanel implements TemplateSelectionListener, ElementSelectionListener, ElementModificationListener {

	private static final long serialVersionUID = 1L;
	
	private final ElementView elementView;
	private final ActionsView actionsView;
	private final OptionsView optionsView;
	private final ZoomView zoomView;
	
	private final Elements elements;

	private de.engehausen.crazygolf.model.Element current;
	
	private final List<ElementModificationListener> modificationListeners;
	private final List<VectorDisplayListener> vectorListeners;

	/**
	 * Creates the elment panel and its views.
	 * @param allElements all known elements.
	 */
	public ElementPanel(final Elements allElements) {
		super();
		elements = allElements;
		modificationListeners = new ArrayList<ElementModificationListener>(2);
		vectorListeners = new ArrayList<VectorDisplayListener>(2);		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(TOP_ALIGNMENT);
		JPanel views = new JPanel();
		views.setLayout(new BoxLayout(views, BoxLayout.X_AXIS));
		zoomView = new ZoomView();
		views.add(zoomView);
		elementView = new ElementView(this);
		views.add(elementView);
		vectorListeners.add(elementView);
		add(views);

		views = new JPanel();
		views.setLayout(new BoxLayout(views, BoxLayout.X_AXIS));
		views.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		actionsView = new ActionsView(this);
		views.add(actionsView);
		optionsView = new OptionsView(this);
		views.add(optionsView);
		views.add(Box.createHorizontalStrut(32));
		
		add(views);
	}

	/**
	 * Show or hide the vector fields using arrows on the graphics.
	 * @param flag <code>true</code> to show the arrows, <code>false/<code> otherwise
	 */
	public void setShowVector(final boolean flag) {
		for (int i = vectorListeners.size(); i-->0; ) {
			vectorListeners.get(i).setShowVector(flag);
		}
	}

	/**
	 * Sets the handler for placing elements.
	 * @param handler the handler for placing elements.
	 */
	public void setPlaceActionHandler(final PlaceActionHandler handler) {
		actionsView.setPlaceActionHandler(handler);
	}

	/**
	 * Sets the handler for modification events.
	 * @param handler the handler for modification events.
	 */
	public void setModificationActionHandler(final ModificationActionHandler handler) {
		actionsView.setModificationActionHandler(handler);		
	}
	
	/**
	 * Adds the given listeners to the panel.
	 * @param aListener the listener, must not be <code>null</code>
	 */
	public void addModificationListener(final ElementModificationListener aListener) {
		modificationListeners.add(aListener);
	}

	/**
	 * Removes the given listeners from the panel.
	 * @param aListener the listener, must not be <code>null</code>
	 */
	public void removeModificationListener(final ElementModificationListener aListener) {
		modificationListeners.remove(aListener);
	}

	/**
	 * Adds the given listeners to the panel.
	 * @param aListener the listener, must not be <code>null</code>
	 */
	public void addVectorListener(final VectorDisplayListener aListener) {
		vectorListeners.add(aListener);
	}

	/**
	 * Removes the given listeners from the panel.
	 * @param aListener the listener, must not be <code>null</code>
	 */
	public void removeVectorListener(final VectorDisplayListener aListener) {
		vectorListeners.remove(aListener);
	}

	/**
	 * Returns the current element the panel is showing.
	 * @return the element the panel is showing.
	 */
	public de.engehausen.crazygolf.model.Element getCurrent() {
		return current;
	}

	@Override
	public void templateSelected(final Element[] templates) {
		if (templates == null) {
			current = null;
			actionsView.elementSelected(null);
			optionsView.elementSelected(null);
			elementView.elementSelected(null);			
		} else {
			elementSelected(new de.engehausen.crazygolf.model.Element(elements, templates));
			actionsView.handlePlacing();
		}
	}

	@Override
	public void elementSelected(final de.engehausen.crazygolf.model.Element element) {
		current = element;
		actionsView.elementSelected(element);
		optionsView.elementSelected(element);
		elementView.elementSelected(element);
	}

	/**
	 * Returns the zoom listener.
	 * @return the zoom listener.
	 */
	public ZoomListener getZoomListener() {
		return zoomView;
	}

	/**
	 * Sets the mode of the currently showing element.
	 * @param type the mode of the element.
	 */
	public void setMode(final int type) {
		if (current != null) {
			current.setMode(type);
			elementView.repaint();
			notifyModificationListeners();
		}
	}

	/**
	 * Flips the currently showing element horizontally.
	 * @param selected whether or not to flip.
	 */
	public void setFlippedH(final boolean selected) {
		if (current != null) {
			current.setFlippedH(selected);
			elementView.repaint();
			notifyModificationListeners();
		}
	}

	/**
	 * Flips the currently showing element vertically.
	 * @param selected whether or not to flip.
	 */
	public void setFlippedV(final boolean selected) {
		if (current != null) {
			current.setFlippedV(selected);
			elementView.repaint();
			notifyModificationListeners();
		}
	}

	/**
	 * Flips the vector field of the currently showing element horizontally.
	 * @param selected whether or not to flip
	 */
	public void setVectorFlippedH(final boolean selected) {
		if (current != null) {
			current.setVectorFlippedH(selected);
			elementView.repaint();
			notifyModificationListeners();
		}
	}

	/**
	 * Flips the vector field of the currently showing element vertically.
	 * @param selected whether or not to flip
	 */
	public void setVectorFlippedV(final boolean selected) {
		if (current != null) {
			current.setVectorFlippedV(selected);
			elementView.repaint();
			notifyModificationListeners();
		}
	}

	/**
	 * Moves the currently showing element to the given coordinates.
	 * @param x the x-position
	 * @param y the y-position
	 */
	public void set(final int x, final int y) {
		if (current != null) {
			current.setX(x);
			current.setY(y);
			actionsView.positionChanged(current);
			notifyModificationListeners();
		}
	}

	@Override
	public void elementModified(final de.engehausen.crazygolf.model.Element element) {
		if (element == current) { //NOPMD
			actionsView.positionChanged(current);
		}
	}

	/**
	 * Notifies all modification listeners that the currently showing
	 * element was modified.
	 */
	protected void notifyModificationListeners() {
		for (ElementModificationListener l : modificationListeners) {
			l.elementModified(current);
		}
	}

}