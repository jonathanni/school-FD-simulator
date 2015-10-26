package scifair;

public class SExit extends SNode {
	private String name;

	public SExit(String name, double width, double height) {
		setName(name);
		setWidth(width);
		setHeight(height);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		return name.hashCode() + super.hashCode();
	}
}
