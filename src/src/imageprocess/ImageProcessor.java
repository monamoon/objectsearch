package imageprocess;
/*import java.awt.Color;

import java.awt.Image;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.*;

public class ImageProcessor {

	private Vector<Double> dataFile;
	private int width = 100;
	private int height = 80;

	public ImageProcessor(){
		setDataFile(new Vector<Double>());
	}

	public Vector<Double> getData(String filePath, double label) throws IOException
	{

		BufferedImage image = ImageIO.read(new File(filePath));
        BufferedImage edgeBuff = sobelEdgeDetection(image);

        Image scaledImage =  edgeBuff.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage scaledBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        scaledBuff.createGraphics().drawImage(scaledImage,0,0,null);



//        BufferedImage monoImage = new BufferedImage(scaledBuff.getWidth(), scaledBuff.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
//        
//        monoImage.createGraphics().drawImage(scaledBuff,0,0,null);



	    for(int i=scaledBuff.getMinX();i<scaledBuff.getWidth();i++)
		{

			for(int j=scaledBuff.getMinY();j<scaledBuff.getHeight();j++){
				Color color = new Color(scaledBuff.getRGB(i, j));
                double val = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();
                if(val < 100)
                	scaledBuff.setRGB(i, j, Color.BLACK.getRGB());
                else
                	scaledBuff.setRGB(i, j, Color.WHITE.getRGB());
			}

		}

	    ImageIO.write(scaledBuff, "png",new File(filePath+".png"));


		Vector<Double> dataFile = new Vector<Double>();
	    for(int i=scaledBuff.getMinX();i<scaledBuff.getWidth();i++)
		{

			for(int j=scaledBuff.getMinY();j<scaledBuff.getHeight();j++){
				Color c = new Color(scaledBuff.getRGB(i, j));
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
	    setDataFile(dataFile);

	    //REMOVE!!
//	    for(Vector<String> curr : datafile){
//	    	for(String item : curr)
//	    		System.out.print(item);
//	    	System.out.println();
//	    }

	    //Print to file


		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		ImageIO.write(scaledBuff, "pnm", bas);
		byte[] data = bas.toByteArray();
//		System.out.println(data.length);
	//	for (int i=0;i<data.length;i++)
		//	System.out.println(data[i]);

		return dataFile;
	}
	 Sobel.js
 * Kas Thomas
 * 31 January 2010
 * Public domain.
 *
 * An edge-detection routine using
 * Java Advanced Imaging.
 *
 * Requires Java Advanced Imaging library:
 * http://java.sun.com/products/java-media/jai/current.html
 *
 * Run this file using ImageMunger:
 * http://asserttrue.blogspot.com/2010/01/simple-java-class-for-running-scripts.html
 *


	BufferedImage sobelEdgeDetection(BufferedImage img) 
	{
		BufferedImage edged = new BufferedImage(img.getWidth(),img.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		float[] hx = new float[]{-1,0,1,
				-2,0,2,
				-1,0,1};

		float[] hy = new float[]{-1,-2,-1,
				0, 0, 0,
				1, 2, 1};

		int[] rgbX = new int[3]; int[] rgbY = new int[3];

		//ignore border pixels strategy
		for(int x = 1; x < img.getWidth()-1; x++)
			for(int y = 1; y < img.getHeight()-1; y++) {
				convolvePixel(hx,3,3, img, x, y, rgbX);
				convolvePixel(hy,3,3, img, x, y, rgbY);

				//instead of using sqrt function for eculidean distance
				//just do an estimation
				int r = Math.abs(rgbX[0]) + Math.abs(rgbY[0]);
				int g = Math.abs(rgbX[1]) + Math.abs(rgbY[1]);
				int b = Math.abs(rgbX[2]) + Math.abs(rgbY[2]);

				//range check
				if(r > 255) r = 255;
				if(g > 255) g = 255;
				if(b > 255) b = 255;
 
				edged.setRGB(x, y,(r<<16)|(g<<8)|b);
			}
		return edged;
	}
	private static int[] convolvePixel(float[] kernel, int kernWidth, int kernHeight,
			BufferedImage src, int x, int y, int[] rgb) {
		if(rgb == null) rgb = new int[3];

		int halfWidth = kernWidth/2;
		int halfHeight = kernHeight/2;

		this algorithm pretends as though the kernel is indexed from -halfWidth 
 *to halfWidth horizontally and -halfHeight to halfHeight vertically.  
 *This makes the center pixel indexed at row 0, column 0.

		for(int component = 0; component < 3; component++) {
			float sum = 0;
			for(int i = 0; i < kernel.length; i++) {
				int row = (i/kernWidth)-halfWidth;  //current row in kernel
				int column = (i-(kernWidth*row))-halfHeight; //current column in kernel

				//range check
				if(x-row < 0 || x-row > src.getWidth()) continue;
				if(y-column < 0 || y-column > src.getHeight()) continue;

				int srcRGB =src.getRGB(x-row,y-column);
				sum = sum + kernel[i]*((srcRGB>>(16-8*component))&0xff);
			}
			rgb[component] = (int) sum;
		}
		return rgb;
	}

	public void setDataFile(Vector<Double> dataFile) {
		this.dataFile = dataFile;
	}

	public Vector<Double> getDataFile() {
		return dataFile;
	}

}
 */

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.PlanarImage;

