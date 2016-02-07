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
		sequence.addWaypoint(10, 5, 0);
		sequence.addWaypoint(20, 10, Math.PI / 2);
		
		Trajectory[] trajectory= TrajectoryGenerator.generateTrajectory(
				sequence, dt, filter1, filter2, maxVelocity, wheelbaseWidth);
		
		//Scaling speed based on maximum speed achieved
		double scaledMaxSpeed = maxVelocity / trajectory[2].getMaxAchievedSpeed() * maxVelocity;
		Trajectory[] scaledTrajectory = TrajectoryGenerator.generateTrajectory(sequence, dt, filter1, filter2, scaledMaxSpeed, wheelbaseWidth);
		
		fileIO.openFile("Profile.txt");
		for(int i = 0; i < scaledTrajectory[0].getLength(); i++) {
			fileIO.writeToFile(scaledTrajectory[0].getPoint(i).getVelocity() + "\t" + 
						scaledTrajectory[0].getPoint(i).getPosition() + "\t" +
						scaledTrajectory[1].getPoint(i).getVelocity() + "\t" +
						scaledTrajectory[1].getPoint(i).getPosition() + "\t" +
						scaledTrajectory[2].getPoint(i).getVelocity() + "\t" +
						scaledTrajectory[2].getPoint(i).getPosition());
		}
		fileIO.closeFile();
		
		fileIO.openFile("Spline.txt");
		for(int i = 0; i < scaledTrajectory[0].getLength(); i++) {
			fileIO.writeToFile(scaledTrajectory[0].getPoint(i).getX() + "\t" + scaledTrajectory[0].getPoint(i).getY()
					+ "\t" + scaledTrajectory[1].getPoint(i).getX() + "\t" + scaledTrajectory[1].getPoint(i).getY()
					+ "\t" + scaledTrajectory[2].getPoint(i).getX() + "\t" + scaledTrajectory[2].getPoint(i).getY());
		}
		fileIO.closeFile();
	}

}
