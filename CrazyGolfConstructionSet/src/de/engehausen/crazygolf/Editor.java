package de.engehausen.crazygolf;  //NOPMD imports are needed, high coupling accepted

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import de.engehausen.crazygolf.event.ElementModificationListener;
import de.engehausen.crazygolf.model.Course;
import de.engehausen.crazygolf.model.Elements;
import de.engehausen.crazygolf.model.Hole;
import de.engehausen.crazygolf.model.ModelIO;
import de.engehausen.crazygolf.ui.Constants;
import de.engehausen.crazygolf.ui.ElementPanel;
import de.engehausen.crazygolf.ui.GamePanel;
import de.engehausen.crazygolf.ui.HelpWindow;
import de.engehausen.crazygolf.ui.Splash;
import de.engehausen.crazygolf.ui.TemplatePanel;
import de.engehausen.mobile.crazygolf.Element;
import de.engehausen.mobile.crazygolf.ElementReader;

/**
 * Main editor class.
 */
public class Editor extends JFrame implements ActionListener, ElementModificationListener {
	
	private static final String ABOUT_TEXT = "<html><b>Crazy Golf Construction Set</b><br><br>This is an editor for creating courses in the<br>mobile phone game <b>Crazy Golf</b><br><br>Author: Jan Engehausen, 2011</html>";
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Crazy Golf Construction Set";
	
	private final JMenuItem newCourse, load, save, exit, help, about;
	private final JMenu holeMenu;
	private final ModelIO io;
	private JRadioButtonMenuItem firstHole;
	private final GamePanel gamePanel;
	private Course current;
	private Hole hole;
	private int holeIndex;
	private String filename;
	private JFileChooser fileChooser;
	private final JComboBox par;
	private boolean dirty;
	private HelpWindow helpWindow;

