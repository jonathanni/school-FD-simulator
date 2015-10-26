package scifair;

public class SNode {
	private double width, height; // In meters
	private SNode parent;
	private int level;

	public String toString() {
		return "NODE: (" + width + ", " + height + ")";
	}

	public int hashCode() {
		return ((Double) width).hashCode() + ((Double) height).hashCode()
				+ ((Integer) level).hashCode();
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public SNode getParent() {
		return parent;
	}

	public void setParent(SNode parent) {
		this.parent = parent;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
