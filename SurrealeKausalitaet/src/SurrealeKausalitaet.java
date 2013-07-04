import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Main class of the program, contains the logic, interaction and visual effects
 */
public class SurrealeKausalitaet extends PApplet {

    /**
     * This List will contain all the Shapes which we will fill with different
     * colors
     */
    public static List<Shape> shapes = new LinkedList<>();
    /**
     * This maps contains the assignment from users to the shapes that he controls
     */
    public static Map<Integer, List<Shape>> userShapes = new HashMap<>();
    SimpleOpenNI context;
    /**
     * Will be set on true as soon as the kinect is configured. Nice for testing without
     * kinect: If kinect is not configured, don't use kinect stuff...
     */
    private boolean kinectIsConfigured = false;
    private float xMax, yMax;
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
        PApplet.main(new String[]{"--present", "--bgcolor=#000000",
                "--present-stop-color=#000000", "SurrealeKausalitaet"});
    }

    /**
     * Setup all basic stuff for the program
     */
    @Override
    public void setup() {
        size(displayWidth, displayHeight, OPENGL);
        initShapes();
        background(Color.BLACK.getRGB());
        //TODO: Zeit kann gelöscht werde, oder?
        time = System.currentTimeMillis();
        stroke(0, 255, 0);
        strokeWeight(3);
        smooth();
        setupKinect();
    }

    /**
     * Setting up the kinect connection and all the stuff that is necessary to use the
     * kinect sensor
     */
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

    /**
     * Draws all the stuff which will be displayed
     */
    @Override
    public void draw() {
        background(Color.BLACK.getRGB());
        context.update();

        // Max ? Spieler
        int numberOfPlayers = 1;
        for (int i = 1; i < (numberOfPlayers + 1); i++) {

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
     * @param shape    the shape which will contain the tail
     * @param color    the color of the shape
     * @param jointPos the position of start of the tail
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

//		PVector jointPos_conv = new PVector();
//		context.convertRealWorldToProjective(jointPos, jointPos_conv);
//
//		noStroke();
//		fill(Color.BLUE.getRGB());
//		float eX = jointPos_conv.x * (shape.getBoundingBox().getWidth() * 2)
//				/ displayWidth + shape.getBoundingBox().getLeftTop().x;
//		float eY = jointPos_conv.y * (shape.getBoundingBox().getHeight() * 2)
//				/ displayHeight + shape.getBoundingBox().getLeftTop().y;
//
//		// ellipse(eX, eY, 20, 20);
//		PVector handPos = new PVector(eX, eY);
//		for (int j = 0; j < shape.tail.length; j++) {
//			shape.tail[j].checkEdges();
//			shape.tail[j].update(handPos);
//			shape.tail[j].display();
//
//		}
    }

    /**
     * Detects position of a users joint
     *
     * @param joint  the joint, whose position will be detected
     * @param userID the user of the given joint
     * @return PVector with coordinates of the joint
     */
    private PVector getPosition(int joint, int userID) {
        PVector position = new PVector();
        context.getJointPositionSkeleton(userID, joint, position);
        return position;
    }

    /**
     * This Method is used to recalculate the Color of a Shape, based on the position of
     * a given joint
     *
     * @param userId the user, whose joint position should be checked
     * @param joint  the joint, whose should be checked
     * @param shape  the shape, whose color should be adjusted
     * @return the recalculated color
     */
    private Color getChangedSaturationColor(int userId, int joint, Shape shape) {
        // jointPos contains coordinats of realWorld
        PVector jointPos = new PVector();
        context.getJointPositionSkeleton(userId, joint, jointPos);
        // jointPos_conv will contain coordinats of projective
        PVector jointPos_conv = new PVector();
        context.convertRealWorldToProjective(jointPos, jointPos_conv);

        float saturation = jointPos_conv.x / displayWidth;
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
        //TODO: Shapes aus Datei parsen, welcher Parameter beim Programmstart übergeben wird.
        shapes.add(new Shape(367, 567));
        shapes.add(new Shape(280, 567));
        shapes.add(new Shape(476, 566));
        shapes.add(new Shape(723, 534));
        shapes.add(new Shape(555, 592));
        shapes.add(new Shape(303, 481));
//        shapes.add(new Shape(297, 507));

        shapes.get(0).add(66, -41);
        shapes.get(0).add(74, 38);
        shapes.get(0).add(-74, 54);
        shapes.get(0).add(-72, -54);

        shapes.get(1).add(15, -54);
        shapes.get(1).add(-15, -5);
        shapes.get(1).add(13, 54);

        shapes.get(2).add(-26, 35);
        shapes.get(2).add(-34, -34);
        shapes.get(2).add(35, 15);

        shapes.get(3).add(68, -3);
        shapes.get(3).add(62, -39);
        shapes.get(3).add(-68, -29);
        shapes.get(3).add(-53, 39);

        shapes.get(4).add(105, -20);
        shapes.get(4).add(-106, -62);
        shapes.get(4).add(59, 62);
        shapes.get(4).add(107, 56);

        shapes.get(5).add(-141, 8);
        shapes.get(5).add(130, 45);
        shapes.get(5).add(141, 15);
        shapes.get(5).add(-92, -45);
//
//        shapes.get(6).add(-135, -18);
//        shapes.get(6).add(136, 19);

        shapes.get(0).setColor(Color.orange);
        shapes.get(1).setColor(Color.blue);
        shapes.get(2).setColor(Color.green);
        shapes.get(3).setColor(Color.yellow);
        shapes.get(4).setColor(Color.cyan);
        shapes.get(5).setColor(Color.PINK);
//		shapes.get(0).setColor(new Color(0xD04328));
//		shapes.get(1).setColor(new Color(0x63382D));
//		shapes.get(2).setColor(new Color(0xC2782F));
//		shapes.get(3).setColor(new Color(0x8A443A));
//		shapes.get(4).setColor(new Color(0x346E45));
//		shapes.get(5).setColor(new Color(0x344761));
//		shapes.get(6).setColor(new Color(0xDB4D27));
//		shapes.get(7).setColor(new Color(0x344761));
//		shapes.get(8).setColor(new Color(0xC2782F));
//		shapes.get(9).setColor(new Color(0xDB4D27));

//		PImage image = loadImage("pic.jpg");

        for (Shape shape : shapes) {
            shape.initTail(this);
            // System.out.println(shape.getBoundingBox());
        }
    }

}
