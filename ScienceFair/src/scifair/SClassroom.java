package scifair;

// A node that is not connected to any other node except the hallway
public class SClassroom extends SNode {

	// Using the observed statistic of 15 seconds per 30 people
	private static final double AVERAGE_CLASS_LINEUP_PER_PERSON_PROPORTION = (1.0 / 2.0);
	private static int STUDENTS_PER_CLASS; // The set student number
	private int roomNumber; // The room number

	private boolean isEmpty; // Is this classroom really a classroom?

	private STeacher teacher; // The teacher assigned to the classroom

	public SClassroom(int roomNumber, boolean empty, double width, double length) {
		setEmpty(empty);
		setRoomNumber(roomNumber);

		setWidth(width);
		setHeight(length);
	}

	@Override
	public String toString() {
		return String.valueOf(roomNumber);
	}

	@Override
	public int hashCode() {
		return ((Double) AVERAGE_CLASS_LINEUP_PER_PERSON_PROPORTION).hashCode()
				+ ((Integer) STUDENTS_PER_CLASS).hashCode()
				+ ((Integer) roomNumber).hashCode()
				+ ((Boolean) isEmpty).hashCode() + teacher.hashCode()
				+ super.hashCode();
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public STeacher getTeacher() {
		return teacher;
	}

	public void setTeacher(STeacher teacher) {
		SHall parent = ((SHall) getParent()); // The SHall where this classroom
												// resides in

		this.teacher = teacher; // Assign the teacher

		getTeacher().setCurrentLevel(getLevel()); // Set the current level of
													// the teacher based on the
													// classroom level
		getTeacher().setHallHorizPosition(
				parent.getNodeSide(this).equals(SSide.LEFT) ? -parent
						.getWidth() / 2 + teacher.getAverageWidth() : parent
						.getWidth() / 2); // Set the teacher's horizontal
											// position based on the side of the
											// classroom and the width of a
											// person

		// Set the teacher's "vertical" position based on the distance from the
		// beginning of the hall
		getTeacher().setHallLengthPosition(parent.getDistToNodeFromBegin(this));

		// Do the same thing for each student of the teacher
		for (SStudent s : getTeacher().getStudents()) {
			s.setCurrentLevel(getLevel());
			s.setHallHorizPosition(parent.getNodeSide(this).equals(SSide.LEFT) ? -parent
					.getWidth() / 2 + teacher.getAverageWidth()
					: parent.getWidth() / 2);
			s.setHallLengthPosition(parent.getDistToNodeFromBegin(this));

			// Set the current SHall for each student
			s.setCurrentHall(parent);
		}

		// Set the current SHall of the teacher
		getTeacher().setCurrentHall(parent);
	}

	public int getNumberOfStudents() {
		return 1 + teacher.getStudents().size();
	}

	public static double getACLPPP() {
		return AVERAGE_CLASS_LINEUP_PER_PERSON_PROPORTION;
	}

	public static int getStudentsPerClass() {
		return STUDENTS_PER_CLASS;
	}

	public static void setStudentsPerClass(int studentsPerClass) {
		STUDENTS_PER_CLASS = studentsPerClass;
	}
}
