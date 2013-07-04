import SimpleOpenNI.SimpleOpenNI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class manages everything between kinect and users...
 */
public class UserManager {
    protected SimpleOpenNI context;

    /**
     * UserManager constructor
     *
     * @param context Kinect for the context
     */
    public UserManager(SimpleOpenNI context) {
        this.context = context;
    }

    /**
     * when a person ('user') enters the field of view
     *
     * @param userId id of the new user
     */
    public void onNewUser(int userId) {
        System.out.println("New User Detected - userId: " + userId);

        // start pose detection
        context.startPoseDetection("Psi", userId);
    }

    /**
     * when a person ('user') leaves the field of view
     *
     * @param userId id of the loft user
     */
    public void onLostUser(int userId) {
        System.out.println("User Lost - userId: " + userId);
        SurrealeKausalitaet.userShapes.remove(userId);
    }

    /**
     * when a user begins a pose
     *
     * @param pose   name of pose
     * @param userId id of the user
     */
    public void onStartPose(String pose, int userId) {
        System.out.println("Start of Pose Detected  - userId: " + userId + ", pose: "
                + pose);

        // stop pose detection
        context.stopPoseDetection(userId);

        // start attempting to calibrate the skeleton
        context.requestCalibrationSkeleton(userId, true);
    }

    /**
     * when calibration begins
     *
     * @param userId id of the user to calibrate
     */
    public void onStartCalibration(int userId) {
        System.out.println("Beginning Calibration - userId: " + userId);
    }

    /**
     * When the callibration ended successfull, shapes will be assigned to the user.
     *
     * @param userId      id of the calibrated user
     * @param successfull calibration successfull or not
     */
    public void onEndCalibration(int userId, boolean successfull) {
        System.out.println("Calibration of userId: " + userId + ", successfull: "
                + successfull);

        if (successfull) {
            System.out.println("  User calibrated !!!");


            List<Shape> tmpShapes = new ArrayList<>(SurrealeKausalitaet.shapes);
            for (List<Shape> usedShapes : SurrealeKausalitaet.userShapes.values()) {
                tmpShapes.removeAll(usedShapes);
            }
            Collections.shuffle(tmpShapes);
            //TODO: Habe gerade keine Kinect um zu testen, aber sublist ist doch nicht destruktiv, oder? Das heißt,die folgende Zeile kann gelöscht werden. Oder?
            tmpShapes.subList(0, 4);
            // Assign Shapes to the user
            SurrealeKausalitaet.userShapes.put(userId, tmpShapes.subList(0, 4));
            // begin skeleton tracking
            context.startTrackingSkeleton(userId);
        } else {
            System.out.println("  Failed to calibrate user !!!");

            // Start pose detection
            context.startPoseDetection("Psi", userId);
        }
    }

}
