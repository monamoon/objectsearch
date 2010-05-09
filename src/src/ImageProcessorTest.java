

import featureextraction.FeatureExtractor;
import featureextraction.FeatureType;
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
	static String imgObj = "bird";
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		String path = System.getProperty("user.dir");
		Utility.cleanup(path+"\\images\\test\\positive");
		Utility.cleanup(path+"\\images\\test\\negative");
		Utility.cleanup(path+"\\images\\train\\positive");
		Utility.cleanup(path+"\\images\\train\\negative");
		//Utility.write("C:\\Users\\kris\\workspace\\objectsearch\\data.txt",data);
		
		
//		
//		extractObjects(path+"\\images\\"+imgObj+"\\train\\");
//		extractObjects(path+"\\images\\"+imgObj+"\\test\\");
////		classify(path+"\\images");
//		clusterObjects(path+"\\images");
	}

	public static void extractObjects(String filePath)
	{
		ImageProcessor ip = new ImageProcessor();
		File [] imgFiles = Utility.listFiles(filePath+"positive\\");
		System.out.println(filePath+"positive\\");
		String path = System.getProperty("user.dir");
		String traindata = path+"\\images\\train\\";
		String testdata = path+"\\images\\test\\";
		for(int i=0;i<imgFiles.length;i++)
		{
			System.out.println(imgFiles[i]);
			try
			{
				
				BufferedImage readImage = ImageIO.read(imgFiles[i]);
				BufferedImage normalBuff = ip.getScaledImage(readImage,ip.getNormalWidth(),ip.getNormalHeight());
				if(normalBuff==null)
					continue;
				BufferedImage segmentImage = ip.getSegmentedImage(normalBuff);
				BufferedImage preprocessed = ip.removeBG(segmentImage);
				Vector<BufferedImage> segments = ip.getSegments(normalBuff,preprocessed);
				if(segments.size()==0)
					continue;
				
				BufferedImage master1 = ip.getMasterImage(normalBuff, segments);
				BufferedImage master2 = ip.processSegment(master1);
				BufferedImage master3 = ip.getMirrorImage(master2);
				
				writeImageFile(master2, traindata+"preprocess/g" +i+".jpg");
				writeImageFile(master3, traindata+"preprocess/g" +(1000-i)+".jpg");
				
				for(int j=0;j<segments.size();j++)
				{
					BufferedImage processedBuff = ip.processSegment(segments.elementAt(j));
					writeImageFile(processedBuff, traindata+"objects/g"+i+"_"+j+".jpg");
				}
				
			}
			catch (IOException e)
			{
				System.out.println("COULD not find file");
				break;
			}
		}
		imgFiles = Utility.listFiles(filePath+"negative\\");
		for(int i=0;i<imgFiles.length;i++)
		{
			try
			{
				BufferedImage readImage = ImageIO.read(imgFiles[i]);
				BufferedImage normalBuff = ip.getScaledImage(readImage,ip.getNormalWidth(),ip.getNormalHeight());
				if(normalBuff==null)
					continue;
				
				BufferedImage segmentImage = ip.getSegmentedImage(normalBuff);
				BufferedImage preprocessed = ip.removeBG(segmentImage);
				Vector<BufferedImage> segments = ip.getSegments(normalBuff,preprocessed);
				if(segments.size()==0)
					continue;
				BufferedImage master1 = ip.getMasterImage(normalBuff, segments);
				BufferedImage master2 = ip.processSegment(master1);
				BufferedImage master3 = ip.getMirrorImage(master2);		
				
				writeImageFile(master2, testdata+"preprocess/b" +i+".jpg");
				writeImageFile(master3, testdata+"preprocess/b" +(1000-i)+".jpg");
				
				for(int j=0,k=0;j<segments.size();j++,k++)
				{
					BufferedImage processedBuff = ip.processSegment(segments.elementAt(j));
					writeImageFile(processedBuff, testdata+"objects/b"+i+"_"+k+".jpg");
				}
			}
			catch (IOException e)
			{
				break;
			}
		}	
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
			
				String fileName = trainPath + imgObj;
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
