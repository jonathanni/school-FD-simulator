package scifair;

public class SStairs extends SNode {

	private String name;

	public SStairs(String name) {
		setName(name);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	// Move a person from the top to the bottom and vice versa
	public void transport(SPerson p, SDirection direction) {
		if (direction == SDirection.UP)
			p.setCurrentLevel(p.getCurrentLevel() + 1);
		else if (direction == SDirection.DOWN)
			;
		p.setCurrentLevel(p.getCurrentLevel() - 1);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
