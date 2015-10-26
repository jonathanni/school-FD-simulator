package loader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import scifair.SClassroom;
import scifair.SDirection;
import scifair.SExit;
import scifair.SHall;
import scifair.SNode;
import scifair.SSchool;
import scifair.SSide;
import scifair.SStairs;

public class SSchoolXMLLoader {

	// Loads XML data

	public static SSchool load(File file) throws ParserConfigurationException,
			IOException, SAXException {
		SSchool tempSchool = null;
		DocumentBuilder docb = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = docb.parse(file);

		Element school = (Element) doc.getElementsByTagName("school").item(0);

		tempSchool = new SSchool(school.getAttribute("name"),
				Double.valueOf(school.getAttribute("width")),
				Double.valueOf(school.getAttribute("height")));

		for (int i = 0; i < school.getElementsByTagName("declare-hall")
				.getLength(); i++) {
			Element hall = (Element) school
					.getElementsByTagName("declare-hall").item(i);

			SHall tHall = new SHall(SDirection.getDirectionalValue(hall
					.getAttribute("direction")), Double.valueOf(hall
					.getAttribute("width")), hall.getAttribute("name"),
					Double.valueOf(hall.getAttribute("x")), Double.valueOf(hall
							.getAttribute("y")), Integer.valueOf(hall
							.getAttribute("level")));

			tempSchool.addHall(tHall);
		}

		for (int i = 0; i < school.getElementsByTagName("hall").getLength(); i++) {

			Element hall = (Element) school.getElementsByTagName("hall")
					.item(i);

			SHall tHall = tempSchool.getHallByID(hall.getAttribute("name"));

			NodeList hallLeft = ((Element) hall.getElementsByTagName("left")
					.item(0)).getChildNodes();
			NodeList hallRight = ((Element) hall.getElementsByTagName("right")
					.item(0)).getChildNodes();

			for (int j = 0; j < hallLeft.getLength(); j++) {
				if (hallLeft.item(j).getNodeType() == Node.ELEMENT_NODE) {
					Element tag = (Element) hallLeft.item(j);

					if (tag.getNodeName().equalsIgnoreCase("class"))

						tHall.addClassroom(
								new SClassroom(Integer.valueOf(tag
										.getAttribute("number")), Integer
										.valueOf(tag.getAttribute("number"))
										.equals(-1) ? true : false, Double
										.valueOf(tag.getAttribute("width")),
										Double.valueOf(tag
												.getAttribute("height"))),
								SSide.LEFT);
					else if (tag.getNodeName().equalsIgnoreCase("stair"))
						tHall.addStair(new SStairs(tag.getAttribute("name")),
								SSide.LEFT);
					else if (tag.getNodeName().equalsIgnoreCase("exit"))
						tHall.addExit(new SExit(tag.getAttribute("name"),
								Double.valueOf(tag.getAttribute("width")),
								Double.valueOf(tag.getAttribute("height"))),
								SSide.LEFT);
					else if (tag.getNodeName().equalsIgnoreCase("junction"))
						tHall.junction(tempSchool.getHallByID(tag
								.getAttribute("name")), SSide.LEFT);
				}
			}

			for (int j = 0; j < hallRight.getLength(); j++) {
				if (hallRight.item(j).getNodeType() == Node.ELEMENT_NODE) {
					Element tag = (Element) hallRight.item(j);

					if (tag.getNodeName().equalsIgnoreCase("class"))

						tHall.addClassroom(
								new SClassroom(Integer.valueOf(tag
										.getAttribute("number")), Integer
										.valueOf(tag.getAttribute("number"))
										.equals(-1) ? true : false, Double
										.valueOf(tag.getAttribute("width")),
										Double.valueOf(tag
												.getAttribute("height"))),
								SSide.RIGHT);
					else if (tag.getNodeName().equalsIgnoreCase("stair"))
						tHall.addStair(new SStairs(tag.getAttribute("name")),
								SSide.RIGHT);
					else if (tag.getNodeName().equalsIgnoreCase("exit"))
						tHall.addExit(new SExit(tag.getAttribute("name"),
								Double.valueOf(tag.getAttribute("width")),
								Double.valueOf(tag.getAttribute("height"))),
								SSide.RIGHT);
					else if (tag.getNodeName().equalsIgnoreCase("junction"))
						tHall.junction(tempSchool.getHallByID(tag
								.getAttribute("name")), SSide.RIGHT);
				}
			}
		}

		return tempSchool;
	}

	public static void save(SSchool school, File file) throws IOException {
		FileWriter out = new FileWriter(file);

		out.write("<?xml version=\"1.0\"?>\n");

		out.write("<school name=\"" + school.getName() + "\" width=\""
				+ school.getWidth() + "\" height=\"" + school.getHeight()
				+ "\">\n");

		for (SHall h : school.getHalls())
			out.write("<declare-hall direction=\""
					+ h.getDirection().toString() + "\" width=\""
					+ h.getWidth() + "\" name=\"" + h.getName() + "\" x=\""
					+ h.getX() + "\" y=\"" + h.getY() + "\" level=\""
					+ h.getLevel() + "\"></declare-hall>\n");

		for (SHall h : school.getHalls()) {

			out.write("<hall name=\"" + h.getName() + "\">\n");

			out.write("<left>\n");

			for (SNode n : h.getLeftNodes())
				if (n instanceof SClassroom)
					out.write("<class number=\""
							+ ((SClassroom) n).getRoomNumber() + "\" width=\""
							+ n.getWidth() + "\" height=\"" + n.getHeight()
							+ "\"></class>\n");
				else if (n instanceof SExit)
					out.write("<exit name=\"" + ((SExit) n).getName()
							+ "\" width=\"" + n.getWidth() + "\" height=\""
							+ n.getHeight() + "\"></exit>\n");
				else if (n instanceof SStairs)
					out.write("<stair name=\"" + ((SStairs) n).getName()
							+ "\"></stair>\n");
				else if (n instanceof SHall)
					out.write("<junction name=\"" + ((SHall) n).getName()
							+ "\"></junction>\n");

			out.write("</left>\n");

			out.write("<right>\n");

			for (SNode n : h.getRightNodes())
				if (n instanceof SClassroom)
					out.write("<class number=\""
							+ ((SClassroom) n).getRoomNumber() + "\" width=\""
							+ n.getWidth() + "\" height=\"" + n.getHeight()
							+ "\"></class>\n");
				else if (n instanceof SExit)
					out.write("<exit name=\"" + ((SExit) n).getName()
							+ "\" width=\"" + n.getWidth() + "\" height=\""
							+ n.getHeight() + "\"></exit>\n");
				else if (n instanceof SStairs)
					out.write("<stair name=\"" + ((SStairs) n).getName()
							+ "\"></stair>\n");
				else if (n instanceof SHall)
					out.write("<junction name=\"" + ((SHall) n).getName()
							+ "\"></junction>\n");

			out.write("</right>\n");

			out.write("</hall>\n");
		}

		out.write("</school>\n");

		out.close();
	}

}
