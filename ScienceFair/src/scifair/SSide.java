package scifair;

public enum SSide {
	//different sides
	LEFT, RIGHT, BOTH;
	
	// String to SSide
	public static SSide getSideValue(String s){
		if(s.equalsIgnoreCase("LEFT"))
			return LEFT;
		else if(s.equalsIgnoreCase("RIGHT"))
			return RIGHT;
		else if(s.equalsIgnoreCase("BOTH"))
			return BOTH;
		return null;
	}
}
