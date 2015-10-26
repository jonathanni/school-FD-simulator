package scifair;

import java.awt.Point;
import java.util.ArrayList;

public class SSchool {

	// List for all the halls
	private ArrayList<SHall> halls = new ArrayList<SHall>();

	// List of people
	private STeacher[] teachers;

	private double width, height;

	private String name;

	public SSchool(String name, double width, double height) {
		setName(name);
		setWidth(width);
		setHeight(height);
	}

	public SSchool(SSchool otherschool) {
		setName(otherschool.getName());
		setWidth(otherschool.getWidth());
		setHeight(otherschool.getHeight());
		setHalls(otherschool.getHalls());

		teachers = otherschool.getTeachers();
	}

	public SHall getHallByID(String ID) {
		for (SHall hall : halls)
			if (hall.getName().equalsIgnoreCase(ID))
				return hall;
		return null;
	}

	public int getSafeClassesNum() {
		int count = 0;

		for (STeacher teacher : teachers)
			if (teacher.isSafe())
				count++;

		return count;
	}

	public int getSafePeopleNum() {
		int count = 0;

		for (SPerson person : getAllPeople())
			if (person.isSafe())
				count++;

		return count;
	}

	public Point getGlobalNodePosition(SNode node, SGUI gui) {
		for (SHall hall : getHalls())
			if (hall.getLeftNodes().contains(node)
					|| hall.getRightNodes().contains(node))
				return hall.getGlobalNodePosition(node, this, gui);
		return null;
	}

	// Initialize the teachers
	public void initTeachers(int perclassroom) {

		teachers = new STeacher[getAllNonEmptyClassrooms().size()];

		SClassroom.setStudentsPerClass(perclassroom);

		for (int i = 0; i < teachers.length; i++) {

			// NOTE: The order of these statements is crucial!

			teachers[i] = new STeacher(perclassroom);
			getAllNonEmptyClassrooms().get(i).setTeacher(teachers[i]);
			teachers[i].calcPath();

		}
	}

	public ArrayList<SExit> getAllExits() {
		ArrayList<SExit> exits = new ArrayList<SExit>();

		for (SHall hall : getHalls()) {
			for (SNode left : hall.getLeftNodes())
				if (left instanceof SExit)
					exits.add((SExit) left);
			for (SNode right : hall.getRightNodes())
				if (right instanceof SExit && !exits.contains((SExit) right))
					exits.add((SExit) right);
		}

		return exits;
	}

	public ArrayList<SPerson> getAllPeople() {
		ArrayList<SPerson> people = new ArrayList<SPerson>();

		for (STeacher teacher : teachers) {
			people.add(teacher);
			people.addAll(teacher.getStudents());
		}

		return people;
	}

	// Get all the classrooms
	public ArrayList<SClassroom> getAllNonEmptyClassrooms() {
		ArrayList<SClassroom> temp = new ArrayList<SClassroom>();

		for (SHall s : halls)
			temp.addAll(s.getAllNonNullClassrooms());

		return temp;
	}

	public ArrayList<SHall> getHalls() {
		return halls;
	}

	public void setHalls(ArrayList<SHall> halls) {
		this.halls = halls;
	}

	public void addHall(SHall s) {
		halls.add(s);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public STeacher[] getTeachers() {
		return teachers;
	}
}
