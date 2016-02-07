package src;

public class Trajectory {

	public static class Point {
		double dt;
		double position, velocity;
		double x, y;
		double heading;
		
		public void copyPoint(Point point, double positionOffset) {
			this.dt = point.getDT();
			this.position = point.getPosition() + positionOffset;
			this.velocity = point.getVelocity();
			this.x = point.getX();
			this.y = point.getY();
			this.heading = point.getHeading();
		}
		
		public void setDT(double itp) {
			this.dt = itp;
		}
		
		public void setPosition(double position) {
			this.position = position;
		}
		
		public void setVelocity(double velocity) {
			this.velocity = velocity;
		}
		
		public void setX(double x) {
			this.x = x;
		}
		
		public void setY(double y) {
			this.y = y;
		}
		
		public void setHeading(double heading) {
			this.heading = heading;
		}
		
		public double getDT() {
			return dt;
		}
		
		public double getPosition() {
			return position;
		}
		
		public double getVelocity() {
			return velocity;
		}
		
		public double getX() {
			return x;
		}
		
		public double getY() {
			return y;
		}
		
		public double getHeading() {
			return heading;
		}
	}
	
	Point[] points;
	double maxAchievedSpeed;
	
	public Trajectory(int numPoints) {
		points = new Point[numPoints];
		for(int i = 0; i < numPoints; i++) {
			points[i] = new Point();
		}
	}
	
	public void setMaxAchievedSpeed(double maxAchievedSpeed) {
		this.maxAchievedSpeed = maxAchievedSpeed;
	}
	
	public Point getPoint(int pointNumber) {
		return points[pointNumber];
	}
	
	public int getLength() {
		return points.length;
	}
	
	public double getMaxAchievedSpeed() {
		return maxAchievedSpeed;
	}
	
}
