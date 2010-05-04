package imageprocess;

public class ImageProcessingConstants {
	static private final int scaleWidth = 1000;
	static private final int scaleHeight = 800;
	static private final int inputWidth = 100;
	static private final int inputHeight = 80;
	static private final int objectThresholdLow = (scaleWidth *scaleWidth*3)/100;
	static private final int objectThresholdHigh = (scaleWidth *scaleWidth*50)/100;
	static private final double monoChromeThreshold = 0.01;
	static private final double srmThreshold = 3.0;
	static private final int bgThreshold = 100;
	static private final int centerThreshold = 100;
	
	public static int getScaleWidth(){
		return scaleWidth;
	}
	
	public static int getScaleheight() {
		return scaleHeight;
	}
	public static int getInputwidth() {
		return inputWidth;
	}
	public static int getInputheight() {
		return inputHeight;
	}
	public static int getObjectthresholdlow() {
		return objectThresholdLow;
	}
	public static int getObjectthresholdhigh() {
		return objectThresholdHigh;
	}
	public static double getMonochromethreshold() {
		return monoChromeThreshold;
	}
	public static double getSrmthreshold() {
		return srmThreshold;
	}
	public static int getBgthreshold() {
		return bgThreshold;
	}
	public static int getCenterthreshold() {
		return centerThreshold;
	}
}
