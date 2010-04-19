package imageprocess;

import java.io.IOException;
import java.util.Vector;

public class ImageProcessorTest {
	public static void main(String[] args) throws IOException {
		String filePath = "cars\\";
		ImageProcessor ip = new ImageProcessor();
		ip.getData(filePath+"car9.jpg", 1);
	}
}
