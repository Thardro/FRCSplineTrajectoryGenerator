package src;

public class Main {
	
	static FileIO fileIO = new FileIO();

	//Start point
	static double x0 = 0, y0 = 0, theta0 = 0;
	
	//End point
	static double x1 = 10, y1 = 5, theta1 = 0;
	
	//Velocity settings
	static double filter1 = 400, filter2 = 200, maxVelocity = 4;
	static int dt = 10;
	
	//Robot parameters
	static double wheelbase = 2;
	
	public static void main(String[] args) {
		Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
				x0, y0, theta0, x1, y1, theta1, dt, filter1, filter2, maxVelocity, wheelbase);
		
		
		fileIO.openFile("Profile.txt");
		for(int i = 0; i < trajectory.getLength(); i++) {
			fileIO.writeToFile(trajectory.getPoint(i).getVelocity() + "\t" + trajectory.getPoint(i).getPosition());
		}
		fileIO.closeFile();
		
		fileIO.openFile("Spline.txt");
		for(int i = 0; i < trajectory.getLength(); i++) {
			fileIO.writeToFile(trajectory.getPoint(i).getX() + "\t" + trajectory.getPoint(i).getY() +
					"\t" + trajectory.getPoint(i).getHeading());
		}
		fileIO.closeFile();
	}

}
