import SimpleOpenNI.SimpleOpenNI;
import ddf.minim.AudioBuffer;
import ddf.minim.AudioInput;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import javax.sound.sampled.Control;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    boolean kinectIsConfigured = false;

    SimpleOpenNI context;
    AudioInput ai;
    Queue<Float> sound;
    /**
     * This List will contain all the Shapes which we will fill with different colors
     */
    private List<Shape> shapes = new LinkedList<>();
    /**
     * The time variable will be used to calculate the time difference between the update intervals
     */
    private long time;
    private PlayBack playBack;
    private Minim minim;

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
        setupCommonStuff();
//        setupAudio();
        setupKinect();



    }

    private void setupCommonStuff(){
        // size for fullscreen window, renderer OpenGL
        //  Don't forget to import native libraries for your OS. If OpenGL doesn't work, render without OpenGL...
//        size(displayWidth, displayHeight, OPENGL);
        size(displayWidth, displayHeight, P3D);

        initShapes();
        background(Color.BLACK.getRGB());
        time = System.currentTimeMillis();


//
        stroke(0, 255, 0);
        strokeWeight(3);
        smooth();

        // create a window the size of the depth information
//        size(context.depthWidth(), context.depthHeight());
    }

    private void setupKinect() {
        // instantiate a new context
        context = new SimpleOpenNI(this);
        // enable depthMap generation
        context.enableDepth();

        // enable skeleton generation for all joints
        context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);

        context.setMirror(true);
        kinectIsConfigured = true;
    }

    private void setupAudio() {
        playBack = new PlayBack();
        minim = new Minim(this);
        ai = minim.getLineIn(Minim.MONO);
        ai.printControls();
        List<Control> controls = Arrays.asList(ai.getControls());
        System.out.println(ai.getControls().length + " Controls: " + controls);
        sound = new LinkedList<>();
    }

    /**
     * Another method that will be called automatically...
     * Do all the drawing stuff in this method.
     */
    @Override
    public void draw() {
        background(Color.BLACK.getRGB());
//        drawBackgroundBySound();
        if (kinectIsConfigured) drawKinectStuff();

//        drawSoundLines();
//        drawTexturedShape(shapes.get(0));
//drawAllShapes();

    }

    private void drawAllShapes(){
        drawColoredShape(shapes.get(0), Color.GREEN);
        drawColoredShape(shapes.get(1), Color.BLUE);
        drawColoredShape(shapes.get(2), Color.RED);
        drawColoredShape(shapes.get(3), Color.YELLOW);
        drawColoredShape(shapes.get(4), Color.CYAN);
        drawColoredShape(shapes.get(5), Color.MAGENTA);
        drawColoredShape(shapes.get(6), Color.ORANGE);
        drawColoredShape(shapes.get(7), Color.RED);
        drawColoredShape(shapes.get(8), Color.GRAY);
        drawColoredShape(shapes.get(9), Color.LIGHT_GRAY);
        save("screenshot.jpg");
    }

    private void drawKinectStuff() {
        context.update();

        // update only after 1000 sec.
//        if ((System.currentTimeMillis() - time) > 1000) {



        for (int i = 1; i < 3; i++) {
            if (context.isTrackingSkeleton(i)) {
//                drawSkeleton(i);
//                draw random colored shapes
                drawColoredShape(shapes.get(i - 1), getColorOfJoint(i, SimpleOpenNI.SKEL_RIGHT_HAND));
                drawColoredShape(shapes.get(i + 1), getColorOfJoint(i, SimpleOpenNI.SKEL_LEFT_HAND));
                drawColoredShape(shapes.get(i + 3), getColorOfJoint(i, SimpleOpenNI.SKEL_RIGHT_KNEE));
                drawColoredShape(shapes.get(i + 5), getColorOfJoint(i, SimpleOpenNI.SKEL_LEFT_KNEE));
                drawColoredShape(shapes.get(i + 7), getColorOfJoint(i, SimpleOpenNI.SKEL_HEAD));



                // set time to current system time
//                time = System.currentTimeMillis();
//            }
            } else {
                //drawColoredShape(shapes.get(i-1), Color.cyan);
            }
        }
    }

    private void drawBackgroundBySound() {


//MINIM
        ai.addListener(playBack); //Samples from mic go to pb
//        ap.addSignal(playBack); //ap will playback pb constantly.
        AudioBuffer audioBuffer = ai.mix;
        float value = audioBuffer.level() * 1.3f;


//        float value = ai.getGain();

        value = (float)Math.round(value * 100) / 100;
        if (sound.size() > 100) {
            sound.remove();
        }
        if (value > 1) value = 1;
        sound.add(value);
        float average = 0;
        for (float f : sound){
            average += f;
        }
        value = average/sound.size();
        Color c = Color.getHSBColor(value, 0.99f, 0.99f);
//        System.out.println("Value: " + value);
        background(c.getRGB());
    }

    private void drawSoundLines() {
//        background(0);
        stroke(255);
        // draw the waveforms
        for (int i = 0; i < ai.bufferSize() - 1; i++) {
            line(i, 50 + ai.mix.get(i) * 50, i + 1, 50 + ai.mix.get(i + 1) * 50);
//            line(i, 150 + ai.right.get(i) * 50, i + 1, 150 + ai.right.get(i + 1) * 50);
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
        shapes.add(new Shape(640, 75));
        shapes.add(new Shape(665, 287));
        shapes.add(new Shape(717, 299));
        shapes.add(new Shape(717, 388));
        shapes.add(new Shape(793, 572));
        shapes.add(new Shape(634, 388));
        shapes.add(new Shape(644, 515));
        shapes.add(new Shape(655, 598));
        shapes.add(new Shape(638, 678));
        shapes.add(new Shape(712, 657));
//        shapes.add(new Shape(692, 447));

        shapes.get(0).add(-11, -73);
        shapes.get(0).add(-65, -61);
        shapes.get(0).add(-38, 74);
        shapes.get(0).add(66, 50);

        shapes.get(1).add(-32, -142);
        shapes.get(1).add(-52, -120);
        shapes.get(1).add(-90, 154);
        shapes.get(1).add(-5, 43);
        shapes.get(1).add(90, -87);
        shapes.get(1).add(60, -127);
        shapes.get(1).add(18, -154);

        shapes.get(2).add(38, -99);
        shapes.get(2).add(57, 100);
        shapes.get(2).add(-57, 31);

        shapes.get(3).add(-57, -58);
        shapes.get(3).add(-24, 58);
        shapes.get(3).add(57, 11);

        shapes.get(4).add(-100, -126);
        shapes.get(4).add(-29, 173);
        shapes.get(4).add(63, 120);
        shapes.get(4).add(100, 79);
        shapes.get(4).add(-19, -173);

        shapes.get(5).add(26, -58);
        shapes.get(5).add(-59, 53);
        shapes.get(5).add(59, 58);

        shapes.get(6).add(-47, 75);
        shapes.get(6).add(-69, -74);
        shapes.get(6).add(48, -68);
        shapes.get(6).add(70, 34);

        shapes.get(7).add(59, -49);
        shapes.get(7).add(5, 50);
        shapes.get(7).add(-58, -8);

        shapes.get(8).add(22, -30);
        shapes.get(8).add(41, 88);
        shapes.get(8).add(-24, 62);
        shapes.get(8).add(-41, -88);

        shapes.get(9).add(2, -108);
        shapes.get(9).add(-52, -9);
        shapes.get(9).add(-33, 109);
        shapes.get(9).add(52, 88);

//        Shape s = new Shape(40,40);
//        s.add(50,-10);
//        s.add(70,30);
//        s.add(50,70);
//        s.add(10,70);
//        s.add(0,0);
//        shapes.add(s);
//        System.out.println(s.getBoundingBox());

        PImage image = loadImage("pic.jpg");

        for (Shape shape : shapes){
            shape.setImage(image);
//            System.out.println(shape.getBoundingBox());
        }
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
    private void drawColoredShape(Shape shape, Color color) {
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
        for (Point point : shape.getPoints()) {
            vertex(point.x, point.y);
        }

        endShape(CLOSE);
        popMatrix();
    }

    /**
     * This method draws a shape and fill it with an color
     *
     * @param shape
     */
    private void drawTexturedShape(Shape shape) {
        // I didn't analyse what pushMatrix(), translate(x,y) and popMatrix() do yet, but without calling this methods,
        //  the shapes will be drawn on a wrong position.
        // TODO: Understand what this methods exactly does...
        pushMatrix();

        texture(shape.getImage());
//fill(Color.BLUE.getRGB());
        translate(shape.x, shape.y);

        beginShape();

        // uncomment to draw NO shape outlines
//        noStroke();
        // uncomment to set the outline color
        stroke(Color.BLACK.getRGB());
        // uncomment to set the outline weight
        strokeWeight(3);

        // draw a vertex between all points of a shape
        for (Point point : shape.getPoints()) {
            vertex(point.x, point.y,0,shape.getBoundingBox().getMidPoint().x,shape.getBoundingBox().getMidPoint().y);

        }

        endShape(CLOSE);
        popMatrix();
//        pushMatrix();
//        fill(Color.red.getRGB());
//        ellipseMode(CENTER);
//        ellipse(shape.getBoundingBox().getMidPoint().x,shape.getBoundingBox().getMidPoint().y,10,10);
//        popMatrix();
    }

    private void drawPolygon(Polygon poly, Color color) {
        //PGraphics

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

//    private class Shape extends Point {
//        public List<Point> points = new LinkedList<>();
//
//        public Shape(int x, int y) {
//            super(x, y);
//        }
//
//        public void add(int x, int y) {
//            points.add(new Point(x, y));
//        }
//    }


}
