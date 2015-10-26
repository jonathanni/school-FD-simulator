package scifair;

public final class SSampleSchool extends SSchool {

	// These are public because getters and setters for these variables will
	// clutter the system

	// Data
	public static final SExit MAIN_ENTRANCE = new SExit("Main Entrance", 3, 1);
	public static final SExit LOADING_DOCK = new SExit("Loading Dock", 1.4, 1);
	public static final SExit LOWER_EXIT_1 = new SExit("Lower Exit #1", 1, 1);
	public static final SExit LOWER_CENTRAL_EXIT = new SExit(
			"Lower Central Exit", 2, 1);
	public static final SExit LOWER_EXIT_2 = new SExit("Lower Exit #2", 1, 1);

	public static final SHall ENTRANCE_HALL = new SHall(
			SDirection.TOP_TO_BOTTOM, 3, "Entrance Hall", 31, 17, 0);
	public static final SHall TOP_TRAVERSING_HALL = new SHall(
			SDirection.LEFT_TO_RIGHT, 2, "Top Traversing Hall", 4, 16, 0);
	public static final SHall LEFT_WING_HALL = new SHall(
			SDirection.TOP_TO_BOTTOM, 1.4, "Left Wing Hall", 5, 4, 0);
	public static final SHall LOADING_DOCK_HALL = new SHall(
			SDirection.LEFT_TO_RIGHT, 1.4, "Loading Dock Hall", 3, 24, 0);
	public static final SHall RIGHT_WING_HALL = new SHall(
			SDirection.TOP_TO_BOTTOM, 1, "Right Wing Hall", 58, 4, 0);
	public static final SHall BOTTOM_TRAVERSING_HALL = new SHall(
			SDirection.LEFT_TO_RIGHT, 2, "Bottom Traversing Hall", 5, 14, -1);
	public static final SHall BOTTOM_MIDDLE_HALL = new SHall(
			SDirection.TOP_TO_BOTTOM, 3, "Bottom Middle Hall", 20.5, 16, -1);
	public static final SHall BOTTOM_LEFT_WING_HALL = new SHall(
			SDirection.TOP_TO_BOTTOM, 2, "Bottom Left Wing Hall", 52, 15, -1);
	public static final SHall BOTTOM_RIGHT_WING_HALL = new SHall(
			SDirection.TOP_TO_BOTTOM, 2, "Bottom Right Wing Hall", 3, 15, -1);

	public static final SStairs LEFT_WING_STAIRS_1 = new SStairs(
			"Left Wing Stairs #1");
	public static final SStairs LEFT_WING_STAIRS_2 = new SStairs(
			"Left Wing Stairs #2");
	public static final SStairs RIGHT_WING_STAIRS_1 = new SStairs(
			"Right Wing Stairs #1");
	public static final SStairs RIGHT_WING_STAIRS_2 = new SStairs(
			"Right Wing Stairs #2");
	public static final SStairs CENTRAL_STAIRS = new SStairs("Central Stairs");
	public static final SStairs GYM_STAIRS = new SStairs("Gym Stairs");

	// Set up the school
	// RULES: top to bottom, left to right
	// The sides are relative
	// The sizes are relative

	/**
	 * 
	 * SRobertoClementeMiddleSchool constructor.
	 * 
	 * @param name
	 *            the name of the school (included to illustrate how the SSchool
	 *            constructor works)
	 * @param width
	 *            the width of the school in meters (included to illustrate how
	 *            the SSchool constructor works)
	 * @param height
	 *            the height of the school in meters (included to illustrate how
	 *            the SSchool constructor works)
	 * @param perclassroom
	 *            the number of students per classroom (included to illustrate
	 *            how the SSchool constructor works)
	 * 
	 */

