import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import javax.sound.sampled.Control;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

public class SurrealeKausalitaet extends PApplet {

	SimpleOpenNI context;
	Queue<Float> sound;
	private boolean kinectIsConfigured = false;
	private boolean soundIsConfigured = false;

	private float xMax, yMax;
	/**
	 * This List will contain all the Shapes which we will fill with different
	 * colors
	 */
	public static List<Shape> shapes = new LinkedList<>();

	public static Map<Integer, List<Shape>> userShapes = new HashMap<>();
	/**
	 * The time variable will be used to calculate the time difference between
	 * the update intervals
	 */
	private long time;

	/**
	 * Run this program as Java application to start the PAppplet in fullscreen
	 * mode
	 */
	static public void main(String args[]) {
		PApplet.main(new String[] { "--present", "--bgcolor=#000000",
				"--present-stop-color=#000000", "SurrealeKausalitaet" });
	}

	@Override
	public void setup() {
		size(displayWidth, displayHeight, OPENGL);
		initShapes();
		background(Color.BLACK.getRGB());
		time = System.currentTimeMillis();
		stroke(0, 255, 0);
		strokeWeight(3);
		smooth();
		setupKinect();
	}

	private void setupKinect() {
		// instantiate a new context
		context = new SimpleOpenNI(this);

		UserManager userManager = new UserManager(context);

		System.out.println(context + "");
		// enable depthMap generation
		context.enableDepth();

		// enable skeleton generation for all joints
		context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL, userManager);

