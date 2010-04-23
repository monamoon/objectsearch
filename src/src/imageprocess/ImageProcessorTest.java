package imageprocess;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import net.sf.javaml.core.Dataset;
import som.ImageSom;

public class ImageProcessorTest {
	public static void main(String[] args) throws IOException 
	{
		extractObjects("cars\\");
		clusterObjects("cars\\components\\");
	}
	
	public static void extractObjects(String filePath) throws IOException
	{
		ImageProcessor ip = new ImageProcessor();
		int k=1;
		for(int i=1; i<=15; i++)
		{
			BufferedImage readImage = readImageFile(filePath+"car" + i + ".jpg");
			Vector<BufferedImage> segments = ip.getSegments(readImage);

			for(int j=0;j<segments.size();j++,k++)
			{
				BufferedImage processedBuff = ip.processSegment(segments.elementAt(j));
				writeImageFile(processedBuff, filePath+"components\\object"+k+".jpg");
			}	
		}
	}
	public static void clusterObjects(String dir) throws IOException
	{
		Vector<Vector<Double>> data = new Vector<Vector<Double>>();	
		for(int k=1;k<1000;k++)
		{
			String fileName = dir+"object"+k+".jpg";
			if(!(new File(fileName)).exists())
				break;
			BufferedImage readImage = readImageFile(fileName);
			data.add(getData(readImage,k));	
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

	public static Vector<Double> getData(BufferedImage bi, double label) 
	{
		Vector<Double> dataFile = new Vector<Double>();
		for(int i=bi.getMinX();i<bi.getWidth();i++)
		{

			for(int j=bi.getMinY();j<bi.getHeight();j++){
				Color c = new Color(bi.getRGB(i, j));
				if(c.getBlue() != 0)
					dataFile.add(1.0); 
				else
					dataFile.add(0.0);
				//               System.out.println(c);
			}			
		}

		//Add class label
		dataFile.add(label);
		//	    System.out.println("size: "+dataFile.size());
		return dataFile;
	}

	public static void writeImageFile(BufferedImage bi, String filePath) throws IOException
	{
		ImageIO.write(bi,"jpg",new File(filePath));
	}
	public static BufferedImage readImageFile(String filePath) throws IOException
	{
		return ImageIO.read(new File(filePath));
	}

}
