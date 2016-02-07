package src;

public class Main {
	
	static FileIO fileIO = new FileIO();
	
	//Velocity settings
	static double filter1 = 400, filter2 = 200, maxVelocity = 4;
	static double dt = 20;
	
	//Robot parameters
	static double wheelbaseWidth = 2;
	
	public static void main(String[] args) {
		WaypointSequence sequence = new WaypointSequence();
		sequence.addWaypoint(0, 0, 0);
		sequence.addWaypoint(10, 5, Math.PI / 2);
		sequence.addWaypoint(20, 15, Math.PI / 2);
		
		Trajectory[] trajectory = TrajectoryGenerator.generateTrajectory(
				sequence, dt, filter1, filter2, maxVelocity, wheelbaseWidth, false);
		
		
		fileIO.openFile("Profile.txt");
		for(int i = 0; i < trajectory[0].getLength(); i++) {
			fileIO.writeToFile(trajectory[0].getPoint(i).getVelocity() + "\t" + 
						trajectory[0].getPoint(i).getPosition() + "\t" +
						trajectory[1].getPoint(i).getVelocity() + "\t" +
						trajectory[1].getPoint(i).getPosition() + "\t" +
						trajectory[2].getPoint(i).getVelocity() + "\t" +
						trajectory[2].getPoint(i).getPosition());
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
