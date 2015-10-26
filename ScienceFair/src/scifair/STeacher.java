package scifair;

import java.util.ArrayList;

public class STeacher extends SPerson {

	private ArrayList<SStudent> students = new ArrayList<SStudent>();

	public STeacher(int perclassroom) {
		for (int i = 0; i < perclassroom; i++)
			students.add(new SStudent(this));
	}

	public ArrayList<SStudent> getStudents() {
		return students;
	}

	@Override
	public void move(SSchool school, SGUI gui) {

		followPath(getPath(), school, gui);

		if (isCloseToNode(getPath().get(getPath().size() - 1)))
			setSafe(true);
	}

	private void followPath(SPath path, SSchool school, SGUI gui) {
		double deltal = 0; // Relative movement

		int index = path.indexOf(getCurrentHall());

		if (index + 1 != path.size() - 1
				&& isCloseToNode(path.get(index + 1) instanceof SHall ? (SHall) path
						.get(index + 1) : (SHall) path.get(index + 2))) { // The
																			// next
																			// hall
			SHall prevhall = getCurrentHall();
			double prevpos = getHallLengthPosition();

			setCurrentHall(path.get(index + 1) instanceof SHall ? (SHall) path
					.get(index + 1) : (SHall) path.get(index + 2));

			setHallLengthPosition(getCurrentHall().getDistToNodeFromBegin(
					prevhall));

			if (isCollidingUsingGlobalCoords(school, gui, 0)) {
				setCurrentHall(prevhall);
				setHallLengthPosition(prevpos);

				if (isCollidingUsingGlobalCoords(school, gui, 0)) {
					setHallHorizPosition(getCurrentHall().getWidth()
							- getHallHorizPosition());

					if (isCollidingUsingGlobalCoords(school, gui, 0))
						setHallHorizPosition(getCurrentHall().getWidth()
								- getHallHorizPosition());
				}
			}
		}

		index = path.indexOf(getCurrentHall()); // Assign it again in the case
												// if it changes

		if (index != path.size() - 2) { // If the person is not on the last leg
										// of the evacuation path

			deltal = Math.signum(getLocNotAbsNodeDist(path.get(index + 1),
					getCurrentHall()))
					* SPerson.getAverageMetersPerSecond()
					* (SHandler.getIncrement() / 1000.0);

		} else { // Otherwise

			deltal = Math.signum(getLocNotAbsNodeDist(
					path.get(path.size() - 1), getCurrentHall()))
					* SPerson.getAverageMetersPerSecond()
					* (SHandler.getIncrement() / 1000.0);

		}

		setHallLengthPosition(getHallLengthPosition() + deltal);

		if (isCollidingUsingGlobalCoords(school, gui,
				Math.signum(deltal) == 1 ? 0.55 : -0.55)) {

			setTimer(getTimer() + 1);

			setHallLengthPosition(getHallLengthPosition() - deltal);

			moveHorizontally(school, gui, deltal);
		}
	}

	private void moveHorizontally(SSchool school, SGUI gui, double deltal) {
		double prevpos = getHallHorizPosition();

		if (getTimer() > getTimerCap()
				&& getHallHorizPosition() > 0
				&& getHallHorizPosition() < getCurrentHall().getWidth()
						- getAverageWidth()) {

			setHallHorizPosition(getHallHorizPosition() + -Math.signum(deltal)
					* SPerson.getAverageMetersPerSecond()
					* (SHandler.getIncrement() / 1000.0));

			if (isCollidingUsingGlobalCoords(school, gui, 0))
				setHallHorizPosition(prevpos);

			setTimer(0);
		}
	}
}
