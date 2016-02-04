package src;

public class Main {
	
	static FileIO fileIO = new FileIO();

	//Start point
	static double x0 = 0, y0 = 0, theta0 = 0;
	
	//End point
	static double x1 = 5, y1 = 10, theta1 = Math.PI / 2;
	
	//Velocity settings
	static double filter1 = 400, filter2 = 200, maxVelocity = 4;
	static int dt = 10;
	
	public static void main(String[] args) {
		Trajectory trajectory = TrajectoryGenerator.generateTrajectory(x0, y0, theta0, x1, y1, theta1, dt, filter1, filter2, maxVelocity);
		
		
		fileIO.openFile("Profile.txt");
		for(int i = 0; i < trajectory.getLength(); i++) {
			fileIO.writeToFile(trajectory.getPoint(i).getVelocity() + "\t" + trajectory.getPoint(i).getPosition());
		}
		fileIO.closeFile();
		
		fileIO.openFile("Spline.txt");
		for(int i = 0; i < trajectory.getLength(); i++) {
			fileIO.writeToFile(trajectory.getPoint(i).getX() + "\t" + trajectory.getPoint(i).getY());
		}
		fileIO.closeFile();
	}

}
