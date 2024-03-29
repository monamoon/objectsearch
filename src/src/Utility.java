
import java.io.*;
import java.util.*;

import classify.NeuralNetwork;
import weka.classifiers.Classifier;
import weka.core.*;
import weka.core.converters.ArffLoader;


public class Utility {
	public static void initDataset(BufferedWriter buff, int dimension) throws IOException
	{
		String str = getHeader(dimension);
		buff.append(str);
		buff.newLine();	
	}
	public static void addSample(BufferedWriter buff, Vector<Double> data) throws IOException
	{
		String str = "";
		for(int i=0; i<(data.size()-1); i++){
			double val = data.get(i);
			str += val + ",";
		}
		str += data.lastElement();
			
		buff.append(str);
		buff.newLine();	
	}
	
	public static void write(String fileName, Vector<Vector<Double>> data)
		 throws IOException {
		File file = new File(fileName);
		int size = data.get(0).size();
		String header = getHeader(size);
		
		BufferedWriter buff =  new BufferedWriter(new FileWriter(file));
		
		buff.write(header);
		buff.newLine();
		for(Vector<Double> line : data){
			System.out.println("line size: " + line.size());
			String string = "";
			for(int j=0; j<(line.size()-1); j++){
				double curr = line.get(j);
				string += curr + ",";
			}
			{
				string += line.lastElement();
			}
//				System.out.print(string);
			buff.append(string);
			buff.newLine();
			
		}
		for(int i=0; i<5; i++){
			buff.newLine();
			buff.append("%");
		}
		buff.newLine();
		if (buff != null) {
			buff.flush();
			buff.close();
		}
		
	}

	public static String getHeader(int size){
		String line = null;
		line = "@RELATION ObjectSearch"+"\n";
		for(int i=0; i<(size-1); i++)
			line += ("@ATTRIBUTE pixel"+i+"\t"+"REAL"+"\t"+"{0.0,1.0}"+"\n");
		line += ("@ATTRIBUTE class"+"\t"+"{0.0,1.0}"+"\n");
		line += ("@DATA");
		return line;
	}
	
	public static Instances getInstances(String fileName) {
		Instances instances = null;
		try{
			BufferedReader buff
			   = new BufferedReader(new FileReader(fileName));	    
			instances = new Instances(buff);
			instances.setClassIndex(instances.numAttributes() - 1);
		    

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return instances;
	}
	
	public static NeuralNetwork neuralNetwork(String fileName) {
		NeuralNetwork network = new NeuralNetwork();
		
		network.init(getInstances(fileName));
		return network;
	}

	public static Vector<Double> classify(String testFile, NeuralNetwork network) {
		Vector<Double> predicted = new Vector<Double>();
		Instances instances = getInstances(testFile);
		//System.out.println(instances);
		for(int i=0; i<instances.numInstances(); i++){
			Instance instance = instances.instance(i);
			
			predicted.add(network.classify(instance));
		}
		
		return predicted;
	}
	public static void cleanup(Vector<String> dirs)
	{
		for(String directory : dirs){
			File dir = new File(directory);
			if(dir.exists()){
				File []files = dir.listFiles();
				for (int i=0;i<files.length;i++)
					files[i].delete();
			}else{
				dir.mkdirs();
			}
		}
	}
	

	public static File[] listFiles(String directory)
	{
		File dir = new File(directory);
		System.out.println(dir);
		return dir.listFiles(); 
	}

}
