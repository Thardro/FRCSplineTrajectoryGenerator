package src;

public class Main {
	
	static FileIO fileIO = new FileIO();

	//Start point
	static double x0 = 0, y0 = 0, theta0 = 0;
	
	//End point
	static double x1 = 10, y1 = 5, theta1 = Math.PI / 2;
	
	//Velocity settings
	static double filter1 = 400, filter2 = 200, maxVelocity = 4;
	static double dt = 10;
	
	//Robot parameters
	static double wheelbaseWidth = 2;
	
	public static void main(String[] args) {
		Trajectory[] trajectory = TrajectoryGenerator.generateTrajectory(
				x0, y0, theta0, x1, y1, theta1, dt, filter1, filter2, maxVelocity, wheelbaseWidth);
		
		
		fileIO.openFile("Profile.txt");
		for(int i = 0; i < trajectory[0].getLength(); i++) {
			fileIO.writeToFile(trajectory[0].getPoint(i).getVelocity() + "\t" + trajectory[0].getPoint(i).getPosition());
		}
		fileIO.closeFile();
		
		fileIO.openFile("Spline.txt");
		for(int i = 0; i < trajectory[0].getLength(); i++) {
			fileIO.writeToFile(trajectory[0].getPoint(i).getX() + "\t" + trajectory[0].getPoint(i).getY()
					+ "\t" + trajectory[1].getPoint(i).getX() + "\t" + trajectory[1].getPoint(i).getY()
					+ "\t" + trajectory[2].getPoint(i).getX() + "\t" + trajectory[2].getPoint(i).getY());
		}
		fileIO.closeFile();
	}

}
