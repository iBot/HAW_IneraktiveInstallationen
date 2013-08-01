

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
    private float scaleFactorX;
    float jointPosConvMaxValue = 0;
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
        scaleFactorX = displayWidth / 640f;
        initShapes();
        background(Color.BLACK.getRGB());
        //TODO: Zeit kann gel��scht werde, oder?
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
        int numberOfPlayers = 2;
        for (int i = 1; i < (numberOfPlayers + 1); i++) {

            //TODO: Umstellen auf User-Tracking. Siehe dazu http://mlab.taik.fi/~fsen/nui/28.3/readycodes/OpenNI_UserPosition_Simple.pde (getCoM -> get Center of Mass)
            //http://stackoverflow.com/questions/14742261/simple-openni-getuserpixels
            if (context.isTrackingSkeleton(i)) {
                drawColoredShape(
                        userShapes.get(i).get(0),
                        getChangedColor(i,
                                SimpleOpenNI.SKEL_LEFT_HAND, userShapes.get(i)
                                .get(0)));
                drawColoredShape(
                        userShapes.get(i).get(1),
                        getChangedColor(i,
                                SimpleOpenNI.SKEL_RIGHT_HAND, userShapes.get(i)
                                .get(1)));
                drawColoredShape(
                        userShapes.get(i).get(2),
                        getChangedColor(i,
                                SimpleOpenNI.SKEL_RIGHT_KNEE, userShapes.get(i)
                                .get(2)));
//                drawColoredShape(
//                        userShapes.get(i).get(3),
//                        getChangedColor(i,
//                                SimpleOpenNI.SKEL_LEFT_KNEE, userShapes.get(i)
//                                .get(3)));
            }
        }
        //TODO: Alle nicht belegten Shapes mit deren Grundfarbe färben.
    }

    /**
     * This method draws a shape and fill it with an color and draw Tails Which
     * follow the Hand
     *
     * @param shape the shape which will contain the tail
     * @param color the color of the shape
     */
    private void drawColoredShape(Shape shape, Color color) {

        pushMatrix();

        // set the color for filling the shape
        fill(color.getRGB());
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
     * @param joint  the joint, whose should be checked heute hier morgen dort
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

        float hue = jointPos_conv.x / displayWidth;
        Color c = shape.getColor();
        float[] hsb = new float[3];
        // System.out.println("Red: " + c.getRed() + " Green: " + c.getGreen()
        // + " getBlue: " + c.getBlue() + " hsb: " + hsb);
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
        Color neu = Color.getHSBColor(hue, hsb[1], hsb[2]);

        return neu;
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
    private Color getChangedColor(int userId, int joint, Shape shape) {
        // jointPos contains coordinats of realWorld
        PVector jointPos = new PVector();
        context.getJointPositionSkeleton(userId, joint, jointPos);
        // jointPos_conv will contain coordinats of projective
        PVector jointPos_conv = new PVector();
        context.convertRealWorldToProjective(jointPos, jointPos_conv);

        System.out.println("JointPos " + jointPos_conv.x + " ScaleFactor " + scaleFactorX);
        float pos = jointPos_conv.x * scaleFactorX;

        float haelfte = displayWidth / 2f;
        Color c = shape.getColor();
        int red = 0, green = 0, blue = 0;
        float delta = 30;
        float abweichungsFaktor;
        if (pos >= haelfte) {
            abweichungsFaktor = 1 + ((pos - haelfte) * delta / 100 / haelfte);
        } else {
            abweichungsFaktor = 1 - ((haelfte - pos) * delta / 100 / haelfte);
        }
        System.out.println("Abweichung = " + abweichungsFaktor + " Hälfte = " + haelfte + " JointPos = " + pos);
//        System.out.println(jointPosConvMaxValue);
       
        red = Math.round(c.getRed() * abweichungsFaktor);
        green = Math.round(c.getGreen()*abweichungsFaktor);
        blue = Math.round(c.getBlue()*abweichungsFaktor);

        //        if(jointPos_conv.x >= haelfte){
//        	jointPos_conv.x = jointPos_conv.x-haelfte;
//        	float prozent = jointPos_conv.x/haelfte*20f;
//        	red = Math.round(c.getRed()*(1+prozent/100f));
//        	green = Math.round(c.getGreen()*(1+prozent/100f));
//        	blue = Math.round(c.getBlue()*(1+prozent/100f));
//
//
//        }
//        else if(jointPos_conv.x < haelfte){
//        	float prozent = jointPos_conv.x/haelfte*20f;
//        	red = Math.round(c.getRed()*(1-prozent/100f));
//        	green = Math.round(c.getGreen()*(1-prozent/100f));
//        	blue = Math.round(c.getBlue()*(1-prozent/100f));
//        }
        if (red < 0) red = 0;
        if (red > 255) red = 255;
        if (green < 0) green = 0;
        if (green > 255) green = 255;
        if (blue < 0) blue = 0;
        if (blue > 255) blue = 255;
        System.out.println("Rot: " + red + " Grün: " + green + " Blau: " + blue);
        Color neu = c;
        if(shape.getColorToChange().equals(ColorToChange.RED))
            neu = new Color(red, c.getGreen(), c.getBlue());
        else if(shape.getColorToChange().equals(ColorToChange.BLUE))
        	neu = new Color(c.getRed(), c.getGreen(), blue);
        else if(shape.getColorToChange().equals(ColorToChange.GREEN))
        	neu = new Color(c.getRed(), green, c.getBlue());

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
        //TODO: Shapes aus Datei parsen, welcher Parameter beim Programmstart ��bergeben wird.
        shapes.add(new Shape(669, 554));
        shapes.add(new Shape(445, 573));
        shapes.add(new Shape(456, 654));
        shapes.add(new Shape(675, 620));
        shapes.add(new Shape(167, 584));
        shapes.add(new Shape(186, 698));
        shapes.add(new Shape(106, 632));

        shapes.get(0).add(84, 15);
        shapes.get(0).add(86, -14);
        shapes.get(0).add(-73, -34);
        shapes.get(0).add(-85, 35);

        shapes.get(1).add(133, -53);
        shapes.get(1).add(121, 22);
        shapes.get(1).add(-133, 54);
        shapes.get(1).add(-110, -24);

        shapes.get(2).add(113, -50);
        shapes.get(2).add(144, -2);
        shapes.get(2).add(-104, 51);
        shapes.get(2).add(-144, -16);

        shapes.get(3).add(-94, -18);
        shapes.get(3).add(-58, 38);
        shapes.get(3).add(94, -4);
        shapes.get(3).add(82, -38);

        shapes.get(4).add(146, -38);
        shapes.get(4).add(-75, -32);
        shapes.get(4).add(-145, 11);
        shapes.get(4).add(119, 39);

        shapes.get(5).add(111, -59);
        shapes.get(5).add(155, 16);
        shapes.get(5).add(-70, 60);
        shapes.get(5).add(-154, -25);

        shapes.get(6).add(99, -1);
        shapes.get(6).add(-81, 29);
        shapes.get(6).add(-98, -28);
        //Orange --> grün
        shapes.get(0).setColor(new Color(0xD04328));
        shapes.get(0).setColorToChange(ColorToChange.GREEN);
//        //Braun - grün
//        shapes.get(1).setColor(new Color(0x63382D));
//        shapes.get(1).setColorToChange(ColorToChange.GREEN);
//        //Ocker -->grün
        shapes.get(2).setColor(new Color(0xC2782F));
        shapes.get(2).setColorToChange(ColorToChange.GREEN);
//        //Laubgrün -> grün verändern
        shapes.get(3).setColor(new Color(0x346E45));
        shapes.get(3).setColorToChange(ColorToChange.GREEN);
//        //Kobaldblau --> grün
        shapes.get(4).setColor(new Color(0x344761));
        shapes.get(4).setColorToChange(ColorToChange.GREEN);
//        //Rot-Orange -->grün
		shapes.get(5).setColor(new Color(0x5A4826));
        shapes.get(5).setColorToChange(ColorToChange.GREEN);
//        
        // -->grün
		shapes.get(6).setColor(new Color(0x796659));
        shapes.get(6).setColorToChange(ColorToChange.GREEN);
        //blau
//		shapes.get(0).setColor(new Color(0x3A7487));
//        shapes.get(0).setColorToChange(ColorToChange.BLUE);
		//blau
		shapes.get(1).setColor(new Color(0x7D9F64));
        shapes.get(1).setColorToChange(ColorToChange.BLUE);
        //grün
//		shapes.get(0).setColor(new Color(0xC63D30));
//        shapes.get(0).setColorToChange(ColorToChange.GREEN);
        //grün
//		shapes.get(0).setColor(new Color(0x1C764F));
//        shapes.get(0).setColorToChange(ColorToChange.GREEN);


        for (Shape shape : shapes) {
            shape.initTail(this);
            // System.out.println(shape.getBoundingBox());
        }
    }

}
