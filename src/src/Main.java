package src;

public class Main {
	
	static FileIO fileIO = new FileIO();
	
	//Velocity settings
	static double filter1 = 400, filter2 = 200, maxVelocity = 4;
	static double dt = 20;
	
	//Robot parameters
	static double wheelbaseWidth = 2, wheelDiameter = 6, scale = wheelDiameter * Math.PI / 12;
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		WaypointSequence sequence = new WaypointSequence();
		sequence.addWaypoint(0, 0, 0);
		sequence.addWaypoint(10, 0, 0);
		sequence.addWaypoint(20, -4, -Math.PI / 3);
		
		Trajectory[] trajectory= TrajectoryGenerator.generateTrajectory(
				sequence, dt, filter1, filter2, maxVelocity, wheelbaseWidth);
		
		//Scaling speed based on maximum speed achieved
		double scaledMaxSpeed = maxVelocity / trajectory[2].getMaxAchievedSpeed() * maxVelocity;
		Trajectory[] scaledTrajectory = TrajectoryGenerator.generateTrajectory(
				sequence, dt, filter1, filter2, scaledMaxSpeed, wheelbaseWidth);
		
		fileIO.openFile("Profile.txt");
		for(int i = 0; i < scaledTrajectory[0].getLength(); i++) {
			fileIO.writeToFile(scale * scaledTrajectory[0].getPoint(i).getVelocity() + "\t" + 
						scale * scaledTrajectory[0].getPoint(i).getPosition() + "\t" +
						scale * scaledTrajectory[1].getPoint(i).getVelocity() + "\t" +
						scale * scaledTrajectory[1].getPoint(i).getPosition() + "\t" +
						scale * scaledTrajectory[2].getPoint(i).getVelocity() + "\t" +
						scale * scaledTrajectory[2].getPoint(i).getPosition());
		}
		fileIO.closeFile();
		
		fileIO.openFile("Spline.txt");
		for(int i = 0; i < scaledTrajectory[0].getLength(); i++) {
			fileIO.writeToFile(scaledTrajectory[0].getPoint(i).getX() + "\t" + scaledTrajectory[0].getPoint(i).getY()
					+ "\t" + scaledTrajectory[1].getPoint(i).getX() + "\t" + scaledTrajectory[1].getPoint(i).getY()
					+ "\t" + scaledTrajectory[2].getPoint(i).getX() + "\t" + scaledTrajectory[2].getPoint(i).getY());
		}
		fileIO.closeFile();
		
		long endTime = System.currentTimeMillis();
		System.out.println((endTime - startTime));
	}

}
