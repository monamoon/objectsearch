

import featureextraction.FeatureExtractor;
import featureextraction.FeatureType;
import imageprocess.ImageProcessingConstants;
import imageprocess.ImageProcessor;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import net.sf.javaml.core.Dataset;
import som.ImageSom;
import classify.SVMClassifier;

public class ImageProcessorTest {
	public static void initAnalysis(String path)
	{
		Vector<String> dirs = new Vector<String>();
		dirs.add(path+"\\positive\\train"); 
		dirs.add(path+"\\positive\\test");
		dirs.add(path+"\\negative\\train"); 
		dirs.add(path+"\\negative\\test");
		
		Utility.cleanup(dirs);
		new File(path+"test.arff").delete();
		new File(path+"train.arff").delete();
		
	}
	public static void runImageAnalysis(String repository, String dumpPath)
	{
		extractObjects(repository+"\\positive\\",dumpPath+"\\positive\\");
		extractObjects(repository+"\\negative\\",dumpPath+"\\negative\\");
	}
	public static void createDataset(String dumpPath) throws InterruptedException, IOException
	{
		getDataset(dumpPath, "test", ImageProcessingConstants.getFeaturetype());
		getDataset(dumpPath, "train", ImageProcessingConstants.getFeaturetype());
	}
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		String path = System.getProperty("user.dir");
		String dumpPath = path+"\\images\\"
								+ImageProcessingConstants.getObjecttype()+"\\"+ImageProcessingConstants.getIdentificationtype(); 
		String repository = path+"\\dataset\\"
								+ImageProcessingConstants.getObjecttype()+"\\"+ImageProcessingConstants.getIdentificationtype();
		if(ImageProcessingConstants.isdoAnalysis())
		{
			initAnalysis(dumpPath);
			runImageAnalysis(repository,dumpPath);
		}
		
		createDataset(dumpPath);
		
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
		BufferedImage croppedImage = ip.cropImage(masterImage);
		BufferedImage scaledImage = ip.getScaledImage(croppedImage,ip.getNormalWidth(),ip.getNormalHeight());
		return scaledImage;
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
					writeImageFile(ImageProcessor.getMirrorImage(masterImage), dumpPath+"mirror_"+i+".jpg");
				}
			}
			catch(Exception e)
			{
				
			}
		}		
	}
	public static void getDataset(String dumpPath,String type,FeatureType ft) throws InterruptedException, IOException
	{
		String posFolder = dumpPath+"positive\\"+type;
		String negFolder = dumpPath+"negative\\"+type;
		String dataFile = dumpPath+type+ft+".arff";
		
		BufferedWriter dataBuff =  new BufferedWriter(new FileWriter(dataFile,true));
		
		boolean firstSample = true;
		File [] posFiles = Utility.listFiles(posFolder);
		File [] negFiles = Utility.listFiles(negFolder);
		
		for(int i=0;i<posFiles.length && i<negFiles.length;i++)
		{
			if(i<posFiles.length)
			{
				try {
					System.out.println(posFiles[i].getPath());
					BufferedImage posB = ImageIO.read(posFiles[i]);
					Vector<Double> fv = FeatureExtractor.getFeatureVector(posB, 1, ft);
					if(firstSample && fv!=null)
					{
						firstSample = false;
						Utility.initDataset(dataBuff, fv.size());
					}
					Utility.addSample(dataBuff, fv);
				}
				catch(Exception e) {
				}
			}
			
			if(i<negFiles.length)
			{
				try {
					System.out.println(negFiles[i].getPath());
					BufferedImage negB = ImageIO.read(negFiles[i]);
					Vector<Double> fv = FeatureExtractor.getFeatureVector(negB, 1, ft);
					if(firstSample && fv!=null)
					{
						firstSample = false;
						Utility.initDataset(dataBuff, fv.size());
					}
					Utility.addSample(dataBuff, fv);
				}
				catch(Exception e) {
				}
			}
		}
		if (dataBuff != null) {
			dataBuff.flush();
			dataBuff.close();
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
 