	public SSampleSchool(String name, double width,
			double height, int perclassroom) {

		// Invoke SSchool constructor
		super(name, width, height);

		// Top floor
		addHall(ENTRANCE_HALL);

		ENTRANCE_HALL.junction(TOP_TRAVERSING_HALL, SSide.BOTH);
		ENTRANCE_HALL.addStair(CENTRAL_STAIRS, SSide.RIGHT);
		ENTRANCE_HALL.addClassroom(new SClassroom(231, false, 2, 4),
				SSide.RIGHT);
		ENTRANCE_HALL.addClassroom(new SClassroom(241, false, 2, 3),
				SSide.RIGHT);
		ENTRANCE_HALL.addClassroom(new SClassroom(-1, true, 18, 18),
				SSide.RIGHT); // Cafeteria
		ENTRANCE_HALL.addExit(MAIN_ENTRANCE, SSide.BOTH);

		addHall(TOP_TRAVERSING_HALL);

		TOP_TRAVERSING_HALL.junction(LEFT_WING_HALL, SSide.BOTH);
		TOP_TRAVERSING_HALL.addStair(RIGHT_WING_STAIRS_2, SSide.RIGHT);
		TOP_TRAVERSING_HALL.addClassroomsOnBothSides(new SClassroom(234, false,
				9, 6), new SClassroom(246, false, 9, 7));
		TOP_TRAVERSING_HALL.addClassroom(new SClassroom(232, false, 8, 6),
				SSide.LEFT);
		TOP_TRAVERSING_HALL.addClassroom(new SClassroom(230, false, 6, 7),
				SSide.LEFT);
		TOP_TRAVERSING_HALL.addClassroom(new SClassroom(-1, true, 4, 4),
				SSide.LEFT);
		TOP_TRAVERSING_HALL.junction(ENTRANCE_HALL, SSide.RIGHT);
		TOP_TRAVERSING_HALL.addClassroom(new SClassroom(228, false, 7, 9),
				SSide.LEFT);
		TOP_TRAVERSING_HALL.addClassroom(new SClassroom(226, false, 7, 4),
				SSide.LEFT);
		TOP_TRAVERSING_HALL.addClassroom(new SClassroom(225, false, 7, 4),
				SSide.LEFT);
		TOP_TRAVERSING_HALL.addClassroom(new SClassroom(224, false, 8, 7),
				SSide.LEFT);
		TOP_TRAVERSING_HALL.junction(RIGHT_WING_HALL, SSide.RIGHT);

		addHall(LEFT_WING_HALL);

		LEFT_WING_HALL.addStair(LEFT_WING_STAIRS_1, SSide.BOTH);
		LEFT_WING_HALL.addClassroomsOnBothSides(
				new SClassroom(239, false, 6, 5), new SClassroom(240, false, 8,
						6));
		LEFT_WING_HALL.addClassroomsOnBothSides(
				new SClassroom(238, false, 6, 4), new SClassroom(237, false, 7,
						5));
		LEFT_WING_HALL.addClassroom(new SClassroom(236, false, 7, 4),
				SSide.RIGHT);
		LEFT_WING_HALL.junction(TOP_TRAVERSING_HALL, SSide.LEFT);
		LEFT_WING_HALL.addClassroom(new SClassroom(235, false, 7, 6),
				SSide.RIGHT);
		LEFT_WING_HALL.addStair(LEFT_WING_STAIRS_2, SSide.RIGHT);
		LEFT_WING_HALL.junction(LOADING_DOCK_HALL, SSide.LEFT);

		addHall(RIGHT_WING_HALL);

		RIGHT_WING_HALL.addStair(RIGHT_WING_STAIRS_1, SSide.RIGHT);
		RIGHT_WING_HALL.addClassroom(new SClassroom(219, false, 5, 4),
				SSide.LEFT);
		RIGHT_WING_HALL.addClassroomsOnBothSides(new SClassroom(218, false, 5,
				4), new SClassroom(220, false, 5, 4));
		RIGHT_WING_HALL.addClassroomsOnBothSides(new SClassroom(217, false, 4,
				4), new SClassroom(221, false, 4, 4));
		RIGHT_WING_HALL.addClassroomsOnBothSides(new SClassroom(216, false, 4,
				3), new SClassroom(222, false, 4, 3));
		RIGHT_WING_HALL.addClassroomsOnBothSides(new SClassroom(213, false, 4,
				3), new SClassroom(224, false, 4, 6));
		RIGHT_WING_HALL.addClassroomsOnBothSides(new SClassroom(212, false, 4,
				3), new SClassroom(211, false, 4, 2.5));
		RIGHT_WING_HALL.junction(TOP_TRAVERSING_HALL, SSide.RIGHT);

		addHall(LOADING_DOCK_HALL);

		LOADING_DOCK_HALL.addExit(LOADING_DOCK, SSide.BOTH);
		LOADING_DOCK_HALL.junction(LEFT_WING_HALL, SSide.LEFT);
		LOADING_DOCK_HALL.addClassroom(new SClassroom(242, false, 7, 7),
				SSide.LEFT);
		LOADING_DOCK_HALL.junction(ENTRANCE_HALL, SSide.BOTH);

		// Bottom floor

		addHall(BOTTOM_MIDDLE_HALL);

		BOTTOM_MIDDLE_HALL.junction(BOTTOM_TRAVERSING_HALL, SSide.BOTH);
		BOTTOM_MIDDLE_HALL.addClassroomsOnBothSides(new SClassroom(131, false,
				6, 5), new SClassroom(128, false, 6, 5));
		BOTTOM_MIDDLE_HALL.addClassroomsOnBothSides(new SClassroom(130, false,
				6, 4), new SClassroom(129, false, 6, 4));
		BOTTOM_MIDDLE_HALL.addExit(LOWER_CENTRAL_EXIT, SSide.BOTH);

		addHall(BOTTOM_TRAVERSING_HALL);

		BOTTOM_TRAVERSING_HALL.addStair(RIGHT_WING_STAIRS_2, SSide.LEFT);
		BOTTOM_TRAVERSING_HALL.junction(BOTTOM_RIGHT_WING_HALL, SSide.RIGHT);
		BOTTOM_TRAVERSING_HALL.addClassroom(new SClassroom(125, false, 4, 4),
				SSide.RIGHT);
		BOTTOM_TRAVERSING_HALL.addClassroom(new SClassroom(126, false, 4, 5),
				SSide.RIGHT);
		BOTTOM_TRAVERSING_HALL.addClassroom(new SClassroom(127, false, 3, 3),
				SSide.RIGHT);
		BOTTOM_TRAVERSING_HALL.addStair(CENTRAL_STAIRS, SSide.LEFT);
		BOTTOM_TRAVERSING_HALL.junction(BOTTOM_MIDDLE_HALL, SSide.RIGHT);
		BOTTOM_TRAVERSING_HALL.addClassroomsOnBothSides(new SClassroom(134,
				false, 4, 3), new SClassroom(136, false, 4, 5));
		BOTTOM_TRAVERSING_HALL.addClassroom(new SClassroom(135, false, 4, 3),
				SSide.LEFT);
		BOTTOM_TRAVERSING_HALL.addClassroomsOnBothSides(new SClassroom(138,
				false, 4, 3), new SClassroom(137, false, 4, 6));
		BOTTOM_TRAVERSING_HALL.addClassroom(new SClassroom(139, false, 4, 3),
				SSide.LEFT);
		BOTTOM_TRAVERSING_HALL.addClassroom(new SClassroom(141, false, 4, 3),
				SSide.LEFT);
		BOTTOM_TRAVERSING_HALL.addStair(LEFT_WING_STAIRS_2, SSide.LEFT);
		BOTTOM_TRAVERSING_HALL.addClassroom(new SClassroom(156, false, 4, 6),
				SSide.LEFT);
		BOTTOM_TRAVERSING_HALL.junction(BOTTOM_LEFT_WING_HALL, SSide.RIGHT);

		addHall(BOTTOM_RIGHT_WING_HALL);

		BOTTOM_RIGHT_WING_HALL.junction(BOTTOM_TRAVERSING_HALL, SSide.LEFT);
		BOTTOM_RIGHT_WING_HALL.addClassroomsOnBothSides(new SClassroom(124,
				false, 4, 6), new SClassroom(112, false, 6, 4));
		BOTTOM_RIGHT_WING_HALL.addClassroomsOnBothSides(new SClassroom(111,
				false, 6, 4), new SClassroom(113, false, 6, 4));
		BOTTOM_RIGHT_WING_HALL.addClassroomsOnBothSides(new SClassroom(122,
				false, 4, 3), new SClassroom(116, false, 6, 4));
		BOTTOM_RIGHT_WING_HALL.addClassroomsOnBothSides(new SClassroom(121,
				false, 4, 3), new SClassroom(117, false, 6, 3));
		BOTTOM_RIGHT_WING_HALL.addClassroomsOnBothSides(new SClassroom(120,
				false, 4, 3), new SClassroom(118, false, 6, 3));
		BOTTOM_RIGHT_WING_HALL.addClassroom(new SClassroom(119, false, 8, 5),
				SSide.RIGHT);
		BOTTOM_RIGHT_WING_HALL.addStair(RIGHT_WING_STAIRS_1, SSide.LEFT);
		BOTTOM_RIGHT_WING_HALL.addExit(LOWER_EXIT_1, SSide.RIGHT);

		addHall(BOTTOM_LEFT_WING_HALL);

		BOTTOM_LEFT_WING_HALL.junction(BOTTOM_TRAVERSING_HALL, SSide.RIGHT);
		BOTTOM_LEFT_WING_HALL.addClassroomsOnBothSides(new SClassroom(155,
				false, 5, 5), new SClassroom(153, false, 6, 3));
		BOTTOM_LEFT_WING_HALL.addClassroomsOnBothSides(new SClassroom(154,
				false, 5, 4), new SClassroom(152, false, 3, 2));
		BOTTOM_LEFT_WING_HALL.addClassroomsOnBothSides(new SClassroom(150,
				false, 5, 4), new SClassroom(143, false, 5, 4));
		BOTTOM_LEFT_WING_HALL.addClassroomsOnBothSides(new SClassroom(149,
				false, 5, 4), new SClassroom(145, false, 5, 3));
		BOTTOM_LEFT_WING_HALL.addClassroomsOnBothSides(new SClassroom(148,
				false, 5, 4), new SClassroom(146, false, 5, 4));
		BOTTOM_LEFT_WING_HALL.addClassroom(new SClassroom(147, false, 8, 5),
				SSide.LEFT);
		BOTTOM_LEFT_WING_HALL.addStair(LEFT_WING_STAIRS_1, SSide.RIGHT);
		BOTTOM_LEFT_WING_HALL.addExit(LOWER_EXIT_2, SSide.LEFT);

		// You must call this
		initTeachers(perclassroom);
	}
}