		context.setMirror(true);
		kinectIsConfigured = true;
	}

	@Override
	public void draw() {
		background(Color.BLACK.getRGB());
		context.update();

		// Max 2 Spieler
		for (int i = 1; i < 3; i++) {

			if (context.isTrackingSkeleton(i)) {
//				System.out.println(userShapes.get(i));
				drawColoredShapeWithTails(
						userShapes.get(i).get(0),
						getChangedSaturationColor(i,
								SimpleOpenNI.SKEL_LEFT_HAND, userShapes.get(i)
										.get(0)),
						getPosition(SimpleOpenNI.SKEL_LEFT_HAND, i));
				drawColoredShapeWithTails(
						userShapes.get(i).get(1),
						getChangedSaturationColor(i,
								SimpleOpenNI.SKEL_RIGHT_HAND, userShapes.get(i)
										.get(1)),
						getPosition(SimpleOpenNI.SKEL_RIGHT_HAND, i));
				drawColoredShapeWithTails(
						userShapes.get(i).get(2),
						getChangedSaturationColor(i,
								SimpleOpenNI.SKEL_RIGHT_KNEE, userShapes.get(i)
										.get(2)),
						getPosition(SimpleOpenNI.SKEL_RIGHT_KNEE, i));
				drawColoredShapeWithTails(
						userShapes.get(i).get(3),
						getChangedSaturationColor(i,
								SimpleOpenNI.SKEL_LEFT_KNEE, userShapes.get(i)
										.get(3)),
						getPosition(SimpleOpenNI.SKEL_LEFT_KNEE, i));
			}
		}
	}

	/**
	 * This method draws a shape and fill it with an color and draw Tails Which
	 * follow the Hand
	 * 
	 * @param shape
	 * @param color
	 */
	private void drawColoredShapeWithTails(Shape shape, Color color,
			PVector jointPos) {
		// I didn't analyse what pushMatrix(), translate(x,y) and popMatrix() do
		// yet, but without calling this methods,
		// the shapes will be drawn on a wrong position.
		// TODO: Understand what this methods exactly does...
		pushMatrix();

		// set the color for filling the shape
		fill(color.getRGB());
		translate(shape.x, shape.y);

		beginShape();

		// uncomment to draw NO shape outlines
		// noStroke();
		// uncomment to set the outline color
		stroke(Color.BLACK.getRGB());
		// uncomment to set the outline weight
		strokeWeight(3);

		// draw a vertex between all points of a shape
		for (Point point : shape.getPoints()) {
			vertex(point.x, point.y);
		}

		endShape(CLOSE);
		popMatrix();

		PVector jointPos_conv = new PVector();
		context.convertRealWorldToProjective(jointPos, jointPos_conv);
		
		noStroke();
		fill(Color.BLUE.getRGB());
		float eX = jointPos_conv.x * (shape.getBoundingBox().getWidth() * 2)
				/ displayWidth + shape.getBoundingBox().getLeftTop().x;
		float eY = jointPos_conv.y * (shape.getBoundingBox().getHeight() * 2)
				/ displayHeight + shape.getBoundingBox().getLeftTop().y;

		// ellipse(eX, eY, 20, 20);
		PVector handPos = new PVector(eX, eY);
		for (int j = 0; j < shape.tail.length; j++) {
			shape.tail[j].checkEdges();
			shape.tail[j].update(handPos);
			shape.tail[j].display();

		}
	}

	private PVector getPosition(int joint, int i) {
		PVector position = new PVector();
		context.getJointPositionSkeleton(i, joint, position);
		return position;
	}

	private Color getChangedSaturationColor(int userId, int joint, Shape shape) {
		PVector jointPos = new PVector();
		context.getJointPositionSkeleton(userId, joint, jointPos);
		PVector jointPos_conv = new PVector();
		context.convertRealWorldToProjective(jointPos, jointPos_conv);
		float saturation = jointPos_conv.y / displayHeight;
		Color c = shape.getColor();
		float[] hsb = new float[3];
		// System.out.println("Red: " + c.getRed() + " Green: " + c.getGreen()
		// + " getBlue: " + c.getBlue() + " hsb: " + hsb);
		Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
		Color neu = Color.getHSBColor(hsb[0], saturation, hsb[2]);

		return neu;
	}

	/**
	 * This method will create all the shapes for this sketch and add them to
	 * the List shapes If you use the Mapper.jar to map surfaces: Use 'Export' >
	 * 'Processing' to create the 'snippet.txt' and replace the content of this
	 * method with the content from the initShapes() method of that file.
	 * Examples shapes are created with a display resolution of 1920 *1080
	 */
	private void initShapes() {
		shapes.add(new Shape(699, 111));
		shapes.add(new Shape(657, 258));
		shapes.add(new Shape(720, 214));
		shapes.add(new Shape(647, 329));
		shapes.add(new Shape(712, 330));
		shapes.add(new Shape(717, 282));
		shapes.add(new Shape(642, 408));
		shapes.add(new Shape(639, 459));
		shapes.add(new Shape(639, 502));
		shapes.add(new Shape(731, 467));

		shapes.get(0).add(-36, -47);
		shapes.get(0).add(11, -45);
		shapes.get(0).add(36, 44);
		shapes.get(0).add(-36, 48);

		shapes.get(1).add(-41, 99);
		shapes.get(1).add(7, -85);
		shapes.get(1).add(28, -99);
		shapes.get(1).add(42, 13);

		shapes.get(2).add(-35, -55);
		shapes.get(2).add(-2, -56);
		shapes.get(2).add(36, -3);
		shapes.get(2).add(-21, 57);

		shapes.get(3).add(-31, 28);
		shapes.get(3).add(32, -35);
		shapes.get(3).add(26, 36);

		shapes.get(4).add(-39, 35);
		shapes.get(4).add(-33, -36);
		shapes.get(4).add(40, 37);

		shapes.get(5).add(-38, 12);
		shapes.get(5).add(39, -71);
		shapes.get(5).add(33, 72);

		shapes.get(6).add(-26, -51);
		shapes.get(6).add(31, -43);
		shapes.get(6).add(25, 25);
		shapes.get(6).add(-30, 51);

		shapes.get(7).add(-27, 0);
		shapes.get(7).add(28, -26);
		shapes.get(7).add(-27, 27);

		shapes.get(8).add(-27, -16);
		shapes.get(8).add(28, -69);
		shapes.get(8).add(22, 68);
		shapes.get(8).add(-26, 69);

		shapes.get(9).add(-58, -102);
		shapes.get(9).add(17, -90);
		shapes.get(9).add(70, 95);
		shapes.get(9).add(-70, 103);

		shapes.get(0).setColor(new Color(0xD04328));
		shapes.get(1).setColor(new Color(0x63382D));
		shapes.get(2).setColor(new Color(0xC2782F));
		shapes.get(3).setColor(new Color(0x8A443A));
		shapes.get(4).setColor(new Color(0x346E45));
		shapes.get(5).setColor(new Color(0x344761));
		shapes.get(6).setColor(new Color(0xDB4D27));
		shapes.get(7).setColor(new Color(0x344761));
		shapes.get(8).setColor(new Color(0xC2782F));
		shapes.get(9).setColor(new Color(0xDB4D27));

//		PImage image = loadImage("pic.jpg");

		for (Shape shape : shapes) {
			shape.initTail(this);
			// System.out.println(shape.getBoundingBox());
		}
	}

}
