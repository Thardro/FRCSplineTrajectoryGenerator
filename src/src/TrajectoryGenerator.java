package src;

public class TrajectoryGenerator {
	private enum Side {
		LEFT, RIGHT;
	}
	
	static double maxAchievedVelocity = 0;
	
	public static Trajectory[] generateTrajectory(WaypointSequence waypointSequence, double dt, double filter1, 
			double filter2, double maxVelocity, double wheelbaseWidth) {
		//Calculating trajectory of center of the robot
		int numSegments = waypointSequence.getSize() - 1;
		Trajectory[] centerTrajectorySequence = new Trajectory[waypointSequence.getSize() - 1];
		int numPoints = 0;
		
		//Generating a center trajectory for each pair of waypoints
		double startX = waypointSequence.getWaypoint(0).getX();
		double startY = waypointSequence.getWaypoint(0).getY();
		double startTheta = waypointSequence.getWaypoint(0).getTheta();
		WaypointSequence.Waypoint previousPoint = new WaypointSequence.Waypoint(startX, startY, startTheta);
		for(int i = 0; i < numSegments; i++) {
			WaypointSequence.Waypoint startPoint = previousPoint;
			WaypointSequence.Waypoint endPoint = waypointSequence.getWaypoint(i + 1);
			centerTrajectorySequence[i] = generateCenterTrajectory(
					startPoint, endPoint, dt, filter1, filter2, maxVelocity);
			int currentLength = centerTrajectorySequence[i].getLength();	
			
			//Setting the start of the next segment to the actual end of the previous one to account for error
			double endX = centerTrajectorySequence[i].getPoint(currentLength - 1).getX();
			double endY = centerTrajectorySequence[i].getPoint(currentLength - 1).getY();
			previousPoint.setX(endX);
			previousPoint.setY(endY);
			previousPoint.setTheta(endPoint.getTheta());
			
			numPoints += currentLength;
		}
		
		//Adding the center trajectories into one
		Trajectory centerTrajectory = new Trajectory(numPoints);
		double positionOffset = 0;
		int pointOffset = 0;
		for(int i = 0; i < numSegments; i++) {
			Trajectory currentSegment = centerTrajectorySequence[i];
			for(int n = 0; n < currentSegment.getLength(); n++) {
				Trajectory.Point currentPoint = currentSegment.getPoint(n);
				centerTrajectory.getPoint(n + pointOffset).copyPoint(currentPoint, positionOffset);
			}
			positionOffset += currentSegment.getPoint(currentSegment.getLength() - 1).getPosition();
			pointOffset += currentSegment.getLength();
		}
		
		//Calculating trajectory of each side using the center
		Trajectory[] trajectory = new Trajectory[3];
		trajectory[0] = generateSideFromCenter(centerTrajectory, wheelbaseWidth, Side.LEFT);
		trajectory[1] = generateSideFromCenter(centerTrajectory, wheelbaseWidth, Side.RIGHT);
		trajectory[2] = centerTrajectory;
		
		trajectory[2].setMaxAchievedSpeed(maxAchievedVelocity);
		
		return trajectory;
	}
	
	private static Trajectory generateCenterTrajectory(
			WaypointSequence.Waypoint startPoint, WaypointSequence.Waypoint endPoint, 
			double dt, double filter1, double filter2, double maxVelocity) {
		
		double x0 = startPoint.getX();
		double y0 = startPoint.getY();
		double theta0 = startPoint.getTheta();
		double x1 = endPoint.getX();
		double y1 = endPoint.getY();
		double theta1 = endPoint.getTheta();
		
		//Generating spline path for center of robot from given start and end points
		Spline centerPath = new Spline(x0, y0, theta0, x1, y1, theta1);
		
		//Generating a basic velocity curve for the center of the robot
		VelocityProfileGenerator velProfile = new VelocityProfileGenerator(
				dt, filter1, filter2, maxVelocity, centerPath.getLength());
		
		Trajectory centerTrajectory = velProfile.calculateProfile();
		
		//Adding spline points, velocity, and heading to the trajectory
		for(int i = 0; i < centerTrajectory.getLength(); i++) {
			double percentage = centerPath.getPercentFromDistance(centerTrajectory.getPoint(i).getPosition());
			Trajectory.Point currentCenterPoint = centerTrajectory.getPoint(i);
			
			//Calculating point
			double[] point = centerPath.getPoint(percentage);
			centerTrajectory.getPoint(i).setX(point[0]);
			centerTrajectory.getPoint(i).setY(point[1]);
			
			//Calculating heading
			double heading = centerPath.getAngle(percentage);
			centerTrajectory.getPoint(i).setHeading(heading);
			
			//Calculating velocity
			if(i == 0) {
				currentCenterPoint.setPosition(0);
				currentCenterPoint.setVelocity(0);
			}
			else {
				double distanceCenter = Math.sqrt(
						Math.pow(currentCenterPoint.getX() - centerTrajectory.getPoint(i - 1).getX(), 2)
						+ Math.pow(currentCenterPoint.getY() - centerTrajectory.getPoint(i - 1).getY(), 2));
							
					currentCenterPoint.setPosition(centerTrajectory.getPoint(i - 1).getPosition() + distanceCenter);
					currentCenterPoint.setVelocity(distanceCenter / (currentCenterPoint.getDT() / 1000) );
			}
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
				
				if(currentSidePoint.getVelocity() > maxAchievedVelocity) {
					maxAchievedVelocity = currentSidePoint.getVelocity();
				}
			}
			
		}
		
		return sideTrajectory;
	}
}
