package classify;
import java.io.IOException;

import weka.classifiers.functions.*;
import weka.core.*;

public class NeuralNetwork {
	
	private Instances instances;
	private MultilayerPerceptron MLP;
		

	public NeuralNetwork(){
		setMLP(new MultilayerPerceptron());
	}
	
	
	public void init(Instances instances) {
		setInstances(instances);
		setupMLP();
		
	}
	
	public void setupMLP(){
		
		int epochs = 10;
		getMLP().setTrainingTime(epochs);
		getMLP().setHiddenLayers("a,a,a,a,a,a,a,a,a,a");
		getMLP().setNormalizeNumericClass(false);
	
		try {
			getMLP().buildClassifier(getInstances());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public double classify(Instance instance){
		double predicted = 11;
		try {
			predicted = getMLP().classifyInstance(instance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return predicted;
	}
	
	public void setInstances(Instances instances) {
		this.instances = instances;
	}
	
	public Instances getInstances() {
		return this.instances;
	}

	public void setMLP(MultilayerPerceptron mLP) {
		this.MLP = mLP;
	}

	public MultilayerPerceptron getMLP() {
		return this.MLP;
	}

	
}
