package scifair;

import java.awt.Point;
import java.util.ArrayList;

public class SHall extends SNode {

	private ArrayList<SNode> left, right; // Left and right sides of the hall
	private SDirection direction; // Direction: see SDirection
	private String name; // The name of the hall displayed on the screen
	private double x, y; // The starting position of the hall

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode() + left.hashCode() + right.hashCode()
				+ direction.hashCode() + new Double(x).hashCode()
				+ new Double(y).hashCode();
	}

	// Positions are from the top left corner down and right
	public SHall(SDirection direction, double width, String name,
			double startx, double starty, int level) {

		// Init the lists
		left = new ArrayList<SNode>();
		right = new ArrayList<SNode>();

		setLevel(level);
		setDirection(direction);

		setWidth(width);

		setName(name);

		setX(startx);
		setY(starty);

		setParent(this);
	}

	// Default constructor
	public SHall() {
		this(SDirection.LEFT_TO_RIGHT, 0, "Untitled", 0, 0, -10);
	}

	public ArrayList<SExit> getAllExits() {
		// Temporary list
		ArrayList<SExit> exits = new ArrayList<SExit>();

		// Add from left and right sides
		for (SNode nodea : getLeftNodes())
			if (nodea instanceof SExit)
				exits.add((SExit) nodea);

		for (SNode nodeb : getRightNodes())
			if (nodeb instanceof SExit)
				exits.add((SExit) nodeb);

		return exits;
	}

	// Find the node
	public boolean has(SNode node) {

		// Check on the left and right sides
		for (SNode nodea : getLeftNodes())
			if (nodea.equals(node))
				return true;

		for (SNode nodeb : getRightNodes())
			if (nodeb.equals(node))
				return true;

		return false;
	}

	public Point getLocalToGlobalCoords(SSchool school, SGUI gui) {
		// Screen coordinates
		return new Point(
				(int) (gui.getDispwidth() * (getX() / school.getWidth())),
				(int) (gui.getDispheight() * (getY() / school.getHeight())));
	}

	public SSide getNodeSide(SNode node) {

		// Get the side a node is on
		// Check both sides

		for (SNode nodea : getLeftNodes())
			if (nodea.equals(node))
				return SSide.LEFT;

		for (SNode nodeb : getRightNodes())
			if (nodeb.equals(node))
				return SSide.RIGHT;

		return null;
	}

	public ArrayList<SHall> getAllHallJunctions() {

		// Temporary list
		ArrayList<SHall> halls = new ArrayList<SHall>();

		// Check both sides for halls
		for (SNode nodea : getLeftNodes())
			if (nodea instanceof SHall)
				halls.add((SHall) nodea);

		for (SNode nodeb : getRightNodes())

			// Check for duplicate halls resulting from SSide.BOTH
			if (nodeb instanceof SHall && !halls.contains(nodeb))
				halls.add((SHall) nodeb);

		return halls;
	}

	public Point getGlobalNodePosition(SNode node, SSchool school, SGUI gui) {

		int x, y;

		if (getDirection().equals(SDirection.LEFT_TO_RIGHT)) {

			// Get the hall's x + distance to node from beginning screen x
			// coordinate, and then get the hall's y +/- the hall's width / 2
			// based on the node's side
			x = (int) (getLocalToGlobalCoords(school, gui).x + gui
					.getDispwidth()
					* (getDistToNodeFromBegin(node) / school.getWidth()));
			y = (int) (getLocalToGlobalCoords(school, gui).y + (getNodeSide(
					node).equals(SSide.LEFT) ? -(gui.getDispheight() * (node
					.getWidth() / school.getHeight()))
					- (gui.getDispheight() * (getWidth() / 2 / school
							.getHeight())) : gui.getDispheight()
					* (getWidth() / 2 / school.getHeight())));
		} else {

			// Do the reverse of above (switch x and y)
			x = (int) (getLocalToGlobalCoords(school, gui).x + (getNodeSide(
					node).equals(SSide.LEFT) ? -(gui.getDispwidth() * (node
					.getWidth() / school.getWidth()))
					- (gui.getDispwidth() * (getWidth() / 2 / school.getWidth()))
					: gui.getDispwidth() * (getWidth() / 2 / school.getWidth())));
			y = (int) (getLocalToGlobalCoords(school, gui).y + gui
					.getDispheight()
					* (getDistToNodeFromBegin(node) / school.getHeight()));
		}
		return new Point(x, y);
	}

	// Get the distance from the beginning to a node
	public double getDistToNodeFromBegin(SNode a) {

		// The counted distance
		double counter = 0;
		boolean hit = false; // Allows the program to skip ahead

		// Check the left side
		for (SNode nodea : left)
			if (!nodea.equals(a)) // Until the node is found
				counter += nodea.getHeight(); // Increment
			else {
				hit = true; // Break out
				break;
			}

		if (hit)
			return counter; // Exit

		// Reset
		counter = 0;

		// Check the right side if it's not on the left side
		for (SNode nodeb : right)
			if (!nodeb.equals(a))
				counter += nodeb.getHeight(); // Increment
			else
				break; // Break out

		return counter;
	}

	// Gets all classrooms with teachers in them
	public ArrayList<SClassroom> getAllNonNullClassrooms() {

		// Temporary list
		ArrayList<SClassroom> temp = new ArrayList<SClassroom>();

		// Check the left side
		for (SNode s : left)
			if (s instanceof SClassroom)
				// Null classrooms' room numbers are 0
				if (((SClassroom) s).getRoomNumber() != -1)
					temp.add((SClassroom) s);

		// Check the right side
		for (SNode s : right)
			if (s instanceof SClassroom)
				if (((SClassroom) s).getRoomNumber() != -1)
					temp.add((SClassroom) s);

		return temp;
	}

	public ArrayList<SNode> getLeftNodes() {
		return left;
	}

	public ArrayList<SNode> getRightNodes() {
		return right;
	}

	@Override
	public double getHeight() {
		return getWidth();
	}

	// Get the hall's length
	public double getLength() {
		// The two sides may have different lengths
		double halll = 0, hallr = 0;

		// Loop through the left side
		for (SNode s : left)
			if (s instanceof SHall) // getWidth() == getHeight() in terms of an
									// SHall
				halll += ((SHall) s).getWidth();
			else
				halll += s.getHeight(); // Vertical distance

		// Loop through the right side
		for (SNode s : right)
			if (s instanceof SHall)
				hallr += ((SHall) s).getWidth();
			else
				hallr += s.getHeight();

		// Return the greater of the two
		return Math.max(halll, hallr);
	}

	// Add an exit
	public void addExit(SExit exit, SSide side) {

		// SExit's level is not initialized at startup
		exit.setLevel(getLevel());

		// "this" owns the exit now
		exit.setParent(this);

		// Add to necessary arrays based on side
		if (side == SSide.LEFT)
			left.add(exit);
		else if (side == SSide.RIGHT)
			right.add(exit);
		else if (side == SSide.BOTH) {
			left.add(exit);
			right.add(exit);
		}
	}

	// Add a stair
	public void addStair(SStairs stairs, SSide side) {

		// SStair's level is not initialized at startup
		stairs.setLevel(getLevel());

		// Do not set parent because stairs can have multiple parents

		// Add to necessary arrays based on side
		if (side == SSide.LEFT)
			left.add(stairs);
		else if (side == SSide.RIGHT)
			right.add(stairs);
		else if (side == SSide.BOTH) {
			left.add(stairs);
			right.add(stairs);
		}
	}

	// Join two halls
	public void junction(SHall intersection, SSide side) {

		// Add a filler if the two halls are not the right size
		if (left.size() > right.size())
			for (int i = left.size() - right.size() - 1; i >= 0; i--)
				right.add(new SNode());
		else if (left.size() < right.size())
			for (int i = right.size() - left.size() - 1; i >= 0; i--)
				left.add(new SNode());

		// Add to necessary arrays based on side
		if (side == SSide.LEFT)
			left.add(intersection);
		else if (side == SSide.RIGHT)
			right.add(intersection);
		else if (side == SSide.BOTH) {
			left.add(intersection);
			right.add(intersection);
		}
	}

	// If the classrooms are on both sides of the hall
	public void addClassroomsOnBothSides(SClassroom left, SClassroom right) {

		// SClassroom's level is not initialized at startup
		left.setLevel(getLevel());
		right.setLevel(getLevel());

		// "this" owns the classrooms now
		left.setParent(this);
		right.setParent(this);

		this.left.add(left);
		this.right.add(right);
	}

	public void addClassroom(SClassroom add, SSide side) {

		// Add a classroom to the list of nodes

		// Set the level of the classroom to the current hall level
		add.setLevel(getLevel());

		// Set the parent of the classroom to this
		add.setParent(this);

		// -1 is the classroom number if it is null
		if (side == SSide.LEFT) {
			left.add(add);

			// Add a filler classroom to take up the space
			right.add(new SClassroom(-1, true, add.getWidth(), add.getHeight()));
		} else if (side == SSide.RIGHT) {
			right.add(add);

			// Filler
			left.add(new SClassroom(-1, true, add.getWidth(), add.getHeight()));
		}
	}

	public SDirection getDirection() {
		return direction;
	}

	public void setDirection(SDirection direction) {
		this.direction = direction;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}