package src;

public class Spline {

	double x0, y0, x1, y1;
	
	//y = ax^5 + bx^4 + cx^3 + ex, form of quintic spline
	double a, b, c, e;
	
	//Offsets, spline is generated with start at origin and end on x-axis
	double xOffset, yOffset, thetaOffset;
	
	double straightLength;
	
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
		System.out.println("thetaOffset = " + thetaOffset + ", offsetTheta0 = " + offsetTheta0 + ","
				+ " offsetTheta1 = " + offsetTheta1);
		
		//Calculating derivative at start and end point
		double dydx0 = Math.tan(offsetTheta0);
		double dydx1 = Math.tan(offsetTheta1);
		System.out.println(dydx1);
		
		//Calculating constants
		a = -3 * (dydx0 + dydx1) / Math.pow(straightLength, 4);
		b = (8 * dydx0 + 7 * dydx1) / Math.pow(straightLength, 3);
		c = (-6 * dydx0 - 4 * dydx1) / Math.pow(straightLength, 2);
		e = dydx0;
		
		System.out.println(a + "*x^5 + " + b + "*x^4 + " + c + "*x^3 + " + e + "*x");
	}
	
	public double getY(double x) {
		double xScalar = straightLength / Math.abs(x1 - x0);
		double rotatedX = x * xScalar;
		double rotatedY = a * Math.pow(rotatedX, 5) + b * Math.pow(rotatedX, 4)
							+ c * Math.pow(rotatedX, 3) + e * rotatedX;
		double y = Math.sin(thetaOffset) * rotatedX + Math.cos(thetaOffset) * rotatedY + yOffset;
		
		return y;
	}
	
}
