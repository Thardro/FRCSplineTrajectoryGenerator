package src;

public class Spline {

	double x0, y0, x1, y1;
	
	//y = ax^5 + bx^4 + cx^3 + ex, form of quintic spline
	double a, b, c, e;
	
	//Offsets, spline is generated with start at origin and end on x-axis
	double xOffset, yOffset, thetaOffset;
	
	double straightLength;
	
	double arcLength = 0;
	
	public Spline(double x0, double y0, double theta0,
			double x1, double y1, double theta1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		
		//Getting offsets
		xOffset = x0;
		yOffset = y0;
		straightLength = Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
		
		thetaOffset = Math.atan2(y1 - y0, x1 - x0);
		double offsetTheta0 = theta0 - thetaOffset;
		double offsetTheta1 = theta1 - thetaOffset;
		
		//Calculating derivative at start and end point
		double dydx0 = Math.tan(offsetTheta0);
		double dydx1 = Math.tan(offsetTheta1);
		
		//Calculating constants
		a = -3 * (dydx0 + dydx1) / Math.pow(straightLength, 4);
		b = (8 * dydx0 + 7 * dydx1) / Math.pow(straightLength, 3);
		c = (-6 * dydx0 - 4 * dydx1) / Math.pow(straightLength, 2);
		e = dydx0;
	}
	
	public String getFormula() {
		return a + "*x^5 + " + b + "*x^4 + " + c + "*x^3 + " + e + "*x";
	}
	
	public double getLength() {
		if(arcLength == 0) {
			calculateLength();
		}
		
		return arcLength;
	}
	
	public double getPercentFromDistance(double distance) {
		return distance / getLength();
	}
	
	public double[] getPoint(double percent) {
		double[] point = new double[2];
		
		double rotatedX = percent * straightLength;
		double rotatedY = a * Math.pow(rotatedX, 5) + b * Math.pow(rotatedX, 4)
			+ c * Math.pow(rotatedX, 3) + e * rotatedX;
		
		point[0] = rotatedX * Math.cos(thetaOffset) - rotatedY * Math.sin(thetaOffset) + xOffset;
		point[1] = rotatedX * Math.sin(thetaOffset) + rotatedY * Math.cos(thetaOffset) + yOffset;
		
		return point;
	}
	
	public double getDerivative(double percentage) {
		double percentX = straightLength * percentage;
		double dydx = 5 * a * Math.pow(percentX, 4) + 4 * b * Math.pow(percentX, 3)
			+ 3 * c * Math.pow(percentX, 2) + e;
		
		return dydx;
	}
	
	public double getAngle(double percentage){
		double angle = Math.atan(getDerivative(percentage)) + thetaOffset;
		
		return angle;
	}
	
	//Calculating arclength using a riemann sum and a high number of samples
	private void calculateLength() {
		final int numSamples = 100000;
		double dydx;
		
		for(int i = 1; i <= numSamples; i++) {
			dydx = getDerivative((double) i / numSamples);
			arcLength += Math.sqrt(1 + Math.pow(dydx, 2));
		}
		arcLength *= straightLength / numSamples;
	}
	
}
