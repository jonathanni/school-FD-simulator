package scifair;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import loader.SSchoolXMLLoader;

import org.xml.sax.SAXException;

public class SGUI extends JFrame implements KeyListener {

	// The minimum top left corner of the frame (window)
	private static final int MIN_X = 5;
	private static final int MIN_Y = 35;

	// The width and height of the frame
	private int width, height;

	// Use this to draw
	private Graphics g;

	// The school and the handler for this GUI
	private SSchool school;
	private SHandler thread;

	// The "virtual" dimensions of the frame (scaled)
	private double dispwidth, dispheight;

	// Instructions
	private static final String INSTRUCTIONS = " Press Q to QUIT, Press P to PAUSE, Press S to SAVE, Press L to LOAD";

	// The last key press
	private int lastEvent = 0;

	// Initialization
	public SGUI(SHandler thread, SSchool school, double scale) {

		// Listen for key presses
		addKeyListener(this);

		// Add the SHandler and the SSchool
		setThread(thread);
		setSchool(school);

		// Get the width and height of the screen
		width = Toolkit.getDefaultToolkit().getScreenSize().width;
		height = Toolkit.getDefaultToolkit().getScreenSize().height;

		// Formatting
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Fire Drill Simulation for " + school.getName());
		setSize(width, height);
		setResizable(false);
		setVisible(true);

		// Set the display boundaries based on the real boundaries and the scale
		setDispwidth(width * scale);
		setDispheight(height * scale);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	// This is called by repaint() and when the AWT event listener detects a
	// change. This function implements Double Buffering.
	@Override
	public void paint(Graphics g) {

		// Create an offscreen image
		Image offScreen = createImage(getWidth(), getHeight());

		// Make it draw on the image
		setG(offScreen.getGraphics());

		// Draw the school using the graphics provided
		drawSchool();

		// Draw the off screen image on the screen
		g.drawImage(offScreen, 0, 0, this);

	}

	// Assuming that you initialized the GUI, draw the school grid
	private void drawSchool() {

		// Clear the screen
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Information line
		g.setColor(Color.WHITE);
		g.drawString(
				"The school " + school.getName() + " is " + school.getWidth()
						+ " x " + school.getHeight()
						+ " meters in dimension. Time Taken (sec.): "
						+ getThread().getMillis() / 1000.0 + " Classes safe: "
						+ school.getSafeClassesNum() + "/"
						+ school.getAllNonEmptyClassrooms().size()
						+ " People safe: " + school.getSafePeopleNum() + "/"
						+ school.getAllPeople().size() + getInstructions(),
				MIN_X, MIN_Y);

		// Draw each hall
		for (SHall s : school.getHalls())
			drawHall(s);

		// Draw each person
		for (SPerson s : school.getAllPeople())
			if (!s.isSafe()) // Only if the
								// person
								// is not safe yet
				drawPerson(s, s.getCurrentHall());
	}

	private void drawHall(SHall hall) {

		// Get the screen coordinates of this hall
		int x1 = (int) (hall.getLocalToGlobalCoords(school, this).x), y1 = (int) (hall
				.getLocalToGlobalCoords(school, this).y);
		int x2 = 0, y2 = 0, width; // Also initialize the endpoint of the hall
									// and the width of the hall

		// Draw with a blue color
		g.setColor(Color.BLUE);

		// If the hall is horizontal
		if (hall.getDirection().equals(SDirection.LEFT_TO_RIGHT)) {
			y2 = y1; // The hall's endpoint's y value is the same

			// Calculate the endpoint's x value based on the x1 value and the
			// length of the hall
			x2 = (int) (x1 + getDispwidth()
					* (hall.getLength() / school.getWidth()));
			width = (int) (getDispheight() * (hall.getWidth() / school
					.getHeight())); // Calculate the width of the hall when put
									// on the screen

			// Draw two lines
			g.drawLine(x1, y1 - (int) (width / 2), x2, y2 - (int) (width / 2));
			g.drawLine(x1, y1 + (int) (width / 2), x2, y2 + (int) (width / 2));

		} else { // If the hall is vertical
			x2 = x1; // The hall's endpoint's x value is the same

			y2 = (int) (y1 + getDispheight()
					* (hall.getLength() / school.getHeight())); // See above
			width = (int) (getDispwidth() * (hall.getWidth() / school
					.getWidth())); // See above

			// See above
			g.drawLine(x1 - (int) (width / 2), y1, x2 - (int) (width / 2), y2);
			g.drawLine(x1 + (int) (width / 2), y1, x2 + (int) (width / 2), y2);
		}

		// Green color
		g.setColor(Color.GREEN);

		// Draw the hall's name
		g.drawString(hall.getName(), x1, y1);

		// Draw the nodes on the left side
		for (SNode sn : hall.getLeftNodes())
			if (!(sn instanceof SHall))
				drawNode(hall, sn, SSide.LEFT);

		// Draw the nodes on the right side
		for (SNode sn : hall.getRightNodes())
			if (!(sn instanceof SHall))
				drawNode(hall, sn, SSide.RIGHT);
	}

	private void drawNode(SHall hall, SNode s, SSide side) {
		int x1 = 0, y1 = 0, width = 0, height = 0; // Dimensions and location

		// If the hall is horizontal
		if (hall.getDirection().equals(SDirection.LEFT_TO_RIGHT)) {

			// Set the width and height to the height and width of the node
			width = (int) (getDispwidth() * (s.getHeight() / school.getWidth()));
			height = (int) (getDispheight() * (s.getWidth() / school
					.getHeight()));

		} else {

			// Set the width and height to the width and height of the node
			width = (int) (getDispwidth() * (s.getWidth() / school.getWidth()));
			height = (int) (getDispheight() * (s.getHeight() / school
					.getHeight()));

		}

		// Get the position of the node based on the screen location
		x1 = hall.getGlobalNodePosition(s, school, this).x;
		y1 = hall.getGlobalNodePosition(s, school, this).y;

		// Draw the nodes differently
		if (s instanceof SClassroom)
			g.setColor(Color.RED);
		else if (s instanceof SExit)
			g.setColor(Color.YELLOW);

		// Draw the rectangle based on the coordinates and dimension
		g.drawRect(x1, y1, width, height);

		// Draw the informational message above the room
		g.setColor(Color.CYAN);
		if (s instanceof SClassroom)
			g.drawString(
					String.valueOf(((SClassroom) s).getRoomNumber() == -1 ? ""
							: ((SClassroom) s).getRoomNumber()), x1, y1);
		else if (s instanceof SExit)
			g.drawString(((SExit) s).getName(), x1, y1);
	}

	private void drawPerson(SPerson s, SHall hall) {
		int x, y;

		// Get the coordinates of the person based on the x and y value of the
		// hall. Also, the length and width position of the person is taken into
		// account.
		if (hall.getDirection().equals(SDirection.LEFT_TO_RIGHT)) {

			// Use the hall's X + the length position
			x = (int) (getDispwidth() * ((hall.getX() + s
					.getHallLengthPosition()) / school.getWidth()));

			// Use the hall's Y + the horizontal position
			y = (int) (getDispheight() * ((hall.getY() + s
					.getHallHorizPosition()) / school.getHeight()));
		} else {

			// Use the hall's X + the horizontal position
			x = (int) (getDispwidth() * ((hall.getX() + s
					.getHallHorizPosition()) / school.getWidth()));

			// Use the hall's Y + the length position
			y = (int) (getDispheight() * ((hall.getY() + s
					.getHallLengthPosition()) / school.getHeight()));
		}

		if (s instanceof STeacher)
			g.setColor(Color.MAGENTA);
		else if (s instanceof SStudent)
			g.setColor(Color.ORANGE);

		g.fillOval(
				(int) (x - getDispwidth()
						* (s.getAverageWidth() / school.getWidth())),
				(int) (y - getDispheight()
						* (s.getAverageWidth() / school.getHeight())),
				(int) (getDispwidth() * (s.getAverageWidth() / school
						.getWidth())), (int) (getDispheight() * (s
						.getAverageWidth() / school.getHeight())));
	}

	public Graphics getG() {
		return g;
	}

	public void setG(Graphics g) {
		this.g = g;
	}

	public SSchool getSchool() {
		return school;
	}

	public void setSchool(SSchool school) {
		this.school = school;
	}

	public double getDispwidth() {
		return dispwidth;
	}

	public void setDispwidth(double dispwidth) {
		this.dispwidth = dispwidth;
	}

	public double getDispheight() {
		return dispheight;
	}

	public void setDispheight(double dispheight) {
		this.dispheight = dispheight;
	}

	public SHandler getThread() {
		return thread;
	}

	public void setThread(SHandler thread) {
		this.thread = thread;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		JFileChooser choose;
		int c;

		switch (keyCode) {
		case KeyEvent.VK_Q:
			// Quit
			getThread().setIsRunning(false);
			System.exit(0);
			break;
		case KeyEvent.VK_P:
			if (getLastEvent() != keyCode)
				getThread().setIsRunning(!getThread().isRunning());
			break;
		case KeyEvent.VK_S:
			choose = new JFileChooser();
			c = choose.showSaveDialog(this);
			if (c == JFileChooser.APPROVE_OPTION)
				try {
					SSchoolXMLLoader.save(getSchool(), new File(
							addExtensionIfNecessary(choose.getSelectedFile()
									.getAbsolutePath())));
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			break;
		case KeyEvent.VK_L:
			choose = new JFileChooser();
			c = choose.showOpenDialog(this);
			if (c == JFileChooser.APPROVE_OPTION)
				try {

					String input;

					SSchool school = SSchoolXMLLoader.load(choose
							.getSelectedFile());
					do {

						System.out.printf("%s",
								"Enter the number of students per classroom: ");
						input = new BufferedReader(new InputStreamReader(
								System.in)).readLine();
					} while (!Pattern.compile("^[0-9]+$").matcher(input)
							.matches());

					int numofpeople = Integer.valueOf(input);

					school.initTeachers(numofpeople);

					SMain.invokeNew(numofpeople, school);

				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (ParserConfigurationException pce) {
					pce.printStackTrace();
				} catch (SAXException saxe) {
					saxe.printStackTrace();
				}
			break;
		}

		if (getLastEvent() != keyCode)
			setLastEvent(keyCode);

		e.consume();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		setLastEvent(0);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private String addExtensionIfNecessary(String s) {
		if (!s.endsWith(".xml"))
			s += ".xml";

		return s;
	}

	public static String getInstructions() {
		return INSTRUCTIONS;
	}

	public int getLastEvent() {
		return lastEvent;
	}

	public void setLastEvent(int lastEvent) {
		this.lastEvent = lastEvent;
	}

}
