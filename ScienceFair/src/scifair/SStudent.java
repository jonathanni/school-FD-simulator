package scifair;

public class SStudent extends SPerson {
	private STeacher parent;

	public SStudent(STeacher parent) {
		setParent(parent);
	}

	public STeacher getParent() {
		return parent;
	}

	public void setParent(STeacher parent) {
		this.parent = parent;
	}

	@Override
	public void move(SSchool school, SGUI gui) {

		// Distance to move
		double delta;

		// Index of current hall in path
		int index = getParent().getPath().indexOf(getCurrentHall());

		// Approaching the next hall
		if (index + 1 != getParent().getPath().size() - 1
				&& isCloseToNode(getParent().getPath().get(index + 1))) {

			// Store the previous hall
			SHall prevhall = getCurrentHall();

			// Store the previous length position
			double prevpos = getHallLengthPosition();

			// Switch halls
			setCurrentHall(getParent().getPath().get(index + 1) instanceof SHall ? (SHall) getParent()
					.getPath().get(index + 1) : (SHall) getParent().getPath()
					.get(index + 2));

			// Change position based on the distance from the previous hall
			setHallLengthPosition(getCurrentHall().getDistToNodeFromBegin(
					prevhall));

			// Collision detection (collided?)
			if (isCollidingUsingGlobalCoords(school, gui, 0)) {

				// Revert
				setCurrentHall(prevhall);
				setHallLengthPosition(prevpos);
			}
		}

		// If the hall changes due to above calculation
		index = getParent().getPath().indexOf(getCurrentHall());

		// Move based on the parent's location
		// This is separate because the getDistToNodeFromBegin function takes
		// more time than the regular getHallLengthPosition function
		if (getParent().getCurrentHall().equals(getCurrentHall())) {

			// Simply move toward the parent
			delta = Math.signum(getParent().getHallLengthPosition()
					- getHallLengthPosition())
					* SPerson.getAverageMetersPerSecond()
					* (SHandler.getIncrement() / 1000.0);

		} else { // Not the parent's hall

			// Move toward the next hall
			delta = Math.signum(getCurrentHall().getDistToNodeFromBegin(
					getParent().getPath().get(index + 1))
					- getHallLengthPosition())
					* SPerson.getAverageMetersPerSecond()
					* (SHandler.getIncrement() / 1000.0);

		}

		// Change position
		setHallLengthPosition(getHallLengthPosition() + delta);

		// Use a buffer space based on the direction the person is going
		// towards, and check for collision based on that buffer
		if (isCollidingUsingGlobalCoords(school, gui,
				Math.signum(delta) == 1 ? 0.55 : -0.55)) {

			// Revert
			setHallLengthPosition(getHallLengthPosition() - delta);

			// Store the horizontal position
			double prevpos = getHallHorizPosition();

			// Keep in a "single file line"
			setHallHorizPosition(getParent().getHallHorizPosition());

			// But revert if it's colliding
			if (isCollidingUsingGlobalCoords(school, gui, 0))
				setHallHorizPosition(prevpos);
		}

		// Check to see if the person is close to the exit
		if (isCloseToNode(getParent().getPath().get(
				getParent().getPath().size() - 1)))
			setSafe(true);
	}
}
