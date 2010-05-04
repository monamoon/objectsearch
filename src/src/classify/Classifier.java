package classify;
import java.util.Vector;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import libsvm.SelfOptimizingLinearLibSVM;


public class Classifier {

	private Dataset dataset;
	private SelfOptimizingLinearLibSVM SVM;
	private Vector<Instance> instances;
	private Vector<Vector<Double>> samples;

	public Classifier(Dataset data){
		setDataset(data);
		setSVM(new SelfOptimizingLinearLibSVM());
		getSVM().buildClassifier(getDataset());
		
	}

	public Object classify(Instance instance){
		return getSVM().classify(instance);
	}
	
	public Vector<Object> classify(Dataset data){
		Vector<Object> classes = new Vector<Object>();
		for(Instance instance : data){
			classes.add(getSVM().classify(instance));
		}
		return classes;
	}
	
	private void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public Dataset getDataset() {
		return dataset;
	}

	private void setSVM(SelfOptimizingLinearLibSVM sVM) {
		SVM = sVM;
	}

	public SelfOptimizingLinearLibSVM getSVM() {
		return SVM;
	}

	private void setInstances(Vector<Instance> instances) {
		this.instances = instances;
	}

	public Vector<Instance> getInstances() {
		return instances;
	}

	private void setSamples(Vector<Vector<Double>> samples) {
		this.samples = samples;
	}

	public Vector<Vector<Double>> getSamples() {
		return samples;
	}
	
}
