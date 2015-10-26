package scifair;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class SPerson {
	// Average Human Gait in KM/H
	private static final double AVERAGE_SPEED = 5;
	private static final double AVERAGE_METERS_PER_SECOND = (AVERAGE_SPEED * 1000) / 3600;

	// Average Human width in M
	private static final double AVERAGE_WIDTH = 0.6;

	// Number of meters to decide if the person is close to a node
	private static final double CLOSE_EPSILON = AVERAGE_WIDTH * 4;

	// Used for multiple floor floor plans
	private int currentLevel = 0;

	// Horizontal position is from 0 to hall's width - person's width approx.
	// Length position is from 0 to the hall's length

	private double hallLengthPosition, hallHorizPosition;

	// The hall that the person is currently in
	private SHall currentHall;

	// Has the person reached the exit
	private boolean isSafe;

	// The time the person has spent waiting for movement
	private long timer = 0L;

	// Time before the person moves sideways
	private static final long TIMER_CAP = 20L;

	// The path
	private SPath path = new SPath();

	// Default constructor
	public SPerson() {
		this(0, 0, 0, new SHall());
	}

	public SPerson(int currentlevel, double hallLengthPosition,
			double hallHorizPosition, SHall currenthall) {

		setCurrentLevel(currentlevel);

		setHallLengthPosition(hallLengthPosition);
		setHallHorizPosition(hallHorizPosition);

		setCurrentHall(currenthall);
	}

	// Get the screen boundaries of this person
	public Rectangle getGlobalBounds(SSchool school, SGUI gui) {

		// Get a java.awt.Rectangle using the global coordinates and the
		// converted width and height to screen coordinates
		return new Rectangle(
				getLocalToGlobalCoords(school, gui).x,
				getLocalToGlobalCoords(school, gui).y,
				(int) (gui.getDispwidth() * (AVERAGE_WIDTH / school.getWidth())),
				(int) (gui.getDispheight() * (AVERAGE_WIDTH / school
						.getHeight())));
	}

	// Uses a recursive function to find every branch
	public void calcPath() {

		// Brute forces calculation, gets all the paths possible
		ArrayList<SPath> allpospaths = new ArrayList<SPath>();

		// Start with the current hall
		SPath firstpath = new SPath();
		firstpath.add(getCurrentHall());

		// Directly add the exits to more paths if the current hall contains one
		if (!getCurrentHall().getAllExits().equals(null))
			addExitsToPath(getCurrentHall(), allpospaths, firstpath);

		// Init recursive
		calcAllPosPaths(getCurrentHall(), allpospaths, firstpath);

		// Find the shortest path
		setPath(SPath.getShortestPath(allpospaths, this));
	}

	private void addExitsToPath(SHall hall, ArrayList<SPath> paths,
			SPath prevpath) {

		// Loop through every exit
		for (SExit exit : hall.getAllExits()) {

			// Create another path (safeguards against shallow copy)
			SPath copypath = new SPath(prevpath);

			// Safeguard against duplicate exits (if possible)
			if (!copypath.contains(hall))
				copypath.add(hall);

			// Add it to the list
			copypath.add(exit);

			// Add the path to the bigger list
			paths.add(new SPath(copypath));
		}
	}

	private void calcAllPosPaths(SHall s, ArrayList<SPath> paths, SPath prevpath) {

		// Get the halls that branch out of this hall
		ArrayList<SHall> branchHalls = s.getAllHallJunctions();

		// Loop through them
		for (SHall hall : branchHalls)

			// Safeguards against infinite loops and halls of different levels
			// (if possible)
			if (!prevpath.contains(hall) && s.getLevel() == hall.getLevel()) {

				// Makes another path
				SPath branchPath = new SPath(prevpath);

				// Add the hall to the path
				branchPath.add(hall);

				// Recursively call it again
				calcAllPosPaths(hall, paths, branchPath);

				// At the end, add all of the exits to the path if there are
				if (!hall.getAllExits().equals(null))
					addExitsToPath(hall, paths, prevpath);
			}
	}

	public boolean isCollidingUsingGlobalCoords(SSchool school, SGUI gui,
			double lengthoffset) {

		// Check all people in the whole school
		for (SPerson s : school.getAllPeople())
			if (s.getCurrentLevel() == getCurrentLevel()
					&& !equals(s) // Prevents "self collision"
					&& isIntersectingUsingGlobalCoords(s, school, gui,
							lengthoffset) && !s.isSafe())
				return true;
		return false;
	}

	public boolean isIntersectingUsingGlobalCoords(SPerson person,
			SSchool school, SGUI gui, double lengthoffset) {

		// Checks if there is a collision (using screen coordinates)

		Rectangle testrect = new Rectangle(getGlobalBounds(school, gui));

		// Apply the offset
		if (getCurrentHall().getDirection().equals(SDirection.LEFT_TO_RIGHT))
			testrect.setLocation(
					testrect.x
							+ (int) (gui.getDispwidth() * (lengthoffset / school
									.getWidth())), testrect.y);
		else
			testrect.setLocation(
					testrect.x,
					testrect.y
							+ (int) (gui.getDispheight() * (lengthoffset / school
									.getHeight())));

		return person.getGlobalBounds(school, gui).intersects(testrect);
	}

	public boolean isCloseToNode(SNode node) {

		// Checks if the distance to the node from the beginning of the hall
		// minus the hall length position of the person is less than the
		// acceptable amount for being close
		if (getCurrentHall().has(node)
				&& Math.abs(getCurrentHall().getDistToNodeFromBegin(node)
						- getHallLengthPosition()) <= CLOSE_EPSILON)
			return true;
		return false;
	}

	public Point getLocalToGlobalCoords(SSchool school, SGUI gui) {

		// Get the screen coordinates of the person based on the hall length and
		// horizontal position, and the hall's coordinates
		if (currentHall.getDirection().equals(SDirection.LEFT_TO_RIGHT))
			return new Point(
					(int) (currentHall.getLocalToGlobalCoords(school, gui).x + gui.getDispwidth()
							* (getHallLengthPosition() / school.getWidth())),
					(int) (currentHall.getLocalToGlobalCoords(school, gui).y + gui
							.getDispheight()
							* (getHallHorizPosition() / school.getHeight())));
		else
			return new Point(
					(int) (currentHall.getLocalToGlobalCoords(school, gui).x + gui.getDispwidth()
							* (getHallHorizPosition() / school.getWidth())),
					(int) (currentHall.getLocalToGlobalCoords(school, gui).y + gui
							.getDispheight()
							* (getHallLengthPosition() / school.getHeight())));
	}

	// Short for getLocationNotAbsoluteValueNodeDistance
	public double getLocNotAbsNodeDist(SNode node, SHall hall) {
		return hall.getDistToNodeFromBegin(node) - getHallLengthPosition();
	}

	public double getGlobalNodeDistance(SNode node, SSchool school, SGUI gui) {
		int dx = Math.abs(school.getGlobalNodePosition(node, gui).x
				- getLocalToGlobalCoords(school, gui).x);
		int dy = Math.abs(school.getGlobalNodePosition(node, gui).y
				- getLocalToGlobalCoords(school, gui).y);

		return Math.sqrt(dx ^ 2 + dy ^ 2);
	}

	public Dimension getGlobalNodeDimDistance(SNode node, SSchool school,
			SGUI gui) {
		int dx = Math.abs(school.getGlobalNodePosition(node, gui).x
				- getLocalToGlobalCoords(school, gui).x);
		int dy = Math.abs(school.getGlobalNodePosition(node, gui).y
				- getLocalToGlobalCoords(school, gui).y);

		return new Dimension(dx, dy);
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public double getAverageWidth() {
		return AVERAGE_WIDTH;
	}

	public double getAverageSpeed() {
		return AVERAGE_SPEED;
	}

	public SHall getCurrentHall() {
		return currentHall;
	}

	public void setCurrentHall(SHall currentHall) {
		this.currentHall = currentHall;
	}

	public double getHallHorizPosition() {
		return hallHorizPosition;
	}

	public void setHallHorizPosition(double hallHorizPosition) {
		this.hallHorizPosition = hallHorizPosition;
	}

	public double getHallLengthPosition() {
		return hallLengthPosition;
	}

	public void setHallLengthPosition(double hallLengthPosition) {
		this.hallLengthPosition = hallLengthPosition;
	}

	public static double getAverageMetersPerSecond() {
		return AVERAGE_METERS_PER_SECOND;
	}

	public boolean isSafe() {
		return isSafe;
	}

	public void setSafe(boolean isSafe) {
		this.isSafe = isSafe;
	}

	public SPath getPath() {
		return path;
	}

	public void setPath(SPath path) {
		this.path = path;
	}

	public abstract void move(SSchool school, SGUI gui);

	public long getTimer() {
		return timer;
	}

	public void setTimer(long timer) {
		this.timer = timer;
	}

	public static long getTimerCap() {
		return TIMER_CAP;
	}

}
