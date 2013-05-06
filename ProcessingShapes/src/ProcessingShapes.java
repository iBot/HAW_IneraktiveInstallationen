import SimpleOpenNI.SimpleOpenNI;
import ddf.minim.AudioInput;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PVector;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.devices.AudioDeviceManager;
import com.jsyn.unitgen.LineIn;
import com.jsyn.unitgen.LineOut;


import javax.sound.sampled.Control;
import java.awt.*;
import java.util.*;
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
    AudioInput ai;
    Queue<Float> sound;

    Synthesizer synth;
    LineIn lineIn;
    LineOut lineOut;
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
        playBack = new PlayBack();
        minim = new Minim(this);
        ai = minim.getLineIn(Minim.STEREO);
        ai.printControls();
        List<Control> controls = Arrays.asList(ai.getControls());
        System.out.println(ai.getControls().length + " Controls: " + controls);
        sound = new LinkedList<>();


        // Create a context for the synthesizer.
        synth = JSyn.createSynthesizer();
        // Add an audio input.
        synth.add( lineIn = new LineIn() );
        // Add an audio output.
        synth.add( lineOut = new LineOut() );
        // Connect the input to the output.
        lineIn.output.connect( 0, lineOut.input, 0 );
        lineIn.output.connect( 1, lineOut.input, 1 );

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
        stroke(0, 255, 0);
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


        for (int i = 1; i < 3; i++) {
            if (context.isTrackingSkeleton(i)) {
//                drawSkeleton(i);
//                draw random colored shapes
                drawShape(shapes.get(i - 1), getColorOfJoint(i, SimpleOpenNI.SKEL_RIGHT_HAND));
                drawShape(shapes.get(i + 1), getColorOfJoint(i, SimpleOpenNI.SKEL_LEFT_HAND));
                drawShape(shapes.get(i + 3), getColorOfJoint(i, SimpleOpenNI.SKEL_RIGHT_KNEE));
                drawShape(shapes.get(i + 5), getColorOfJoint(i, SimpleOpenNI.SKEL_LEFT_KNEE));
                // set time to current system time
//                time = System.currentTimeMillis();
//            }
            } else {
                //drawShape(shapes.get(i-1), Color.cyan);
            }
        }

        drawSoundLines();

        drawBackgroundBySound();

    }

    private void drawBackgroundBySound(){
        ai.addListener(playBack); //Samples from mic go to pb
        //ap.addSignal(playBack); //ap will playback pb constantly.
        float value = ai.mix.level()*50;
        value = (float)Math.round(value * 100) / 100;
        if (sound.size()>500){
            sound.remove();
        }
        sound.add(value);
        float average = 0;
        for (float f : sound){
            average += f;
        }
        value = average/sound.size();
        Color c = Color.getHSBColor(value, 0.99f, 0.99f);
        System.out.println("Value: " + value);
        background(c.getRGB());
    }

    private void drawSoundLines() {
        background(0);
        stroke(255);
        // draw the waveforms
        for (int i = 0; i < ai.bufferSize() - 1; i++) {
            line(i, 50 + ai.left.get(i) * 50, i + 1, 50 + ai.left.get(i + 1) * 50);
            line(i, 150 + ai.right.get(i) * 50, i + 1, 150 + ai.right.get(i + 1) * 50);
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
        shapes.add(new Shape(596, 408));
        shapes.add(new Shape(663, 214));
        shapes.add(new Shape(847, 389));
        shapes.add(new Shape(274, 123));
        shapes.add(new Shape(99, 123));
        shapes.add(new Shape(277, 355));
        shapes.add(new Shape(1076, 509));
        shapes.add(new Shape(1111, 153));
//        shapes.add(new Shape(1007, 311));

        shapes.get(0).add(195, -141);
        shapes.get(0).add(-195, -242);
        shapes.get(0).add(-195, -184);
        shapes.get(0).add(-179, -159);
        shapes.get(0).add(87, 242);
        shapes.get(0).add(93, 165);
        shapes.get(0).add(151, 185);
        shapes.get(0).add(169, 188);

        shapes.get(1).add(-262, -48);
        shapes.get(1).add(213, -52);
        shapes.get(1).add(253, -42);
        shapes.get(1).add(262, -32);
        shapes.get(1).add(128, 53);

        shapes.get(2).add(78, -207);
        shapes.get(2).add(82, -189);
        shapes.get(2).add(-82, 207);
        shapes.get(2).add(-56, -122);

        shapes.get(3).add(78, -107);
        shapes.get(3).add(81, 104);
        shapes.get(3).add(-80, 108);
        shapes.get(3).add(-81, -104);

        shapes.get(4).add(-80, -105);
        shapes.get(4).add(81, -106);
        shapes.get(4).add(79, 105);
        shapes.get(4).add(-80, 107);

        shapes.get(5).add(-86, -111);
        shapes.get(5).add(86, -114);
        shapes.get(5).add(85, 107);
        shapes.get(5).add(-82, 115);

        shapes.get(6).add(-82, -86);
        shapes.get(6).add(66, -87);
        shapes.get(6).add(82, 85);
        shapes.get(6).add(-67, 88);

        shapes.get(7).add(-123, -153);
        shapes.get(7).add(-104, 158);
        shapes.get(7).add(123, 154);
        shapes.get(7).add(98, -157);

//        shapes.get(8).add(0, 0);
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

    private class Shape extends Point {
        public List<Point> points = new LinkedList<>();

        public Shape(int x, int y) {
            super(x, y);
        }

        public void add(int x, int y) {
            points.add(new Point(x, y));
        }
    }


}
