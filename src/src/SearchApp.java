import imageprocess.ImageProcessingConstants;
import imageprocess.ImageProcessor;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import featureextraction.Circles;
import featureextraction.Corners;
import featureextraction.FeatureExtractor;
import featureextraction.FeatureType;
import featureextraction.Lines;
import featureextraction.hystThresh;
import featureextraction.nonMaxSuppression;
import featureextraction.sobel;



public class SearchApp {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		BufferedImage buf = ImageIO.read(new File("C:\\cars\\8.jpg"));
		BufferedImage buf1 = getLineImage(buf);
//		ImageIO.write(buf1, "jpg", new File("C:\\cars\\8_line.jpg"));
//		BufferedImage buf21 = getCircleImage(buf,10);
//		ImageIO.write(buf21, "jpg", new File("C:\\cars\\8_circle1.jpg"));
//		BufferedImage buf22 = getCircleImage(buf,20);
//		ImageIO.write(buf22, "jpg", new File("C:\\cars\\8_circle2.jpg"));
//		BufferedImage buf23 = getCircleImage(buf,30);
//		ImageIO.write(buf23, "jpg", new File("C:\\cars\\8_circle3.jpg"));
//		
//		BufferedImage buf3 = getFullBitmap(buf);
//		ImageIO.write(buf3, "jpg", new File("C:\\cars\\8_mono.jpg"));
		BufferedImage buf4 = getCornerImage(buf);
		ImageIO.write(buf4, "jpg", new File("C:\\cars\\8_corner.jpg"));
		
	}
	public static BufferedImage getFullBitmap(BufferedImage bi) throws InterruptedException 
	{
		ImageProcessor ip = new ImageProcessor();
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
		
		BufferedImage scaledImage = ip.getScaledImage(fullImage, ImageProcessingConstants.getInputwidth(),ImageProcessingConstants.getInputheight());
		BufferedImage monoImage = ip.getMonoChromeImage(scaledImage, ImageProcessingConstants.getMonochromethreshold());
		
		return monoImage;
	}
	public static BufferedImage getCircleImage(BufferedImage bi,int radius) throws InterruptedException, IOException
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
		
		hystThresh hystThreshObject = new hystThresh();
		hystThreshObject.init(orig,width,height, 25,50);
		orig = hystThreshObject.process();
		
		Circles circles = new Circles();
		circles.init(orig, width, height,radius);
		orig = circles.process();
		
		BufferedImage circleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		piximg = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, orig,0,width));
		circleImage.getGraphics().drawImage(piximg, 0, 0, null);
		return circleImage; 
	}
	public static BufferedImage getImage(int []orig,int width,int height)
	{
		BufferedImage buff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Image piximg = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, orig, 0, width));
		buff.getGraphics().drawImage(piximg, 0, 0, null);
		return buff;
	}
	public static BufferedImage getCornerImage(BufferedImage bi) throws InterruptedException
	{
		int width = bi.getWidth();
		int height = bi.getHeight();
		int []orig=new int[width*height];
		PixelGrabber grabber = new PixelGrabber(bi, 0, 0, width, height, orig, 0, width);
		grabber.grabPixels();
	
		Corners harrisObj = new Corners();
		harrisObj.init(orig,width, height, 0.2);
		orig=harrisObj.process();
		orig = overlayImage(orig,bi,width,height);
		BufferedImage cornerImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Image piximg = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, orig,0,width));
		cornerImage.getGraphics().drawImage(piximg, 0, 0, null);
		return cornerImage; 
	
	}
	private static int[] overlayImage(int[] input, Image base, int width, int height){
		
		int[] myImage=new int[width*height];
		
		PixelGrabber grabber = new PixelGrabber(base, 0, 0, width, height, myImage, 0, width);
		try {
			grabber.grabPixels();
		}
		catch(InterruptedException e2) {
			System.out.println("error: " + e2);
		}


		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if((input[y*width+x]&0xff)>0)
					myImage[y*width+x]= 0xffff0000;
			}
		}

		return myImage;

	}

	public static BufferedImage getLineImage(BufferedImage bi) throws InterruptedException
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
	
		hystThresh hystThreshObject = new hystThresh();
		hystThreshObject.init(orig,width,height, 25,50);
		orig = hystThreshObject.process();
		
		Lines lines = new Lines();
		lines.init(orig, width, height);
		orig = lines.process();
		
		BufferedImage lineImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for(int x=0;x<width*height;x++)
			lineImage.setRGB(x/height, x%height, orig[x]);
		Image piximg = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width,height, orig, 0, width));
		lineImage.getGraphics().drawImage(piximg, 0, 0, null);
		return lineImage; 
		
    }

/*		Vector<Vector<Double>> data = new Vector<Vector<Double>>();
		String filePath = "cars\\";
		ImageProcessor ip = new ImageProcessor();
		for(int i=1; i<11; i++)
			data.add(ip.getData((filePath+"car" + i + ".jpg"), i));
		for(int i=11; i<15; i++){
			data.add(ip.getData((filePath+"car" + i + ".jpg"), i));
		}
//		String dataFile = filePath + "inputSOM.data";
//		Utility.writeData(dataFile, data);
		
		ImageSom som = new ImageSom(data);
		Dataset[] datasom = som.cluster();
	
		for(int i=0; i<datasom.length; i++){
			Dataset curr = datasom[i];
			System.out.println("Cluster "+i+": ");
			for(int j=0; j<curr.size(); j++)
				System.out.println(curr.get(j).classValue());
		}
*/
	
}
