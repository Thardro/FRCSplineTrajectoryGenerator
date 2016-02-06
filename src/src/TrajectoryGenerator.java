package src;

public class TrajectoryGenerator {
	private enum Side {
		LEFT, RIGHT;
	}
	
	public static Trajectory[] generateTrajectory(double x0, double y0, double theta0, 
			double x1, double y1, double theta1, 
			double dt, double filter1, double filter2, double maxVelocity, double wheelbaseWidth) {
		//Calculating trajectory of center of the robot
		Trajectory centerTrajectory = generateCenterTrajectory(x0, y0, theta0, x1, y1, theta1, dt, filter1, filter2, maxVelocity);
		
		//Calculating trajectory of each side using the center
		Trajectory[] trajectory = new Trajectory[3];
		trajectory[0] = generateSideFromCenter(centerTrajectory, wheelbaseWidth, Side.LEFT);
		trajectory[1] = generateSideFromCenter(centerTrajectory, wheelbaseWidth, Side.RIGHT);
		
		trajectory[2] = centerTrajectory;
		
		return trajectory;
	}
	
	private static Trajectory generateCenterTrajectory(double x0, double y0, double theta0, 
			double x1, double y1, double theta1, 
			double dt, double filter1, double filter2, double maxVelocity) {
		
		//Generating spline path for center of robot from given start and end points
		Spline centerPath = new Spline(x0, y0, theta0, x1, y1, theta1);
		System.out.println(centerPath.getLength());
		
		//Generating a basic velocity curve for the center of the robot
		VelocityProfileGenerator velProfile = new VelocityProfileGenerator(
				dt, filter1, filter2, maxVelocity, centerPath.getLength());
		
		Trajectory centerTrajectory = velProfile.calculateProfile();
		
		//Adding spline points and heading to the trajectory
		for(int i = 0; i < centerTrajectory.getLength(); i++) {
			double percentage = centerPath.getPercentFromDistance(centerTrajectory.getPoint(i).getPosition());
			
			double[] point = centerPath.getPoint(percentage);
			centerTrajectory.getPoint(i).setX(point[0]);
			centerTrajectory.getPoint(i).setY(point[1]);
			
			double heading = centerPath.getAngle(percentage);
			centerTrajectory.getPoint(i).setHeading(heading);
		}
		
		return centerTrajectory;
	}
	
	private static Trajectory generateSideFromCenter(Trajectory centerTrajectory, double wheelbaseWidth, Side side) {
		Trajectory sideTrajectory = new Trajectory(centerTrajectory.getLength());
		
		for(int i = 0; i < centerTrajectory.getLength(); i++) {
			Trajectory.Point currentCenterPoint = centerTrajectory.getPoint(i);
			Trajectory.Point currentSidePoint = sideTrajectory.getPoint(i);
			
			//Setting dt to that of the center path
			currentSidePoint.setDT(currentCenterPoint.getDT());
			
			//Calculating path of each wheel
			if(side == Side.LEFT) {
				currentSidePoint.setX(currentCenterPoint.getX() - wheelbaseWidth / 2 * 
						Math.sin(currentCenterPoint.getHeading()));
				currentSidePoint.setY(currentCenterPoint.getY() + wheelbaseWidth / 2 * 
						Math.cos(currentCenterPoint.getHeading()));
			}
			else if(side == Side.RIGHT) {
				currentSidePoint.setX(currentCenterPoint.getX() + wheelbaseWidth / 2 * 
						Math.sin(currentCenterPoint.getHeading()));
				currentSidePoint.setY(currentCenterPoint.getY() - wheelbaseWidth / 2 * 
						Math.cos(currentCenterPoint.getHeading()));
			}
			
			//Calculating position and velocity
			if(i == 0) {
				currentSidePoint.setPosition(0);
				currentSidePoint.setVelocity(0);
			}
			else {
				double distanceSide = Math.sqrt(
						Math.pow(currentSidePoint.getX() - sideTrajectory.getPoint(i - 1).getX(), 2)
						+ Math.pow(currentSidePoint.getY() - sideTrajectory.getPoint(i - 1).getY(), 2));
				currentSidePoint.setPosition(sideTrajectory.getPoint(i - 1).getPosition() + distanceSide);
				currentSidePoint.setVelocity(distanceSide / (currentSidePoint.getDT() / 1000) );
				
				double distanceCenter = Math.sqrt(
						Math.pow(currentCenterPoint.getX() - centerTrajectory.getPoint(i - 1).getX(), 2)
						+ Math.pow(currentCenterPoint.getY() - centerTrajectory.getPoint(i - 1).getY(), 2));
				currentCenterPoint.setPosition(centerTrajectory.getPoint(i - 1).getPosition() + distanceCenter);
				currentCenterPoint.setVelocity(distanceCenter / (currentCenterPoint.getDT() / 1000) );
			}
		}
		
		return sideTrajectory;
	}
}
