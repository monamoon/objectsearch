package imageprocess;

public class ImageProcessingConstants {
	static final int scaleWidth = 1000;
	static final int scaleHeight = 800;
	static final int inputWidth = 100;
	static final int inputHeight = 80;
	static final int objectThresholdLow = (scaleWidth *scaleWidth*3)/100;
	static final int objectThresholdHigh = (scaleWidth *scaleWidth*50)/100;
	static final double monoChromeThreshold = 0.01;
	static final double srmThreshold = 3.0;
	static final int bgThreshold = 100;
	static final int centerThreshold = 100;
}
