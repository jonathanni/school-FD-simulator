package scifair;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

/**
 * 
 * 
 * 
 * @author Jonathan
 * 
 */

public class SMain {

	// private static final int numofpeople = 21;

	// Easy run
	public static void main(String[] args) throws NumberFormatException,
			IOException {

		String input;

		do
			input = JOptionPane
					.showInputDialog("Enter the number of students per classroom: ");
		while (!Pattern.compile("^[0-9]+$").matcher(input).matches());

		int numofpeople = Integer.valueOf(input);

		new Thread(new SHandler(numofpeople)).start();
	}

	public static void invokeNew(int numofpeople, SSchool school) {
		new Thread(new SHandler(numofpeople, school)).start();
	}
}
