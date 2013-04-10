import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tobi (iBot), 08th April 2013
 *  Based on:
 *      auto generated code from Mapper.jar
 *      some example code from the project 'Interaktive Installationen', HAW Hamburg, SS2013
 * Java 1.7
 * Used external libraries:
 *  Processing 2.08b, jogl
 *      http://www.processing.org/
 */

/**
 * This class contains code, to color different Shapes
 */
public class ProcessingShapes extends PApplet {

    SimpleOpenNI context;
    /**
     * This List will contain all the Shapes which we will fill with different colors
     */
    private List<Shape> shapes = new LinkedList<>();
    /**
     * The time variable will be used to calculate the time difference between the update intervals
     */
    private long time;

    /**
     * Run this program as Java application to start the PAppplet in fullscreen mode
     */
    static public void main(String args[]) {
        PApplet.main(new String[]{"--present", "--bgcolor=#000000", "--present-stop-color=#000000",
                "ProcessingShapes"});
    }

    /**
     * This methode will be called from a super class or somewhere else... idk...
     * Whatever: Do all the stuff tu setup your processing sketch here!
     */
    @Override
    public void setup() {
        // size for fullscreen window, renderer OpenGL
        //  Don't forget to import native libraries for your OS. If OpenGL doesn't work, render without OpenGL...
//        size(displayWidth, displayHeight, OPENGL);
        size(displayWidth, displayHeight);

        initShapes();
        background(Color.BLACK.getRGB());
        time = System.currentTimeMillis();

        // instantiate a new context
        context = new SimpleOpenNI(this);

        // enable depthMap generation
        context.enableDepth();

        // enable skeleton generation for all joints
        context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);

        context.setMirror(true);
//
        stroke(0,255,0);
        strokeWeight(3);
        smooth();

