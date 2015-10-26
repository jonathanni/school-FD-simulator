package scifair;

import java.util.ArrayList;

public class SPath extends ArrayList<SNode> {

	private static final long serialVersionUID = 5737540438376526372L;

	public SPath() {
	}

	public SPath(SPath copy) {
		addAll(copy);
	}

	public static SPath getShortestPath(ArrayList<SPath> paths, SPerson person) {

		double length = Double.MAX_VALUE;
		SPath shortest = null;

		for (SPath path : paths)
			if (path.getLength(person) < length
					&& path.get(path.size() - 1) instanceof SExit) {
				length = path.getLength(person);
				shortest = path;
			}

		return shortest;
	}

	public double getLength(SPerson person) {
		double length = 0;

		if (size() != 2) {
			length += Math.abs(((SHall) get(0)).getDistToNodeFromBegin(get(1)) - person.getHallLengthPosition());

			for (int i = 1; i < size() - 2; i++)
				if (get(i) instanceof SHall)
					length += Math.abs(((SHall) get(i))
							.getDistToNodeFromBegin(get(i + 1))
							- ((SHall) get(i))
									.getDistToNodeFromBegin(get(i - 1)));
				else
					length += Math.abs(((SHall) get(i + 1))
							.getDistToNodeFromBegin(get(i))
							- ((SHall) get(i + 1))
									.getDistToNodeFromBegin(get(i + 2)));
		} else
			length = Math.abs(((SHall) get(0))
					.getDistToNodeFromBegin(get(size() - 1))
					- person.getHallLengthPosition());

		return length;
	}
}
