package imageprocess;

import java.io.IOException;
import java.util.Vector;

import net.sf.javaml.core.Dataset;
import som.ImageSom;

public class ImageProcessorTest {
	public static void main(String[] args) throws IOException {
		Vector<Vector<Double>> data = new Vector<Vector<Double>>();
		String filePath = "cars\\";
		ImageProcessor ip = new ImageProcessor();
		
		for(int i=1; i<11; i++)
			data.add(ip.getData((filePath+"car" + i + ".jpg"), i));
		for(int i=11; i<15; i++){
			data.add(ip.getData((filePath+"car" + i + ".jpg"), i));
		}
		System.out.println("Output size: "+data.size());
		ImageSom som = new ImageSom(data);
		Dataset[] datasom = som.cluster();
	
		for(int i=0; i<datasom.length; i++){
			Dataset curr = datasom[i];
			System.out.println("Cluster "+i+": ");
			for(int j=0; j<curr.size(); j++)
				System.out.println(curr.get(j).classValue());
		}
	}
}
