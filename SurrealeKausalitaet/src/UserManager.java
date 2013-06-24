import SimpleOpenNI.SimpleOpenNI;

import java.util.*;

public class UserManager
{
  protected SimpleOpenNI  context;
  
  public UserManager(SimpleOpenNI context)
  {
    this.context = context;
  }
  
  
  
//when a person ('user') enters the field of view
public void onNewUser(int userId) {
	System.out.println("New User Detected - userId: " + userId);

    // start pose detection
    context.startPoseDetection("Psi", userId);
}

// when a person ('user') leaves the field of view
public void onLostUser(int userId) {
	System.out.println("User Lost - userId: " + userId);
    SurrealeKausalitaet.userShapes.remove(userId);
}

// when a user begins a pose
public void onStartPose(String pose, int userId) {
    System.out.println("Start of Pose Detected  - userId: " + userId + ", pose: "
            + pose);

    // stop pose detection
    context.stopPoseDetection(userId);

    // start attempting to calibrate the skeleton
    context.requestCalibrationSkeleton(userId, true);
}

// when calibration begins
public void onStartCalibration(int userId) {
	System.out.println("Beginning Calibration - userId: " + userId);
}

// when calibaration ends - successfully or unsucessfully
public void onEndCalibration(int userId, boolean successfull) {
	System.out.println("Calibration of userId: " + userId + ", successfull: "
            + successfull);

    if (successfull) {
    	System.out.println("  User calibrated !!!");

        // begin skeleton tracking
        context.startTrackingSkeleton(userId);
        List<Shape> tmpShapes = new ArrayList<>(SurrealeKausalitaet.shapes);
        for (List<Shape> usedShapes: SurrealeKausalitaet.userShapes.values()){
            tmpShapes.removeAll(usedShapes);
        }
        Collections.shuffle(tmpShapes);
        tmpShapes.subList(0,4);
        SurrealeKausalitaet.userShapes.put(userId,tmpShapes.subList(0,4));
    } else {
    	System.out.println("  Failed to calibrate user !!!");

        // Start pose detection
        context.startPoseDetection("Psi", userId);
    }
}

}
