package de.engehausen.crazygolf.ui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.engehausen.crazygolf.event.ElementSelectionListener;
import de.engehausen.crazygolf.event.ModificationActionHandler;
import de.engehausen.crazygolf.event.PlaceActionHandler;
import de.engehausen.crazygolf.model.Element;

/**
 * A view with all the actions possible on an element.
 */
public class ActionsView extends JPanel implements ElementSelectionListener, ActionListener, FocusListener {

	private static final long serialVersionUID = 1L;
	private static final Insets NULL_INSETS = new Insets(0,0,0,0);
	private static final String DEFAULT_NAME = "...";

	private final ElementPanel parent;

	private final JTextField name;
	private final JTextField xPos;
	private final JTextField yPos;
	private final JButton moveUp, moveDown, moveLeft, moveRight;
	private final JButton alignTop, alignBottom, alignLeft, alignRight;
	private final JButton centerH, centerV, toTop, toBottom;
	
	private PlaceActionHandler placingHandler;
	private ModificationActionHandler modificationHandler;
	
	private static final InputVerifier verifier = new InputVerifier() {		
		@Override
		public boolean verify(final JComponent jcomponent) {
			final JTextField field = (JTextField) jcomponent;
			try {
				Integer.parseInt(field.getText());
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		}
	};

	/**
	 * Creates the view.
	 * @param aParent the parent element panel of the view.
	 */
	public ActionsView(final ElementPanel aParent) {
		super();
		parent = aParent;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(CENTER_ALIGNMENT);
		name = new JTextField(DEFAULT_NAME);
		name.setEditable(false);
		name.setHorizontalAlignment(JTextField.CENTER);
		name.setBorder(null);
		add(name);
		xPos = new JTextField(5);
		xPos.setInputVerifier(verifier);
		xPos.addFocusListener(this);
		yPos = new JTextField(5);
		yPos.setInputVerifier(verifier);
		yPos.addFocusListener(this);
		add(createTextLine("x", xPos));
		add(createTextLine("y", yPos));
		add(Box.createVerticalStrut(8));
		
		moveUp = new JButton(createImageIcon("up.png"));
		moveDown = new JButton(createImageIcon("down.png"));
		moveLeft = new JButton(createImageIcon("left.png"));
		moveRight = new JButton(createImageIcon("right.png"));		
		final JButton dummy = new JButton(createImageIcon("empty.png"));
		dummy.setEnabled(false);		
		add(createButtonLine(moveUp));
		add(createButtonLine(moveLeft, dummy, moveRight));
		add(createButtonLine(moveDown));
		
		add(Box.createVerticalStrut(8));
		toTop = new JButton(createImageIcon("to_top.png"));
		toBottom = new JButton(createImageIcon("to_bottom.png"));
		alignTop = new JButton(createImageIcon("align_top.png"));
		alignBottom = new JButton(createImageIcon("align_bottom.png"));
		alignLeft = new JButton(createImageIcon("align_left.png"));
		alignRight = new JButton(createImageIcon("align_right.png"));
		centerH = new JButton(createImageIcon("align_hcenter.png"));
		centerV = new JButton(createImageIcon("align_vcenter.png"));
		
		add(createButtonLine(toTop, toBottom, alignTop, alignBottom));
		add(createButtonLine(alignLeft, alignRight, centerH, centerV));
		add(Box.createVerticalStrut(48));
		
		addActionListeners(moveUp, moveDown, moveLeft, moveRight, toTop,
				           toBottom, alignBottom, alignTop, alignLeft, alignRight,
				           centerH, centerV);
	}
	
	private void addActionListeners(final JButton ...buttons) {
		for (JButton b : buttons) {
			b.addActionListener(this);
		}
	}
	
	/**
	 * Sets the handler for placing elements.
	 * @param handler the handler for placing elements.
	 */
	public void setPlaceActionHandler(final PlaceActionHandler handler) {
		placingHandler = handler;
	}
	
	/**
	 * Sets the handler for modification events.
	 * @param handler the handler for modification events.
	 */
	public void setModificationActionHandler(final ModificationActionHandler handler) {
		modificationHandler = handler;
	}
	
	@Override
	public void elementSelected(final Element e) {
		if (e == null) {
			name.setText(DEFAULT_NAME);
		} else {
			name.setText(e.getName());			
		}
		positionChanged(e);
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		final Object source = event.getSource();
		if (source == moveUp) { //NOPMD
			modificationHandler.move(ModificationActionHandler.Direction.UP);
		} else if (source == moveDown) {  //NOPMD
			modificationHandler.move(ModificationActionHandler.Direction.DOWN);
		} else if (source == moveLeft) {  //NOPMD
			modificationHandler.move(ModificationActionHandler.Direction.LEFT);
		} else if (source == moveRight) { //NOPMD
			modificationHandler.move(ModificationActionHandler.Direction.RIGHT);
		} else if (source == alignTop) {  //NOPMD
			modificationHandler.align(ModificationActionHandler.Boundary.TOP);			
		} else if (source == alignBottom) { //NOPMD
			modificationHandler.align(ModificationActionHandler.Boundary.BOTTOM);
		} else if (source == alignLeft) { //NOPMD
			modificationHandler.align(ModificationActionHandler.Boundary.LEFT);
		} else if (source == alignRight) { //NOPMD
			modificationHandler.align(ModificationActionHandler.Boundary.RIGHT);
		} else if (source == centerH) { //NOPMD
			modificationHandler.center(ModificationActionHandler.Orientation.HORIZONTAL);
		} else if (source == centerV) { //NOPMD
			modificationHandler.center(ModificationActionHandler.Orientation.VERTICAL);	
		} else if (source == toTop) { //NOPMD
			modificationHandler.toTop();
		} else if (source == toBottom) { //NOPMD
			modificationHandler.toBottom();
		}
	}

	@Override
	public void focusGained(final FocusEvent event) {
		// ignore
	}

	@Override
	public void focusLost(final FocusEvent event) {
		final Object source = event.getSource();
		if (source == xPos || source == yPos) { //NOPMD
			try {
				parent.set(Integer.parseInt(xPos.getText()), Integer.parseInt(yPos.getText()));
			} catch (NumberFormatException e) { //NOPMD no alternate action
				// ignore and do nothing
			}
		}
	}

	// TODO make event listener interface for his? the game panel wants to know too
	public void positionChanged(final de.engehausen.crazygolf.model.Element current) {
		xPos.setText(Integer.toString(current.getX()));
		yPos.setText(Integer.toString(current.getY()));
	}
	
	protected void handlePlacing() {
		if (placingHandler != null) {
			placingHandler.setPlacingElement(parent.getCurrent());
		}
	}

	private JPanel createTextLine(final String text, final JTextField field) {
		final JPanel result = new JPanel();
		result.add(new JLabel(text));
		result.add(field);		
		return result;
	}
	
	private JPanel createButtonLine(final JButton ... buttons) {
		final JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setMargin(NULL_INSETS);
			result.add(buttons[i]);
		}		
		return result;
	}
	
	private ImageIcon createImageIcon(final String name) {
		try {
			return new ImageIcon(ImageIO.read(ActionsView.class.getResource(name)));
		} catch (IOException e) {
			return null;
		}
	}

}
