import SimpleOpenNI.SimpleOpenNI;
import ddf.minim.AudioBuffer;
import ddf.minim.AudioInput;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

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
    private boolean kinectIsConfigured = false;
    private boolean soundIsConfigured = false;

    private float xMax, yMax;
    /**
     * This List will contain all the Shapes which we will fill with different colors
     */
    public static List<Shape> shapes = new LinkedList<>();


    public static Map<Integer,List<Shape>> userShapes = new HashMap<>();
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


	private void setupCommonStuff() {
        // size for fullscreen window, renderer OpenGL
        //  Don't forget to import native libraries for your OS. If OpenGL doesn't work, render without OpenGL...
        size(displayWidth, displayHeight, OPENGL);
//        size(displayWidth, displayHeight, P3D);
        //size(displayWidth, displayHeight, P2D);
        //size(1000, 400, P3D);

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
        
        UserManager userManager = new UserManager(context);
        

    	System.out.println(context+"");
        // enable depthMap generation
        context.enableDepth();

        // enable skeleton generation for all joints
        context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL, userManager);

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

        soundIsConfigured = true;
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

        if (soundIsConfigured) drawSoundLines();

        if (!soundIsConfigured & !kinectIsConfigured){
            drawAllShapes();

//            drawTexturedShape(shapes.get(0));

//            save("screenshot.jpg");
        }
