package src;

public class Main {
	
	static FileIO fileIO = new FileIO();

	//Start point
	static double x0 = 0, y0 = 0, theta0 = 0;
	
	//End point
	static double x1 = 10, y1 = 5, theta1 = Math.PI / 2;
	
	public static void main(String[] args) {
		Spline trajectory = new Spline(x0, y0, theta0, x1, y1, theta1);

		fileIO.openFile("Spline.txt");
		for(double i = 0; i <= 500; i++) {
			double x = i / 50.0;
			double y = trajectory.getY(x);
			fileIO.writeToFile(x + "\t" + y);
		}
	}

}
