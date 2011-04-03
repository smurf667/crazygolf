package de.engehausen.crazygolf.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import de.engehausen.crazygolf.event.ElementSelectionListener;
import de.engehausen.crazygolf.model.Element;

public class OptionsView extends JPanel implements ElementSelectionListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private final ElementPanel parent;

	private final JRadioButton normal;
	private final JRadioButton down;
	private final JRadioButton up;
	
	private final JCheckBox horizontal, vertical;
	private final JCheckBox vectorHorizontal, vectorVertical;
	
	private final JCheckBox vectors;
	
	public OptionsView(final ElementPanel aParent) {
		super();
		parent = aParent;
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(CENTER_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		final JPanel modes = new JPanel();
		modes.setLayout(new BoxLayout(modes, BoxLayout.Y_AXIS));		
		modes.setAlignmentX(CENTER_ALIGNMENT);
		modes.setAlignmentY(TOP_ALIGNMENT);
		modes.setBorder(BorderFactory.createTitledBorder("Mode"));
		normal = new JRadioButton("normal");
		down = new JRadioButton("down");
		
		up = new JRadioButton("up");
		final ButtonGroup group = new ButtonGroup();
		group.add(normal);
		group.add(down);
		group.add(up);
		modes.add(normal);
		modes.add(down);
		modes.add(up);
		
		final JPanel flips = new JPanel();
		flips.setAlignmentX(CENTER_ALIGNMENT);
		flips.setAlignmentY(TOP_ALIGNMENT);
		flips.setLayout(new BoxLayout(flips, BoxLayout.Y_AXIS));
		flips.setBorder(BorderFactory.createTitledBorder("Flip"));
		horizontal = new JCheckBox("horizontal");
		vertical = new JCheckBox("vertical");
		flips.add(horizontal);
		flips.add(vertical);

		final JPanel vectorFlips = new JPanel();
		vectorFlips.setAlignmentX(CENTER_ALIGNMENT);
		vectorFlips.setAlignmentY(TOP_ALIGNMENT);
		vectorFlips.setLayout(new BoxLayout(vectorFlips, BoxLayout.Y_AXIS));
		vectorFlips.setBorder(BorderFactory.createTitledBorder("Vector Flip"));
		vectorHorizontal = new JCheckBox("horizontal");
		vectorVertical = new JCheckBox("vertical");
		vectorFlips.add(vectorHorizontal);
		vectorFlips.add(vectorVertical);

		add(modes);
		add(flips);
		add(vectorFlips);
		
		vectors = new JCheckBox("show vector");
		vectors.setSelected(true);
		vectors.setAlignmentX(CENTER_ALIGNMENT);
		add(vectors);
		
		addActionListeners(normal, down, up, horizontal, vertical, vectorHorizontal, vectorVertical, vectors);
	}
	
	private void addActionListeners(final AbstractButton ... buttons) {
		for (AbstractButton b : buttons) {
			b.addActionListener(this);
		}
	}

	@Override
	public void elementSelected(final Element e) {
		if (e == null) {
			normal.setSelected(true);
			normal.setEnabled(false);
			down.setEnabled(false);
			up.setEnabled(false);
			horizontal.setSelected(false);
			horizontal.setEnabled(false);
			vertical.setSelected(false);
			vertical.setEnabled(false);
			vectorHorizontal.setSelected(false);
			vectorHorizontal.setEnabled(false);
			vectorVertical.setSelected(false);
			vectorVertical.setEnabled(false);
		} else {
			final int mode = e.getMode();
			normal.setSelected(mode == de.engehausen.mobile.crazygolf.Element.TYPE_NORMAL);
			normal.setEnabled(e.hasMode(de.engehausen.mobile.crazygolf.Element.TYPE_NORMAL));
			down.setSelected(mode == de.engehausen.mobile.crazygolf.Element.TYPE_DOWN);
			down.setEnabled(e.hasMode(de.engehausen.mobile.crazygolf.Element.TYPE_DOWN));
			up.setSelected(mode == de.engehausen.mobile.crazygolf.Element.TYPE_UP);
			up.setEnabled(e.hasMode(de.engehausen.mobile.crazygolf.Element.TYPE_UP));
			horizontal.setSelected(e.isFlippedH());
			horizontal.setEnabled(e.canFlipHorizontal());
			vertical.setSelected(e.isFlippedV());
			vertical.setEnabled(e.canFlipVertical());			
			vectorHorizontal.setSelected(e.isVectorFlippedH()); // TODO öööö
			vectorHorizontal.setEnabled(true);
			vectorVertical.setSelected(e.isVectorFlippedV());
			vectorVertical.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		final Object source = event.getSource();
		if (source == normal) { //NOPMD
			parent.setMode(de.engehausen.mobile.crazygolf.Element.TYPE_NORMAL);
		} else if (source == down) { //NOPMD
			parent.setMode(de.engehausen.mobile.crazygolf.Element.TYPE_DOWN);
		} else if (source == up) { //NOPMD
			parent.setMode(de.engehausen.mobile.crazygolf.Element.TYPE_UP);
		} else if (source == horizontal) { //NOPMD
			parent.setFlippedH(horizontal.isSelected());
		} else if (source == vertical) { //NOPMD
			parent.setFlippedV(vertical.isSelected());
		} else if (source == vectorHorizontal) { //NOPMD
			parent.setVectorFlippedH(vectorHorizontal.isSelected());
		} else if (source == vectorVertical) { //NOPMD
			parent.setVectorFlippedV(vectorVertical.isSelected());
		} else if (source == vectors) { //NOPMD
			parent.setShowVector(vectors.isSelected());
		}
	}
	
}