        // create a window the size of the depth information
//        size(context.depthWidth(), context.depthHeight());

    }

    /**
     * Another method that will be called automatically...
     * Do all the drawing stuff in this method.
     */
    @Override
    public void draw() {
        background(Color.BLACK.getRGB());
        context.update();

        // update only after 1000 sec.
//        if ((System.currentTimeMillis() - time) > 1000) {


        for (int i = 1; i < 4; i++) {
            if (context.isTrackingSkeleton(i)) {
//                drawSkeleton(i);
                // draw random colored shapes
                drawShape(shapes.get(i-1), getColorOfJoint(i, SimpleOpenNI.SKEL_RIGHT_HAND));

                // set time to current system time
//                time = System.currentTimeMillis();
//            }
            }
        }
    }

    public void drawSkeleton(int userId) {
        // draw limbs
        context.drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);

        context.drawLimb(userId, SimpleOpenNI.SKEL_NECK,
                SimpleOpenNI.SKEL_LEFT_SHOULDER);
        context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER,
                SimpleOpenNI.SKEL_LEFT_ELBOW);
        context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW,
                SimpleOpenNI.SKEL_LEFT_HAND);

        context.drawLimb(userId, SimpleOpenNI.SKEL_NECK,
                SimpleOpenNI.SKEL_RIGHT_SHOULDER);
        context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER,
                SimpleOpenNI.SKEL_RIGHT_ELBOW);
        context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW,
                SimpleOpenNI.SKEL_RIGHT_HAND);

        context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER,
                SimpleOpenNI.SKEL_TORSO);
        context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER,
                SimpleOpenNI.SKEL_TORSO);

        context.drawLimb(userId, SimpleOpenNI.SKEL_TORSO,
                SimpleOpenNI.SKEL_LEFT_HIP);
        context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP,
                SimpleOpenNI.SKEL_LEFT_KNEE);
        context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE,
                SimpleOpenNI.SKEL_LEFT_FOOT);

        context.drawLimb(userId, SimpleOpenNI.SKEL_TORSO,
                SimpleOpenNI.SKEL_RIGHT_HIP);
        context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP,
                SimpleOpenNI.SKEL_RIGHT_KNEE);
        context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE,
                SimpleOpenNI.SKEL_RIGHT_FOOT);
    }



    /**
     * This method will create all the shapes for this sketch and add them to the List shapes
     * If you use the Mapper.jar to map surfaces:
     * Use 'Export' > 'Processing' to create the 'snippet.txt' and replace the content of this method with the content
     * from the initShapes() method of that file.
     * Examples shapes are created with a display resolution of 1920 *1080
     */
    private void initShapes() {
        shapes.add(new Shape(363, 321));
        shapes.add(new Shape(467, 381));
        shapes.add(new Shape(600, 303));

        shapes.get(0).add(137, -92);
        shapes.get(0).add(130, -157);
        shapes.get(0).add(84, -117);
        shapes.get(0).add(-136, 157);

        shapes.get(1).add(-240, 97);
        shapes.get(1).add(-191, 151);
        shapes.get(1).add(228, 153);
        shapes.get(1).add(188, 110);
        shapes.get(1).add(240, 61);
        shapes.get(1).add(33, -152);

        shapes.get(2).add(-100, -74);
        shapes.get(2).add(-107, -139);
        shapes.get(2).add(107, 139);
    }



    private Color getColorOfJoint(int userId, int joint) {
        // get 3D position of a joint
        PVector jointPos = new PVector();
        context.getJointPositionSkeleton(userId, joint,
                jointPos);
        PVector jointPos_conv = new PVector();
        context.convertRealWorldToProjective(jointPos, jointPos_conv);


        // senkrecht = x Koordinate???
        float hue = jointPos_conv.y / displayHeight;
//        System.out.println("Hue: " + hue);
        float saturation = 0.99f;
        float value = 0.99f;
        Color c = Color.getHSBColor(hue, saturation, value);

        return c;
//        String rgb = Integer.toHexString(c.getRGB());
//        //System.out.println(rgb);
//
//
//        int r = c.getRed();
//        int g = c.getGreen();
//        int b = c.getBlue();
//        int opacity = c.getAlpha();
//        //System.out.println("Rot:" + r + "/n Gruen:" + g + "/n Blau:" + b
//        //		+ "/n Alpha:" + opacity);
//
//        background(r,g,b);
//        fill(r, g, b, opacity);
//        rect(jointPos_conv.x, jointPos_conv.y, 50, 50);
    }

    /**
     * This method draws a shape and fill it with an color
     *
     * @param shape
     * @param color
     */
    private void drawShape(Shape shape, Color color) {
        // I didn't analyse what pushMatrix(), translate(x,y) and popMatrix() do yet, but without calling this methods,
        //  the shapes will be drawn on a wrong position.
        // TODO: Understand what this methods exactly does...
        pushMatrix();

        // set the color for filling the shape
        fill(color.getRGB());

        translate(shape.x, shape.y);

        beginShape();

        // uncomment to draw NO shape outlines
//        noStroke();
        // uncomment to set the outline color
        stroke(Color.BLACK.getRGB());
        // uncomment to set the outline weight
        strokeWeight(3);

        // draw a vertex between all points of a shape
        for (Point point : shape.points) {
            vertex(point.x, point.y);
        }

        endShape(CLOSE);
        popMatrix();
    }

    private class Shape extends Point {
        public List<Point> points = new LinkedList<>();

        public Shape(int x, int y) {
            super(x, y);
        }

        public void add(int x, int y) {
            points.add(new Point(x, y));
        }
    }

    private class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    // when a person ('user') enters the field of view
    public void onNewUser(int userId) {
        println("New User Detected - userId: " + userId);

        // start pose detection
        context.startPoseDetection("Psi", userId);
    }

    // when a person ('user') leaves the field of view
    public void onLostUser(int userId) {
        println("User Lost - userId: " + userId);
    }

    // when a user begins a pose
    public void onStartPose(String pose, int userId) {
        println("Start of Pose Detected  - userId: " + userId + ", pose: "
                + pose);

        // stop pose detection
        context.stopPoseDetection(userId);

        // start attempting to calibrate the skeleton
        context.requestCalibrationSkeleton(userId, true);
    }

    // when calibration begins
    public void onStartCalibration(int userId) {
        println("Beginning Calibration - userId: " + userId);
    }

    // when calibaration ends - successfully or unsucessfully
    public void onEndCalibration(int userId, boolean successfull) {
        println("Calibration of userId: " + userId + ", successfull: "
                + successfull);

        if (successfull) {
            println("  User calibrated !!!");

            // begin skeleton tracking
            context.startTrackingSkeleton(userId);
        } else {
            println("  Failed to calibrate user !!!");

            // Start pose detection
            context.startPoseDetection("Psi", userId);
        }
    }
}
