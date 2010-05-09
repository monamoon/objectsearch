package imageprocess;

import featureextraction.Circles;
import featureextraction.Corners;
import featureextraction.hystThresh;
import featureextraction.nonMaxSuppression;
import featureextraction.sobel;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

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
	public BufferedImage getCircleImage(BufferedImage bi) throws InterruptedException
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
		
		Circles circles = new Circles();
		circles.init(orig, width, height,30 );
		orig = circles.process();
		
		BufferedImage circleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Image piximg = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, orig,0,width));
		circleImage.getGraphics().drawImage(piximg, 0, 0, null);
		return circleImage; 
		
    }
	public BufferedImage getCornerImage(BufferedImage bi) throws InterruptedException
	{
		int width = bi.getWidth();
		int height = bi.getHeight();
		int []orig=new int[width*height];
		PixelGrabber grabber = new PixelGrabber(bi, 0, 0, width, height, orig, 0, width);
		grabber.grabPixels();
	
		Corners harrisObj = new Corners();
		harrisObj.init(orig,width, height, 0.14);
		orig=harrisObj.process();

		BufferedImage cornerImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Image piximg = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, orig,0,width));
		cornerImage.getGraphics().drawImage(piximg, 0, 0, null);
		return cornerImage; 
	
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
	public BufferedImage normalizeImage(BufferedImage bi) throws IOException 
	{
		BufferedImage cropBuff = cropImage(bi);
		BufferedImage scaledBuff = getScaledImage(cropBuff,width,height);
		return scaledBuff;
	}


		
	public int getNormalHeight(){
		return this.normalHeight;
	}
	public int getNormalWidth(){
		return this.normalWidth;
	}
	

}