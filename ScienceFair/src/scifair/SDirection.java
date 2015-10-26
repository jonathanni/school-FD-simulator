package scifair;

public enum SDirection {
	UP, DOWN, LEFT_TO_RIGHT, TOP_TO_BOTTOM;

	// Convert String to SDirection
	public static SDirection getDirectionalValue(String s) {
		if (s.equalsIgnoreCase("LEFT_TO_RIGHT"))
			return LEFT_TO_RIGHT;
		else if (s.equalsIgnoreCase("TOP_TO_BOTTOM"))
			return TOP_TO_BOTTOM;
		return null;
	}

	// Convert SDirection to String

}
