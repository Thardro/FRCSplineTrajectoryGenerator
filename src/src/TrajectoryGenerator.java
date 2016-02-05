package src;

public class TrajectoryGenerator {
	private enum Side {
		LEFT, RIGHT;
	}
	
	public static Trajectory generateTrajectory(double x0, double y0, double theta0, 
			double x1, double y1, double theta1, 
			int dt, double filter1, double filter2, double maxVelocity, double wheelbase) {
		//Calculating trajectory of center of the robot
		Trajectory centerTrajectory = generateCenterTrajectory(x0, y0, theta0, x1, y1, theta1, dt, filter1, filter2, maxVelocity);
		
		
		
		return centerTrajectory;
	}
	
	private static Trajectory generateCenterTrajectory(double x0, double y0, double theta0, 
			double x1, double y1, double theta1, 
			int dt, double filter1, double filter2, double maxVelocity) {
		
		//Generating spline path for center of robot from given start and end points
		Spline centerPath = new Spline(x0, y0, theta0, x1, y1, theta1);
		System.out.println(centerPath.getLength());
		
		//Generating a basic velocity curve for the center of the robot
		VelocityProfileGenerator velProfile = new VelocityProfileGenerator(
				dt, filter1, filter2, maxVelocity, centerPath.getLength());
		
		Trajectory centerTrajectory = velProfile.calculateProfile();
		
		//Adding spline points and heading to the trajectory
		for(int i = 0; i < centerTrajectory.getLength(); i++) {
			double percentage = (double) i / centerTrajectory.getLength();
			
			double[] point = centerPath.getPoint(percentage);
			centerTrajectory.getPoint(i).setX(point[0]);
			centerTrajectory.getPoint(i).setY(point[1]);
			
			double heading = centerPath.getAngle(percentage);
			centerTrajectory.getPoint(i).setHeading(heading);
		}
		
		return centerTrajectory;
	}
	
	private static Trajectory generateSideFromCenter(Trajectory centerTrajectory, double wheelbase, Side side) {
		for(int i = 0; i < centerTrajectory.getLength(); i++) {
			
		}
		
		return null;
	}
}
