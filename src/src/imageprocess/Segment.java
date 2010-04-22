package imageprocess;

import java.awt.Color;
import java.awt.image.BufferedImage;


public class Segment {
	BufferedImage bi;
	int count;
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
		bi = new BufferedImage(ImageProcessingConstants.scaleWidth, ImageProcessingConstants.scaleHeight, BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<bi.getWidth();i++)
			for(int j=0;j<bi.getHeight();j++)
				bi.setRGB(i, j, Color.BLACK.getRGB());
		count = 0;
		color = c;
	}
	void setPixel(int i,int j)
	{
		bi.setRGB(i, j, color.getRGB());
		count = count+1;
	}
};
