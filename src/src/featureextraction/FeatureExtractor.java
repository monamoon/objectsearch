package featureextraction;

import imageprocess.ImageProcessingConstants;
import imageprocess.ImageProcessor;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.Vector;

import util.Utility;

public class FeatureExtractor {
	Vector<Double> dataFile = new Vector<Double>();
	public static Vector<Double> getFeatureVector(BufferedImage bi, double label, FeatureType featuretype) throws InterruptedException
	{
		boolean isValid = false;
		FeatureExtractor fe = new FeatureExtractor();
		switch(featuretype)
		{
			case fullbitmap:
				fe.getFullBitmap(bi);
				break;
			
			case corners:
				fe.addCornerVector(bi);
				break;
				
			case lines:
				fe.addLineVector(bi);
				break;
				
			case curves:
				fe.addCircleVector(bi,10);
				fe.addCircleVector(bi,20);
				fe.addCircleVector(bi,30);
				break;
		}
		for(double current : fe.dataFile){
			if(current != 0.0)
				isValid = true;
		}
		if(isValid){
			fe.dataFile.add(label);
			return fe.dataFile;
		}
		return null;
	}
	public void addLineVector(BufferedImage bi) throws InterruptedException
	{
		int width = bi.getWidth();
		int height = bi.getHeight();
		int []orig=new int[width*height];
		PixelGrabber grabber = new PixelGrabber(bi, 0, 0, width, height, orig, 0, width);
		grabber.grabPixels();
		
		sobel sobelObject = new sobel();
		sobelObject.init(orig,width,height);
		orig = sobelObject.process();
		
		double direction[] = new double[width*height];
		direction=sobelObject.getDirection();
		
		nonMaxSuppression nonmaxObject = new nonMaxSuppression();
		nonmaxObject.init(orig,direction,width,height);
		orig = nonmaxObject.process();
	
//		hystThresh hystThreshObject = new hystThresh();
//		hystThreshObject.init(orig,width,height, 25,50);
//		orig = hystThreshObject.process();
		
		Lines lines = new Lines();
		lines.init(orig, width, height);
		orig = lines.process();
		
		dataFile.addAll(lines.getResults());
	}
	
	public void addCircleVector(BufferedImage bi, int radius) throws InterruptedException
	{
		int width = bi.getWidth();
		int height = bi.getHeight();
		int []orig=new int[width*height];
		PixelGrabber grabber = new PixelGrabber(bi, 0, 0, width, height, orig, 0, width);
		grabber.grabPixels();
		
		sobel sobelObject = new sobel();
		sobelObject.init(orig,width,height);
		orig = sobelObject.process();
		
		BufferedImage fullImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Image piximg = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, orig,0,width));
		fullImage.getGraphics().drawImage(piximg, 0, 0, null);
		
				
		double direction[] = new double[width*height];
		direction=sobelObject.getDirection();
		
		nonMaxSuppression nonmaxObject = new nonMaxSuppression();
		nonmaxObject.init(orig,direction,width,height);
		orig = nonmaxObject.process();
	
//		hystThresh hystThreshObject = new hystThresh();
//		hystThreshObject.init(orig,width,height, 25,50);
//		orig = hystThreshObject.process();
		
		Circles circles = new Circles();
		circles.init(orig, width, height,radius);
		orig = circles.process();
		
		dataFile.addAll(circles.getResults());
    }
	public void addCornerVector(BufferedImage bi) throws InterruptedException
	{

		
		int []orig=new int[bi.getWidth()* bi.getHeight()];
		PixelGrabber grabber = new PixelGrabber(bi, 0, 0, bi.getWidth(),  bi.getHeight(), orig, 0, bi.getWidth());
		grabber.grabPixels();
	
		Corners harrisObj = new Corners();
		harrisObj.init(orig,bi.getWidth(), bi.getHeight(), 0.04);
		orig=harrisObj.process();
		

		int sw = ImageProcessingConstants.getInputwidth();
		int sh = ImageProcessingConstants.getInputheight();
		for (int i=0;i<sw*sh;i++)
			dataFile.add(0.0);
		
		for(int i=0;i<bi.getWidth();i++)
		{
			for(int j=0;j<bi.getHeight();j++)
			{
				if(orig[i*bi.getWidth()+j]>0)
					dataFile.set(i + j/sh, dataFile.get(i + j/sh)+1);
			}			
		}

	}
	public void getFullBitmap(BufferedImage bi) throws InterruptedException 
	{
		
		int width = bi.getWidth();
		int height = bi.getHeight();
		
		int []orig=new int[width*height];
		PixelGrabber grabber = new PixelGrabber(bi, 0, 0, width, height, orig, 0, width);
		grabber.grabPixels();
		sobel sobelObject = new sobel();
		sobelObject.init(orig,width,height);
		orig = sobelObject.process();
		
		dataFile.addAll(Utility.createImage(orig, width, height));
		
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
