package imageprocess;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import net.sf.javaml.core.Dataset;
import som.ImageSom;

public class ImageProcessorTest {
	public static void main(String[] args) throws IOException {
		Vector<Vector<Double>> data = new Vector<Vector<Double>>();
		String filePath = "cars\\";
		ImageProcessor ip = new ImageProcessor();
		double k=0;
		for(int i=1; i<=14; i++)
		{
			BufferedImage readImage = ip.readImageFile(filePath+"car" + i + ".jpg");
			Vector<BufferedImage> segments = ip.getSegments(readImage);
			
			for(int j=0;j<segments.size();j++)
			{
				BufferedImage processedBuff = ip.processSegment(segments.elementAt(j));
				ip.writeImageFile(processedBuff, filePath+"components/object_"+i+"_"+j+".jpg");
				k=k+1;
				data.add(ip.getData(processedBuff, k));	
			}
			System.out.println("Done processing "+filePath+"car" + i + ".jpg");
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
