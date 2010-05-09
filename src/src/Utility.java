
import java.io.*;
import java.util.*;

import classify.NeuralNetwork;
import weka.classifiers.Classifier;
import weka.core.*;
import weka.core.converters.ArffLoader;


public class Utility {

	public static void write(String fileName, Vector<Vector<Double>> data)
		 throws IOException {
		File file = new File(fileName);
		int size = data.get(0).size();
		System.out.println(size);
		String header = getHeader(size);
		
		if (!file.canWrite()){
			throw new IllegalArgumentException("File cannot be written: " + file);
		}
		
		
		//use buffering
		FileOutputStream out = new FileOutputStream(file);
		OutputStreamWriter writer = new OutputStreamWriter(out);
		BufferedWriter buff = new BufferedWriter(writer);
		
		buff.write(header);
		buff.newLine();
		for(Vector<Double> line : data){
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
		if (out != null) writer.close();
		
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
	public static void cleanup(String directory)
	{
		File dir = new File(directory);
		File []files = dir.listFiles();
		for (int i=0;i<files.length;i++)
			files[i].delete();
	}
	public static File[] listFiles(String directory)
	{
		File dir = new File(directory);
		return dir.listFiles(); 
	}
}
