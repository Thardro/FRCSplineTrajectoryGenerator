package src;

public class TrajectoryGenerator {
	public static Trajectory generateTrajectory(double x0, double y0, double theta0, 
			double x1, double y1, double theta1, 
			int dt, double filter1, double filter2, double maxVelocity) {
		
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
		
		//Adding spline points to the trajectory
		for(int i = 0; i < centerTrajectory.getLength(); i++) {
			double[] point = centerPath.getPoint((double) i / centerTrajectory.getLength());
			centerTrajectory.getPoint(i).setX(point[0]);
			centerTrajectory.getPoint(i).setY(point[1]);
		}
		
		return centerTrajectory;
	}
}
