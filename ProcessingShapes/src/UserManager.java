import SimpleOpenNI.SimpleOpenNI;

public class UserManager
{
  protected SimpleOpenNI  _context;
  
  public UserManager(SimpleOpenNI context)
  {
    _context = context;
  }
  
  public void onNewUser(int userId)
  {
    System.out.println(_context + " onNewUser - userId: " + userId);
    System.out.println("  start pose detection");

    _context.startPoseDetection("Psi", userId);
  }

  public void onLostUser(int userId)
  {
	  System.out.println(_context + " onLostUser - userId: " + userId);
  }

  public void onStartCalibration(int userId)
  {
	  System.out.println(_context + " onStartCalibration - userId: " + userId);
  }

  public void onEndCalibration(int userId, boolean successfull)
  {
	  System.out.println(_context + " onEndCalibration - userId: " + userId + ", successfull: " + successfull);

    if (successfull) 
    { 
    	System.out.println("  User calibrated !!!");
      _context.startTrackingSkeleton(userId);
    } 
    else 
    { 
    	System.out.println("  Failed to calibrate user !!!");
    	System.out.println("  Start pose detection");
      _context.startPoseDetection("Psi", userId);
    }
  }

  public void onStartPose(String pose, int userId)
  {
	  System.out.println(_context + " onStartdPose - userId: " + userId + ", pose: " + pose);
	  System.out.println(" stop pose detection");

    _context.stopPoseDetection(userId); 
    _context.requestCalibrationSkeleton(userId, true);
  }

  public void onEndPose(String pose, int userId)
  {
	  System.out.println( _context + " onEndPose - userId: " + userId + ", pose: " + pose);
  }
}
