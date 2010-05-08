package featureextraction;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Vector;


public class FeatureExtractor {

	public static Vector<Double> getFullBitmap(BufferedImage bi, double label) 
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
	public static Vector<Double> getMD(BufferedImage bi, double label) 
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

	

}
