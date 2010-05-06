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
	
	private void setupMLP(){
		int epochs = 10000;
		getMLP().setTrainingTime(epochs);
		getMLP().setHiddenLayers("a,a,a,a,a,a,a,a,a,a");
		
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
	
	private void setInstances(Instances instances) {
		this.instances = instances;
	}
	
	private Instances getInstances() {
		return this.instances;
	}

	private void setMLP(MultilayerPerceptron mLP) {
		MLP = mLP;
	}

	private MultilayerPerceptron getMLP() {
		return this.MLP;
	}

	
}
