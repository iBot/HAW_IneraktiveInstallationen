import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main class of the program, contains the logic, interaction and visual effects
 */
public class SurrealeKausalitaet extends PApplet {

    /**
     * This List will contain all the Shapes which are not assigned to any user
     */
    public static List<Shape> shapes = Collections.synchronizedList(new LinkedList<Shape>());
    /**
     * This map mapps the user IDs of all detected user to the shapes which are controlled by the user.
     */
    public static Map<Integer, List<Shape>> userShapes = new ConcurrentHashMap<>();
    /**
     * Number of shapes per user
     */
    public final int SHAPES_PER_USER = 2;
    /**
     * The SimpleOpenNI context (Kinect sensor) for this program
     */
    SimpleOpenNI context;
    /**
     * By converting rela world position to perspective positions the result is a vector within the kinect resolution of 640 * 480 pixels.
     * This scaleFactor will be used to scale the x coordinate (between 0 and 640) to the display width (between 0 and displayWidth)
     */
    private float scaleFactorX;
    /**
     * Will be set on true as soon as the kinect is configured. Nice for testing
     * without kinect: If kinect is not configured, don't use kinect stuff...
     */
    private boolean kinectIsConfigured = false;
    
    private static Random rand = new Random();

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
        scaleFactorX = displayWidth / 640f;

        colorMode(HSB, displayWidth, 99, 99);
        
        initShapes();
        background(Color.BLACK.getRGB());
        stroke(0, 255, 0);
        strokeWeight(3);
        smooth();
        setupKinect();
    }

    /**
     * Setting up the kinect connection and all the stuff that is necessary to
     * use the kinect sensor
     */
    private void setupKinect() {
        // instantiate a new context
        context = new SimpleOpenNI(this);

        System.out.println(context + "");
        // enable depthMap generation
        context.enableDepth();

        // enable skeleton generation for all joints
        context.enableUser(SimpleOpenNI.SKEL_PROFILE_NONE);

        context.setMirror(true);
        kinectIsConfigured = true;
    }

    /**
     * Draws all everything which will be displayed
     */
    @Override
    public void draw() {
        background(Color.BLACK.getRGB());
        context.update();

        //Draw all shapes assigned to an user
        for (Map.Entry<Integer, List<Shape>> entry : userShapes.entrySet()) {
            int userID = entry.getKey();
            List<Shape> shapesForCurrentUser = entry.getValue();
            PVector position = getPosition(userID);
            if ((position.x > 0) && (position.x <= displayWidth)) {
                for (Shape shape : shapesForCurrentUser) {
                    drawColoredShape(shape, getChangedColor2(position, shape));
                }
            } else {
                for (Shape shape : shapesForCurrentUser) {
                    drawColoredShape(shape, shape.getHue());
                }
            }
        }

        //Draw all shapes which are NOT assigned to an user
        for (Shape shape : shapes) {
            drawColoredShape(shape, shape.getHue());
        }
    }

    /**
     * This method draws a shape and fill it with an color
     *
     * @param shape the shape which will contain the tail
     * @param hue the color of the shape
     */
    private void drawColoredShape(Shape shape, float hue) {

        pushMatrix();
        hue = hue % displayWidth;
        // set the color for filling the shape
        int neu =  color(hue, 99, 99);
        Color colorNeu = new Color(neu);
        fill(colorNeu.getRGB());
        translate(shape.x, shape.y);

        beginShape();

        // set the outline color
        stroke(Color.BLACK.getRGB());
        // set the outline weight
        strokeWeight(3);

        // draw a vertex between all points of a shape
        for (Point point : shape.getPoints()) {
            vertex(point.x, point.y);
        }

        endShape(CLOSE);
        popMatrix();

    }

    /**
     * Detects position of a users
     *
     * @param userID the user id of the user whose position will be detected
     * @return PVector with coordinates of the joint
     */
    private PVector getPosition(int userID) {

        PVector position = new PVector();
        PVector position_conv = new PVector();
        //Get "Center of Mass" of the user
        context.getCoM(userID, position);
        context.convertRealWorldToProjective(position, position_conv);
        return position_conv;
    }

    /**
     * This Method is used to recalculate the Color of a Shape, based on the
     * position of a given joint
     *
     * @param position
     * @param shape    the shape, whose color should be adjusted
     * @return the recalculated color
     */
