

import featureextraction.FeatureExtractor;
import featureextraction.FeatureType;
import imageprocess.ImageProcessingConstants;
import imageprocess.ImageProcessor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import classify.NeuralNetwork;
import classify.SVMClassifier;

import net.sf.javaml.core.Dataset;
import som.ImageSom;

public class ImageProcessorTest {
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		String path = System.getProperty("user.dir");
		String posFolder = path+"\\images\\positive\\"; 
		String negFolder =  path+"\\images\\negative\\";
		String imgFolder = path+"\\dataset\\"
								+ImageProcessingConstants.getObjecttype()+"\\"+ImageProcessingConstants.getIdentificationtype()+"\\";
		
		Utility.cleanup(posFolder+"train"); 
		Utility.cleanup(posFolder+"test");
		Utility.cleanup(posFolder+"train"); 
		Utility.cleanup(posFolder+"test");
		
		extractObjects(imgFolder+"positive\\",posFolder);
		extractObjects(imgFolder+"negative\\",negFolder);
		
		Vector<Vector<Double>> data = getDataset(path + "\\images\\", "test");
		Utility.write(path + "test.arff", data);
		data = getDataset(path + "\\images\\", "train");
		Utility.write(path + "train.arff", data);
	}
	public static BufferedImage cleanupImage(BufferedImage bi) throws IOException
	{
		ImageProcessor ip = new ImageProcessor();
		BufferedImage normalBuff = ip.getScaledImage(bi,ip.getNormalWidth(),ip.getNormalHeight());
		if(normalBuff==null)
			return null;
		BufferedImage segmentImage = ip.getSegmentedImage(normalBuff);
		BufferedImage preprocessed = ip.removeBG(segmentImage);
		Vector<BufferedImage> segments = ip.getSegments(normalBuff,preprocessed);
		if(segments.size()==0)
			return null;
		BufferedImage masterImage = ip.getMasterImage(normalBuff, segments);
		return masterImage;
	}
	public static void extractObjects(String srcPath, String destPath)
	{
		File [] imgFiles = Utility.listFiles(srcPath);
		String dumpPath = destPath;
		for(int i=0;i<imgFiles.length;i++)
		{
			
			if(i<ImageProcessingConstants.getTraincount())
				dumpPath = destPath+"train\\";
			else
				dumpPath = destPath+"test\\";
			
			try
			{
				BufferedImage readImage = ImageIO.read(imgFiles[i]);
				BufferedImage masterImage = cleanupImage(readImage);
				if(masterImage != null)
				{
					writeImageFile(masterImage, dumpPath+i+".jpg");
					ImageProcessor ip = new ImageProcessor();
					writeImageFile(ip.getMirrorImage(masterImage), dumpPath+"mirror_"+i+".jpg");
				}
			}
			catch(Exception e)
			{
				
			}
		}		
	}
	public static Vector<Vector<Double>> getDataset(String dumpPath,String type) throws InterruptedException, IOException
	{
		Vector<Vector<Double>> dataset = new Vector<Vector<Double>>();
		String posFolder = dumpPath+"positive\\"+type;
		String negFolder = dumpPath+"negative\\"+type;
		
		FeatureType ft = ImageProcessingConstants.getFeaturetype();
		File [] posFiles = Utility.listFiles(posFolder);
		File [] negFiles = Utility.listFiles(negFolder);
		for(int i=0;i<posFiles.length && i<negFiles.length;i++)
		{
			if(i<posFiles.length)
			{
				BufferedImage posB = ImageIO.read(posFiles[i]);
				dataset.add(FeatureExtractor.getFeatureVector(posB, 1, ft));
			}
			if(i<negFiles.length)
			{
				BufferedImage negB = ImageIO.read(negFiles[i]);
				dataset.add(FeatureExtractor.getFeatureVector(negB, 0, ft));
			}
		}
		return dataset;
	}
	public static void clusterObjects(String dir) throws IOException, InterruptedException
	{
		Vector<Vector<Double>> data = new Vector<Vector<Double>>();	
		for(int k=1;k<100;k++)
		{
			String fileName = dir+"\\train\\preprocess\\g"+k+".jpg";
			if(!(new File(fileName)).exists())
				continue;
			BufferedImage readImage = ImageIO.read(new File(fileName));
			data.add(FeatureExtractor.getFeatureVector(readImage, k, FeatureType.FULLBITMAP));
		}
		for(int k=1;k<100;k++)
		{
			String fileName = dir+"\\train\\preprocess\\b"+k+".jpg";
			if(!(new File(fileName)).exists())
				continue;
			BufferedImage readImage = ImageIO.read(new File(fileName));
			data.add(FeatureExtractor.getFeatureVector(readImage, -k, FeatureType.FULLBITMAP));	
		}
		for(int k=1;k<100;k++)
		{
			String fileName = dir+"\\test\\preprocess\\g"+k+".jpg";
			if(!(new File(fileName)).exists())
				continue;
			BufferedImage readImage = ImageIO.read(new File(fileName));
			data.add(FeatureExtractor.getFeatureVector(readImage, k, FeatureType.FULLBITMAP));	
		}
		for(int k=1;k<100;k++)
		{
			String fileName = dir+"\\test\\preprocess\\b"+k+".jpg";
			if(!(new File(fileName)).exists())
				continue;
			BufferedImage readImage = ImageIO.read(new File(fileName));
			data.add(FeatureExtractor.getFeatureVector(readImage, -k, FeatureType.FULLBITMAP));
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

	
	public static void classify(String path) throws IOException{
		Vector<Vector<Double>> trainset = new Vector<Vector<Double>>();
		Vector<Vector<Double>> testset = new Vector<Vector<Double>>();
		SVMClassifier svm = new SVMClassifier();
		
		//train SVM
		String trainPath = path+"\\train\\preprocess\\";
			
		for(int j=0; j<10; j++){
		for(int k=1;k<100;k++)
		{
			//positive examples
			
				String fileName = trainPath;// + imgObj;
				if((new File(fileName)).exists())
				{	
				BufferedImage readImage = ImageIO.read(new File(fileName));
		//		trainset.add(FeatureExtractor.getFullBitmap(readImage,1));
				}
				
			//negative examples
			
				fileName = trainPath + "b"+k+/*"_"+j+*/".jpg";
				if((new File(fileName)).exists())
				{
				BufferedImage readImage = ImageIO.read(new File(fileName));
				//trainset.add(FeatureExtractor.getFullBitmap(readImage,0));
				}
		}
		}
		
		//test SVM
		String testPath = path+"\\test\\preprocess\\";
			
		for(int k=1;k<100;k++)
		{
			//positive examples
//			for(int j=0; j<100; j++){
				String fileName = testPath + "g"+k+/*"_"+j+*/".jpg";
				if((new File(fileName)).exists())
				{
				BufferedImage readImage = ImageIO.read(new File(fileName));
		//		testset.add(FeatureExtractor.getFullBitmap(readImage,1));
				}
			//}
			//negative examples
			//for(int j=0; j<100; j++){
				/*String*/ fileName = testPath + "b"+k+/*"_"+j+*/".jpg";
				if((new File(fileName)).exists())
				{
				BufferedImage readImage = ImageIO.read(new File(fileName));
		//		testset.add(FeatureExtractor.getFullBitmap(readImage,0));
				}
			//}
		}
		
//		svm.train(trainset);
//		Vector<Object> predictions = svm.classify(testset);
		
		String trainFile = "train.arff";
		String testFile = "test.arff";
//		for(Vector<Double> current : trainset)
//			System.out.println(current.size());
//		System.out.println(testset);
		Utility.write(trainFile, trainset);
//		Utility.write(testFile, testset);
		
//		NeuralNetwork network = Utility.neuralNetwork(trainFile);
//		Vector<Double> predicted = Utility.classify(testFile, network);
//		System.out.println(predicted);
	}
	
	
	
	public static void writeImageFile(BufferedImage bi, String filePath) throws IOException
	{
		ImageIO.write(bi,"jpg",new File(filePath));
	}
}
