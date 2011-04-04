package de.engehausen.crazygolf.ui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import de.engehausen.crazygolf.event.TemplateSelectionListener;
import de.engehausen.crazygolf.model.Elements;
import de.engehausen.mobile.crazygolf.Element;

/**
 * List with all known elements, presented as "template".
 */
public class TemplatePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final List<TemplateSelectionListener> listeners;
	private final Elements elements;

	/**
	 * Creates the template panel.
	 * @param allElements the elements backing the templates.
	 */
	public TemplatePanel(final Elements allElements) {
		super();
		elements = allElements;
		listeners = new ArrayList<TemplateSelectionListener>(2);
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(TOP_ALIGNMENT);
		setBackground(Constants.DARK_GREEN);
		for (Element e : allElements.getAllElements()) {
			if (e.getType() == Element.TYPE_NORMAL) {
				final ElementButton button = new ElementButton(e, elements.getImage(e));
				button.addActionListener(this);
				add(button);
			}
		}		
	}

	/**
	 * Adds a template selection listener.
	 * @param l the template selection listener.
	 */
	public void addTemplateSelectionListener(final TemplateSelectionListener l) {
		listeners.add(l);
	}

	/**
	 * Removes a template selection listener.
	 * @param l the template selection listener.
	 */
	public void removeTemplateSelectionListener(final TemplateSelectionListener l) {
		listeners.remove(l);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final ElementButton button = (ElementButton) e.getSource();
		notifySelection(elements.getAllModes(button.element.getName()));
	}

	/**
	 * Notify all listeners about a template selection.
	 * @param templates the selected templates.
	 */
	protected void notifySelection(final Element[] templates) {
		for (TemplateSelectionListener l : listeners) {
			l.templateSelected(templates);
		}
	}
	
	private static class ElementButton extends JButton {

		private static final long serialVersionUID = 1L;

		protected final Element element;
		
		public ElementButton(final Element anElement, final BufferedImage img) {
			super(new ImageIcon(img));
			element = anElement;
			setContentAreaFilled(false);
		}

		public void paintComponent(final Graphics g) {
			g.setColor(Constants.GREEN);
			g.fillRect(0, 0, getWidth(), getHeight());
			super.paintComponent(g);
		}

	}

}