class bgColor
{
	Color color;
	public Color getColor() {
		return color;
	}
	int count;
	bgColor(Color c)
	{
		color = c;
		count = 1;
	}
	public void addPixel()
	{
		count = count + 1;
	}
	public int getCount()
	{
		return count;
	}
};
public class ImageProcessor {

	int width = ImageProcessingConstants.getInputwidth();
	int height = ImageProcessingConstants.getInputheight();
	private int normalWidth = ImageProcessingConstants.getScaleWidth();
	private int normalHeight = ImageProcessingConstants.getScaleheight();
	double threshold = ImageProcessingConstants.getMonochromethreshold();
	public BufferedImage removeBG(BufferedImage bi)
	{
		Vector<bgColor> bgcolors = new Vector<bgColor>();
		
		for(int i=0,x=0;i<normalWidth;i++)
		{
			Color c = new Color(bi.getRGB(i,0));
	
			for(x=0;x<bgcolors.size();x++)
			{
				if (c.equals(bgcolors.elementAt(x).getColor()))
				{
					bgcolors.elementAt(x).addPixel();
					break;
				}
			}
			if(x==bgcolors.size())
				bgcolors.add(new bgColor(c));	
			
			
			c = new Color(bi.getRGB(i,normalHeight-1));
			
			for(x=0;x<bgcolors.size();x++)
			{
				if (c.equals(bgcolors.elementAt(x).getColor()))
				{
					bgcolors.elementAt(x).addPixel();
					break;
				}
			}
			if(x==bgcolors.size())
				bgcolors.add(new bgColor(c));
		
		}
		for(int j=0,x=0;j<normalHeight;j++)
		{
			Color c = new Color(bi.getRGB(0,j));
			
			for(x=0;x<bgcolors.size();x++)
			{
				if (c.equals(bgcolors.elementAt(x).getColor()))
				{
					bgcolors.elementAt(x).addPixel();
					break;
				}
			}
			if(x>bgcolors.size())
				bgcolors.add(new bgColor(c));
			
			c = new Color(bi.getRGB(normalWidth-1,j));
			
			for(x=0;x<bgcolors.size();x++)
			{
				if (c.equals(bgcolors.elementAt(x).getColor()))
				{
					bgcolors.elementAt(x).addPixel();
					break;
				}
			}
			if(x>bgcolors.size())
				bgcolors.add(new bgColor(c));
		}
				
		BufferedImage buff = bi;
		for(int k=0;k<bgcolors.size();k++)
		{
			
			bgColor bgcolor = bgcolors.elementAt(k);
			if(bgcolor.getCount()<ImageProcessingConstants.getBgthreshold())
				continue;
			
			int rgbcolor = bgcolor.getColor().getRGB();
			int rgbblack = Color.BLACK.getRGB();
			for(int i=0;i<getNormalWidth();i++)
				for(int j=0;j<normalHeight;j++)
					if(buff.getRGB(i,j)==rgbcolor)
						buff.setRGB(i, j, rgbblack);
		}
		return buff;
	}

