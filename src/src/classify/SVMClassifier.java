package classify;
import java.util.Vector;

import libsvm.LibSVM;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;


public class SVMClassifier {

	private Dataset dataset;
	private Dataset testset;
	private Classifier SVM;
	private Vector<Instance> instances;
	private Vector<Instance> testInstances;
	private Vector<Vector<Double>> samples;
	private Vector<Vector<Double>> testsamples;

	public SVMClassifier(){
		setSVM(new LibSVM());
	}
	
	public void train(Vector<Vector<Double>> data){
		setSamples(data);
		setupInstances();
		setupDataset();
		getSVM().buildClassifier(getDataset());
	}

	public Object classify(Instance instance){
		return getSVM().classify(instance);
	}
	
	public Vector<Object> classify(Vector<Vector<Double>> data){
		Vector<Object> classes = new Vector<Object>();
		setTestsamples(data);
		setupTestInstances();
		setupTestset();
		
		for(Instance instance : getTestset()){
			Object actual = instance.classValue();
			Object predicted = getSVM().classify(instance);
			classes.add(predicted);
			System.out.println("Actual Class: "+actual
								+ "Predicted Class: "+ predicted);
			
		}
				
		return classes;
	}
	
	private void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public Dataset getDataset() {
		return dataset;
	}

	private void setSVM(LibSVM sVM) {
		SVM = sVM;
	}

	public Classifier getSVM() {
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

	public void setTestset(Dataset test) {
		this.testset = test;
	}

	public Dataset getTestset() {
		return testset;
	}
	
	private void setupInstances(){
		Vector<Instance> instances = new Vector<Instance>();
		for(Vector<Double> data : getSamples()){
			double[] arrayData = new double[data.size()];
			for(int i=0; i<(data.size()-1); i++)
				arrayData[i] = data.get(i);
			instances.add(new DenseInstance(arrayData, data.get(data.size()-1)));	
		}
		setInstances(instances);
	}

	private void setupTestInstances(){
		Vector<Instance> instances = new Vector<Instance>();
		for(Vector<Double> data : getTestsamples()){
			double[] arrayData = new double[data.size()];
			for(int i=0; i<(data.size()-1); i++)
				arrayData[i] = data.get(i);
			instances.add(new DenseInstance(arrayData, data.get(data.size()-1)));	
		}
		setTestInstances(instances);
	}
	
	public void setTestsamples(Vector<Vector<Double>> testsamples) {
		this.testsamples = testsamples;
	}

	public Vector<Vector<Double>> getTestsamples() {
		return testsamples;
	}
	
	private void setupDataset(){
		setDataset(new DefaultDataset(getInstances()));	
	}

	private void setupTestset(){
		setTestset(new DefaultDataset(getTestInstances()));	
	}

	
	public void setTestInstances(Vector<Instance> testInstances) {
		this.testInstances = testInstances;
	}

	public Vector<Instance> getTestInstances() {
		return testInstances;
	}
}
