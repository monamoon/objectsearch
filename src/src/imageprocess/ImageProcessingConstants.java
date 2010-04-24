package imageprocess;

public class ImageProcessingConstants {
	static int scaleWidth = 1000;
	static int scaleHeight = 800;
	static int inputWidth = 100;
	static int inputHeight = 80;
	static int objectThresholdLow = (scaleWidth *scaleWidth*3)/100;
	static int objectThresholdHigh = (scaleWidth *scaleWidth*50)/100;
	static double monoChromeThreshold = 0.01;
	static double srmThreshold = 3.0;
	static int bgThreshold = 100;
}