	public BufferedImage getMonoChromeImage(BufferedImage bi, double threshold)
	{
		BufferedImage bufferedImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int i=bi.getMinX();i<bi.getWidth();i++)
		{
			for(int j=bi.getMinY();j<bi.getHeight();j++)
			{
				Color color = new Color(bi.getRGB(i, j));
				double rgbValue = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();
				if(rgbValue < threshold * 255)
					bufferedImage.setRGB(i, j, Color.BLACK.getRGB());
				else
					bufferedImage.setRGB(i, j, Color.WHITE.getRGB());
			}
		}
		return bufferedImage;
	}
	public BufferedImage getScaledImage(BufferedImage bi, int width, int height)
	{
		try
		{
			Image scaledImage =  bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			bufferedImage.createGraphics().drawImage(scaledImage,0,0,null);
			return bufferedImage;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public Vector<BufferedImage> getSegments(BufferedImage normalbi, BufferedImage segBi) throws IOException
	{
		Vector<Segment> segments = new Vector<Segment>();
		
		for(int i=segBi.getMinX();i<segBi.getWidth();i++)
		{
			for(int j=segBi.getMinY();j<segBi.getHeight();j++)
			{
				Color color = new Color(segBi.getRGB(i, j));
				int x;
				for(x=0;x<segments.size();x++)
					if(segments.elementAt(x).getColor().equals(color))
						break;

				if(x==segments.size())
				{
					Segment curSegment = new Segment(color);
					curSegment.setPixel(i,j,normalbi.getRGB(i,j));
					segments.add(curSegment);
				}
				else
				{
					segments.elementAt(x).setPixel(i, j,normalbi.getRGB(i,j));
				}
			}	
		}
		Vector<BufferedImage> segmentBuff = new Vector<BufferedImage>();

		for(int i=0;i<segments.size();i++)
		{
			Segment segment = segments.elementAt(i); 

			if(segment.getCount()>ImageProcessingConstants.getObjectthresholdlow() && segment.getCount()<ImageProcessingConstants.getObjectthresholdhigh() && !segment.isCorner)
			{
				BufferedImage segBuff = segment.getImage();
				segmentBuff.add(segBuff);
				//segmentBuff.add(getMirrorImage(segBuff));
			}	
				
		}
		return segmentBuff;
	}
	public BufferedImage getMasterImage(BufferedImage normalbi, Vector<BufferedImage> segments)
	{
		if(segments.size()==0)
			return null;
		BufferedImage masterImage = new BufferedImage(normalbi.getWidth(), normalbi.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<segments.size();i++)
		{
			BufferedImage segBuff = segments.elementAt(i); 
			
			for(int j=0;j<masterImage.getWidth();j++)
			{
				for(int k=0;k<masterImage.getHeight();k++)
				{
					if(segBuff.getRGB(j,k)!=Color.BLACK.getRGB())
						masterImage.setRGB(j,k,normalbi.getRGB(j,k));	
				}
			}
		}	
		return masterImage;
	}
	public BufferedImage getMirrorImage(BufferedImage bi)
	{
	  BufferedImage buff = this.getScaledImage(bi, bi.getWidth(), bi.getHeight());
	  for(int j=0;j<buff.getHeight();j++)
		  for(int i=0;i<buff.getWidth();i++)
		  	  buff.setRGB(i,j,bi.getRGB(bi.getWidth()-i-1,j));
	  return buff;
	}
	public BufferedImage getSegmentedImage(BufferedImage bi) throws IOException
	{
		SRM srm = new SRM();
		srm.processImage(bi);
		Image segImage = srm.getImgseg();
		BufferedImage bufferedImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
		bufferedImage.createGraphics().drawImage(segImage,0,0,null);
		return bufferedImage;
	}
	public void constructDataset(BufferedImage bi, String fileName)
	{
		try {
			FileWriter fstream;
			fstream = new FileWriter(fileName,true);
			BufferedWriter fop = new BufferedWriter(fstream);

			for(int i=bi.getMinX();i<bi.getWidth();i++)
			{
				for(int j=bi.getMinY();j<bi.getHeight();j++)
				{
					Color color = new Color(bi.getRGB(i, j));
					if(color.equals(Color.black))
						fop.write("0 ");
					else
						fop.write("1 ");
				}	
			}
			fop.write("\n");
			fop.close();
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	public BufferedImage cropImage(BufferedImage bi)
	{
		int T = ImageProcessingConstants.getCenterthreshold();
		int minX=normalWidth-1;
		int minY=normalHeight-1;
		int maxX=0;
		int maxY=0;
		int rgbblack = Color.BLACK.getRGB();
		for(int i=0;i<normalWidth-1;i++)
		{
			for(int j=0;j<normalHeight-1;j++)
			{
				if(bi.getRGB(i, j) != rgbblack)
				{
					if(minX > i)minX=i;
					if(minY > j)minY=j;
					if(maxX < i)maxX=i;
					if(maxY < j)maxY=j;
				}
			}
		}
		if(!(maxX>minX && maxY > minY))
		{
			System.out.println("OUT OF BOUNDS");
			return bi;
		}
		
		BufferedImage buff = new BufferedImage(maxX-minX+100, maxY-minY+100, BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<T+maxX-minX;i++)
		{
			for(int j=0;j<T+maxY-minY;j++)
			{
				if(i<T/2 || j<T/2 || i>(T/2+maxX-minX) || j>(T/2+maxY-minY))
					buff.setRGB(i, j, rgbblack);
				else 
					buff.setRGB(i,j,bi.getRGB(minX+i-T/2,minY+j-T/2));
			}
		}
		return buff;
	}
	public BufferedImage processSegment(BufferedImage bi) throws IOException 
	{
	//	BufferedImage edgeBuff = sobelEdgeDetection2(bi);
		//		ImageIO.write(edgeBuff, "jpg",new File(filePath+"_1.jpg"));
		BufferedImage edgeImage = sobelEdgeDetection(bi);
		BufferedImage cropBuff = cropImage(edgeImage);
		
		BufferedImage scaledBuff = getScaledImage(cropBuff,width,height);
		//		ImageIO.write(scaledBuff, "jpg",new File(filePath+"_2.jpg"));
		
		BufferedImage monoBuff = getMonoChromeImage(scaledBuff, threshold);
		//		ImageIO.write(monoBuff, "jpg",new File(filePath+"_3.jpg"));

		return monoBuff;
	}


	/* Sobel.js
	 * Kas Thomas
	 * 31 January 2010
	 * Public domain.
	 *
	 * An edge-detection routine using
	 * Java Advanced Imaging.
	 *
	 * Requires Java Advanced Imaging library:
	 * http://java.sun.com/products/java-media/jai/current.html
	 *
	 * Run this file using ImageMunger:
	 * http://asserttrue.blogspot.com/2010/01/simple-java-class-for-running-scripts.html
	 *
	 */

	BufferedImage sobelEdgeDetection2(BufferedImage img) 
	{

		BufferedImage edged = new BufferedImage(img.getWidth(),img.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		float[] hx = new float[]{-1,0,1,
				-2,0,2,
				-1,0,1};

		float[] hy = new float[]{-1,-2,-1,
				0, 0, 0,
				1, 2, 1};

		int[] rgbX = new int[3]; int[] rgbY = new int[3];

		//ignore border pixels strategy
		for(int x = 1; x < img.getWidth()-1; x++)
			for(int y = 1; y < img.getHeight()-1; y++) {
				convolvePixel(hx,3,3, img, x, y, rgbX);
				convolvePixel(hy,3,3, img, x, y, rgbY);

				//instead of using sqrt function for eculidean distance
				//just do an estimation
				int r = Math.abs(rgbX[0]) + Math.abs(rgbY[0]);
				int g = Math.abs(rgbX[1]) + Math.abs(rgbY[1]);
				int b = Math.abs(rgbX[2]) + Math.abs(rgbY[2]);

				//range check
				if(r > 255) r = 255;
				if(g > 255) g = 255;
				if(b > 255) b = 255;

				edged.setRGB(x, y,(r<<16)|(g<<8)|b);
			}
		return edged;
	}
	private static int[] convolvePixel(float[] kernel, int kernWidth, int kernHeight,
			BufferedImage src, int x, int y, int[] rgb) {
		if(rgb == null) rgb = new int[3];

		int halfWidth = kernWidth/2;
		int halfHeight = kernHeight/2;

		for(int component = 0; component < 3; component++) {
			float sum = 0;
			for(int i = 0; i < kernel.length; i++) {
				int row = (i/kernWidth)-halfWidth;  //current row in kernel
				int column = (i-(kernWidth*row))-halfHeight; //current column in kernel

				//range check
				if(x-row < 0 || x-row > src.getWidth()) continue;
				if(y-column < 0 || y-column > src.getHeight()) continue;

				int srcRGB =src.getRGB(x-row,y-column);
				sum = sum + kernel[i]*((srcRGB>>(16-8*component))&0xff);
			}
			rgb[component] = (int) sum;
		}
		return rgb;
	}
	BufferedImage sobelEdgeDetection(BufferedImage bi) 
	{


		// Create a constant array with the Sobel horizontal kernel.
		float[] kernelMatrix = { -1, -2, -1,
				0,  0,  0,
				1,  2,  1 };
		// Read the image.
		PlanarImage input = PlanarImage.wrapRenderedImage(bi);
		// Create the kernel using the array.
		KernelJAI kernel = new KernelJAI(3,3,kernelMatrix);
		// Run the convolve operator, creating the processed image.
		PlanarImage output = JAI.create("convolve", input, kernel);

		return output.getAsBufferedImage();
	}
	
	public int getNormalHeight(){
		return this.normalHeight;
	}
	public int getNormalWidth(){
		return this.normalWidth;
	}
	

}