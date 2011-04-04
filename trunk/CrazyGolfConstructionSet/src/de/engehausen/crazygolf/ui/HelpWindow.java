package de.engehausen.crazygolf.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * Show help for the editor.
 */
public class HelpWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates the help window
	 * @throws IOException in case of error
	 */
	public HelpWindow() throws IOException {
		super("Help");
		final JEditorPane htmlPane = new JEditorPane();
		htmlPane.setPage(getClass().getResource("help.html"));
        htmlPane.setEditable(false);
        getContentPane().add(new JScrollPane(htmlPane), BorderLayout.CENTER);
        setPreferredSize(new Dimension(512, 640));
        pack();
	}

}