//        printCoordinatesOfLeftHand();
    }

    private void printCoordinatesOfLeftHand() {
        if (context.isTrackingSkeleton(1)) {
            PVector jointPos = new PVector();
            context.getJointPositionSkeleton(1, SimpleOpenNI.SKEL_LEFT_HAND,
                    jointPos);
            if (xMax<jointPos.x) xMax = jointPos.x;
            if (yMax<jointPos.y) yMax = jointPos.y;
            System.out.printf("X= %s (%s); Y= %s (%s); Z= %s%n", jointPos.x,xMax, jointPos.y,yMax, jointPos.z);
            context.drawLimb(1, SimpleOpenNI.SKEL_LEFT_HAND, SimpleOpenNI.SKEL_LEFT_WRIST);

        }
    }

    private void drawAllShapes() {
//        drawColoredShape(shapes.get(0), Color.GREEN);
//        drawColoredShape(shapes.get(1), Color.BLUE);
//        drawColoredShape(shapes.get(2), Color.RED);
//        drawColoredShape(shapes.get(3), Color.YELLOW);
//        drawColoredShape(shapes.get(4), Color.CYAN);
//        drawColoredShape(shapes.get(5), Color.MAGENTA);
//        drawColoredShape(shapes.get(6), Color.ORANGE);
//        drawColoredShape(shapes.get(7), Color.RED);
//        drawColoredShape(shapes.get(8), Color.GRAY);
//        drawColoredShape(shapes.get(9), Color.LIGHT_GRAY);

        //drawColoredShapeWithForms(shapes.get(0),Color.ORANGE);
        for (Shape shape : shapes){
            drawColoredShape(shape, Color.CYAN);
        }
    }

    private void drawKinectStuff() {
        context.update();

        // update only after 1000 sec.
//        if ((System.currentTimeMillis() - time) > 1000) {



        for (int i = 1; i < 3; i++) {
        	
            if (context.isTrackingSkeleton(i)) {
            	//System.out.println(context+"context tracked skeleton "+i);
            	//System.out.println(Arrays.toString(context.getUsers()));
//                drawSkeleton(i);
//                draw random colored shapes
            	
            	//drawBoundingBox(shapes.get(shapes.size()-1));
//                drawColoredShapeWithForms(shapes.get(shapes.size()-1), getColorOfJoint(i, SimpleOpenNI.SKEL_RIGHT_HAND, context), position);
                drawColoredShapeWithForms(userShapes.get(i).get(0),
                		getChangedSaturationColor(i, SimpleOpenNI.SKEL_LEFT_HAND, userShapes.get(i).get(0)),
                		getPosition(SimpleOpenNI.SKEL_LEFT_HAND, i));
                drawColoredShapeWithForms(userShapes.get(i).get(1),
                		getChangedSaturationColor(i, SimpleOpenNI.SKEL_RIGHT_KNEE, userShapes.get(i).get(1)),
                		getPosition(SimpleOpenNI.SKEL_RIGHT_KNEE, i));
                drawColoredShapeWithForms(userShapes.get(i).get(2),
                		getChangedSaturationColor(i, SimpleOpenNI.SKEL_LEFT_KNEE, userShapes.get(i).get(2)),
                		getPosition(SimpleOpenNI.SKEL_LEFT_KNEE, i));
                drawColoredShapeWithForms(userShapes.get(i).get(3),
                		getChangedSaturationColor(i, SimpleOpenNI.SKEL_RIGHT_HAND, userShapes.get(i).get(3)),
                		getPosition(SimpleOpenNI.SKEL_RIGHT_HAND, i));

          }
        }
    }

    private PVector getPosition(int joint, int i) {
    	PVector position = new PVector();
    	context.getJointPositionSkeleton(i, joint, position);
    	return position;
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

//        Shape s = new Shape(40,40);
//        s.add(50,-10);
//        s.add(70,30);
//        s.add(50,70);
//        s.add(10,70);
//        s.add(0,0);
//        shapes.add(s);
//        System.out.println(s.getBoundingBox());
		
		shapes.get(0).setColor(new Color(0xD04328));
		shapes.get(1).setColor(new Color(0x63382D));
		shapes.get(2).setColor(new Color(0xC2782F));
		shapes.get(3).setColor(new Color(0x8A443A));
		shapes.get(4).setColor(new Color(0x346E45));
		shapes.get(5).setColor(new Color(0x344761));
		shapes.get(6).setColor(new Color(0xDB4D27));
		shapes.get(7).setColor(new Color(0x344761));
		shapes.get(8).setColor(new Color(0xC2782F));
		shapes.get(8).setColor(new Color(0xDB4D27));

        PImage image = loadImage("pic.jpg");

        for (Shape shape : shapes){
            shape.setImage(image);
//            System.out.println(shape.getBoundingBox());
        }
    }

    private Color getChangedSaturationColor(int userId, int joint, Shape shape){
    	PVector jointPos = new PVector();
        context.getJointPositionSkeleton(userId, joint,
                jointPos);
        PVector jointPos_conv = new PVector();
        context.convertRealWorldToProjective(jointPos, jointPos_conv);
    	float saturation = jointPos_conv.y / displayHeight;
    	Color c = shape.getColor();
    	float[] hsb = new float[3];
    	Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(),hsb);
    	Color neu = Color.getHSBColor(hsb[0], saturation, hsb[2]);
    	
    	return neu;
    }
    
    private Color getColorOfJoint(int userId, int joint, SimpleOpenNI context) {
        // get 3D position of a joint
        PVector jointPos = new PVector();
        context.getJointPositionSkeleton(userId, joint,
                jointPos);
        PVector jointPos_conv = new PVector();
        context.convertRealWorldToProjective(jointPos, jointPos_conv);

        System.out.println("X: "+jointPos.x+" Y: "+jointPos.y);
        System.out.println("Konvertiert: X: "+jointPos_conv.x+" Y: "+jointPos_conv.y);
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
    
    private void drawColoredShapeWithHandFollower(Shape shape, Color color, PVector jointPos){
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
        
        BoundingBox bb = shape.getBoundingBox();
        
        pushMatrix();
        
        System.out.println("BoundingBox: X: "+bb.getLeftTop().x+" Y: "+bb.getLeftTop().y);
        translate(bb.getLeftTop().x, bb.getLeftTop().y);
        
        fill(Color.DARK_GRAY.getRGB());
        noStroke();
        PVector ellipsePos = new PVector();
        PVector jointPos_conv = new PVector();
        context.convertRealWorldToProjective(jointPos, jointPos_conv);
        
        ellipsePos.x = jointPos_conv.x * (shape.getBoundingBox().getWidth()*2)/displayWidth;
        ellipsePos.y = jointPos_conv.y * (shape.getBoundingBox().getHeight()*2)/displayHeight;
        
        System.out.println("Spieler X: "+jointPos.x+" Y: "+jointPos.y);
        System.out.println("Breite: "+shape.getBoundingBox().getWidth()+" Höhe: "+shape.getBoundingBox().getHeight());
        System.out.println("X: "+ellipsePos.x+" Y: "+ellipsePos.y);
        ellipse(ellipsePos.x, ellipsePos.y, 20, 20);
        popMatrix();
        
    }
    
    /**
     * This method draws a shape and fill it with an color
     *
     * @param shape
     * @param color
     */
    private void drawColoredShapeWithForms(Shape shape, Color color, PVector jointPos) {
       
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

        
        BoundingBox bb = shape.getBoundingBox();
        
        PVector jointPos_conv = new PVector();
        context.convertRealWorldToProjective(jointPos, jointPos_conv);
        
        noStroke();
        fill(Color.DARK_GRAY.getRGB());
        // Cycle through the array, using a different entry on each frame.
        // Using modulo (%) like this is faster than moving all the values over.
        int which = frameCount % shape.num;
        shape.mx[which] = jointPos_conv.x * (bb.getWidth()*2)/displayWidth+bb.getLeftTop().x;
        shape.my[which] = jointPos_conv.y * (bb.getHeight()*2)/displayHeight+bb.getLeftTop().y;

        for (int i = 0; i < shape.num; i++) {
            // which+1 is the smallest (the oldest in the array)
            int index = (which+1 + i) % shape.num;
            if (shape.getPolygon().contains(shape.mx[index], shape.my[index])){
//                System.out.println(">>>>>>>> true");

                ellipse(shape.mx[index], shape.my[index], i, i);
            } //else {
//                System.out.println("######## false");
            //}
        }
    }

	/**
     * This method draws a shape and fill it with an color
     * !!!!!!Funzt grad nicht
     * !!!!!!Funktioniert nur ohne translate...dh die Punkte in Shape m���ssen aufs ganze Koordinatensystem
     * umgerechnet werden. 
     *1
     * @param shape
     */
    private void drawTexturedShape(Shape shape) {
        // I didn't analyse what pushMatrix(), translate(x,y) and popMatrix() do yet, but without calling this methods,
        //  the shapes will be drawn on a wrong position.
        // TODO: Understand what this methods exactly does...
        pushMatrix();

        textureMode(NORMAL);
        beginShape();

        // uncomment to draw NO shape outlines
//        noStroke();
        // uncomment to set the outline color
        stroke(Color.BLACK.getRGB());
        // uncomment to set the outline weight
        strokeWeight(3);

        texture(shape.getImage());
        int i = 1;
        // draw a vertex between all points of a shape
        for (Point point : shape.getPoints()) {
        	switch (i)
        	{
        	case 1: vertex(point.x, point.y, 0, 0,0);
        			break;
        	case 2: vertex(point.x, point.y, 0, 1, 0);
        			break;
        	case 3: vertex(point.x, point.y, 0, 1, 1);
        			break;
        	case 4: vertex(point.x, point.y, 0, 0,1);
        			break;
        	default: vertex(point.x, point.y, 0);
        	}
        	i++;

            
        }

        endShape(CLOSE);
//        popMatrix();
//        pushMatrix();

        ellipseMode(CENTER);
        fill(Color.green.getRGB());
        ellipse(shape.getBoundingBox().getLeftTop().x,shape.getBoundingBox().getLeftTop().y,10,10);
        fill(Color.yellow.getRGB());
        ellipse(shape.getBoundingBox().getRightBottom().x,shape.getBoundingBox().getRightBottom().y,10,10);
        fill(Color.red.getRGB());
        ellipse(shape.getBoundingBox().getMidPoint().x,shape.getBoundingBox().getMidPoint().y,10,10);
        popMatrix();
    }
    
    private void drawBoundingBox(Shape shape)
    {
    	noFill();
    	strokeWeight(3);
    	stroke(Color.red.getRGB());
    	BoundingBox bb = shape.getBoundingBox();
    	//rectMode(CORNERS);
    	//rect(bb.getLeftTop().x, bb.getLeftTop().y, bb.getRightBottom().x, bb.getRightBottom().y);
    	rect(bb.getLeftTop().x, bb.getLeftTop().y, bb.getWidth(), bb.getHeight());
    }

//    private void drawPolygon(Polygon poly, Color color) {
//        //PGraphics
//
//    }

//    // when a person ('user') enters the field of view
//    public void onNewUser(int userId) {
//        println("New User Detected - userId: " + userId);
//
//        // start pose detection
//        context2.startPoseDetection("Psi", userId);
//    }
//
//    // when a person ('user') leaves the field of view
//    public void onLostUser(int userId) {
//        println("User Lost - userId: " + userId);
//    }
//
//    // when a user begins a pose
//    public void onStartPose(String pose, int userId) {
//        println("Start of Pose Detected  - userId: " + userId + ", pose: "
//                + pose);
//
//        // stop pose detection
//        context2.stopPoseDetection(userId);
//
//        // start attempting to calibrate the skeleton
//        context2.requestCalibrationSkeleton(userId, true);
//    }
//
//    // when calibration begins
//    public void onStartCalibration(int userId) {
//        println("Beginning Calibration - userId: " + userId);
//    }
//
//    // when calibaration ends - successfully or unsucessfully
//    public void onEndCalibration(int userId, boolean successfull) {
//        println("Calibration of userId: " + userId + ", successfull: "
//                + successfull);
//
//        if (successfull) {
//            println("  User calibrated !!!");
//
//            // begin skeleton tracking
//            context2.startTrackingSkeleton(userId);
//        } else {
//            println("  Failed to calibrate user !!!");
//
//            // Start pose detection
//            context2.startPoseDetection("Psi", userId);
//        }
//    }



}
