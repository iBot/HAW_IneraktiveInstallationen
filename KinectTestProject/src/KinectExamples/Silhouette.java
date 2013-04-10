package KinectExamples;

import SimpleOpenNI.IntVector;
import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.LinkedList;
import java.util.List;

// import library

/**
 * Created with IntelliJ IDEA.
 * User: tobi
 * Date: 18.03.13
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class Silhouette extends PApplet {
    // Kinect Basic Example by Amnon Owed (15/09/12)


    // declare SimpleOpenNI object
    SimpleOpenNI context;

    // PImage to hold incoming imagery
    PImage cam;

    int high = 480;
    int width = 640;

    public void setup() {
        // same as Kinect dimensions
        size(width, high);
        // initialize SimpleOpenNI object
        context = new SimpleOpenNI(this);
        if (!context.enableScene()) {
            // if context.enableScene() returns false
            // then the Kinect is not working correctly
            // make sure the green light is blinking
            println("Kinect not connected!");
            exit();
        } else {
            // mirror the image to be more intuitive
            context.setMirror(true);
//            context.enableDepth();
            context.enableRGB();
//            context.enableIR();
//            context.enableHands();
            context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
        }
    }

    public void draw() {

    	// update the camera
        context.update();

        // draw depth image
        image(context.depthImage(),0,0);


        IntVector userList = new IntVector();

        context.getUsers(userList);


        if (!userList.isEmpty()) {
            int userID = userList.get(0);

            List<PVector> skeletonPositions = new LinkedList<PVector>();
            context.startTrackingSkeleton(userID);
//            System.out.println(context.isTrackingSkeleton(userID));


            if (context.isTrackingSkeleton(userID)) {
                System.out.println(context.isTrackingSkeleton(userID));

                PVector leftHand = new PVector();
                context.getJointPositionSkeleton(userID, SimpleOpenNI.SKEL_LEFT_HAND, leftHand);
                skeletonPositions.add(0, leftHand);

//                PVector rightHand = new PVector();
//                context.getJointPositionSkeleton(userID, SimpleOpenNI.SKEL_RIGHT_HAND, rightHand);
//                skeletonPositions.add(0,rightHand);
//
//                PVector head = new PVector();
//                context.getJointPositionSkeleton(userID, SimpleOpenNI.SKEL_HEAD,head);
//                skeletonPositions.add(0, head);


//                float angleInRadians = PVector.angleBetween(rightHand, leftHand);
//                float angleInDegrees = degrees(angleInRadians);

                for (PVector skeletonPosition : skeletonPositions) {
                    PVector convertedPosition = new PVector(640,480);
                    context.convertRealWorldToProjective(skeletonPosition, convertedPosition);
//                    System.out.println("## Print skeleton positions...");
//                    System.out.println(String.format("orgX=%.2f; orgY=%.2f", skeletonPosition.x, skeletonPosition.y));
//                    System.out.println(String.format("orgX=%.2f; orgY=%.2f; conX=%.2f; conY=%.2f", skeletonPosition.x, skeletonPosition.y, convertedPosition.x, convertedPosition.y));
//                    ellipseMode(CENTER);
                    ellipse(skeletonPosition.x, high-skeletonPosition.y, 50, 50);
                }

            }


        }


    }

}