//    private Color getChangedColor(PVector position, Shape shape) {
//
//        float pos = position.x * scaleFactorX;
//
//        float haelfte = displayWidth / 2f;
//        Color c = shape.getColor();
//        int red = 0, green = 0, blue = 0;
//        float delta = 60;
//        float abweichungsFaktor;
//        if (pos >= haelfte) {
//            abweichungsFaktor = 1 + ((pos - haelfte) * delta / 100 / haelfte);
//        } else {
//            abweichungsFaktor = 1 - ((haelfte - pos) * delta / 100 / haelfte);
//        }
//
//
//        red = Math.round(c.getRed() * abweichungsFaktor);
//        green = Math.round(c.getGreen() * abweichungsFaktor);
//        blue = Math.round(c.getBlue() * abweichungsFaktor);
//
//
//        if (red < 0)
//            red = 0;
//        if (red > 255)
//            red = 255;
//        if (green < 0)
//            green = 0;
//        if (green > 255)
//            green = 255;
//        if (blue < 0)
//            blue = 0;
//        if (blue > 255)
//            blue = 255;
//
//        Color neu = c;
//        if (shape.getColorToChange().equals(ColorToChange.RED)) {
//            neu = new Color(red, c.getGreen(), c.getBlue());
//        } else if (shape.getColorToChange().equals(ColorToChange.BLUE)) {
//            neu = new Color(c.getRed(), c.getGreen(), blue);
//        } else {
//            neu = new Color(c.getRed(), green, c.getBlue());
//        }
//        return neu;
//    }

    /**
     * This Method is used to recalculate the Color of a Shape, based on the position of
     * a given joint
     *
     * @param pos
     * @param shape  the shape, whose color should be adjusted
     * @return the recalculated color
     */
    private Color getChangedSaturationColor(PVector pos, Shape shape) {
        // jointPos contains coordinats of realWorld

        float posX = pos.x * scaleFactorX;
//        float hue = posX / displayWidth;
        Color c = shape.getColor();
        float[] hsb = new float[3];
        // System.out.println("Red: " + c.getRed() + " Green: " + c.getGreen()
        // + " getBlue: " + c.getBlue() + " hsb: " + hsb);
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
        colorMode(HSB, displayWidth, 99, 99);
        int neu =  color(posX, hsb[1], hsb[2]);
        Color colorNeu = new Color(neu);
        return colorNeu;
    }

    /**
     * This Method is used to recalculate the Color of a Shape, based on the position of
     * a given joint
     *
     * @param pos
     * @param shape  the shape, whose color should be adjusted
     * @return the recalculated color
     */
    private float getChangedColor2(PVector pos, Shape shape) {
        // jointPos contains coordinats of realWorld

        float posX = pos.x * scaleFactorX;
        float hue = shape.getHue();
        float newHue = hue + posX;
        
        return newHue;
//        int neu =  color(newHue, 99, 99);
//        Color colorNeu = new Color(neu);
//        return colorNeu;
    }

    /**
     * when a person ('user') enters the field of view
     *
     * @param userId id of the new user
     */
    public void onNewUser(int userId) {
        println("detected" + userId);

        //assign random shapes to the new user
        Collections.shuffle(shapes);
        List<Shape> tmpShapes;
        int size = shapes.size();
        if (size <= 0) {
            tmpShapes = Collections.emptyList();
        } else if (size <= SHAPES_PER_USER) {
            tmpShapes = new ArrayList<>(shapes.subList(0, size));
        } else {
            tmpShapes = new ArrayList<>(shapes.subList(0, SHAPES_PER_USER));
        }
        for (Shape shape : tmpShapes) {
            System.out.println(shape);
        }
        userShapes.put(userId, tmpShapes);
        shapes.removeAll(tmpShapes);
    }

    /**
     * when a person ('user') leaves the field of view
     *
     * @param userId id of the loft user
     */
    public void onLostUser(int userId) {
        println("lost: " + userId);

        shapes.addAll(userShapes.remove(userId));
    }

    /**
     * This method will create all the shapes for this sketch and add them to
     * the List shapes If you use the Mapper.jar to map surfaces: Use 'Export' >
     * 'Processing' to create the 'snippet.txt' and replace the content of this
     * method with the content from the initShapes() method of that file.
     * Examples shapes are created with a display resolution of 1920 *1080
     */
    private void initShapes() {
//        shapes.add(new Shape(389, 438));
//        shapes.add(new Shape(138, 450));
//        shapes.add(new Shape(494, 417));
//        shapes.add(new Shape(601, 457));
//        shapes.add(new Shape(601, 527));
//        shapes.add(new Shape(406, 536));
//        shapes.add(new Shape(185, 585));
//        shapes.add(new Shape(137, 496));
//
//        shapes.get(0).add(-113, -46);
//        shapes.get(0).add(63, -77);
//        shapes.get(0).add(114, 40);
//        shapes.get(0).add(-108, 77);
//
//        shapes.get(1).add(127, -59);
//        shapes.get(1).add(-111, -61);
//        shapes.get(1).add(-133, -16);
//        shapes.get(1).add(-130, -7);
//        shapes.get(1).add(133, 61);
//
//        shapes.get(2).add(-28, -53);
//        shapes.get(2).add(28, -39);
//        shapes.get(2).add(15, 54);
//
//        shapes.get(3).add(-71, -72);
//        shapes.get(3).add(82, 68);
//        shapes.get(3).add(80, 73);
//        shapes.get(3).add(-82, 19);
//
//        shapes.get(4).add(-82, -41);
//        shapes.get(4).add(-55, 36);
//        shapes.get(4).add(82, 42);
//        shapes.get(4).add(78, 12);
//
//        shapes.get(5).add(104, -48);
//        shapes.get(5).add(-126, -10);
//        shapes.get(5).add(-63, 48);
//        shapes.get(5).add(126, 25);
//
//        shapes.get(6).add(144, 2);
//        shapes.get(6).add(-57, 61);
//        shapes.get(6).add(-144, -38);
//        shapes.get(6).add(86, -60);
//
//        shapes.get(7).add(-121, -37);
//        shapes.get(7).add(122, 23);
//        shapes.get(7).add(-101, 37);
//        // Orange --> gr��n
//        shapes.get(0).setColor(new Color(0xD04328));
//        shapes.get(0).setColorToChange(ColorToChange.GREEN);
//        // //Braun - gr��n
//        // shapes.get(1).setColor(new Color(0x63382D));
//        // shapes.get(1).setColorToChange(ColorToChange.GREEN);
//        // //Ocker -->gr��n
//        shapes.get(2).setColor(new Color(0xC2782F));
//        shapes.get(2).setColorToChange(ColorToChange.GREEN);
//        // //Laubgr��n -> gr��n ver��ndern
//        shapes.get(3).setColor(new Color(0x346E45));
//        shapes.get(3).setColorToChange(ColorToChange.GREEN);
//        // //Kobaldblau --> gr��n
//        shapes.get(4).setColor(new Color(0x344761));
//        shapes.get(4).setColorToChange(ColorToChange.GREEN);
//        // //Rot-Orange -->gr��n
//        shapes.get(5).setColor(new Color(0x5A4826));
//        shapes.get(5).setColorToChange(ColorToChange.GREEN);
//        //
//        // -->gr��n
//        shapes.get(6).setColor(new Color(0x796659));
//        shapes.get(6).setColorToChange(ColorToChange.GREEN);
////        blau
//        shapes.get(7).setColor(new Color(0x3A7487));
//        shapes.get(7).setColorToChange(ColorToChange.BLUE);
//        // blau
//        shapes.get(1).setColor(new Color(0x7D9F64));
//        shapes.get(1).setColorToChange(ColorToChange.BLUE);
//        // gr��n
//        // shapes.get(0).setColor(new Color(0xC63D30));
//        // shapes.get(0).setColorToChange(ColorToChange.GREEN);
//        // gr��n
//        // shapes.get(0).setColor(new Color(0x1C764F));
//        // shapes.get(0).setColorToChange(ColorToChange.GREEN);

    	shapes.add(new Shape(337, 392));
		shapes.add(new Shape(903, 392));

		shapes.get(0).add(-268, -318);
		shapes.get(0).add(269, -323);
		shapes.get(0).add(270, 323);
		shapes.get(0).add(-270, 316);

		shapes.get(1).add(-240, -325);
		shapes.get(1).add(-226, 326);
		shapes.get(1).add(240, 314);
		shapes.get(1).add(204, -311);
		
		for (Shape s:shapes)
		{
			float hue =rand.nextFloat()*displayWidth;
			s.setHue(hue);
		}
		
    }

}
