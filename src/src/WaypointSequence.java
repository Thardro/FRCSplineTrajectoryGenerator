package src;

import java.util.ArrayList;

public class WaypointSequence {
	public class Waypoint {
		double x, y, theta;
		
		public Waypoint(double x, double y, double theta) {
			this.x = x;
			this.y = y;
			this.theta = theta;
		}
		
		public void setX(double x) {
			this.x = x;
		}
		
		public void setY(double y) {
			this.y = y;
		}
		
		public void setTheta(double theta) {
			this.theta = theta;
		}
		
		public double getX() {
			return x;
		}
		
		public double getY() {
			return y;
		}
		
		public double getTheta() {
			return theta;
		}
	}
	
	ArrayList<Waypoint> sequence;
	
	public WaypointSequence() {
		sequence = new ArrayList<Waypoint>();
	}
	
	public void addWaypoint(double x, double y, double theta) {
		Waypoint point = new Waypoint(x, y, theta);
		sequence.add(point);
	}
	
	public int getSize() {
		return sequence.size();
	}
	
	public Waypoint getWaypoint(int pointNumber) {
		return sequence.get(pointNumber);
	}

}
