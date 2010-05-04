package imageprocess;

import java.awt.Color;
import java.awt.image.BufferedImage;


public class Segment {
	BufferedImage bi;
	int count;
	boolean isCorner = false;
	public int getCount() {
		return count;
	}
	Color color;
	public BufferedImage getImage() {
		return bi;
	}
	public Color getColor() {
		return color;
	}
	Segment(Color c)
	{
		Color bg = Color.BLACK;
		if(c.equals(bg))
			bg = Color.WHITE;
		bi = new BufferedImage(ImageProcessingConstants.getScaleWidth(), ImageProcessingConstants.getScaleheight(), BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<bi.getWidth();i++)
			for(int j=0;j<bi.getHeight();j++)
				bi.setRGB(i, j, bg.getRGB());
		count = 0;
		color = c;
	}
	void setPixel(int i,int j)
	{
		bi.setRGB(i, j, color.getRGB());
		count = count+1;
		if(i==0 || i==ImageProcessingConstants.getScaleWidth()-1 || j==0 || j==ImageProcessingConstants.getScaleheight()-1)
			isCorner = true;
	}
};
