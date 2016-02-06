package src;

public class Trajectory {

	public static class Point {
	double dt;
		double position, velocity, acceleration;
		double x, y;
		double heading;
		
		public void setDT(double itp) {
			this.dt = itp;
		}
		
		public void setPosition(double position) {
			this.position = position;
		}
		
		public void setVelocity(double velocity) {
			this.velocity = velocity;
		}
		
		public void setAcceleration(double acceleration) {
			this.acceleration = acceleration;
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
		
		public double getAcceleration() {
			return acceleration;
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
	
	public Trajectory(int numPoints) {
		points = new Point[numPoints];
		for(int i = 0; i < numPoints; i++) {
			points[i] = new Point();
		}
	}
	
	public Point getPoint(int pointNumber) {
		return points[pointNumber];
	}
	
	public int getLength() {
		return points.length;
	}
	
}