	public Editor(final Elements elements) {
		super(TITLE);
		io = new ModelIO(elements);
		newCourse = new JMenuItem("New course...");
		newCourse.setMnemonic('n');
		newCourse.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		load = new JMenuItem("Load...");
		load.setMnemonic('l');
		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		save = new JMenuItem("Save...");
		save.setMnemonic('s');
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		exit = new JMenuItem("Exit");
		exit.setMnemonic('x');
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		holeMenu = new JMenu("Hole");
		holeMenu.setMnemonic('o');
		help = new JMenuItem("Help...");
		help.setMnemonic('p');
		about = new JMenuItem("About...");
		about.setMnemonic('t');
		setJMenuBar(createMenuBar());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		final JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		JPanel line = new JPanel();
		line.setLayout(new BoxLayout(line, BoxLayout.X_AXIS));
		gamePanel = new GamePanel();
		final ElementPanel elementPanel = new ElementPanel(elements);
		final JPanel game = new JPanel();
		game.setAlignmentX(CENTER_ALIGNMENT);
		game.setAlignmentY(TOP_ALIGNMENT);
		game.setLayout(new BoxLayout(game, BoxLayout.Y_AXIS));
		game.add(gamePanel);
		par = new JComboBox(new String[] {
				"Par 1", "Par 2", "Par 3",
				"Par 4", "Par 5", "Par 6",
				"Par 7", "Par 8", "Par 9"
				});
		par.addActionListener(this);
		game.add(par);
		line.add(game);
		line.add(elementPanel);
		root.add(line);
		line = new JPanel();
		line.setLayout(new BoxLayout(line, BoxLayout.X_AXIS));
		final JPanel temp = new JPanel();
		temp.setLayout(new GridBagLayout());
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		constraints.anchor = GridBagConstraints.CENTER;
		final TemplatePanel templatePanel = new TemplatePanel(elements);
		temp.add(templatePanel, constraints);
		temp.setBackground(Constants.DARK_GREEN);
		final JScrollPane scroller = new JScrollPane(temp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setAlignmentX(CENTER_ALIGNMENT);
		scroller.setAlignmentY(TOP_ALIGNMENT);
		scroller.setPreferredSize(new Dimension(320+180+180,180));
		line.add(scroller);
		root.add(line);
		getContentPane().add(root);		
		wirePanels(gamePanel, elementPanel, templatePanel);
		setResizable(false);
		pack();
	}
	
	private void wirePanels(final GamePanel gamePanel, final ElementPanel elementPanel, final TemplatePanel templatePanel) {
		templatePanel.addTemplateSelectionListener(elementPanel);
		elementPanel.addModificationListener(gamePanel);
		elementPanel.addVectorListener(gamePanel);
		elementPanel.setPlaceActionHandler(gamePanel);
		elementPanel.setModificationActionHandler(gamePanel);
		gamePanel.addModificationListener(elementPanel);
		gamePanel.addModificationListener(this);
		gamePanel.addSelectionListener(elementPanel);
		gamePanel.addZoomListener(elementPanel.getZoomListener());
	}
	
	private JMenuBar createMenuBar() {
		newCourse.addActionListener(this);
		load.addActionListener(this);
		save.addActionListener(this);
		exit.addActionListener(this);
		final JMenuBar result = new JMenuBar();
		final JMenu temp = new JMenu("File");
		temp.setMnemonic('f');
		temp.add(newCourse);
		temp.add(new JSeparator());
		temp.add(load);
		temp.add(save);
		temp.add(new JSeparator());
		temp.add(exit);
		result.add(temp);
		holeMenu.setEnabled(false);
		final ButtonGroup group = new ButtonGroup();
		final String shorts = "123456789ABCDEFGHI";
		for (int i = 0; i < 18; i++) {
			final String name;
			if (i < 9) {
				name = "0"+(i+1);
			} else {
				name = Integer.toString(i+1);
			}
			final JRadioButtonMenuItem item = new JRadioButtonMenuItem(name);
			item.addActionListener(this);
			item.setAccelerator(KeyStroke.getKeyStroke(shorts.charAt(i), ActionEvent.CTRL_MASK));
			group.add(item);
			holeMenu.add(item);
			if (i == 0) {
				firstHole = item;
			}
		}
		result.add(holeMenu);

		final JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('h');
		help.addActionListener(this);
		about.addActionListener(this);
		helpMenu.add(help);
		helpMenu.add(about);
		result.add(helpMenu);

		return result;
	}

	@Override
	public void elementModified(final de.engehausen.crazygolf.model.Element element) {
		if (!dirty) {
			dirty = true;
			setTitle(getTitle()+"*");
		}
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		final Object source = event.getSource();
		if (source == load) { //NOPMD
			if (dirty && !confirm()) {
				return;
			}
			initFileChooser();
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					final File f = fileChooser.getSelectedFile();
					final Course c = io.load(f);
					setCourse(c, f.getAbsolutePath());
					setTitle();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, "I/O error - cannot load");
				}
			}
		} else if (source == save) { //NOPMD
			initFileChooser();
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					final File f = fileChooser.getSelectedFile();
					io.save(f, current);
					dirty = false;
					setTitle();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, "I/O error - cannot save");
				}
			}
		} else if (source == newCourse) {  //NOPMD
			createNewCourse();
		} else if (source == exit) { //NOPMD
			if (dirty && !confirm()) {
				return;
			}
			setVisible(false);
			dispose();
		} else if (source == par) { //NOPMD
			if (hole != null) {
				hole.setPar(par.getSelectedIndex()+1);
			}
		} else if (source == about) { //NOPMD
			JOptionPane.showMessageDialog(this, ABOUT_TEXT);
		} else if (source == help) { //NOPMD
			if (helpWindow == null) {
				try {
					helpWindow = new HelpWindow();
				} catch (IOException e) {
					return;
				}
			}
			helpWindow.setVisible(true);
		} else {
			if (source instanceof JRadioButtonMenuItem) {
				final JRadioButtonMenuItem item = (JRadioButtonMenuItem) source;
				if (item.isSelected()) {
					holeIndex = Integer.parseInt(item.getText())-1;
					selectHole(current.getHoles().get(holeIndex));
				}
			}
		}
	}

	private void setTitle() {
		if (current == null) {
			setTitle(TITLE);			
		} else {
			setTitle(TITLE+" - "+current.getName());
		}
	}
	private void initFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileFilter() {				
				@Override
				public String getDescription() {
					return "Golf Courses (*.crs)";
				}
				@Override
				public boolean accept(final File file) {
					return file.isDirectory() || file.getName().endsWith(".crs");
				}
			});
		}
		if (filename != null) {
			fileChooser.setSelectedFile(new File(filename));
		}
	}
	
	private void createNewCourse() {
		final String name = JOptionPane.showInputDialog(this, "Enter course name");
		if (name != null) {
			setCourse(new Course(fixName(name)), null);
		}
	}
	
	private String fixName(final String str) {
		final int max = str.length();
		final StringBuilder sb = new StringBuilder(max);
		final String temp = str.toLowerCase();  //NOPMD basically ascii anyway
		for (int i = 0; i < max; i++) {
			final char c = temp.charAt(i);
			if (c == ' ' || (c>='a' && c<='z') || (c >= '0' && c <= '9')) {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	private void setCourse(final Course c, final String fileName) {
		current = c;
		filename = fileName;
		holeMenu.setEnabled(true);
		firstHole.setSelected(true);
		selectHole(c.getHoles().get(0));
		setTitle();
	}
	
	private void selectHole(final Hole h) {
		gamePanel.setHole(h);
		hole = h;
		par.setSelectedIndex(hole.getPar()-1);
	}
	
	private boolean confirm() {
		return JOptionPane.showConfirmDialog(
			    this,
			    "Changes will be lost - continue?",
			    "Pending modifications",
			    JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION;		
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) throws Throwable {
		final ElementReader rdr = ElementReader.getInstance();
		final Element[] arr = rdr.read(ElementReader.class.getResourceAsStream(de.engehausen.mobile.crazygolf.Constants.DEFAULT_ELEMENTS));
		final Editor editor = new Editor(new Elements(arr));
		editor.setVisible(true);
		new Splash(editor);
	}

}
